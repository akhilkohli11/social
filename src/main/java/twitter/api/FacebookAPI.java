package twitter.api;


import facebook4j.*;
import facebook4j.auth.AccessToken;
import org.joda.time.DateTime;
import facebook4j.internal.org.json.JSONObject;
import java.util.Date;

/**
 * Created by akohli on 9/19/14.
 */
public class FacebookAPI {

    public static void main(String args[]) throws Exception {

        Facebook facebook = new FacebookFactory().getInstance();
            facebook.setOAuthAppId("716670628425125", "c2647b471fc740abd80c30838b041218");
        // facebook.setOAuthPermissions(commaSeparetedPermissions);
        facebook.setOAuthAccessToken(new AccessToken("716670628425125|K8Q1ZVxbMVAbgvXdskRe29L3o7M", null));
        Date date = new Date();
        while (true) {
            Reading reading = new Reading();
            Date since = new DateTime(date).minusDays(4).toDate();
            Date until = new DateTime(date).minusDays(1).toDate();
            date = until;
            reading.since(since.toString());
            reading.until(until.toString());
          //  ResponseList<facebook4j.Post> results = facebook.searchPosts("Game Of Thrones", reading);
            // ResponseList<JSONObject> results = facebook.search("Game of Thrones TV", reading);
          //  System.out.println(results.size());
//            for (Post post : results) {
//                System.out.println( post.getLikes().size()+"  "+post.getComments().size()+" "+
//                post.getLink());
//                //  post.get
//
//            }
            ResponseList<facebook4j.Page> pages=facebook.searchPages("Game of Thrones");
            for (Page page : pages) {
                System.out.println( "Page"+page.getLink()+page.getLikes()+"  "+page.getTalkingAboutCount()
                        +page.getLink()+page.getName());
                Reading reading1=new Reading();
                reading1.filter(page.getId());
                ResponseList<Post> feed = facebook.getFeed(page.getId());
                for (Post post : feed) {
                    System.out.println(post.getSharesCount()+"  "

                            +post.getComments().getCount());
              //  System.out.println( "  "+post.getComments().size()+" "+
               // post.getLink()+post.getName());
                }
//                //  post.get
                           //  post.get
//                ResponseList<facebook4j.Album> photo= facebook.getAlbums(reading1);
//                for(Album album: photo)
//                {
//                    System.out.println(album.getLink());
//                }
            }

        }
    }
}

