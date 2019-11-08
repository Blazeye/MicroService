package service;




import model.Rating;
import model.Movie;
import java.util.Arrays;
import model.CatalogItem;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Educom
 */
@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
        
        UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/" + userId, UserRating.class);
        
        // foreach movieId, call MovieInfoService and get movie details 
        // stream().map() lets you convert an object to something else
        return ratings.getUserRating().stream().map(rating -> {
            Movie movie = restTemplate.getForObject("http://localhost:8081/movies/" + rating.getMovieId(), Movie.class);
            
            return new CatalogItem(movie.getName(), "movie " + movie.getMovieId(), rating.getRating());
                    
                    }).collect(Collectors.toList());
        
//        Asynchronous version:
//        webClientBuilder.build()
//                        .get()
//                        .uri(s: "http:localhost:8081/movies/" + rating.getMovieId())
//                        .retrieve()
//                        .bodyToMono(Movie.class)

    }
}
