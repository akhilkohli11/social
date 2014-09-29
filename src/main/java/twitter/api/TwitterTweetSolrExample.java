package twitter.api;

/**
 * Created by akohli on 9/22/14.
 */
public class TwitterTweetSolrExample {
    public static void main(String args[]) throws Exception
    {
        CloudSolrPersistenceLayer.getInstance().init();
        NewJumblrMain.init();
           LoadApp.init();
//        LoadApp.initialize(0,200);
    }
}
