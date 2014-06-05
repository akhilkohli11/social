package twitter.api;

/**
 * Created by akohli on 6/2/14.
 */
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/twitter")
public class TwitterRestService {
    @GET
    @Path("/load")
    @Produces(MediaType.APPLICATION_JSON)
    public void getTrackInJSON() {
        try {
            System.out.println("started");
            LoadApp.init();
            //   return TwitterDataRetriever.getTweets(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}