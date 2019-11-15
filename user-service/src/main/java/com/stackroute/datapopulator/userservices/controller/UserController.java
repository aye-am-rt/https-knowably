package com.stackroute.datapopulator.userservices.controller;

import com.stackroute.datapopulator.userservices.domain.JwtResponse;
import com.stackroute.datapopulator.userservices.domain.UserDTO;
import com.stackroute.datapopulator.userservices.exceptions.UserException;
import com.stackroute.datapopulator.userservices.exceptions.UserNotFoundException;
import com.stackroute.datapopulator.userservices.services.UserServiceImpl;
import com.stackroute.datapopulator.userservices.config.JwtTokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Api(value = "User Authentication Rest API")
@CrossOrigin
@RestController
@RequestMapping(value = "api/v1")
public class UserController {

    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private UserServiceImpl userService;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserServiceImpl userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
    }

    //to authenticate a user logged in system and generate a Jwt Token and return that to user for further access
    @ApiOperation(value = "Authenticate User and generate Jwt Token")
    @PostMapping(value = "/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody UserDTO authenticationRequest) throws UserNotFoundException {
        authenticate(authenticationRequest.getName(), authenticationRequest.getPassword());
        final UserDetails userDetails = userService.loadUserByUsername(authenticationRequest.getName());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(String username, String password) throws UserNotFoundException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (BadCredentialsException e) {
            throw new UserNotFoundException(e.getMessage());
        }
    }


    //to register a new user
    @ApiOperation(value = "Register a new User")
    @PostMapping(value = "/register")
    public ResponseEntity<?> saveUser(@RequestBody UserDTO userDTO) throws UserException {
        return new ResponseEntity<>(userService.save(userDTO), HttpStatus.CREATED);
    }
}