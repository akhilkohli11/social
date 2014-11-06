package twitter.api;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.util.Collection;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

/**
* Created by akohli on 11/3/14.
*/
public class MapSocialWebsitesToShows {
    public static void main(String args[]) throws Exception
    {
        try {
            init();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static void init() throws Exception
    {
        Map<String,Object> params = new HashMap<String,Object>();
        int start=0;
        int fetchSize=50;
        params.put("q", "category:shows");
        params.put("start", start);
        params.put("rows",fetchSize);
        SolrDocumentList documents = CloudSolrPersistenceLayer.getInstance().getSolrDocumentsWithPagination(params, start, fetchSize);
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
            documents = CloudSolrPersistenceLayer.getInstance().getSolrDocumentsWithPagination(params, start, fetchSize);
            ListIterator<SolrDocument> iterator=documents.listIterator();

            while (iterator.hasNext())
            {
                SolrDocument document=iterator.next();
                SolrDocument seachDocument=searchTitle(document);
                if(seachDocument!=null) {
                    CloudSolrPersistenceLayer.getInstance().populateDocWithSocialStuff(document, seachDocument);
                }
            }
        }
    }

    private static SolrDocument searchTitle(SolrDocument document) throws Exception{
        Map<String,String> params=new HashMap<String, String>();
        params.put("q", "id:"+document.get("id").toString()+"xml");
        return CloudSolrPersistenceLayer.getInstance().findSolrDocument(params);
    }


}
