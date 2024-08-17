package com.niit.UserAuthenticationService.controller;

import com.niit.UserAuthenticationService.domain.User;
import com.niit.UserAuthenticationService.exception.InvalidCredentialsException;
import com.niit.UserAuthenticationService.exception.UserAlreadyExistsException;
import com.niit.UserAuthenticationService.exception.UserNotFoundException;
import com.niit.UserAuthenticationService.security.SecurityTokenGenerator;
import com.niit.UserAuthenticationService.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    IUserService iUserService;
    ResponseEntity responseEntity;
    SecurityTokenGenerator securityTokenGenerator;
    @Autowired
    public UserController(IUserService iUserService, SecurityTokenGenerator securityTokenGenerator) {
        this.iUserService = iUserService;
        this.securityTokenGenerator = securityTokenGenerator;
    }
    @PostMapping("/save")
    public ResponseEntity saveUser(@RequestBody User user) throws UserAlreadyExistsException {
        try {
            User user1=iUserService.saveUser(user);
            responseEntity=new ResponseEntity<>(user1,HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            throw new UserAlreadyExistsException();
        }catch (Exception e){
            responseEntity=new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody User user) throws InvalidCredentialsException{
        try{
            User user1=iUserService.getUserByUserIdAndPassword(user.getUserId(), user.getPassword());
           if (user1==null){
               throw new InvalidCredentialsException();
           }
            System.out.println(user1);
           String token=securityTokenGenerator.createToken(user1);
           responseEntity=new ResponseEntity<>(token,HttpStatus.OK);
        } catch (Exception e) {
            responseEntity =new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
    @PutMapping("/update")
    public ResponseEntity updateUser(@RequestBody User user) throws UserNotFoundException {
        try {
            User user1=iUserService.updateUser(user);
            responseEntity = new ResponseEntity<>("updated", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        } catch (Exception e){
            responseEntity=new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }



}
