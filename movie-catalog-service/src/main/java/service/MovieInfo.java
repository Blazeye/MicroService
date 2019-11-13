/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import model.CatalogItem;
import model.Movie;
import model.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Educom
 */
@Service
public class MovieInfo {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem")
    public CatalogItem getCatalogItem(Rating rating){
        Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
        return new CatalogItem(movie.getName(), "movie " + movie.getMovieId(), rating.getRating());
    }
    
    public CatalogItem getFallbackCatalogItem(Rating rating) {
        return new CatalogItem("Movie name not found", "", rating.getRating());
    }
}
