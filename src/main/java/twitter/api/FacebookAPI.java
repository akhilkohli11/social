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
        facebook.setOAuthAppId("204880952860915", "a4bc36ce7da8ec474877da4ff9d85594");
        // facebook.setOAuthPermissions(commaSeparetedPermissions);
        facebook.setOAuthAccessToken(new AccessToken("CAAC6VpBqoPMBADSi7j1sFQr1iKsztT2Kji6qHsnjB3hTkLnOElynSoUAJLIboFAVoYr8CIgKGdCC9C0ET8E4Su4iBs72r9Jn4GWrp9vN50z3wscMIlbOwVCAZABsMoWzZCmVCNYZB2L9kJQ3xc6ZANZCuSuar81K9EldC4ULSvGsF1400VwJS85tmz9pQqMCpEN8f6Kt3jIZC0H9gZC76hI", null));
        Date date = new Date();
        while (true) {
            Reading reading = new Reading();
            Date since = new DateTime(date).minusDays(4).toDate();
            Date until = new DateTime(date).minusDays(1).toDate();
            date = until;
            reading.since(since.toString());
            reading.until(until.toString());
            ResponseList<facebook4j.Post> results = facebook.searchPosts("Game Of Thrones", reading);
            // ResponseList<JSONObject> results = facebook.search("Game of Thrones TV", reading);
            System.out.println(results.size());
            for (Post post : results) {
                System.out.println( post.getLikes().size()+"  "+post.getComments().size());
                //  post.get

            }
            ResponseList<facebook4j.Page> pages=facebook.searchPages("Game Of Thrones", reading);
            for (Page page : pages) {
                System.out.println( "Page"+page.getLikes()+"  "+page.getTalkingAboutCount());
                //  post.get

            }
        }
    }
}

