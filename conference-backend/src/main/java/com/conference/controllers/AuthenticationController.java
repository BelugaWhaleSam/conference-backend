package com.conference.controllers;

import com.conference.exceptions.EmailAlreadyTakenException;
import com.conference.exceptions.EmailFailedToSendException;
import com.conference.exceptions.UserDoesNotExistException;
import com.conference.models.ApplicationUser;
import com.conference.models.RegistrationObject;
import com.conference.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @ExceptionHandler({EmailFailedToSendException.class})
    public ResponseEntity<String> handleFailedEmail() {
        return new ResponseEntity<String>("Email failed to send, try again in a moment", HttpStatus.CONFLICT);
    }

    @ExceptionHandler({EmailAlreadyTakenException.class})
    public ResponseEntity<String> handleEmailTaken() {
        return new ResponseEntity<String>("The email you have provided is already taken", HttpStatus.CONFLICT);
    }

    @ExceptionHandler({UserDoesNotExistException.class})
    public ResponseEntity<String> handleUserDoesntExist() {
        return new ResponseEntity<String>("The user does not exist", HttpStatus.NOT_FOUND);
    }

    @PostMapping("/register")
    public ApplicationUser registerUser(@RequestBody RegistrationObject ro) {
        return userService.registerUser(ro);
    }

    @PutMapping("/update/phone")
    public ApplicationUser updatePhoneNumber(@RequestBody LinkedHashMap<String,String> body) {
        String username = body.get("username");
        String phone = body.get("phone");

        ApplicationUser user = userService.getUserByUsername(username);

        user.setPhone(phone);

        return userService.updateUser(user);
    }

    @PostMapping("/email/code")
    public ResponseEntity<String> createEmailVerification(@RequestBody LinkedHashMap<String,String> body) {
        userService.generateEmailVerification(body.get("username"));
        return new ResponseEntity<String>("Verification code generated, email sent",HttpStatus.OK);
    }
}
