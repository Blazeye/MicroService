/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
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
    
    @HystrixCommand(fallbackMethod = "getFallbackCatalogItem",
            threadPoolKey = "movieInfoPool",
            threadPoolProperties = {
                // Maximum amount of threads waiting for a response per bulkhead 
                @HystrixProperty(name = "coreSize", value = "20"),
                // Maximum amount of requests waiting in queue for access to a thread 
                @HystrixProperty(name = "maxQueueSize", value = "10")
            },
            commandProperties = {
                // Timeout is 2s
                @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
                // Checks 5 consecutive threads for timeouts
                @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "5"),
                // Percentage of checked threads in timeout, which triggers circuit break
                @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
                // Sleeps for 5s after circuit break 
                @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000")
            })
    public CatalogItem getCatalogItem(Rating rating){
        Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
        return new CatalogItem(movie.getName(), "movie " + movie.getMovieId(), rating.getRating());
    }
    
    public CatalogItem getFallbackCatalogItem(Rating rating) {
        return new CatalogItem("Movie name not found", "", rating.getRating());
    }
}
