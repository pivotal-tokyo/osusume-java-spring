package com.tokyo.beach.application.user;

import com.tokyo.beach.application.logon.LogonCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private UserRepository userRepository;

    @Autowired
    public UserController(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public DatabaseUser registerUser(@RequestBody LogonCredentials credentials) {
        return userRepository.create(credentials.getEmail(), credentials.getPassword());
    }

    @RequestMapping(value = "/unauthenticated")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String authError() {
        return "Authentication Error";
    }

}
