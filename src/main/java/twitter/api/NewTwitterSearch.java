
    package twitter.api;

    import org.joda.time.DateTime;
    import twitter4j.*;
    import twitter4j.auth.AccessToken;
    import twitter4j.auth.RequestToken;

    import java.io.*;
    import java.text.DateFormat;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.Date;
    import java.util.List;

    /**
     * Created by akohli on 6/30/14.
     */
    public class NewTwitterSearch {
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

            {
                Query query = new Query("Gameofthrones");

                //query.setUntil(formatter5.format(until).toString());
                // query.setUntil(formatter5.format(until).toString());
                query.setSince("2014-09-01");

                QueryResult result = twitter.search(query);
                for (Status status1 : result.getTweets()) {
                    System.out.println("@" + status1.getUser().getScreenName() + ":" + status1.getText() + ":" + status1.getCreatedAt());

                }


            }
        }
        private static void storeAccessToken(int useId, AccessToken accessToken){
            //store accessToken.getToken()
            //store accessToken.getTokenSecret()
        }

    }


