package com.niit.UserAuthenticationService.service;


import com.niit.UserAuthenticationService.domain.User;
import com.niit.UserAuthenticationService.exception.InvalidCredentialsException;
import com.niit.UserAuthenticationService.exception.UserAlreadyExistsException;
import com.niit.UserAuthenticationService.exception.UserNotFoundException;

public interface IUserService {
    User saveUser(User user) throws UserAlreadyExistsException;
    User updateUser(User user) throws UserNotFoundException;
    User getUserByUserIdAndPassword(String userId, String password) throws InvalidCredentialsException;
}
