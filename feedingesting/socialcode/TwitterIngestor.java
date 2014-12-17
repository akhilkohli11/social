package org.zap2it.ingester.social;

import org.apache.solr.common.SolrInputDocument;
import org.zap2it.clients.SolrClient;
import twitter4j.*;
import twitter4j.auth.AccessToken;
import java.util.List;

/**
 * Created by akohli on 12/4/14.
 */
public class TwitterIngestor {

    private twitter4j.Twitter twitter=null;
    public void init() throws Exception
    {
        twitter = TwitterFactory.getSingleton();
        twitter.setOAuthConsumer("nDhWQNt3buDkIWNyBp9iilIXp", "OAFBjrd8mHCgMV6YXE5SgadBKP4Fl0cHBM9zU94vgn6OIDafHC");
        AccessToken accessToken = new AccessToken("2524277576-3OyRFktWxMMB3NZw68C71Q1eZTrCc3tQQyWN8t0", "B3XQM3PM88xyZP6uiIA9RRaWkVNa4QrIaTShSJlwMZrzb");

        //persist to the accessToken for future reference.
        twitter.setOAuthAccessToken(accessToken);

    }

    public void ingestData(List<Twitter> twitterList)
    {
        for(Twitter twitterEntry : twitterList)
        {
            try {
                String handlePage=twitterEntry.getHandle().replace("@","");
                ResponseList<twitter4j.Status> statusResponseList = twitter.getUserTimeline(handlePage);
                int i = 0;
                while ((i < statusResponseList.size())) {
                    populateTweet(statusResponseList.get(i),twitterEntry);
                    i++;
                }
                Thread.sleep(1000);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private void populateTweet(Status status,Twitter twitterEntry)
    {
        if(status==null && !status.getLang().equals("en"))
        {
            return;
        }
        String twitterText=status.getText();
        boolean toDisplay=false;
        String slug=getSlug(twitterText);
        if(slug==null)
        {
            return;
        }
        if(twitterText==null)
        {
            return;
        }
        int retweetCount=status.getRetweetCount();
        String mediaUrl=null;
        MediaEntity[] mediaEntities = status.getExtendedMediaEntities();

        for (MediaEntity mediaEntity : mediaEntities) {
            mediaUrl=mediaEntity.getMediaURL();

        }
        if(mediaUrl!=null && twitterText!=null)
        {
            toDisplay=true;
        }

        String url="https://twitter.com/" + status.getUser().getScreenName()
                + "/status/" + status.getId();
        System.out.println("Tweet "+twitterText+"   "+url);
        SolrClient client = new SolrClient("content");
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", "Tweet" + status.getId());
        doc.addField("document_type", "tweet");
        doc.addField("lang", "en");
        doc.addField("title", twitterText);
        doc.addField("description", twitterText);
        doc.addField("publish_date",status.getCreatedAt());
        doc.addField("slug_id", slug + "twitter-"+status.getId());
        doc.addField("author", twitterEntry.getHandle());
        doc.addField("is_current", true);
        doc.addField("s_orig_url", url);
        if(mediaUrl!=null) {
            doc.addField("thumbnail", mediaUrl);
        }
        doc.addField("s_provider", "twitter");
        doc.addField("recommended_shows", twitterEntry.getTmsShowID());
        doc.addField("num_ratings_retweet", retweetCount);
        doc.addField("s_current", "true");
        doc.addField("b_displayContent", toDisplay);
        client.postToSolr(doc);
    }

    private String getSlug(String tweet)
    {
        String slug="";
        String tweetText[]=tweet.split(" ");
        if(tweetText.length>=4)
        {
            for(int i=0;i<4;i++) {
                slug+= tweetText[i].toLowerCase().replaceAll("[^A-Za-z0-9 ]", "") + "-";
            }
        }
        return slug;
    }

}
