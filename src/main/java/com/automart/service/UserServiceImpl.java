package com.automart.service;


import com.automart.controllers.UserController;
import com.automart.entity.AddressEntity;
import com.automart.entity.Transactions;
import com.automart.exceptions.ErrorMessages;
import com.automart.exceptions.UserServiceException;
import com.automart.repository.AddressRepository;
import com.automart.repository.CarRepository;
import com.automart.repository.TransactionsRepository;

import com.automart.response.*;
import com.automart.shared.AddressDTO;
import com.automart.security.JwtService;

import com.automart.userRequest.RegisterRequest;
import com.automart.userRequest.TransferRequest;
import com.automart.userRequest.UserLoginRequest;
import com.automart.userRequest.UserUpdateRequest;
import com.automart.utils.AccountUtils;
import com.automart.utils.UserUtils;
import com.automart.entity.UserEntity;
import com.automart.repository.UserRepository;
import com.automart.shared.UserDto;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final AddressRepository addressRepository;
  private final CarRepository carRepository;
    private final TransactionsRepository transactionsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final AccountUtils accountUtils;
    private final UserUtils userUtils;
    private final Date date;

    public AuthenticationResponse signup(RegisterRequest request) {


        ModelMapper modelMapper = new ModelMapper();

        UserDto userDto = modelMapper.map(request, UserDto.class);


        if (repository.findByEmail(userDto.getEmail()).isPresent()) throw new RuntimeException("User already exists");

        for (int i = 0; i < request.getAddresses().size(); i++) {

            AddressDTO addressDTO = userDto.getAddresses().get(i);
            addressDTO.setUserDetails(userDto);
            addressDTO.setAddressId(userUtils.generateAdressId(15));
            userDto.getAddresses().set(i, addressDTO);

        }


        UserEntity createdUser = modelMapper.map(userDto, UserEntity.class);
        createdUser.setUserId(userUtils.generateUserId(15));
        createdUser.setAccountNumber(accountUtils.generate(10));
        createdUser.setBalance(50000);
        createdUser.setPassword(passwordEncoder.encode(createdUser.getPassword()));

        List<Transactions> userTransaction = new ArrayList<>();
        Transactions InitialTransactions = new Transactions();

        InitialTransactions.setDetails("User Created with email " + request.getEmail());
        InitialTransactions.setDate(String.valueOf(date));
        InitialTransactions.setTransactionId(userUtils.generateTransactionId(10));
        InitialTransactions.setUserDetails(createdUser);

        userTransaction.add(InitialTransactions);
        createdUser.setTransactions(userTransaction);

        if (repository.findByAccountNumber(createdUser.getAccountNumber()).isPresent())
            throw new RuntimeException("User already exists");
        repository.save(createdUser);
        var jwtToken = jwtService.generateToken(createdUser);

        AuthenticationResponse response = AuthenticationResponse.builder()
                .userId(createdUser.getUserId())
                .firstName(createdUser.getFirstName())
                .lastName(createdUser.getLastName())
                .email(createdUser.getEmail())
                .build();
        Link link = WebMvcLinkBuilder.linkTo(methodOn(UserController.class).getUser(createdUser.getUserId())).withRel("user");
        response.add(link);
        return response;
    }

    public AuthenticationResponse login(UserLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(user.getUserId())
                .email(user.getEmail())
                .accountNumber(user.getAccountNumber())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())


                .build();
        Link userLink = linkTo(methodOn(UserController.class).getUser(user.getUserId())).withRel("user");
        Link transferLink = linkTo(UserController.class).slash("transfer").slash(user.getUserId()).withRel("user");
        Link transactionsLink = linkTo(UserController.class).slash(user.getUserId()).slash("transactions").withRel("user");
        response.add(userLink);
        response.add(transferLink);
        response.add(transactionsLink);
        return response;

    }


    public TransferResponse transfer(TransferRequest request, String userId) {

        UserEntity user = repository.findByUserId(userId);
        UserEntity user2 = repository.findByAccountNumber(request.getAccountNumber()).orElseThrow();
        if (repository.findByUserId(userId) == null)
            throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());
        if (repository.findByAccountNumber(request.getAccountNumber()).isEmpty())
            throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());
        if (user.getBalance() < request.getBalance()) {
            Transactions transaction = new Transactions();
            transaction.setDetails(" Failed, insufficient Funds! Transfer of " + request.getBalance() + " to " + user2.getFirstName() + " " + user2.getLastName());
            transaction.setDate(String.valueOf(date));
            transaction.setUserDetails(user);
            transaction.setTransactionId(userUtils.generateTransactionId(10));
            user.getTransactions().add(transaction);
            repository.save(user);
            repository.save(user2);

            return TransferResponse.builder()

                    .TransferStatus(" Failed, insufficient Funds! Transfer of " + request.getBalance() + " to " + user2.getFirstName() + " " + user2.getLastName())
                    .build();
        }

        if (request.getBalance() <= 0) {
            return TransferResponse.builder()

                    .TransferStatus("Failed, wrong input")
                    .build();

        }
        if (request.getBalance() > 0 && user.getBalance() >= request.getBalance()) {
            user.setBalance(user.getBalance() - request.getBalance());
            user2.setBalance(user2.getBalance() + request.getBalance());

            Transactions transaction = new Transactions();
            transaction.setDetails("Transfer to " + user2.getFirstName() + " " + user2.getLastName() + " Successful. Amount " + String.valueOf(request.getBalance()));
            transaction.setDate(String.valueOf(date));
            transaction.setUserDetails(user);
            transaction.setTransactionId(userUtils.generateTransactionId(10));
            user.getTransactions().add(transaction);

            Transactions transaction2 = new Transactions();
            transaction2.setDetails("Transfer from " + user.getFirstName() + " " + user.getLastName() + " Successful. Amount " + String.valueOf(request.getBalance()));
            transaction2.setDate(String.valueOf(date));
            transaction2.setUserDetails(user2);
            transaction2.setTransactionId(userUtils.generateTransactionId(10));
            user.getTransactions().add(transaction);
            user2.getTransactions().add(transaction2);

            repository.save(user);
            repository.save(user2);

            return TransferResponse.builder()

                    .TransferStatus(" You have successfully sent " + String.valueOf(request.getBalance())
                            + " to " + user2.getFirstName()
                            + " Your balance is " + String.valueOf(user.getBalance()))
                    .build();
        }
        return null;
    }

    public AuthenticationResponse getUser(String userId) {
        if (repository.findByUserId(userId) == null) throw new RuntimeException("User not found");
        UserEntity user = repository.findByUserId(userId);
        return AuthenticationResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userId(user.getUserId())
                .email(user.getEmail())
                .accountNumber(user.getAccountNumber())
                .token("UNAVAILABLE")
                .build();
    }

    public DeleteResponse deleteUser(String userId) {
        if (repository.findByUserId(userId) == null) throw new RuntimeException("User not found");
        UserEntity user = repository.findByUserId(userId);
        repository.delete(user);
        return DeleteResponse.builder()
                .operationalName(OperationalName.DELETE.name())
                .operationalResult(OperationalResult.SUCCESS.name())
                .build();
    }

    public List<PageModel> getUsers(int page, int limit, org.springframework.data.domain.Pageable pageable) {
        List<PageModel> returnValue = new ArrayList<>();
        if (page > 0) page = page - 1;
        pageable = PageRequest.of(page, limit);

        Page<UserEntity> usersPage = repository.findAll(pageable);

        List<UserEntity> users = usersPage.getContent();

        for (UserEntity userEntity : users) {
            PageModel pageModel = new PageModel();
            BeanUtils.copyProperties(userEntity, pageModel);
            returnValue.add(pageModel);
        }
        return returnValue;

    }

    public List<TransactionResponse> getTransactions(String userId) {
        if (repository.findByUserId(userId) == null) throw new RuntimeException("User not found");
        List<TransactionResponse> returnValue = new ArrayList<>();

        ModelMapper modelMapper = new ModelMapper();

        UserEntity userEntity = repository.findByUserId(userId);
        Iterable<Transactions> transactionsList = transactionsRepository.findAllByUserDetails(userEntity);
        List<Transactions> transactions = transactionsRepository.findAllByUserDetails(userEntity);
        if (transactionsList != null) {
            java.lang.reflect.Type listType = new TypeToken<List<TransactionResponse>>() {
            }.getType();
            returnValue = new ModelMapper().map(transactionsList, listType);
        }
        for (int i = 0; i < transactions.size(); i++) {
            Link transactionLink = linkTo(UserController.class).slash(userEntity.getUserId()).slash("transactions").slash(transactions.get(i).getTransactionId()).withRel("user");
            returnValue.get(i).add(transactionLink);
        }

        return returnValue;
    }

    public TransactionResponse getTransaction(String userId, String transactionId) {
        if (repository.findByUserId(userId) == null) throw new RuntimeException("User not found");
        TransactionResponse returnValue = new TransactionResponse();

        ModelMapper modelMapper = new ModelMapper();

        if (repository.findByUserId(userId) == null)
            throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());
        Transactions transaction = transactionsRepository.findByTransactionId(transactionId);


        returnValue = new ModelMapper().map(transaction, TransactionResponse.class);
        Link transactionsLink = linkTo(UserController.class).slash(userId).slash("transactions").withRel("user");
        returnValue.add(transactionsLink);

        return returnValue;
    }


    public List<AddressResponse> getAddresses(String userId) {
        if (repository.findByUserId(userId) == null) throw new RuntimeException("User not found");
        List<AddressResponse> returnValue = new ArrayList<>();

        ModelMapper modelMapper = new ModelMapper();

        UserEntity userEntity = repository.findByUserId(userId);
        Iterable<AddressEntity> addressEntities = addressRepository.findAllByUserDetails(userEntity);

        List<AddressEntity> addressEntity = addressRepository.findAllByUserDetails(userEntity);
        if (addressEntities != null) {
            java.lang.reflect.Type listType = new TypeToken<List<AddressResponse>>() {
            }.getType();
            returnValue = new ModelMapper().map(addressEntities, listType);
        }
        for (int i = 0; i < addressEntity.size(); i++) {
            Link transactionLink = linkTo(UserController.class).slash(userEntity.getUserId()).slash("addresses").slash(addressEntity.get(i).getAddressId()).withRel("user");
            returnValue.get(i).add(transactionLink);
        }

        return returnValue;
    }

    public AddressResponse getAddress(String userId, String addressId) {
        if (repository.findByUserId(userId) == null) throw new RuntimeException("User not found");
        AddressResponse returnValue = new AddressResponse();

        ModelMapper modelMapper = new ModelMapper();

        if (repository.findByUserId(userId) == null)
            throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());
        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
        returnValue = new ModelMapper().map(addressEntity, AddressResponse.class);
        Link transactionsLink = linkTo(UserController.class).slash(userId).slash("addresses").withRel("user");
        returnValue.add(transactionsLink);

        return returnValue;
    }

    public UpdateResponse updateUser(String userId, UserUpdateRequest userDetails) {

        var user = repository.findByUserId(userId);
        if(user == null) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());
        if(user.getFirstName().equals(userDetails.getFirstName()) && user.getLastName().equals(userDetails.getLastName())) throw new UserServiceException(ErrorMessages.DETAILS_ALREADY_EXISTS.getErrorMessage());
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());



        repository.save(user);

   return UpdateResponse.builder()
           .result(OperationalResult.valueOf(OperationalResult.UPDATED.name()))
           .firstName(user.getFirstName())
           .lastName(user.getLastName())
           .build();

    }




}

