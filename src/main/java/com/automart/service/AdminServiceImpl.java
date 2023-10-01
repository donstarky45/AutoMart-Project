package com.automart.service;

import com.automart.controllers.UserController;
import com.automart.entity.AddressEntity;
import com.automart.entity.CarAD;
import com.automart.entity.Transactions;
import com.automart.entity.UserEntity;
import com.automart.exceptions.ErrorMessages;
import com.automart.exceptions.UserServiceException;
import com.automart.repository.AddressRepository;
import com.automart.repository.CarADsRepository;
import com.automart.repository.TransactionsRepository;
import com.automart.repository.UserRepository;
import com.automart.response.AuthenticationResponse;
import com.automart.response.TransferResponse;
import com.automart.security.JwtService;
import com.automart.shared.AddressDTO;
import com.automart.shared.UserDto;
import com.automart.userRequest.AdminRegisterRequest;
import com.automart.userRequest.RegisterRequest;
import com.automart.userRequest.TransferRequest;
import com.automart.userRequest.UserLoginRequest;
import com.automart.utils.AccountUtils;
import com.automart.utils.UserUtils;
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
    private final CarADsRepository registerRepository;
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
                .cars(Collections.emptyList())
                .orders(Collections.emptyList())
                .carADS(Collections.emptyList())
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





}
