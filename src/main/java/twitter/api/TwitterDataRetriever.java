package twitter.api;

import com.google.common.collect.Lists;
import org.json.JSONArray;
import com.mysql.jdbc.StringUtils;
import com.twitter.Extractor;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by akohli on 6/2/14.
 */
public class TwitterDataRetriever {

    static Map<String,String> tagToshowName =new HashMap<String, String>();
    static List<String> followTerms=new ArrayList<String>();


    static Set<String> shows=new HashSet<String>();
    static Map<String,String> showToNewName=new HashMap<String, String>();
    static Map<String,String> cityToSateListUs=new HashMap<String, String>();
    static Set<String> cityListUs=new HashSet<String>();
    static StanfordCoreNLP pipeline;

    public static void populateShowIDToShowName(String showName,String twitterHandle,String casteHandle[],String hashtag[]) throws Exception

    {
        String table=new String(showName);
        table=table.trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'","");
        showToNewName.put(table,showName);

        for(int i=0;i<hashtag.length;i++) {
            if (!StringUtils.isNullOrEmpty(hashtag[i]) && !StringUtils.isNullOrEmpty(showName)) {
                tagToshowName.put(hashtag[i].trim().replaceAll(",",""), showName);
                followTerms.add(hashtag[i].trim().replaceAll(",",""));
            }
        }
        shows.add(showName.trim());

        if(!StringUtils.isNullOrEmpty(showName) && !StringUtils.isNullOrEmpty(showName)) {
            tagToshowName.put(showName.trim(), showName);
        }

            if(!StringUtils.isNullOrEmpty(twitterHandle) && !StringUtils.isNullOrEmpty(showName)) {
                tagToshowName.put(twitterHandle.trim(), showName);
               // followTerms.add(twitterHandle.trim());
            }
//        for(int i=0;i<casteHandle.length;i++) {
//            System.out.print("  "+casteHandle[i]+",");
//            if (!StringUtils.isNullOrEmpty(casteHandle[i]) && !StringUtils.isNullOrEmpty(showName)) {
//                tagToshowName.put(casteHandle[i].trim(), showName);
//                followTerms.add(casteHandle[i].trim());
//
//            }
//        }
    }

    public static  Map<String,String> getShowToTableName()
    {
        return showToNewName;
    }

    public static Set<String> getShows()
    {
        return shows;
    }
    static int start=0;
    static int end=200;
    static int init=0;
    public static String getTweets(int count,SocialMysqlLayer socialMysqlLayer,int start,int end) throws Exception {
        List<String> moreTerms=new ArrayList<String>();
        if(init==0) {
            cityToSateListUs = socialMysqlLayer.getCityToStateMap();
            cityListUs = socialMysqlLayer.getCityList();
            init=1;
        }
        Authentication hosebirdAuth = new OAuth1("nDhWQNt3buDkIWNyBp9iilIXp", "OAFBjrd8mHCgMV6YXE5SgadBKP4Fl0cHBM9zU94vgn6OIDafHC",
                "2524277576-3OyRFktWxMMB3NZw68C71Q1eZTrCc3tQQyWN8t0", "B3XQM3PM88xyZP6uiIA9RRaWkVNa4QrIaTShSJlwMZrzb");

        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);
        BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>(1000);

        /** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
        // Optional: set up some followings and track terms
        // List<Long> followings = Lists.newArrayList(83023305L);

            moreTerms=followTerms.subList(start,end);

        System.out.println("twiiter"+moreTerms+moreTerms.size());

        System.out.println(moreTerms);
        List<String> terms = Lists.newArrayList(moreTerms);

        // hosebirdEndpoint.followings(followings);
        hosebirdEndpoint.trackTerms(terms);
        System.out.println("follow"+followTerms);
        ClientBuilder builder = new ClientBuilder()
                .name("Hosebird-Client-01")                              // optional: mainly for the logs
                .hosts(hosebirdHosts)
                .authentication(hosebirdAuth)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue))
                .eventMessageQueue(eventQueue);                          // optional: use this if you want to process client events

        Client hosebirdClient = builder.build();
// Attempts to establish a connection.
        hosebirdClient.connect();
        int retrycount=0;
        while (retrycount++<20) {

            Extractor extractor = new Extractor();
            String tweetConcat = "";
            int tweetID = count;
            java.util.Date date = new java.util.Date();
            int failcount = 0;
            while (!hosebirdClient.isDone()) {
                String msg = msgQueue.take();

                try {
                    msg.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    continue;
                }
                tweetID++;
                JSONObject jsonObject = new JSONObject(msg);
                String createdAt = jsonObject.get("created_at").toString();
                String tweettext = jsonObject.get("text").toString();
                String lang = jsonObject.get("lang").toString();
                String newloc = jsonObject.get("user").toString();
                String selectedCity = null;
                String selectedCountry = null;

                JSONObject user = new JSONObject(newloc);
                A:
                if (user.has("location")) {

                    String location = user.get("location").toString();
                    if (location.trim().equals("")) {
                        break A;
                    }
                    int found = 0;
                    for (String city : cityListUs) {
                        city = city.trim();
                        location = location.trim();
                        if (city.contains(location)) {
                            selectedCity = city;
                            //output.write(location1);
                            //output.newLine();
                        }
                    }
                    if (selectedCity == null) {
                        for (String city : cityListUs) {
                            city = city.trim();
                            location = location.trim();
                            if (location.contains(city)) {
                                selectedCity = city;
                            }

                        }
                    }
                }
                if (!lang.toLowerCase().equals("en")) {
                    continue;
                }
                if (isUTF8MisInterpreted(tweettext)) {
                    continue;
                }
                String tweet = new String(tweettext);
                List<String> removeList = new ArrayList<String>();
                removeList.addAll(extractor.extractURLs(tweet));
                removeList.addAll(extractor.extractHashtags(tweet));
                removeList.add(extractor.extractReplyScreenname(tweet));
                removeList.addAll(Arrays.asList("http:/", "http://", "http:"));
                removeList.addAll(extractor.extractMentionedScreennames(tweet));
                String time = new Timestamp(date.getTime()).toString();
                for (String remove : removeList) {
                    if (remove != null) {
                        tweet = tweet.replace(remove, "");
                    }
                }
                String showName = "";
                for (Map.Entry<String, String> entry : tagToshowName.entrySet()) {
                    if (tweettext.contains(entry.getKey()) || tweettext.toLowerCase().contains(entry.getKey().toLowerCase())
                            ) {
                        showName = entry.getValue();
                        break;
                    }
                }
                String type = "text";
                String embedCode = null;
                if (jsonObject.get("text").toString().contains("http:")) {
                    if (jsonObject.has("entities")) {
                        JSONObject entities = new JSONObject(jsonObject.get("entities").toString());
                        if (entities.has("media")) {
                            type = "photo";
                            JSONArray jsonArray = entities.getJSONArray("media");
                            for (int i = 0; i < 1; i++) {
                                JSONObject explrObject = jsonArray.getJSONObject(i);
                                embedCode = explrObject.get("media_url").toString();
                            }


                        } else if (entities.has("urls")) {
                            JSONArray jsonArray = entities.getJSONArray("urls");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                type = "links";
                                JSONObject explrObject = jsonArray.getJSONObject(i);
                                embedCode = explrObject.get("expanded_url").toString();
                            }

                        }

                    }
                }
                String selectedState = null;
                try {
                    if (selectedCity != null) {
                        selectedState = cityToSateListUs.get(selectedCity);
                        selectedCountry = "United States of America";
                    }
                    socialMysqlLayer.populateTweetData(msg.trim(), tweettext.trim(), showName.trim(),
                            createdAt, time, 0, type, embedCode, selectedCity, selectedCountry, selectedState);
                    CloudSolrPersistenceLayer.getInstance().populateTweetData(msg.trim(), tweettext.trim(), showName.trim(),
                            createdAt, time, 0, type, embedCode, selectedCity, selectedCountry, selectedState);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Thread.sleep(3000);
            hosebirdClient.reconnect();
        }

            hosebirdClient.stop();
            return "ok";

        }




    public static void init() {
      // pipeline = new StanfordCoreNLP("MyPropFile.properties");
    }

    public static int findSentiment(String tweet) {

        int mainSentiment = 0;
        if (tweet != null && tweet.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(tweet);
            for (CoreMap sentence : annotation
                    .get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence
                        .get(SentimentCoreAnnotations.AnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }

            }
        }
        return mainSentiment;
    }

    public static boolean isUTF8MisInterpreted( String input ) {
        //convenience overload for the most common UTF-8 misinterpretation
        //which is also the case in your question
        return isUTF8MisInterpreted( input, "Windows-1252");
    }

    public static boolean isUTF8MisInterpreted( String input, String encoding) {

        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
        CharsetEncoder encoder = Charset.forName(encoding).newEncoder();
        ByteBuffer tmp;
        try {
            tmp = encoder.encode(CharBuffer.wrap(input));
        }

        catch(CharacterCodingException e) {
            return false;
        }

        try {
            decoder.decode(tmp);
            return true;
        }
        catch(CharacterCodingException e){
            return false;
        }
    }
}
