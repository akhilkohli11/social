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
    public static void populateShowIDToShowName(String showName,String twitterHandle,String casteHandle[],String hashtag[]) throws Exception

    {
        System.out.println();
        System.out.print(showName+"     ");
        for(int i=0;i<hashtag.length;i++) {
            System.out.print(hashtag[i]+",");
            if (!StringUtils.isNullOrEmpty(hashtag[i]) && !StringUtils.isNullOrEmpty(showName)) {
                tagToshowName.put(hashtag[i].trim(), showName);
                followTerms.add(hashtag[i].trim());
            }
        }
        shows.add(showName.trim());

        if(!StringUtils.isNullOrEmpty(showName) && !StringUtils.isNullOrEmpty(showName)) {
            tagToshowName.put(showName.trim(), showName);
        }

            if(!StringUtils.isNullOrEmpty(twitterHandle) && !StringUtils.isNullOrEmpty(showName)) {
                System.out.print("  "+twitterHandle+"     ");
                tagToshowName.put(twitterHandle.trim(), showName);
                followTerms.add(twitterHandle.trim());
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

    public static Set<String> getShows()
    {
        return shows;
    }

    public static String getTweets(int count,SocialMysqlLayer socialMysqlLayer) throws Exception {
        init();
        System.out.println(followTerms);
        Authentication hosebirdAuth = new OAuth1("nDhWQNt3buDkIWNyBp9iilIXp", "OAFBjrd8mHCgMV6YXE5SgadBKP4Fl0cHBM9zU94vgn6OIDafHC",
                "2524277576-3OyRFktWxMMB3NZw68C71Q1eZTrCc3tQQyWN8t0", "B3XQM3PM88xyZP6uiIA9RRaWkVNa4QrIaTShSJlwMZrzb");

        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);
        BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>(1000);

        /** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
        // Optional: set up some followings and track terms
        // List<Long> followings = Lists.newArrayList(83023305L);
        List<String> terms = Lists.newArrayList(followTerms);
        // hosebirdEndpoint.followings(followings);
        hosebirdEndpoint.trackTerms(terms);
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

                try {
                      socialMysqlLayer.populateTweetData( msg.trim(), tweettext.trim(), showName.trim(),
                           createdAt, time,0,type,embedCode);
                } catch (Exception e) {
                    //.printStackTrace();
                       //  System.out.println(tweettext);
                       // System.out.println(failcount);
                }
            }

            hosebirdClient.stop();
            return tweetConcat;

    }

static StanfordCoreNLP pipeline;

    public static void init() {
      //  pipeline = new StanfordCoreNLP("MyPropFile.properties");
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
