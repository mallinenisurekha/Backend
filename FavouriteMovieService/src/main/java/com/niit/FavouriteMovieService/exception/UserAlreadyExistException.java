package com.niit.FavouriteMovieService.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT,reason = "User Already Exists")
public class UserAlreadyExistException extends Exception{
}
