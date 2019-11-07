/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Educom
 */
public class Movie {
    private String movieId;
    private String name;
    
    protected Movie(){}

    public Movie(String movieId, String name) {
        this.movieId = movieId;
        this.name = name;
    }
    
    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
