import com.google.common.collect.Lists;
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


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.*;
/**
 * Created by akohli on 5/26/14.
 */
public class Test {
public static void main(String args[]) throws Exception
{
    BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);
    BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>(1000);

/** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
    Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
    StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
// Optional: set up some followings and track terms
   // List<Long> followings = Lists.newArrayList(83023305L);
    List<String> terms = Lists.newArrayList("@cwthe100","@nbgat");
  //  hosebirdEndpoint.followings(followings);
    hosebirdEndpoint.trackTerms(terms);

// These secrets should be read from a config file
    Authentication hosebirdAuth = new OAuth1("nDhWQNt3buDkIWNyBp9iilIXp", "OAFBjrd8mHCgMV6YXE5SgadBKP4Fl0cHBM9zU94vgn6OIDafHC",
            "2524277576-3OyRFktWxMMB3NZw68C71Q1eZTrCc3tQQyWN8t0", "B3XQM3PM88xyZP6uiIA9RRaWkVNa4QrIaTShSJlwMZrzb");
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
    // on a di
    //     Extractor extractor = http:/
   // init();
    Extractor extractor = new Extractor();
    while (!hosebirdClient.isDone()) {
        String msg = msgQueue.take();
        System.out.println(msg);
        JSONObject jsonObject = new JSONObject(msg);
        String tweet=jsonObject.get("text").toString();
        List<String> removeList=new ArrayList<String>();
         removeList.addAll(extractor.extractURLs(tweet));
        removeList.addAll(extractor.extractHashtags(tweet));
        removeList.add(extractor.extractReplyScreenname(tweet));
        removeList.addAll(Arrays.asList("http:/","http://","http:"));
        removeList.addAll(extractor.extractMentionedScreennames(tweet));
        for(String remove : removeList)
        {
            if(remove!=null) {
                tweet = tweet.replace(remove, "");
            }
        }
     //   System.out.println( tweet+"......score=      "+findSentiment(tweet));
     }

}
    static StanfordCoreNLP pipeline;

    public static void init() {
        pipeline = new StanfordCoreNLP("MyPropFile.properties");
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
}

