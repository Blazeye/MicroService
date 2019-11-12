/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Educom
 */
// Tags the class as a source of bean definitions for the application context.
@Configuration
// Tells Spring to look for other components, configurations, and services
// in the server package, letting it find the controllers.
@ComponentScan
// Tells Spring to start adding beans based on classpath settings, other beans, 
// and various property settings. 
@EnableAutoConfiguration
public class Application {
    
    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
    
    public static void main(String[] args){
        SpringApplication.run(Application.class, args);
    }
}

