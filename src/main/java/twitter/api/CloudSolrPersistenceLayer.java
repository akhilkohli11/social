package twitter.api;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
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

import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by akohli on 9/22/14.
 */
public class CloudSolrPersistenceLayer {

    int commitCountyoutube=0;
    int commitCounttwitter=0;
    int commitCounttumblr=0;
    int commitCountfacebook=0;
    int commitCounttorrentz=0;
    int commitCountklout=0;

    List<String> deleteIDs=new ArrayList<String>();
    int deleteCount=0;
    DateFormat solrDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

    DateFormat tumblrDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static List<SolrDocument> popularDocuments=new ArrayList<SolrDocument>();

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
                                  String selectedState,String showID,String id) throws Exception
    {

        try {
            SolrInputDocument doc = new SolrInputDocument();
//            SolrDocument exitingDoc=findSolrDocument("id",  showName+createdOn);
//            if(exitingDoc!=null)
//            {
//                deleteDocuments(exitingDoc.get("id").toString());
//            }
            doc.addField("id", showID+id+"twitter");

//            doc.addField("city", selectedCity);
//            doc.addField("socialTextcontent", tweetText);
//            doc.addField("sentimentalScore", sentimentalScore);
//            doc.addField("country", selectedCountry);
//            doc.addField("state", selectedState);
//            doc.addField("embedCode", embedCode);
            //   doc.addField("socialcontenttype", type);
            doc.addField("createdOn", new Date(df.parse(createdOn).getTime()));
            doc.addField("last_modified", new Date(df.parse(createdOn).getTime()));
            doc.addField("category", "twitter");
            doc.addField("showID", showID);


            server.add(doc);
            commitCounttwitter++;
            if (commitCounttwitter % 100 == 0) {
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
                                    String time,String url,String postURL, String showID) throws Exception {
        try {
            SolrInputDocument doc = new SolrInputDocument();
//            SolrDocument exitingDoc=findSolrDocument("id",  showName+postID);
//            if(exitingDoc!=null)
//            {
//                deleteDocuments(exitingDoc.get("id").toString());
//            }
            doc.addField("id", showName+postID+"tumblr");
            doc.addField("showName", showName);
            doc.addField("socialTextcontent", text);
            doc.addField("sentimentalScore", sentimentalScore);
            doc.addField("title", title);
            doc.addField("official", official);
            doc.addField("socialcontenttype", type);
            doc.addField("likes", likes);
            doc.addField("followers", followes);
            doc.addField("views", likes+followes);
            doc.addField("url", url);
            doc.addField("posturl", postURL);
            doc.addField("embedCode", embed);
            doc.addField("showID", showID);

            doc.addField("createdOn", new Date(tumblrDF.parse(time).getTime()));
            doc.addField("last_modified", new Date(tumblrDF.parse(time).getTime()));
            doc.addField("category", "tumblr");

            server.add(doc);
            commitCounttumblr++;
            if (commitCounttumblr % 20 == 0) {
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



    public  void populateTorrentzData(int count,int peers,String showName, String showID,String type) throws Exception {
        try {
            SolrInputDocument doc = new SolrInputDocument();
            Date date=new Date();
            solrDate.setTimeZone(TimeZone.getTimeZone("UTC"));
//            SolrDocument exitingDoc=findSolrDocument("id", showID+"torrentz");
//            if(exitingDoc!=null)
//            {
//                deleteDocuments(exitingDoc.get("id").toString());
//            }
            doc.addField("id", showID+type+count+peers);
            doc.addField("showName", showName);
            doc.addField("views", peers);
            doc.addField("showID", showID);
            doc.addField("createdOn", solrDate.format(new Date()));
            doc.addField("last_modified", solrDate.format(new Date()));
            doc.addField("category", type);

            server.add(doc);
            commitCounttorrentz++;
            if (commitCounttorrentz % 10 == 0) {
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
                                     String showID) throws Exception {
        try {
            SolrInputDocument doc = new SolrInputDocument();
//            SolrDocument exitingDoc=findSolrDocument("id",showID+"youtube");
//            if(exitingDoc!=null)
//            {
//                deleteDocuments(exitingDoc.get("id").toString());
//            }
            doc.addField("id", showName+id+"youtube");
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
            commitCountyoutube++;
            if (commitCountyoutube % 10 == 0) {
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


    public  void populateFacebook(String id,Integer shareCount,Date publishedTime,String showName,String showID) {


        try {
            SolrInputDocument doc = new SolrInputDocument();
//            SolrDocument exitingDoc=findSolrDocument("id",showID+"youtube");
//            if(exitingDoc!=null)
//            {
//                deleteDocuments(exitingDoc.get("id").toString());
//            }
            doc.addField("id", showID+id+"facebook");
            doc.addField("showName", showName);
            doc.addField("showID", showID);
            if(shareCount==null) {
                doc.addField("views", 0);
            }
            else
            {
                doc.addField("views", shareCount.intValue());

            }
            doc.addField("createdOn", publishedTime);
            doc.addField("last_modified", publishedTime);
            doc.addField("category", "facebook");

            server.add(doc);
            commitCountfacebook++;
            if (commitCountfacebook % 10 == 0) {
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


    public  void populateTotal(String id,long count,String category,String type) {


        try {
            SolrInputDocument doc = new SolrInputDocument();
//            SolrDocument exitingDoc=findSolrDocument("id",showID+"youtube");
//            if(exitingDoc!=null)
//            {
//                deleteDocuments(exitingDoc.get("id").toString());
//            }
            doc.addField("id", id+type+category);
            doc.addField("showID", id);
            doc.addField("total_l", count);
            doc.addField("category", category+type);

            server.add(doc);

            server.commit();


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
        }


    }


    public void deleteDocuments(String query) {
        try {
            server.deleteByQuery(query);
            server.commit();
        }
        catch (Exception e) {

            e.printStackTrace();
        }
    }




    public SolrDocument findSolrDocument(Map<String,String> params) throws SolrServerException {
        try {
            SolrQuery parameters = new SolrQuery();
            for (String key : params.keySet()) {
                parameters.set(key, params.get(key).toString());
            }

            QueryResponse response = server.query(parameters);

            SolrDocumentList list = response.getResults();
            if (list.size() >= 1) {
                return list.get(0);
            }
            return null;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
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

    public void populateDoc(SolrDocument document)  throws Exception{
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id",document.getFieldValue("id"));
        doc.addField("showID",document.getFieldValue("id"));
        doc.addField("series_id",document.getFieldValue("series_id"));
        doc.addField("tms_id",document.getFieldValue("tms_id"));
        doc.addField("tms_show_id",document.getFieldValue("tms_show_id"));
        doc.addField("title",document.getFieldValue("title"));
        doc.addField("createdOn",document.getFieldValue("publish_date"));
        doc.addField("genre",document.getFieldValue("genre"));
        doc.addField("is_showcard",document.getFieldValue("is_showcard"));
        doc.addField("is_current",document.getFieldValue("is_current"));
        doc.addField("is_popular",document.getFieldValue("is_popular"));

        doc.addField("category","shows");



        server.add(doc);
        commitCountyoutube++;
        if (commitCountyoutube % 20 == 0) {
            server.commit();
        }

    }



    public void populateDocWithSocialStuff(SolrDocument document,SolrDocument searchDoc)  throws Exception{
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id",document.getFieldValue("id"));
        doc.addField("showID",document.getFieldValue("id"));

        doc.addField("tms_id",document.getFieldValue("tms_id"));
        doc.addField("tms_show_id",document.getFieldValue("tms_show_id"));
        doc.addField("title",document.getFieldValue("title"));
        doc.addField("createdOn",document.getFieldValue("publish_date"));
        doc.addField("genre",document.getFieldValue("genre"));
        doc.addField("is_showcard",document.getFieldValue("is_showcard"));
        doc.addField("is_current",document.getFieldValue("is_current"));
        doc.addField("is_popular",document.getFieldValue("is_popular"));
        if (searchDoc.containsKey("wiki_s")) {
            doc.addField("wiki_s", searchDoc.get("wiki_s").toString());

        }
        if (searchDoc.containsKey("imdb_s")) {
            doc.addField("imdb_s", searchDoc.get("imdb_s").toString());

        }
        if (searchDoc.containsKey("twit")) {
            doc.addField("twit", searchDoc.get("twit").toString());

        }
        if (searchDoc.containsKey("fb")) {
            doc.addField("fb", searchDoc.get("fb").toString());

        }
        doc.addField("category","shows");



        server.add(doc);
        commitCountyoutube++;
        if (commitCountyoutube % 100 == 0) {
            server.commit();
        }

    }

    public void persist(String rootID,String title, Map<String, String> map) throws Exception{

        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("title",title);
        doc.addField("id",rootID+"xml");

        for(Map.Entry<String,String> entry :map.entrySet()) {
            if (entry.getKey().equals("Wikipedia")) {
                doc.addField("wiki_s", entry.getValue());

            }
            if (entry.getKey().equals("IMDB")) {
                doc.addField("imdb_s", entry.getValue());

            }
            if (entry.getKey().equals("Twitter")) {
                doc.addField("twit", entry.getValue());

            }
            if (entry.getKey().equals("Facebook")) {
                doc.addField("fb", entry.getValue());

            }
        }
        doc.addField("category","xmlshow");
        server.add(doc);
        commitCountyoutube++;
        if (commitCountyoutube % 100 == 0) {
            server.commit();
        }

    }

    public  SolrDocumentList getSolrDocumentsWithPagination(Map<String,Object> params, int start, int rows) throws SolrServerException {
        SolrQuery parameters = new SolrQuery();
        for(String key : params.keySet()) {
            parameters.set(key, params.get(key).toString());
        }
        parameters.set("start", start);
        parameters.set("rows", rows);
        QueryResponse response = server.query(parameters);
        SolrDocumentList list = response.getResults();
        return list;
    }


    public void populateKloutScore(String key, String value, double score,
                                   double dayChange, double weekChange, double monthChange) throws Exception{
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("showID",value);
        doc.addField("title",key);
        doc.addField("id",value+"klout");
        doc.addField("day_d",dayChange);
        doc.addField("score_d",score);
        doc.addField("week_d",weekChange);
        doc.addField("month_d",monthChange);

        doc.addField("category","klout");
        server.add(doc);
        commitCountklout++;
        if (commitCountklout % 100 == 0) {
            server.commit();
        }
    }




    public Map<String,String> getPopular100ShowMap() throws Exception{
        Map<String,String> showNameToIdMap=new HashMap<String, String>();
        ListIterator<SolrDocument> newiterator=popularDocuments.listIterator();
        while (newiterator.hasNext())
        {
            SolrDocument document=newiterator.next();
            String title=document.getFieldValue("title").toString();
            showNameToIdMap.put(title, document.get("showID").toString());
        }

        return showNameToIdMap;
    }

    public List<SolrDocument> getPopularDocuments()
    {
        return popularDocuments;
    }

    public long getViewsTotal(String id, String type,String field) throws Exception{
        Map<String,String> showNameToIdMap=new HashMap<String, String>();
        Map<String,Object> params = new HashMap<String,Object>();
        int start=0;
        int fetchSize=50;
        params.put("q", "showID:"+id +" AND category:"+type);
        params.put("start", start);
        params.put("rows",fetchSize);
        long score=0;
        SolrDocumentList documents = CloudSolrPersistenceLayer.getInstance().getSolrDocumentsWithPagination(params, start, fetchSize);
        ListIterator<SolrDocument> newiterator=documents.listIterator();

        while (newiterator.hasNext())
        {
            SolrDocument document=newiterator.next();
            if(document.containsKey(field)) {
                score += Long.parseLong(document.get(field).toString());
            }
        }
        SolrDocumentList allDocs = new SolrDocumentList();
        int size=0;
        while(documents.size() > 0 && size++<950) {
            allDocs.addAll(documents);
            start += fetchSize;
            params.put("start",fetchSize);
            documents = CloudSolrPersistenceLayer.getInstance().getSolrDocumentsWithPagination(params, start, fetchSize);
            ListIterator<SolrDocument> iterator=documents.listIterator();

            while (iterator.hasNext())
            {
                SolrDocument document=iterator.next();
                if(document.containsKey(field)) {
                    score += Long.parseLong(document.get(field).toString());
                }


            }
        }
        return score;
    }



    public double getKloutScore(String id, String type,String field) throws Exception{
        Map<String,String> showNameToIdMap=new HashMap<String, String>();
        Map<String,Object> params = new HashMap<String,Object>();
        int start=0;
        int fetchSize=50;
        params.put("q", "showID:"+id +" AND category:"+type);
        params.put("start", start);
        params.put("rows",fetchSize);
        double score=-2;
        SolrDocumentList documents = CloudSolrPersistenceLayer.getInstance().getSolrDocumentsWithPagination(params, start, fetchSize);
        ListIterator<SolrDocument> newiterator=documents.listIterator();

        while (newiterator.hasNext())
        {
            SolrDocument document=newiterator.next();
            if(document.containsKey(field)) {
                score += Double.parseDouble(document.get(field).toString());
            }
        }

        return score;
    }


    public long getTotalCount(String id, String type) throws Exception{

        SolrQuery q = new SolrQuery("showID:"+id +" AND category:"+type);
        q.setRows(0);

        return server.query(q).getResults().getNumFound();
    }

    public long getTMSIDTotalCountForTwitter(String id) throws Exception{
        String tmsID=null;
        for(SolrDocument solrDocument : popularDocuments)
        {
            if(solrDocument.get("showID").toString().equals(id))
            {
                return getTotalCount(solrDocument.get("tms_show_id").toString(),"twitter");
            }
        }
        return 0;
    }




    public void loadPopularDocuments(String id,String[] hashtag) throws Exception {
        Map<String,String> showNameToIdMap=new HashMap<String, String>();
        Map<String,Object> params = new HashMap<String,Object>();
        int start=0;
        int fetchSize=2;
        params.put("q", "tms_show_id:"+id.trim().replaceAll("\"","") +" AND category:shows");
        SolrDocumentList documents = CloudSolrPersistenceLayer.getInstance().getSolrDocumentsWithPagination(params, start, fetchSize);
        ListIterator<SolrDocument> newiterator=documents.listIterator();

        while (newiterator.hasNext())
        {
            SolrDocument document=newiterator.next();
            String title=document.getFieldValue("title").toString();

            System.out.print(title + "   " + document.get("id") + "   " + "");
            for(String hash : hashtag)
            {
                System.out.print(hash);
            }
            System.out.println();

            popularDocuments.add(document);

        }

    }

    public void populateRanks(SolrDocument document, Integer youtubeRank, Integer facebookRank,Integer torrentzRank,Integer torrentHoundRank,
                              Integer twtterRank,Integer tumblrRank,Integer kloutRank) throws Exception{
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id",document.getFieldValue("id")+"ranks");
        doc.addField("showID",document.getFieldValue("id"));

        doc.addField("tms_id",document.getFieldValue("tms_id"));
        doc.addField("tms_show_id",document.getFieldValue("tms_show_id"));
        doc.addField("title",document.getFieldValue("title"));
        doc.addField("createdOn",document.getFieldValue("publish_date"));
        doc.addField("genre",document.getFieldValue("genre"));
        doc.addField("is_showcard",document.getFieldValue("is_showcard"));
        doc.addField("is_current",document.getFieldValue("is_current"));
        doc.addField("is_popular",document.getFieldValue("is_popular"));
        doc.addField("youtuberank_i",youtubeRank);
        doc.addField("facebookrank_i",facebookRank);
        doc.addField("torrentzrank_i",torrentzRank);
        doc.addField("torrenthoundrank_i",torrentHoundRank);
        doc.addField("twitterrank_i",twtterRank);
        doc.addField("tumblrrank_i",tumblrRank);
        doc.addField("kloutrank_i",kloutRank);

        doc.addField("category","ranks");



        server.add(doc);
        server.commit();

    }
}

