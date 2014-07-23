package twitter.api;

/**
 * Created by akohli on 7/21/14.
 */
public class RefactoredTumblrTwitterMain {
    public static void main(String args[])
    {
       TumblrDaemon tumblrDaemon=new TumblrDaemon();
        tumblrDaemon.init();
        TwitterTweetDaeomon twitterTweetDaeomon=new TwitterTweetDaeomon();
        twitterTweetDaeomon.init();
       TwitterTumblrDaemon twitterTumblrDaemon=new TwitterTumblrDaemon();
       twitterTumblrDaemon.init();
    }
}
