package twitter.api;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by akohli on 7/1/14.
 */
public class Aggregator {

    Runnable command = new Runnable() {
        public void run() {
            BufferedReader br = null;

            try {

                // socialMysqlLayer.populateLocationDataForTwitter();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        }
    };
    public  void init() throws Exception
    {
        ViewsYoutubeLoader.init();
        RefactoredTumblrLoader.init();
        KloutDaemon kloutDaemon=new KloutDaemon();
        kloutDaemon.init();
        YoutubeDaemon youtubeDaemon=new YoutubeDaemon();
        youtubeDaemon.init();

        TorrentzDaemon torrentzDaemon = new TorrentzDaemon();
        torrentzDaemon.init();


        TumblrDaemon tumblrDaemon=new TumblrDaemon();
        tumblrDaemon.init();





        TwitterTweetDaeomon twitterTweetDaeomon=new TwitterTweetDaeomon();
        twitterTweetDaeomon.init();


        new TwitterOneMore().init();

        FacebookDaemon facebookDaemon=new FacebookDaemon();
        facebookDaemon.init();
    }

    public static void main(String args[]) throws Exception
    {
        CloudSolrPersistenceLayer.getInstance().init();
        InitializePopularDocuments.init();

        Aggregator aggregator=new Aggregator();
        aggregator.aggregateTotal();
        //run first then run all then run trends
    }
    public void aggregateTotal() throws Exception
    {
        //Also delete docs
        Map<String, String> showNameToIDMap= ShowLoader.getShowLoader().getShowTOIDMap();

        for(Map.Entry<String,String> entry :showNameToIDMap.entrySet())
        {
            long youtubeviews=CloudSolrPersistenceLayer.getInstance().getViewsTotal(entry.getValue(), "youtube", "views");
            System.out.println("youtube  "+entry.getKey()+"   "+entry.getValue()+"   "+youtubeviews);

            if(youtubeviews>0) {

                CloudSolrPersistenceLayer.getInstance().populateTotal(entry.getValue(), youtubeviews, "youtube", "views");
            }
            long youtubecount=CloudSolrPersistenceLayer.getInstance().getTotalCount(entry.getValue(), "youtube");
            System.out.println("youtube total  "+entry.getKey()+"   "+entry.getValue()+"   "+youtubecount);

            if(youtubecount>0) {

                CloudSolrPersistenceLayer.getInstance().populateTotal(entry.getValue(), youtubecount, "youtube", "count");

            }
            long facebookviews=CloudSolrPersistenceLayer.getInstance().getViewsTotal(entry.getValue(), "facebook", "views");
            System.out.println("facebook  "+entry.getKey()+"   "+entry.getValue()+"   "+facebookviews);

            if(facebookviews>0) {

                CloudSolrPersistenceLayer.getInstance().populateTotal(entry.getValue(), facebookviews,
                        "facebook", "views");
            }


            //also

            long facebookcount=CloudSolrPersistenceLayer.getInstance().getTotalCount(entry.getValue(), "facebook");
            System.out.println("facebook total  "+entry.getKey()+"   "+entry.getValue()+"   "+facebookcount);
            if(facebookcount>0) {

                CloudSolrPersistenceLayer.getInstance().populateTotal(entry.getValue(), facebookcount,
                        "facebook", "count");
            }

            long torrentzViews=CloudSolrPersistenceLayer.getInstance().getViewsTotal(entry.getValue(), "torrentz", "views");
            System.out.println("torrentz  "+entry.getKey()+"   "+entry.getValue()+"   "+torrentzViews);



            long torrentHound=CloudSolrPersistenceLayer.getInstance().getViewsTotal(entry.getValue(), "torrenthound", "views");
            System.out.println("torrenthound  "+entry.getKey()+"   "+entry.getValue()+"   "+torrentHound);

            if(torrentzViews>0 && torrentHound>0) {
                CloudSolrPersistenceLayer.getInstance().populateTotal(entry.getValue(), torrentzViews,
                        "torrentz", "views");
                CloudSolrPersistenceLayer.getInstance().populateTotal(entry.getValue(), torrentHound,
                        "torrenthound", "views");
            }


            long twitter=CloudSolrPersistenceLayer.getInstance().getTotalCount(entry.getValue(), "twitter");
            System.out.println("twitter total  "+entry.getKey()+"   "+entry.getValue()+"   "+twitter);
            if(twitter>0) {
                CloudSolrPersistenceLayer.getInstance().populateTotal(entry.getValue(), twitter,
                        "twitter", "views");
            }


            long tumblrViews=CloudSolrPersistenceLayer.getInstance().getViewsTotal(entry.getValue(), "tumblr", "views");
            System.out.println("tumblr  "+entry.getKey()+"   "+entry.getValue()+"   "+tumblrViews);

            if(tumblrViews>0) {
                CloudSolrPersistenceLayer.getInstance().populateTotal(entry.getValue(), tumblrViews,
                        "tumblr", "views");
            }
            //also

            long tumblrCount=CloudSolrPersistenceLayer.getInstance().getTotalCount(entry.getValue(), "tumblr");
            System.out.println("tumblr total  "+entry.getKey()+"   "+entry.getValue()+"   "+tumblrCount);

            if(tumblrCount>0) {
                CloudSolrPersistenceLayer.getInstance().populateTotal(entry.getValue(), tumblrCount,
                        "tumblr", "count");
            }

            double kloutscore=CloudSolrPersistenceLayer.getInstance().getKloutScore(entry.getValue(), "klout", "day_d");
            System.out.println("klout day change "+entry.getKey()+"   "+entry.getValue()+"   "+kloutscore);


            CloudSolrPersistenceLayer.getInstance().deleteDocuments("category:youtube");
            CloudSolrPersistenceLayer.getInstance().deleteDocuments("category:facebook");
            CloudSolrPersistenceLayer.getInstance().deleteDocuments("category:tumblr");
            CloudSolrPersistenceLayer.getInstance().deleteDocuments("category:twitter");
            CloudSolrPersistenceLayer.getInstance().deleteDocuments("category:torrentz");
            CloudSolrPersistenceLayer.getInstance().deleteDocuments("category:torrenthound");


        }

    }
}
