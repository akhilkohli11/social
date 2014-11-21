package twitter.api;

/**
 * Created by akohli on 11/20/14.
 */
public class SocialInitializer {
    public static void main(String args[])
    {
        new SocialInitializer().init();
    }

    public void init() {
        try {
         //   Zap2ItSolrApi.init();
            CloudSolrPersistenceLayer.getInstance().init();
            InitializePopularDocuments.init();
            Aggregator aggregator=new Aggregator();
            aggregator.aggregateTrend();
//            aggregator.delete();
        //    aggregator.init();
//            aggregator.aggregateTrend();
//            aggregator.aggregateTotal();
//            aggregator.delete();
            //      aggregator.init();
//            Zap2ItSolrApi.init();
//            Thread.sleep(10000);
//            SocialXMLParser.init();
//            Thread.sleep(2000);
            //      MapSocialWebsitesToShows.init();

//            ViewsYoutubeLoader.init();
//            RefactoredTumblrLoader.init();
//            KloutDaemon kloutDaemon=new KloutDaemon();
//            kloutDaemon.init();
//            YoutubeDaemon youtubeDaemon=new YoutubeDaemon();
//            youtubeDaemon.init();
//
//            TorrentzDaemon torrentzDaemon = new TorrentzDaemon();
//            torrentzDaemon.init();
//
//
//            TumblrDaemon tumblrDaemon=new TumblrDaemon();
//            tumblrDaemon.init();
//
//
//
//
//
//            TwitterTweetDaeomon twitterTweetDaeomon=new TwitterTweetDaeomon();
//            twitterTweetDaeomon.init();
//
//
//            new TwitterOneMore().init();
//
//            FacebookDaemon facebookDaemon=new FacebookDaemon();
//            facebookDaemon.init();


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
