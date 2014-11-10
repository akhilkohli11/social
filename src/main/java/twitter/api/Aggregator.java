package twitter.api;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
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
        //run first then run all then run trends
    }

    public void delete() throws Exception
    {

        CloudSolrPersistenceLayer.getInstance().deleteDocuments("category:youtube");
        CloudSolrPersistenceLayer.getInstance().deleteDocuments("category:facebook");
        CloudSolrPersistenceLayer.getInstance().deleteDocuments("category:tumblr");
        CloudSolrPersistenceLayer.getInstance().deleteDocuments("category:twitter");
        CloudSolrPersistenceLayer.getInstance().deleteDocuments("category:torrentz");
        CloudSolrPersistenceLayer.getInstance().deleteDocuments("category:torrenthound");
    }

//    public void aggregateTotal() throws Exception {
//        Map<String, String> showNameToIDMap= ShowLoader.getShowLoader().getShowTOIDMap();
//        Map<String,Long> youtubeRank=new HashMap<String, Long>();
//
//
//        for(Map.Entry<String,String> entry :showNameToIDMap.entrySet())
//        {
//            long youtubeviews=CloudSolrPersistenceLayer.getInstance().getViewsTotal(entry.getValue(), "youtube", "views");
//            System.out.println("youtube  "+entry.getKey()+"   "+entry.getValue()+"   "+youtubeviews);
//
//            if(youtubeviews>0) {
//
//                //CloudSolrPersistenceLayer.getInstance().populateTotal(entry.getValue(), youtubeviews, "youtube", "views");
//                youtubeRank.put(entry.getValue(),youtubeviews);
//            }
//            long youtubecount=CloudSolrPersistenceLayer.getInstance().getTotalCount(entry.getValue(), "youtube");
//            System.out.println("youtube total  "+entry.getKey()+"   "+entry.getValue()+"   "+youtubecount);
//
//            if(youtubecount>0) {
//
//                //  CloudSolrPersistenceLayer.getInstance().populateTotal(entry.getValue(), youtubecount, "youtube", "count");
//
//            }
//            long facebookviews=CloudSolrPersistenceLayer.getInstance().getViewsTotal(entry.getValue(), "facebook", "views");
//            System.out.println("facebook  "+entry.getKey()+"   "+entry.getValue()+"   "+facebookviews);
//
//            if(facebookviews>0) {
//
//                CloudSolrPersistenceLayer.getInstance().populateTotal(entry.getValue(), facebookviews,
//                        "facebook", "views");
//            }
//
//
//            //also
//
//            long facebookcount=CloudSolrPersistenceLayer.getInstance().getTotalCount(entry.getValue(), "facebook");
//            System.out.println("facebook total  "+entry.getKey()+"   "+entry.getValue()+"   "+facebookcount);
//            if(facebookcount>0) {
//
//                CloudSolrPersistenceLayer.getInstance().populateTotal(entry.getValue(), facebookcount,
//                        "facebook", "count");
//            }
//
//            long torrentzViews=CloudSolrPersistenceLayer.getInstance().getViewsTotal(entry.getValue(), "torrentz", "views");
//            System.out.println("torrentz  "+entry.getKey()+"   "+entry.getValue()+"   "+torrentzViews);
//
//
//
//            long torrentHound=CloudSolrPersistenceLayer.getInstance().getViewsTotal(entry.getValue(), "torrenthound", "views");
//            System.out.println("torrenthound  "+entry.getKey()+"   "+entry.getValue()+"   "+torrentHound);
//
//            if(torrentzViews>0 && torrentHound>0) {
//                CloudSolrPersistenceLayer.getInstance().populateTotal(entry.getValue(), torrentzViews,
//                        "torrentz", "views");
//                CloudSolrPersistenceLayer.getInstance().populateTotal(entry.getValue(), torrentHound,
//                        "torrenthound", "views");
//            }
//
//
//            long twitter=CloudSolrPersistenceLayer.getInstance().getTMSIDTotalCountForTwitter(entry.getValue());
//            System.out.println("twitter total  "+entry.getKey()+"   "+entry.getValue()+"   "+twitter);
//            if(twitter>0) {
//                CloudSolrPersistenceLayer.getInstance().populateTotal(entry.getValue(), twitter,
//                        "twitter", "views");
//            }
//
//
//            long tumblrViews=CloudSolrPersistenceLayer.getInstance().getViewsTotal(entry.getValue(), "tumblr", "views");
//            System.out.println("tumblr  "+entry.getKey()+"   "+entry.getValue()+"   "+tumblrViews);
//
//            if(tumblrViews>0) {
//                CloudSolrPersistenceLayer.getInstance().populateTotal(entry.getValue(), tumblrViews,
//                        "tumblr", "views");
//            }
//            //also
//
//            long tumblrCount=CloudSolrPersistenceLayer.getInstance().getTotalCount(entry.getValue(), "tumblr");
//            System.out.println("tumblr total  "+entry.getKey()+"   "+entry.getValue()+"   "+tumblrCount);
//
//            if(tumblrCount>0) {
//                CloudSolrPersistenceLayer.getInstance().populateTotal(entry.getValue(), tumblrCount,
//                        "tumblr", "count");
//            }
//
//            double kloutscore=CloudSolrPersistenceLayer.getInstance().getKloutScore(entry.getValue(), "klout", "day_d");
//            System.out.println("klout day change "+entry.getKey()+"   "+entry.getValue()+"   "+kloutscore);
//
//
//
//        }
//    }
//
//


    public void aggregateTrend() throws Exception {
        Map<String, String> showNameToIDMap= ShowLoader.getShowLoader().getShowTOIDMap();
        Map<String,Long> youtubeRank=new HashMap<String, Long>();
        Map<String,Long> facebookRank=new HashMap<String, Long>();
        Map<String,Long> torrentzRank=new HashMap<String, Long>();
        Map<String,Long> torrentHoundRank=new HashMap<String, Long>();
        Map<String,Long> twitterRank=new HashMap<String, Long>();
        Map<String,Long> tumblrRank=new HashMap<String, Long>();
        Map<String,Double> kloutRank=new HashMap<String, Double>();



        for(Map.Entry<String,String> entry :showNameToIDMap.entrySet())
        {
            long youtubeviews=CloudSolrPersistenceLayer.getInstance().getViewsTotal(entry.getValue(), "youtube", "views");
            System.out.println("youtube  "+entry.getKey()+"   "+entry.getValue()+"   "+youtubeviews);

            if(youtubeviews>0) {
                //CloudSolrPersistenceLayer.getInstance().populateTotal(entry.getValue(), youtubeviews, "youtube", "views");
                youtubeRank.put(entry.getValue(),youtubeviews);
            }

            long facebookviews=CloudSolrPersistenceLayer.getInstance().getViewsTotal(entry.getValue(), "facebook", "views");
            System.out.println("facebook total  "+entry.getKey()+"   "+entry.getValue()+"   "+facebookviews);
            if(facebookviews>0) {

                facebookRank.put(entry.getValue(),facebookviews);
            }

            long torrentzViews=CloudSolrPersistenceLayer.getInstance().getViewsTotal(entry.getValue(), "torrentz", "views");
            System.out.println("torrentz  "+entry.getKey()+"   "+entry.getValue()+"   "+torrentzViews);



            long torrentHound=CloudSolrPersistenceLayer.getInstance().getViewsTotal(entry.getValue(), "torrenthound", "views");
            System.out.println("torrenthound  "+entry.getKey()+"   "+entry.getValue()+"   "+torrentHound);

            if(torrentzViews>0 ) {
                torrentzRank.put(entry.getValue(), torrentzViews);
            }
            if(torrentHound>0 ) {

                torrentHoundRank.put(entry.getValue(),torrentHound);
            }


            long twitter=CloudSolrPersistenceLayer.getInstance().getTMSIDTotalCountForTwitter(entry.getValue());
            System.out.println("twitter total  "+entry.getKey()+"   "+entry.getValue()+"   "+twitter);
            if(twitter>0) {
                twitterRank.put(entry.getValue(),twitter);
            }


            long tumblrViews=CloudSolrPersistenceLayer.getInstance().getViewsTotal(entry.getValue(), "tumblr", "views");
            System.out.println("tumblr  "+entry.getKey()+"   "+entry.getValue()+"   "+tumblrViews);


            long tumblrCount=CloudSolrPersistenceLayer.getInstance().getTotalCount(entry.getValue(), "tumblr");
            System.out.println("tumblr total  "+entry.getKey()+"   "+entry.getValue()+"   "+tumblrCount);

            if(tumblrViews>0) {
               tumblrRank.put(entry.getValue(),tumblrViews);
            }

            double kloutscore=CloudSolrPersistenceLayer.getInstance().getKloutScore(entry.getValue(), "klout", "day_d");
            System.out.println("klout day change "+entry.getKey()+"   "+entry.getValue()+"   "+kloutscore);
            kloutRank.put(entry.getValue(),kloutscore);

        }
        Map<String,Integer>  youtubeRankMap=sort(youtubeRank);
        Map<String,Integer>  facebookRankMap=sort(facebookRank);
        Map<String,Integer>  torrentzRankMap=sort(torrentzRank);
        Map<String,Integer>  torrentHoundRankMap=sort(torrentHoundRank);
        Map<String,Integer>  tumblrRankMap=sort(tumblrRank);
        Map<String,Integer>  twitterRankMap=sort(twitterRank);
        Map<String,Integer>  kloutRankMap=sortDouble(kloutRank);

        Map<String,Integer> overAllRank=calculateOverAllRank(youtubeRankMap,facebookRankMap,tumblrRankMap,twitterRankMap);


        loadRanks(youtubeRankMap, facebookRankMap, torrentzRankMap, torrentHoundRankMap, twitterRankMap, tumblrRankMap,
                kloutRankMap,overAllRank);
    }

    private Map<String, Integer> calculateOverAllRank(Map<String, Integer> youtubeRankMap, Map<String, Integer> facebookRankMap,
                                                      Map<String, Integer> tumblrRankMap, Map<String, Integer> twitterRankMap) {

        Map<String,Long> sortedMap=new HashMap<String, Long>();
        for(Map.Entry<String,Integer> entry :youtubeRankMap.entrySet())
        {
            long value=0;
            value+=youtubeRankMap.get(entry.getKey())*6;
            if(facebookRankMap.containsKey(entry.getKey()) && twitterRankMap.containsKey(entry.getKey()))
            {
                value+=facebookRankMap.get(entry.getKey())*4;
                value+=twitterRankMap.get(entry.getKey())*8;
                sortedMap.put(entry.getKey(),value);
                continue;
            }
            if(facebookRankMap.containsKey(entry.getKey()) && tumblrRankMap.containsKey(entry.getKey()))
            {
                value+=facebookRankMap.get(entry.getKey())*4;
                value+=tumblrRankMap.get(entry.getKey())*2;
                sortedMap.put(entry.getKey(),value);
                continue;
            }
            if(twitterRankMap.containsKey(entry.getKey()) && tumblrRankMap.containsKey(entry.getKey()))
            {
                value+=twitterRankMap.get(entry.getKey())*8;
                value+=tumblrRankMap.get(entry.getKey())*2;
                sortedMap.put(entry.getKey(),value);
                continue;
            }
        }
        Map<String,Integer>  rankMap=sort(sortedMap);
        return rankMap;

    }


    public Map<String,Integer> sort(Map<String,Long> trends)
    {
        long numbers[]=new long[10000];
        int count1=0;
        Map<Long,String> trends1=new HashMap<Long, String>();

        for(Map.Entry<String,Long> entry : trends.entrySet())
        {
            if(trends1.containsKey(entry.getValue()))
            {
                Random random=new Random();
                trends1.put(entry.getValue()+random.nextInt(10),entry.getKey());
            }
            else {
                trends1.put(entry.getValue(), entry.getKey());
            }
        }
        for(Map.Entry<String,Long> entry : trends.entrySet())
        {
            numbers[count1++] =entry.getValue();
        }


        long temp;

        for(int i = 0; i < numbers.length; i++)
        {
            for(int j = 1; j < (numbers.length-i); j++)
            {
                //if numbers[j-1] < numbers[j], swap the elements
                if(numbers[j-1] < numbers[j])
                {
                    temp = numbers[j-1];
                    numbers[j-1]=numbers[j];
                    numbers[j]=temp;
                }
            }
        }
        Map<Integer,String> rankMap=new HashMap<Integer, String>();
        Map<String,Integer> showRankMap=new HashMap<String, Integer>();

        int rank=1;
        for(int i = 0; i < numbers.length; i++)
        {
                rankMap.put(rank++,trends1.get(numbers[i]));


        }
        for(Map.Entry<Integer,String> entry :rankMap.entrySet())
        {
            showRankMap.put(entry.getValue(),entry.getKey());
        }
        return showRankMap;
    }

    public static void loadRanks(Map<String, Integer> youtubeRankMap, Map<String, Integer> facebookRankMap, Map<String, Integer> torrentzRankMap,
                                 Map<String, Integer> torrentHoundRankMap, Map<String, Integer> twitterRankMap, Map<String,Integer>  tumblrRankMap, Map<String,Integer>  kloutRankMap,
             Map<String,Integer>  overAllRank
                                 ) throws Exception
    {
        List<SolrDocument> solrDocuments=CloudSolrPersistenceLayer.getInstance().getPopularDocuments();
        for(SolrDocument solrDocument : solrDocuments)
        {
            String id=solrDocument.get("showID").toString();
            CloudSolrPersistenceLayer.getInstance().populateRanks(solrDocument, youtubeRankMap.get(solrDocument.get("showID").toString()), facebookRankMap.get(solrDocument.get("showID").toString()),
                    torrentzRankMap.get(id),torrentHoundRankMap.get(id),twitterRankMap.get(id),tumblrRankMap.get(id),kloutRankMap.get(id),overAllRank.get(id));
        }
    }



    public Map<String,Integer> sortDouble(Map<String,Double> trends)
    {
        double numbers[]=new double[10000];
        int count1=0;
        Map<Double,String> trends1=new HashMap<Double, String>();

        for(Map.Entry<String,Double> entry : trends.entrySet())
        {
            trends1.put(entry.getValue(),entry.getKey());
        }
        for(Map.Entry<String,Double> entry : trends.entrySet())
        {
            numbers[count1++] =entry.getValue();
        }


        double temp;

        for(int i = 0; i < numbers.length; i++)
        {
            for(int j = 1; j < (numbers.length-i); j++)
            {
                //if numbers[j-1] < numbers[j], swap the elements
                if(numbers[j-1] < numbers[j])
                {
                    temp = numbers[j-1];
                    numbers[j-1]=numbers[j];
                    numbers[j]=temp;
                }
            }
        }
        Map<Integer,String> rankMap=new HashMap<Integer, String>();
        Map<String,Integer> showRankMap=new HashMap<String, Integer>();

        int rank=1;
        for(int i = 0; i < numbers.length; i++)
        {
            rankMap.put(rank++,trends1.get(numbers[i]));


        }
        for(Map.Entry<Integer,String> entry :rankMap.entrySet())
        {
            showRankMap.put(entry.getValue(),entry.getKey());
        }
        return showRankMap;
    }


}

