package com.niit.FavouriteMovieService.controller;

import com.niit.FavouriteMovieService.domain.FavouriteMovie;
import com.niit.FavouriteMovieService.domain.User;
import com.niit.FavouriteMovieService.exception.FavouriteMovieAlreadyExistException;
import com.niit.FavouriteMovieService.exception.FavouriteMovieNotFoundException;
import com.niit.FavouriteMovieService.exception.UserAlreadyExistException;
import com.niit.FavouriteMovieService.exception.UserNotFoundException;
import com.niit.FavouriteMovieService.service.IFavouriteMovieService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2")
public class FavouriteMovieController {
    ResponseEntity responseEntity;
    IFavouriteMovieService iFavouriteMovieService;

    @Autowired
    public FavouriteMovieController(IFavouriteMovieService iFavouriteMovieService) {
        this.iFavouriteMovieService = iFavouriteMovieService;
    }

    private String getUserIdFromClaims(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("claims");
        System.out.println("User ID from claims :: " + claims.getSubject());
        return claims.getSubject();
    }

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestBody User user) throws UserAlreadyExistException {
        try {
            User registeredUser = iFavouriteMovieService.registerUser(user);
            responseEntity = new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (UserAlreadyExistException e) {
            throw new UserAlreadyExistException();
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @PutMapping("/user/update")
    public ResponseEntity updateUser(@RequestBody User user, HttpServletRequest request) throws UserNotFoundException {
        try {
            User updatedUser = iFavouriteMovieService.updateUser(user, getUserIdFromClaims(request));
            responseEntity = new ResponseEntity<>("updated", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        } catch (Exception e) {
            responseEntity = new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }


    @PostMapping("/user/saveFavouriteMovie")
    public ResponseEntity<?> saveMovieToUserFavorites(@RequestBody FavouriteMovie favouriteMovie, HttpServletRequest request)
            throws UserNotFoundException, FavouriteMovieAlreadyExistException {
        try {
            // Save the favourite movie to the user's list
            responseEntity= new ResponseEntity<>(iFavouriteMovieService.saveFavouriteMovieToList(favouriteMovie, getUserIdFromClaims(request)), HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            // Rethrow the UserNotFoundException if the user does not exist
            throw new UserNotFoundException();
        } catch (FavouriteMovieAlreadyExistException e) {
            // Rethrow if the movie already exists in the user's favourite list
            throw new FavouriteMovieAlreadyExistException();
        } catch (Exception e) {
            // Return 500 status for any other unexpected exceptions
            responseEntity= new ResponseEntity<>("An error occurred while saving the movie", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }


    @GetMapping("/user/getAllFavouriteMovies")
    public ResponseEntity<?> getAllMoviesFromUserFavorites(HttpServletRequest request) throws UserNotFoundException {
        try {
            responseEntity = new ResponseEntity<>(iFavouriteMovieService.getAllFavouriteMoviesFromList(getUserIdFromClaims(request)), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        }catch (Exception e){
            responseEntity=new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }

    @DeleteMapping("/movies/{movieId}")
    public ResponseEntity<?> deleteMovieFromUserFavorites( @PathVariable String movieId,HttpServletRequest request) {
        try {
            iFavouriteMovieService.deleteMovieFromFavList(getUserIdFromClaims(request),movieId);
            return new ResponseEntity<>("Movie deleted successfully from user's favorites.", HttpStatus.OK);
        } catch (UserNotFoundException | FavouriteMovieNotFoundException e) {
            // Use e.getMessage() to provide the error message in the response body
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Generic error handling
            return new ResponseEntity<>("An error occurred while deleting the movie.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/search/{movieName}")
    public ResponseEntity<?> searchMovieFromUserFavorites(@PathVariable String movieName,HttpServletRequest request) {
        try {
            List<FavouriteMovie> users = iFavouriteMovieService.searchMovieFromFavList(movieName,getUserIdFromClaims(request));
            return new ResponseEntity<>(users,HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>("No users found with the movie name: " + movieName,HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
