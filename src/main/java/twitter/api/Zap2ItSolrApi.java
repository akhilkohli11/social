package twitter.api;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.sql.Date;
import java.util.Collection;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by akohli on 10/29/14.
 */
public class Zap2ItSolrApi {
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_URL = "jdbc:mysql://localhost/social?"
            + "user=root";
    static YoutubeSqlLayer youtubeSqlLayer=null;


    public static void main(String args[]) throws Exception
    {
        init();
    }

    public static void init() throws Exception
    {
        CloudSolrPersistenceLayer.getInstance().init();
        HttpSolrServer server = new HttpSolrServer("http://zap2it-prod-solr-lb1-337769341.us-west-2.elb.amazonaws.com:8983/solr/content");
        Map<String,Object> params = new HashMap<String,Object>();
        int start=0;
        int fetchSize=50;
        params.put("q", "tms_show_id:* AND is_showcard:true");
        params.put("start", start);
        params.put("rows",fetchSize);
        SolrDocumentList documents = getSolrDocumentsWithPagination(server,params, start, fetchSize);
        ListIterator<SolrDocument> newiterator=documents.listIterator();

        while (newiterator.hasNext())
        {
            SolrDocument document=newiterator.next();
            CloudSolrPersistenceLayer.getInstance().populateDoc(document);
        }
        SolrDocumentList allDocs = new SolrDocumentList();
        while(documents.size() > 0) {
            allDocs.addAll(documents);
            start += fetchSize;
            params.put("start",fetchSize);
            documents = getSolrDocumentsWithPagination(server,params, start, fetchSize);
            ListIterator<SolrDocument> iterator=documents.listIterator();

            while (iterator.hasNext())
            {
                SolrDocument document=iterator.next();
                CloudSolrPersistenceLayer.getInstance().populateDoc(document);
            }
            Thread.sleep(1000);
        }
    }

    public static SolrDocumentList getSolrDocumentsWithPagination(HttpSolrServer server,Map<String,Object> params, int start, int rows) throws SolrServerException {
        SolrQuery parameters = new SolrQuery();
        for(String key : params.keySet()) {
            parameters.set(key, params.get(key).toString());
        }
        parameters.set("start", start);
        parameters.set("rows", rows);
        QueryResponse response = server.query(parameters);
        SolrDocumentList list = response.getResults();
        System.out.println(list);
        return list;
    }

}
