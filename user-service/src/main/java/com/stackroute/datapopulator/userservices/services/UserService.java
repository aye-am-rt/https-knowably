package com.stackroute.datapopulator.userservices.services;

import com.stackroute.datapopulator.userservices.domain.User;
import com.stackroute.datapopulator.userservices.exceptions.UserException;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    public UserDetails getUserByName(String name) throws UserException;
    public User saveUser(User user) throws UserException;
}
