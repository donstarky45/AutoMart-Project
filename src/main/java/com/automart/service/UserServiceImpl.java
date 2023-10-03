package com.automart.service;


import com.automart.controllers.UserController;
import com.automart.entity.*;
import com.automart.exceptions.ErrorMessages;
import com.automart.exceptions.UserServiceException;
import com.automart.repository.*;

import com.automart.request.*;
import com.automart.response.*;
import com.automart.security.JwtService;

import com.automart.utils.UserUtils;
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

    private final CarRepository carRepository;

    private final OrderRepository orderRepository;

    private final FlagRepository flagRepository;

    private final ReceivedOrdersRepository receivedOrdersRepository;
    private final TransactionsRepository transactionsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    private final UserUtils userUtils;
    private final Date date;

    public AuthenticationResponse signup(RegisterRequest request) {


        ModelMapper modelMapper = new ModelMapper();

        UserDto userDto = modelMapper.map(request, UserDto.class);


        if (repository.findByEmail(userDto.getEmail()).isPresent()) throw new RuntimeException("User already exists");


        UserEntity createdUser = modelMapper.map(userDto, UserEntity.class);
        createdUser.setUserId(userUtils.generateUserId(15));
        createdUser.setAddress(request.getAddress());
        createdUser.setPassword(passwordEncoder.encode(createdUser.getPassword()));

        List<Transactions> userTransaction = new ArrayList<>();
        Transactions InitialTransactions = new Transactions();

        InitialTransactions.setDetails("User Created with email " + request.getEmail());
        InitialTransactions.setDate(String.valueOf(date));
        InitialTransactions.setTransactionId(userUtils.generateTransactionId(10));
        InitialTransactions.setUserDetails(createdUser);

        userTransaction.add(InitialTransactions);
        createdUser.setTransactions(userTransaction);


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
                .address(user.getAddress())
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


    public AuthenticationResponse getUser(String userId) {
        if (repository.findByUserId(userId) == null) throw new RuntimeException("User not found");
        UserEntity user = repository.findByUserId(userId);
        return AuthenticationResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .userId(user.getUserId())
                .email(user.getEmail())
                .address(user.getAddress())
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


    public UpdateResponse updateUser(String userId, UserUpdateRequest userDetails) {

        var user = repository.findByUserId(userId);
        if (user == null) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());
        if (user.getFirstName().equals(userDetails.getFirstName()) && user.getLastName().equals(userDetails.getLastName()))
            throw new UserServiceException(ErrorMessages.DETAILS_ALREADY_EXISTS.getErrorMessage());
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());


        repository.save(user);

        return UpdateResponse.builder()
                .result(OperationalResult.valueOf(OperationalResult.UPDATED.name()))
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();

    }

    public CarAdsResponse postAd(CarPostRequest request, String id) {
        UserEntity owner = repository.findByUserId(id);

        if (owner == null) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());

        Car car = Car.builder()
                .carId(userUtils.generateCarId(10))
                .bodyType(request.getBodyType())
                .owner(owner)
                .createdOn(date)
                .image(request.getImage())
                .model(request.getModel())
                .manufacturer(request.getManufacturer())
                .state(request.getState())
                .status(Constants.AVAILABLE.getMessage())
                .price(request.getPrice())
                .build();
        carRepository.save(car);
        owner.getCars().add(car);

        return CarAdsResponse.builder()
                .response(ResponseMessages.POSTED.getMessage())
                .carId(car.getCarId())
                .bodyType(car.getBodyType())
                .createdOn(car.getCreatedOn())
                .image(car.getImage())
                .model(car.getModel())
                .manufacturer(car.getManufacturer())
                .state(car.getState())
                .status(car.getStatus())
                .price(car.getPrice())
                .build();

    }

    public OrderResponse orderPurchase(OrderRequest request, String userId, String carId) {
        UserEntity buyer = repository.findByUserId(userId);
        Car car = carRepository.findByCarId(carId);
        UserEntity seller = repository.findByUserId(car.getOwner().getUserId());

        if (buyer == null || seller == null)
            throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());

        SentOrders sentOrder = SentOrders.builder()
                .orderId(userUtils.generateOrderId(6))
                .orderType(ResponseMessages.SENT_ORDER.getMessage())
                .userId(buyer)
                .carId(carId)
                .price(request.getPrice())
                .status(ResponseMessages.ORDER_PENDING_ACCEPTANCE.getMessage())
                .build();
        buyer.getOrders().add(sentOrder);
        if (orderRepository.findByOrderId(sentOrder.getOrderId()) != null)
            throw new UserServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());
        orderRepository.save(sentOrder);
        ReceivedOrders receivedOrder = ReceivedOrders.builder()
                .orderId(sentOrder.getOrderId())
                .orderType(ResponseMessages.RECEIVED_ORDER.getMessage())
                .userId(seller)
                .carId(sentOrder.getCarId())
                .price(sentOrder.getPrice())
                .status(ResponseMessages.ORDER_PENDING_ACCEPTANCE.getMessage())
                .build();
        seller.getReceivedOrders().add(receivedOrder);
        receivedOrdersRepository.save(receivedOrder);


        return OrderResponse.builder()
                .buyer(buyer.getFirstName() + " " + buyer.getLastName())
                .seller(seller.getFirstName() + " " + seller.getLastName())
                .orderType(ResponseMessages.SENT_ORDER.getMessage())
                .carId(sentOrder.getCarId())
                .price(sentOrder.getPrice())
                .status(ResponseMessages.ORDER_PENDING_ACCEPTANCE.getMessage())
                .build();
    }

    public ResponseMessages updatePrice(OrderRequest request, String userId, String orderId) {
        SentOrders order = orderRepository.findByOrderId(orderId);
        ReceivedOrders receivedOrder = receivedOrdersRepository.findByOrderId(orderId);
        UserEntity user = repository.findByUserId(userId);

        if (order == null || receivedOrder == null)
            throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());

        if (!user.getOrders().contains(order))
            throw new UserServiceException(ErrorMessages.ONLY_BUYERS_CAN_UPDATE_ORDER_PRICE.getErrorMessage());

        order.setPrice(request.getPrice());
        receivedOrder.setPrice(request.getPrice());

        orderRepository.save(order);
        receivedOrdersRepository.save(receivedOrder);


        return ResponseMessages.PRICE_UPDATED;

    }

    public ResponseMessages acceptOrder(String userId, String orderId) {


        SentOrders order = orderRepository.findByOrderId(orderId);
        ReceivedOrders receivedOrder = receivedOrdersRepository.findByOrderId(orderId);
        UserEntity user = repository.findByUserId(userId);

        if (user.getReceivedOrders().contains(receivedOrder)) {
            receivedOrder.setStatus(ResponseMessages.ORDER_ACCEPTED.getMessage());
            order.setStatus(ResponseMessages.ORDER_ACCEPTED.getMessage());

            orderRepository.save(order);
            receivedOrdersRepository.save(receivedOrder);

        } else {
            throw new UserServiceException(ErrorMessages.ONLY_SELLERS_CAN_UPDATE_STATUS.getErrorMessage());
        }


        return ResponseMessages.ORDER_ACCEPTED;

    }

    public ResponseMessages rejectOrder(String userId, String orderId) {

        SentOrders order = orderRepository.findByOrderId(orderId);
        ReceivedOrders receivedOrder = receivedOrdersRepository.findByOrderId(orderId);
        UserEntity user = repository.findByUserId(userId);

        if (user.getReceivedOrders().contains(receivedOrder)) {
            receivedOrder.setStatus(ResponseMessages.ORDER_REJECTED.getMessage());
            order.setStatus(ResponseMessages.ORDER_REJECTED.getMessage());

            orderRepository.save(order);
            receivedOrdersRepository.save(receivedOrder);

        } else {
            throw new UserServiceException(ErrorMessages.ONLY_SELLERS_CAN_UPDATE_STATUS.getErrorMessage());
        }


        return ResponseMessages.ORDER_REJECTED;

    }

    public OperationalResult markAsSold(String carId) {
        Car car = carRepository.findByCarId(carId);
        if (car == null) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());

        UserEntity user = repository.findByUserId(car.getOwner().getUserId());

        if (user == null) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());

        car.setStatus(Constants.SOLD.getMessage());
        carRepository.save(car);

        return OperationalResult.SOLD;

    }


    public OperationalResult adPriceUpdate(OrderRequest request, String carId) {
        Car car = carRepository.findByCarId(carId);
        if (car == null) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());

        UserEntity user = repository.findByUserId(car.getOwner().getUserId());

        if (user == null) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());

        car.setPrice(request.getPrice());
        carRepository.save(car);

        return OperationalResult.UPDATED;

    }


    public CarAdsResponse viewCar(String carId) {
        Car car = carRepository.findByCarId(carId);
        if (car == null) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());


        return CarAdsResponse.builder()
                .response("Ads posted on " + String.valueOf(date))
                .owner(car.getOwner().getFirstName() + " " + car.getOwner().getLastName())
                .carId(car.getCarId())
                .bodyType(car.getBodyType())
                .createdOn(car.getCreatedOn())
                .image(car.getImage())
                .model(car.getModel())
                .manufacturer(car.getManufacturer())
                .state(car.getState())
                .status(car.getStatus())
                .price(car.getPrice())
                .build();

    }


    public List<CarAdsResponse> viewAvailaleCars() {
        List<Car> cars = carRepository.findAllByStatus(Constants.AVAILABLE.getMessage());
        if (cars == null) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());
        List<CarAdsResponse> carsList = new ArrayList<>();

        for (Car car : cars) {
            CarAdsResponse carResponse = CarAdsResponse.builder()
                    .response("Ads posted on " + String.valueOf(date))
                    .owner(car.getOwner().getFirstName() + " " + car.getOwner().getLastName())
                    .carId(car.getCarId())
                    .bodyType(car.getBodyType())
                    .createdOn(car.getCreatedOn())
                    .image(car.getImage())
                    .model(car.getModel())
                    .manufacturer(car.getManufacturer())
                    .state(car.getState())
                    .status(car.getStatus())
                    .price(car.getPrice())
                    .build();
            carsList.add(carResponse);
        }
        if (carsList.isEmpty()) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());
        return carsList;

    }


    public List<CarAdsResponse> viewAvailaleCarsWithPrice(OrderRequest request) {
        List<Car> cars = carRepository.findAllByStatus(Constants.AVAILABLE.getMessage());
        if (cars == null) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());
        List<CarAdsResponse> carsList = new ArrayList<>();

        for (Car car : cars) {
            CarAdsResponse carResponse = CarAdsResponse.builder()
                    .response("Ads posted on " + String.valueOf(date))
                    .owner(car.getOwner().getFirstName() + " " + car.getOwner().getLastName())
                    .carId(car.getCarId())
                    .bodyType(car.getBodyType())
                    .createdOn(car.getCreatedOn())
                    .image(car.getImage())
                    .model(car.getModel())
                    .manufacturer(car.getManufacturer())
                    .state(car.getState())
                    .status(car.getStatus())
                    .price(car.getPrice())
                    .build();
            if (carResponse.getPrice() <= request.getPrice()) {
                carsList.add(carResponse);
            }

        }

        if (carsList.isEmpty()) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());

        return carsList;

    }


    public DeleteResponse deleteAd(String carId) {
        Car car = carRepository.findByCarId(carId);
        if (car == null) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());
        carRepository.delete(car);

        return DeleteResponse.builder()
                .operationalName(OperationalName.DELETE.name())
                .operationalResult(ResponseMessages.ADS_DELETED.getMessage())
                .build();
    }

    public List<CarAdsResponse> viewAllCars() {
        List<Car> cars = carRepository.findAll();
        List<CarAdsResponse> carsList = new ArrayList<>();

        for (Car car : cars) {
            CarAdsResponse carResponse = CarAdsResponse.builder()
                    .response("Ads posted on " + String.valueOf(date))
                    .owner(car.getOwner().getFirstName() + " " + car.getOwner().getLastName())
                    .carId(car.getCarId())
                    .bodyType(car.getBodyType())
                    .createdOn(car.getCreatedOn())
                    .image(car.getImage())
                    .model(car.getModel())
                    .manufacturer(car.getManufacturer())
                    .state(car.getState())
                    .status(car.getStatus())
                    .price(car.getPrice())
                    .build();
            carsList.add(carResponse);
        }
        if (carsList.isEmpty()) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());
        return carsList;

    }


    public List<CarAdsResponse> viewSoldCars() {
        List<Car> cars = carRepository.findAllByStatus(Constants.SOLD.getMessage());
        if (cars == null) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());
        List<CarAdsResponse> carsList = new ArrayList<>();

        for (Car car : cars) {
            CarAdsResponse carResponse = CarAdsResponse.builder()
                    .response("Ads posted on " + String.valueOf(date))
                    .owner(car.getOwner().getFirstName() + " " + car.getOwner().getLastName())
                    .carId(car.getCarId())
                    .bodyType(car.getBodyType())
                    .createdOn(car.getCreatedOn())
                    .image(car.getImage())
                    .model(car.getModel())
                    .manufacturer(car.getManufacturer())
                    .state(car.getState())
                    .status(car.getStatus())
                    .price(car.getPrice())
                    .build();
            carsList.add(carResponse);
        }
        if (carsList.isEmpty()) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());
        return carsList;

    }


    public List<CarAdsResponse> viewCarsByBodyType(CarFilterRequest request) {
        List<Car> cars = carRepository.findAllByBodyType(request.getFilter());
        if (cars == null) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());
        List<CarAdsResponse> carsList = new ArrayList<>();

        for (Car car : cars) {
            CarAdsResponse carResponse = CarAdsResponse.builder()
                    .response("Ads posted on " + String.valueOf(date))
                    .owner(car.getOwner().getFirstName() + " " + car.getOwner().getLastName())
                    .carId(car.getCarId())
                    .bodyType(car.getBodyType())
                    .createdOn(car.getCreatedOn())
                    .image(car.getImage())
                    .model(car.getModel())
                    .manufacturer(car.getManufacturer())
                    .state(car.getState())
                    .status(car.getStatus())
                    .price(car.getPrice())
                    .build();
            carsList.add(carResponse);

        }
        if (carsList.isEmpty()) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());
        return carsList;

    }




    public List<CarAdsResponse> viewAvailableAndNewCars() {
        List<Car> cars = carRepository.findAll();
        List<CarAdsResponse> carsList = new ArrayList<>();

        for (Car car : cars) {
            CarAdsResponse carResponse = CarAdsResponse.builder()
                    .response("Ads posted on " + String.valueOf(date))
                    .owner(car.getOwner().getFirstName() + " " + car.getOwner().getLastName())
                    .carId(car.getCarId())
                    .bodyType(car.getBodyType())
                    .createdOn(car.getCreatedOn())
                    .image(car.getImage())
                    .model(car.getModel())
                    .manufacturer(car.getManufacturer())
                    .state(car.getState())
                    .status(car.getStatus())
                    .price(car.getPrice())
                    .build();
            if (carResponse.getStatus().equalsIgnoreCase(Constants.AVAILABLE.getMessage()) && carResponse.getState().equalsIgnoreCase(Constants.NEW.getMessage())){
                carsList.add(carResponse);
            }

        }

        if (carsList.isEmpty()) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());
        return carsList;

    }

    public List<CarAdsResponse> viewAvailableAndUsedCars() {
        List<Car> cars = carRepository.findAll();
        List<CarAdsResponse> carsList = new ArrayList<>();

        for (Car car : cars) {
            CarAdsResponse carResponse = CarAdsResponse.builder()
                    .response("Ads posted on " + String.valueOf(date))
                    .owner(car.getOwner().getFirstName() + " " + car.getOwner().getLastName())
                    .carId(car.getCarId())
                    .bodyType(car.getBodyType())
                    .createdOn(car.getCreatedOn())
                    .image(car.getImage())
                    .model(car.getModel())
                    .manufacturer(car.getManufacturer())
                    .state(car.getState())
                    .status(car.getStatus())
                    .price(car.getPrice())
                    .build();
            if (carResponse.getStatus().equalsIgnoreCase(Constants.AVAILABLE.getMessage()) && carResponse.getState().equalsIgnoreCase(Constants.USED.getMessage())){
                carsList.add(carResponse);
            }

        }

        if (carsList.isEmpty()) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());
        return carsList;

    }

    public List<CarAdsResponse> viewAvailableCarsManufacturer(CarFilterRequest request) {
        List<Car> cars = carRepository.findAll();
        List<CarAdsResponse> carsList = new ArrayList<>();

        for (Car car : cars) {
            CarAdsResponse carResponse = CarAdsResponse.builder()
                    .response("Ads posted on " + String.valueOf(date))
                    .owner(car.getOwner().getFirstName() + " " + car.getOwner().getLastName())
                    .carId(car.getCarId())
                    .bodyType(car.getBodyType())
                    .createdOn(car.getCreatedOn())
                    .image(car.getImage())
                    .model(car.getModel())
                    .manufacturer(car.getManufacturer())
                    .state(car.getState())
                    .status(car.getStatus())
                    .price(car.getPrice())
                    .build();
            if (carResponse.getStatus().equalsIgnoreCase(Constants.AVAILABLE.getMessage()) && carResponse.getManufacturer().equalsIgnoreCase(request.getFilter())){
                carsList.add(carResponse);
            }

        }

        if (carsList.isEmpty()) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());
        return carsList;

    }

    public FlagResponse flagAds(FlagRequest request, String carId) {
        Car car = carRepository.findByCarId(carId);
        if (car == null) throw new UserServiceException(ErrorMessages.RECORD_NOT_FOUND.getErrorMessage());

Flag flag = Flag.builder()
        .carId(car)
        .userId(car.getOwner())
        .reportId(userUtils.generateReportId(6))
        .createOn(date)
        .reason(request.getReason())
        .description(request.getDescription())
        .build();

car.getOwner().getFlags().add(flag);
car.getFlags().add(flag);
flagRepository.save(flag);

return FlagResponse.builder()
        .carId(carId)
        .owner(car.getOwner().getFirstName() +" " + car.getOwner().getLastName())
        .createOn(flag.getCreateOn())
        .reason(flag.getReason())
        .description(flag.getDescription())
        .build();

    }
}

