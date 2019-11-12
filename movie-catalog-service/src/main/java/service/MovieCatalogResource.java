package service;




import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
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
    @HystrixCommand(fallbackMethod = "getFallbackCatalog")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
        
        UserRating ratings = restTemplate.getForObject("http://ratings-data-service/ratingsdata/users/" + userId, UserRating.class);
        
        return ratings.getUserRating().stream().map(rating -> {
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
            
            return new CatalogItem(movie.getName(), "movie " + movie.getMovieId(), rating.getRating());
                    
                    }).collect(Collectors.toList());
        
//        Asynchronous version:
//        webClientBuilder.build()
//                        .get()
//                        .uri(s: "http:localhost:8081/movies/" + rating.getMovieId())
//                        .retrieve()
//                        .bodyToMono(Movie.class)

    }
    
    // The fallback method for hystrix for when the servers get overloaded 
    public List<CatalogItem> getFallbackCatalog(@PathVariable("userId") String userId){
        return Arrays.asList(new CatalogItem("No movie", "", 0));
    }
}
