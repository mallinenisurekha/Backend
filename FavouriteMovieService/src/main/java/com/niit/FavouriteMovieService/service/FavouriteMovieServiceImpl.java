package com.niit.FavouriteMovieService.service;

import com.niit.FavouriteMovieService.domain.FavouriteMovie;
import com.niit.FavouriteMovieService.domain.User;
import com.niit.FavouriteMovieService.exception.FavouriteMovieAlreadyExistException;
import com.niit.FavouriteMovieService.exception.FavouriteMovieNotFoundException;
import com.niit.FavouriteMovieService.exception.UserAlreadyExistException;
import com.niit.FavouriteMovieService.exception.UserNotFoundException;
import com.niit.FavouriteMovieService.proxy.IUserProxy;
import com.niit.FavouriteMovieService.repository.IFavouriteMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class FavouriteMovieServiceImpl implements IFavouriteMovieService{
    IFavouriteMovieRepository iFavouriteMovieRepository;
    IUserProxy iUserProxy;

    @Autowired
    public FavouriteMovieServiceImpl(IFavouriteMovieRepository iFavouriteMovieRepository, IUserProxy iUserProxy) {
        this.iFavouriteMovieRepository = iFavouriteMovieRepository;
        this.iUserProxy =iUserProxy;
    }

    @Override
    public User registerUser(User user) throws UserAlreadyExistException {
        if (iFavouriteMovieRepository.findById(user.getUserId()).isPresent()){
            throw new UserAlreadyExistException();
        }
        User user1=iFavouriteMovieRepository.save(user);

        if (!user1.getUserId().isEmpty() && !user1.getPassword().isEmpty()){
            iUserProxy.saveUser(user1);
        }
        return user1;
    }

    @Override
    public User updateUser(User user,String userId) throws UserNotFoundException {
        User existingUser=iFavouriteMovieRepository.findById(userId).get();
        if (existingUser==null){
            throw new UserNotFoundException();
        }
        if (user.getUsername() != null && !user.getUsername().isEmpty()) {
            existingUser.setUsername(user.getUsername());
        }
        if(user.getEmail() != null && !user.getEmail().isEmpty()) {
            existingUser.setEmail(user.getEmail());
        }
        if (user.getPassword() !=null && !user.getPassword().isEmpty()){
            existingUser.setPassword(user.getPassword());
        }
        if (user.getImageUrl() !=null && !user.getImageUrl().isEmpty()){
            existingUser.setImageUrl(user.getImageUrl());
        }

      User user1=  iFavouriteMovieRepository.save(existingUser);

        if (!user1.getUserId().isEmpty()){
            iUserProxy.updateUser(user);
        }
        return user1;
    }

    @Override
    public User saveFavouriteMovieToList(FavouriteMovie favouriteMovie, String userId) throws UserNotFoundException, FavouriteMovieAlreadyExistException {
        // Check if user exists
        if(iFavouriteMovieRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException();
        }

        User user = iFavouriteMovieRepository.findById(userId).get();

        // Check if user's movie list is null
        if(user.getMovieDetails() == null) {
            user.setMovieDetails(Arrays.asList(favouriteMovie));
        } else {
            // Check if the movie already exists in the list
            List<FavouriteMovie> movies = user.getMovieDetails();
            for (FavouriteMovie movie : movies) {
                if (movie.getMovieId().equals(favouriteMovie.getMovieId())) {
                    throw new FavouriteMovieAlreadyExistException();
                }
            }
            // Add the movie to the list
            movies.add(favouriteMovie);
            user.setMovieDetails(movies);
        }

        // Save the user with the updated movie list
        return iFavouriteMovieRepository.save(user);
    }


    @Override
    public List<FavouriteMovie> getAllFavouriteMoviesFromList(String userId) throws UserNotFoundException {
        // Get all products from the User list
        if(iFavouriteMovieRepository.findById(userId).isEmpty())
        {
            throw new UserNotFoundException();
        }
        return iFavouriteMovieRepository.findById(userId).get().getMovieDetails();
    }
    @Override
    public void deleteMovieFromFavList(String userId, String movieId) throws UserNotFoundException, FavouriteMovieNotFoundException {
        User existingUser = iFavouriteMovieRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        List<FavouriteMovie> movieDetails = existingUser.getMovieDetails();
        Optional<FavouriteMovie> movieToDelete = movieDetails.stream().
                filter(movie -> movie.getMovieId().equals(movieId))
                .findFirst();

        if (movieToDelete.isPresent()) {
            movieDetails.remove(movieToDelete.get());
            existingUser.setMovieDetails(movieDetails);
            iFavouriteMovieRepository.save(existingUser);
        } else {
            throw new FavouriteMovieNotFoundException();
        }
    }


    @Override
    public List<FavouriteMovie> searchMovieFromFavList(String movieName, String userId) throws UserNotFoundException {
        List<FavouriteMovie> movieList = iFavouriteMovieRepository.findByMovieDetailsMovieName(movieName);
        if (movieList.isEmpty()) {
            throw new UserNotFoundException();
        }
        return movieList;
    }
}
