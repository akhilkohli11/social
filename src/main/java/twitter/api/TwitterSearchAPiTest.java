package twitter.api;

import org.joda.time.DateTime;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by akohli on 6/30/14.
 */
public class TwitterSearchAPiTest {
    static SimpleDateFormat formatter5=new SimpleDateFormat("yyyy-MM-dd");
    public static void main(String args[]) throws Exception
    {
        Twitter twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer("nDhWQNt3buDkIWNyBp9iilIXp", "OAFBjrd8mHCgMV6YXE5SgadBKP4Fl0cHBM9zU94vgn6OIDafHC");
        RequestToken requestToken = twitter.getOAuthRequestToken();
        AccessToken accessToken = null;
        accessToken = new AccessToken("2524277576-3OyRFktWxMMB3NZw68C71Q1eZTrCc3tQQyWN8t0", "B3XQM3PM88xyZP6uiIA9RRaWkVNa4QrIaTShSJlwMZrzb");

        //persist to the accessToken for future reference.
        twitter.setOAuthAccessToken(accessToken);
        Date date = new Date();
        Date newdaysAgo = new DateTime(date).minusDays(5).toDate();
        Date until = new DateTime(date).minusDays(8).toDate();
        System.out.println(newdaysAgo);
        System.out.println(formatter5.format(newdaysAgo));
        date=newdaysAgo;
//        Long unixTime =
//        Long newUnixTime = (long) until.getTime()/1000;
        List<String> showNames= Arrays.asList("BiggBossSuvarna", "Mahaparva", "AkashaDeepa", "Mahabharat", "TaarakMehtaKaOoltahChashmah", "BalikaVadhu"
                , "Jhalak","comedynightswithkapil");

        for(String showName :showNames) {
            Query query = new Query("@mahabharatA_sp");
            //query.setUntil(formatter5.format(until).toString());
            // query.setUntil(formatter5.format(until).toString());

            QueryResult result = twitter.search(query);
            for (Status status1 : result.getTweets()) {
                System.out.println("@" + status1.getUser().getScreenName() + ":" + status1.getText() + ":" + status1.getCreatedAt());
            }


            do {

                List<Status> tweets = result.getTweets();
                long max = 0;
                for (Status tweet : tweets) {

                    System.out.println("Tweet: " + tweet.getText() + "    " + tweet.getCreatedAt());
                    if (tweet.getId() > max) {
                        max = tweet.getId();
                    }

                }

                query = new Query("@mahabharata_sp");
                //   query.setUntil(formatter5.format(until).toString());
                if (query != null)

                    result = twitter.search(query);
                Thread.sleep(1000 * 60);

            } while (query != null);
        }
    }
    private static void storeAccessToken(int useId, AccessToken accessToken){
        //store accessToken.getToken()
        //store accessToken.getTokenSecret()
    }

}
