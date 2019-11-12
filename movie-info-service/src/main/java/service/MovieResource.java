/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Movie;
import model.MovieSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Educom
 */
@RestController
@RequestMapping("/movies")
public class MovieResource {
    
    @Value("${api.key}")
    private String apiKey;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @RequestMapping("/{movieId}")
    public Movie getMovieInfo(@PathVariable("movieId") String movieId){
        try {
            ResponseEntity<String> response = restTemplate.getForEntity("https://omdbapi.com/?i=tt" + movieId + "&apikey=" + apiKey, String.class);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            String title = root.path("Title").asText();
            String plot = root.path("Plot").asText();
            
            return new Movie(movieId, title, plot);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(MovieResource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
