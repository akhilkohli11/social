package org.zap2it.ingester.social;

import org.zap2it.clients.SolrClient;

/**
* Created by akohli on 11/18/14.
*/
public class DeleteSolr {
    public static void main(String args[]) throws Exception
    {
        SolrClient solrClient=new SolrClient("content");
        solrClient.solrServer.deleteByQuery("s_provider:tumblr");

     //  solrClient.solrServer.deleteByQuery("document_type:twitter");
//        solrClient.solrServer.deleteByQuery("document_type:tweet");
   //    solrClient.solrServer.deleteByQuery("document_type:tumblr_video");
    //    solrClient.solrServer.deleteByQuery("document_type:tumblr_article");
       //solrClient.solrServer.deleteByQuery("document_type:tumblr_photogallery");

        solrClient.solrServer.commit();
    }
}
