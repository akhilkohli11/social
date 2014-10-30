package twitter.api;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by akohli on 10/30/14.
 */
public class YoutubeTest {
    public static void main(String args[]) throws Exception {
        CloudSolrPersistenceLayer.getInstance().init();
        readSolr();
    }


    public static void readSolr() throws Exception
    {
        HttpSolrServer server = new HttpSolrServer("http://localhost:8983/solr/collection1");
        Map<String,Object> params = new HashMap<String,Object>();
        int start=0;
        int fetchSize=50;
        params.put("q", "category:youtube");
        params.put("start", start);
        params.put("rows",fetchSize);
        SolrDocumentList documents = getSolrDocumentsWithPagination(server,params, start, fetchSize);
        SolrDocumentList allDocs = new SolrDocumentList();
        while(documents.size() > 0) {
            allDocs.addAll(documents);
            start += fetchSize;
            params.put("start",fetchSize);
            documents = getSolrDocumentsWithPagination(server,params, start, fetchSize);
        }
    }

    public static SolrDocumentList getSolrDocumentsWithPagination(HttpSolrServer server,Map<String,Object> params, int start, int rows) throws
            SolrServerException {
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

