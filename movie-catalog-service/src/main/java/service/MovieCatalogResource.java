package service;




import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import model.Rating;
import model.Movie;
import java.util.Arrays;
import model.CatalogItem;
import java.util.List;
import java.util.stream.Collectors;
import model.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Educom
 */
@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    MovieInfo movieInfo;
    
    @Autowired
    UserRatingInfo userRatingInfo;
    
    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId){
        
        UserRating ratings = userRatingInfo.getUserRating(userId);

        return ratings.getUserRating().stream().map(rating -> movieInfo.getCatalogItem(rating))
                                               .collect(Collectors.toList());
        
    }
            
    public List<CatalogItem> getFallbackCatalog(@PathVariable("userId") String userId) {
        return Arrays.asList(new CatalogItem("No movie", "", 0));
    }
}
