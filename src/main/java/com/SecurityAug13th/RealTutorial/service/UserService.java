package com.SecurityAug13th.RealTutorial.service;


import com.SecurityAug13th.RealTutorial.entity.Transactions;
import com.SecurityAug13th.RealTutorial.exceptions.ErrorMessages;
import com.SecurityAug13th.RealTutorial.exceptions.UserServiceException;
import com.SecurityAug13th.RealTutorial.shared.AddressDTO;
import com.SecurityAug13th.RealTutorial.shared.TransactionDto;
import com.SecurityAug13th.RealTutorial.userRequest.*;
import com.SecurityAug13th.RealTutorial.security.JwtService;
import com.SecurityAug13th.RealTutorial.generate.AccountUtils;
import com.SecurityAug13th.RealTutorial.generate.UserUtils;
import com.SecurityAug13th.RealTutorial.entity.UserEntity;
import com.SecurityAug13th.RealTutorial.repository.UserRepository;
import com.SecurityAug13th.RealTutorial.shared.UserDto;
import com.SecurityAug13th.RealTutorial.response.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    //   private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    private final AccountUtils accountUtils;
    private final UserUtils userUtils;
    private final Date date;

    public AuthenticationResponse register(RegisterRequest request) {



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
        createdUser.setBalance(1000);
        createdUser.setPassword(passwordEncoder.encode(createdUser.getPassword()));

        List<Transactions> userTransaction = new ArrayList<>();
        Transactions InitialTransactions = new Transactions();

            InitialTransactions.setDetails("User Created with email " + request.getEmail());
        InitialTransactions.setDate(String.valueOf(date));
        InitialTransactions.setTransactionId(userUtils.generateTransactionId(10));
        InitialTransactions.setUserDetails(createdUser);

        userTransaction.add(InitialTransactions);
        createdUser.setTransactions(userTransaction);
//        var user = UserEntity.builder()
//                .firstName(userDto.getFirstName())
//                .lastName(userDto.getLastName())
//                .email(userDto.getEmail())
//                .accountNumber(accountNumber)
//                .userId(user_Id)
//                .password(passwordEncoder.encode(userDto.getPassword()))
//                .role(request.getRole())
//                .build();
        if (repository.findByAccountNumber(createdUser.getAccountNumber()).isPresent())
            throw new RuntimeException("User already exists");
        repository.save(createdUser);
        var jwtToken = jwtService.generateToken(createdUser);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(createdUser.getUserId())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .accountNumber(createdUser.getAccountNumber())
                .addresses(createdUser.getAddresses())
                .build();
    }

    public AuthenticationResponse authenticate(UserLoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var userid = copyProperties(request.getEmail());
        //  var refreshToken = jwtService.generateRefreshToken(user);
//        revokeAllUserTokens(user);
//        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(userid)
                .email(user.getEmail())
                .accountNumber(user.getAccountNumber())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                //   .refreshToken(refreshToken)
                .build();
    }

    public String copyProperties(String email) {
        UserDto returnValue = new UserDto();
        var user = repository.findByEmail(email);
        BeanUtils.copyProperties(user, returnValue);

        return returnValue.getUserId();
    }

    public TransferResponse transfer(TransferRequest request, String email) {

        var user = repository.findByEmail(email).orElseThrow();
        var user2 = repository.findByAccountNumber(request.getAccountNumber()).orElseThrow();
        if (repository.findByEmail(email).isEmpty())
            throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());
        if (repository.findByAccountNumber(request.getAccountNumber()).isEmpty())
            throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());
        if (user.getBalance() < request.getBalance()){
            Transactions transaction = new Transactions();
            transaction.setDetails(" Failed, insufficient Funds! Transfer of " + request.getBalance() + " to " + user2.getFirstName()+ " "+ user2.getLastName());
            transaction.setDate(String.valueOf(date));
            transaction.setUserDetails(user);
            transaction.setTransactionId(userUtils.generateTransactionId(10));
            user.getTransactions().add(transaction);
            repository.save(user);
            repository.save(user2);

            return TransferResponse.builder()

                    .TransferStatus( " Failed, insufficient Funds! Transfer of " + request.getBalance() + " to " + user2.getFirstName()+ " "+ user2.getLastName())
                    .build();
          //  throw new UserServiceException(ErrorMessages.INSUFFICIENT_FUNDS.getErrorMessage());
        }

        if (request.getBalance() <= 0){
//            Transactions transaction = new Transactions();
//            transaction.setDetails("Failed, wrong input");
//            transaction.setDate(String.valueOf(date));
//            transaction.setUserDetails(user);
//            transaction.setTransactionId(userUtils.generateTransactionId(10));
//            user.getTransactions().add(transaction);

            return TransferResponse.builder()

                    .TransferStatus("Failed, wrong input")
                    .build();
          //  throw new UserServiceException(ErrorMessages.INVALID_VALUE.getErrorMessage());
        }
if(request.getBalance() >0 && user.getBalance() >= request.getBalance()) {
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


return  null;
    }

    public AuthenticationResponse getUser(String userId) {
        if (repository.findByUserId(userId).isEmpty()) throw new RuntimeException("User not found");
        var user = repository.findByUserId(userId);
        return AuthenticationResponse.builder()
                .firstName(user.get().getFirstName())
                .lastName(user.get().getLastName())
                .userId(user.get().getUserId())
                .email(user.get().getEmail())
                .accountNumber(user.get().getAccountNumber())
                .token("UNAVAILABLE")
                .build();
    }

    public DeleteResponse deleteUser(String userId) {
        if (repository.findByUserId(userId).isEmpty()) throw new RuntimeException("User not found");
        var user = repository.findByUserId(userId);
        repository.delete(user.get());
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

//
//    private void saveUserToken(User user, String jwtToken) {
//        var token = Token.builder()
//                .user(user)
//                .token(jwtToken)
//                .tokenType(TokenType.BEARER)
//                .expired(false)
//                .revoked(false)
//                .build();
//        tokenRepository.save(token);
//    }
//
//    private void revokeAllUserTokens(User user) {
//        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
//        if (validUserTokens.isEmpty())
//            return;
//        validUserTokens.forEach(token -> {
//            token.setExpired(true);
//            token.setRevoked(true);
//        });
//        tokenRepository.saveAll(validUserTokens);
//    }
//
//    public void refreshToken(
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) throws IOException {
//        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        final String refreshToken;
//        final String userEmail;
//        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
//            return;
//        }
//        refreshToken = authHeader.substring(7);
//        userEmail = jwtService.extractUsername(refreshToken);
//        if (userEmail != null) {
//            var user = this.repository.findByEmail(userEmail)
//                    .orElseThrow();
//            if (jwtService.isTokenValid(refreshToken, user)) {
//                var accessToken = jwtService.generateToken(user);
//                revokeAllUserTokens(user);
//                saveUserToken(user, accessToken);
//                var authResponse = AuthenticationResponse.builder()
//                        .accessToken(accessToken)
//                        .refreshToken(refreshToken)
//                        .build();
//                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
//            }
//        }
//    }
}