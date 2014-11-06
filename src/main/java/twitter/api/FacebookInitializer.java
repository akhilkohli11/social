package twitter.api;

import facebook4j.*;
import facebook4j.auth.AccessToken;
import org.joda.time.DateTime;

import java.net.URL;
import java.util.Date;
import java.util.Map;

/**
 * Created by akohli on 11/2/14.
 */
public class FacebookInitializer {
    static Facebook facebook;

    public static void main(String args[]) throws Exception
    {
            init();

    }

    public static void init() throws Exception {
        facebook = new FacebookFactory().getInstance();
        facebook.setOAuthAppId("204880952860915", "a4bc36ce7da8ec474877da4ff9d85594");
        // facebook.setOAuthPermissions(commaSeparetedPermissions);
        facebook.setOAuthAccessToken(new AccessToken("CAAC6VpBqoPMBADSi7j1sFQr1iKsztT2Kji6qHsnjB3hTkLnOElynSoUAJLIboFAVoYr8CIgKGdCC9C0ET8E4Su4iBs72r9Jn4GWrp9vN50z3wscMIlbOwVCAZABsMoWzZCmVCNYZB2L9kJQ3xc6ZANZCuSuar81K9EldC4ULSvGsF1400VwJS85tmz9pQqMCpEN8f6Kt3jIZC0H9gZC76hI", null));

    }


    public static void populate( Map<String,Integer> searchMap) throws Exception {


        int newcount = 0;
        while (newcount++ < 500) {


            Date date = new Date();
            for (Map.Entry<String, Integer> showSearch : searchMap.entrySet()) {
                try {
                    Reading reading = new Reading();
                    Date since = new DateTime(date).minusDays(4).toDate();
                    Date until = new DateTime(date).minusDays(1).toDate();
                    date = until;
                    reading.since(since.toString());
                    reading.until(until.toString());
                    ResponseList<Post> results = facebook.searchPosts(showSearch.getKey()+" TV", reading);
                    System.out.println(showSearch.getKey()+" TV"+results.size());
                    for (Post post : results) {
                        System.out.println( post.getLikes().size()+"  "+post.getComments().size());
                        //  post.get

                    }
                    ResponseList<facebook4j.Page> pages=facebook.searchPages(showSearch.getKey()+" Series", reading);
                    for (Page page : pages) {
                        CloudSolrPersistenceLayer.getInstance().populateFacebook(showSearch.getKey(),showSearch.getValue(),page.getLink(),
                                page.getLikes(),page.getCategory(),page.getWereHereCount(),
                                page.getId(),page.getCreatedTime());


                    }

                    pages=facebook.searchPages(showSearch.getKey()+" season", reading);
                    for (Page page : pages) {
                        CloudSolrPersistenceLayer.getInstance().populateFacebook(showSearch.getKey(),showSearch.getValue(),page.getLink(),
                                page.getLikes(),page.getCategory(),page.getWereHereCount(),
                                page.getId(),page.getCreatedTime());



                    }

                    pages=facebook.searchPages(showSearch.getKey()+" show", reading);
                    for (Page page : pages) {
                        CloudSolrPersistenceLayer.getInstance().populateFacebook(showSearch.getKey(),showSearch.getValue(),page.getLink(),
                                page.getLikes(),page.getCategory(),page.getWereHereCount(),
                                page.getId(),page.getCreatedTime());


                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }
        Thread.sleep(1000*60*300);
    }


}
