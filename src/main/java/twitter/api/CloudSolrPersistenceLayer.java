package twitter.api;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.RangeFacet;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.schema.DateField;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.sql.DriverManager;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by akohli on 9/22/14.
 */
public class CloudSolrPersistenceLayer {

    int commitCount=0;
    DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

    DateFormat tumblrDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    static CloudSolrPersistenceLayer cloudSolrPersistenceLayer= new CloudSolrPersistenceLayer();

    public static CloudSolrPersistenceLayer getInstance()
    {

        return cloudSolrPersistenceLayer;
    }

    public HttpSolrServer server;

    public void init()
    {
         server = new HttpSolrServer("http://localhost:8983/solr/collection1");

    }

    public  int populateTweetData(String tweet,String tweetText,String showName,
                                  String createdOn,String persistTime,int sentimentalScore,String type,String embedCode,String selectedCity,String selectedCountry,
                                  String selectedState) throws Exception
    {

        try {
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("id", showName+createdOn);
            doc.addField("city", selectedCity);
            doc.addField("showName", showName);
            doc.addField("socialTextcontent", tweetText);
            doc.addField("sentimentalScore", sentimentalScore);
            doc.addField("country", selectedCountry);
            doc.addField("state", selectedState);
            doc.addField("embedCode", embedCode);
            doc.addField("socialcontenttype", type);
            doc.addField("createdOn", new Date(df.parse(createdOn).getTime()));
            doc.addField("last_modified", new Date(df.parse(createdOn).getTime()));
            doc.addField("category", "twitter");

            server.add(doc);
            commitCount++;
            if (commitCount % 10 == 0) {
                server.commit();
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
        }
        return 0;
    }


    public  void populateTumblrData(long postID,String blogName,String text,String showName,
                                    String title,String official,String type,int sentimentalScore,
                                    int likes,int followes,int width,String embed,
                                    String time,String url,String postURL) throws Exception {
        try {
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("id", showName+postID);
            doc.addField("blogName", blogName);
            doc.addField("showName", showName);
            doc.addField("socialTextcontent", text);
            doc.addField("sentimentalScore", sentimentalScore);
            doc.addField("title", title);
            doc.addField("official", official);
            doc.addField("socialcontenttype", type);
            doc.addField("likes", likes);
            doc.addField("followers", followes);
            doc.addField("url", url);
            doc.addField("posturl", postURL);
            doc.addField("embedCode", embed);



            doc.addField("createdOn", new Date(tumblrDF.parse(time).getTime()));
            doc.addField("last_modified", new Date(tumblrDF.parse(time).getTime()));
            doc.addField("category", "tumblr");

            server.add(doc);
            commitCount++;
            if (commitCount % 10 == 0) {
                server.commit();
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
        }
    }


    public  void populateYoutubeData(String id,String showName,String title,String official,int likes,
                                     int dislikes,int views,int comments,String embedCodeVideo,
                                     String embedCodePic,String tag, com.google.api.client.util.DateTime createdAt,String channel,String link,
                                     int showID) throws Exception {
        try {
            SolrInputDocument doc = new SolrInputDocument();
            doc.addField("id", showName+id);
            doc.addField("showName", showName);
            doc.addField("sentimentalScore", -1);
            doc.addField("title", title);
            doc.addField("official", official);
            doc.addField("socialcontenttype", "video");
            doc.addField("likes", likes);
            doc.addField("dislikes", dislikes);
            doc.addField("views", views);
            doc.addField("commentcount", comments);
            doc.addField("photoUrl", embedCodePic);
            doc.addField("posturl", link);
            doc.addField("embedCode", embedCodeVideo);
            doc.addField("channel", channel);
            doc.addField("showID", showID);
            doc.addField("createdOn", new Date(createdAt.getValue()));
            doc.addField("last_modified", new Date(createdAt.getValue()));
            doc.addField("category", "youtube");

            server.add(doc);
            commitCount++;
            if (commitCount % 10 == 0) {
                server.commit();
            }


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
        }
    }





    int count1=0;
    private void readQuery() throws Exception{
        SolrQuery solrQuery = new  SolrQuery().
                    setQuery("showName:Suits AND createdOn:[2014-09-23T00:00:00Z TO 2014-11-17T00:00:00Z] ");
        solrQuery.setRows(10);
        solrQuery.setStart(count1);

        QueryResponse rsp = server.query(solrQuery);
            int count=0;
            System.out.println(rsp.getResults().size());
            while (count < rsp.getResults().size()) {
                SolrDocument solrDocument = rsp.getResults().get(count);
               System.out.println(solrDocument.getFieldValueMap().get("id"));
                count++;
            }
        count1+=count;

    }


    public static String toUtcDate(Date date) throws Exception{
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }
}
