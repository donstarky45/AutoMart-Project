package com.Starky.codes.service;

import com.Starky.codes.controllers.UserController;
import com.Starky.codes.entity.AddressEntity;
import com.Starky.codes.entity.RegisteredUsers;
import com.Starky.codes.entity.Transactions;
import com.Starky.codes.entity.UserEntity;
import com.Starky.codes.exceptions.ErrorMessages;
import com.Starky.codes.exceptions.UserServiceException;
import com.Starky.codes.repository.AddressRepository;
import com.Starky.codes.repository.RegisteredUserRepository;
import com.Starky.codes.repository.TransactionsRepository;
import com.Starky.codes.repository.UserRepository;
import com.Starky.codes.response.AuthenticationResponse;
import com.Starky.codes.response.TransferResponse;
import com.Starky.codes.security.JwtService;
import com.Starky.codes.shared.AddressDTO;
import com.Starky.codes.shared.UserDto;
import com.Starky.codes.userRequest.AdminRegisterRequest;
import com.Starky.codes.userRequest.RegisterRequest;
import com.Starky.codes.userRequest.TransferRequest;
import com.Starky.codes.userRequest.UserLoginRequest;
import com.Starky.codes.utils.AccountUtils;
import com.Starky.codes.utils.UserUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {


    private final UserRepository repository;
    private final RegisteredUserRepository registerRepository;
    private final AddressRepository addressRepository;
    private final TransactionsRepository transactionsRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final AccountUtils accountUtils;
    private final UserUtils userUtils;
    private final Date date;


    public AuthenticationResponse signupAdmin(AdminRegisterRequest request) {
        if (repository.findByEmail(request.getEmail()).isPresent())
            throw new UserServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());

        UserEntity createdUser = UserEntity.builder()
                .accountNumber(accountUtils.generate(10))
                .addresses(request.getAddresses())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .balance(50000)
                .registeredUsers(Collections.emptyList())
                .subscribedUsers(Collections.emptyList())
                .role(request.getRole())
                .transactions(request.getTransactions())
                .userId(userUtils.generateUserId(20))
                .build();

        for (int i = 0; i < request.getAddresses().size(); i++) {

            AddressEntity addressEntity = request.getAddresses().get(i);
            addressEntity.setUserDetails(createdUser);
            addressEntity.setAddressId(userUtils.generateAdressId(15));
            createdUser.getAddresses().set(i, addressEntity);

        }

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
                .accountNumber(createdUser.getAccountNumber())
                .firstName(createdUser.getFirstName())
                .lastName(createdUser.getLastName())

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


    public AuthenticationResponse signupUser(RegisterRequest request, String userId) {


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
        UserEntity admin = repository.findByUserId(userId);
        RegisteredUsers registeredUser = RegisteredUsers.builder()
                .userId(createdUser.getUserId())
                .firstName(createdUser.getFirstName())
                .lastName(createdUser.getLastName())
                .accountNumber(createdUser.getAccountNumber())
                .userDetails(admin)
                .build();

        if (admin == null) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());

        admin.getRegisteredUsers().add(registeredUser);
        registerRepository.save(registeredUser);
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

    public TransferResponse fundUser(TransferRequest request, String userId) {

        if (findUser(request.getAccountNumber(), userId) == null)
            throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());

        UserEntity user = repository.findByUserId(userId);
        var user2 = repository.findByAccountNumber(request.getAccountNumber()).get();
        if (repository.findByUserId(userId) == null)
            throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());

        if (user2 == null) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());
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

    public String findUser(String accountNumber, String userId) {

        UserEntity admin = repository.findByUserId(userId);


        for (RegisteredUsers registeredUser : admin.getRegisteredUsers()) {
            if (registeredUser.getAccountNumber().equals(accountNumber)) {
                return registeredUser.getAccountNumber();
            }
        }
        return null;
    }
}
