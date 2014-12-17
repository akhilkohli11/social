
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

            ResponseList<twitter4j.Status> statusResponseList=twitter.getUserTimeline("GameOfThrones");
            int i=0;
            while ((i<statusResponseList.size()))
            {
                System.out.println(statusResponseList.get(i).getText());
                MediaEntity[] mediaEntities=statusResponseList.get(i).getExtendedMediaEntities();
                Status status=statusResponseList.get(i);
                String url= "https://twitter.com/" + status.getUser().getScreenName()
                        + "/status/" + status.getId();
                System.out.println("url "+url);
                for(MediaEntity mediaEntity : mediaEntities)
                {
                    System.out.println(mediaEntity.getMediaURL());
                    System.out.println(mediaEntity.getDisplayURL());


                }
                System.out.println(statusResponseList.get(i).getRetweetCount());


                // System.out.println(statusResponseList.get(i).getSource());


                //  System.out.println(statusResponseList.get(i).getId());
                i++;
            }

        }
        private static void storeAccessToken(int useId, AccessToken accessToken){
            //store accessToken.getToken()
            //store accessToken.getTokenSecret()
        }

    }


