package com.SecurityAug13th.RealTutorial.controllers;



import com.SecurityAug13th.RealTutorial.userRequest.*;
import com.SecurityAug13th.RealTutorial.service.UserService;
import com.SecurityAug13th.RealTutorial.response.AuthenticationResponse;
import com.SecurityAug13th.RealTutorial.response.DeleteResponse;
import com.SecurityAug13th.RealTutorial.response.PageModel;
import com.SecurityAug13th.RealTutorial.response.TransferResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody UserLoginRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PutMapping(path = "/{email}")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest request, @PathVariable String email) {
        return ResponseEntity.ok(service.transfer(request, email));
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<AuthenticationResponse> getUser(@PathVariable String userId){
        return ResponseEntity.ok(service.getUser(userId));
    }
    @DeleteMapping (path = "/{userId}")
    public ResponseEntity<DeleteResponse> deleteUser(@PathVariable String userId){
        return ResponseEntity.ok(service.deleteUser(userId));
    }
    @GetMapping
    public ResponseEntity<List<PageModel>> getUsers(@RequestParam (value ="page", defaultValue = "0") int page,
                                                    @RequestParam(value = "limit", defaultValue = "25") int limit, Pageable pageable){
        return ResponseEntity.ok(service.getUsers(page,limit, pageable));
    }


//    @PostMapping("/refresh-token")
//    public void refreshToken(
//            HttpServletRequest request,
//            HttpServletResponse response
//    ) throws IOException {
//        service.refreshToken(request, response);
//    }

// to do
}
