package twitter.api;

/**
* Created by akohli on 9/18/14.
*/
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrInputDocument;
public class CloudServer {

    public static void main(String args[]) throws Exception
    {
        write();
        read();
    }

    private static void read() throws Exception
    {
        CloudSolrServer server = new CloudSolrServer("localhost:2181");
        server.setDefaultCollection("collection1");
        SolrQuery solrQuery = new  SolrQuery().
                setQuery("showName:kaise");
        QueryResponse rsp = server.query(solrQuery);
        System.out.println(rsp);

    }

    private static void write() throws Exception
    {
        CloudSolrServer server = new CloudSolrServer("localhost:2181");
        server.setDefaultCollection("collection1");
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField( "id", "1609");
        doc.addField( "show_id", 9);
        doc.addField( "showName", "brobrobro kaise ho tum");
        doc.addField( "showtype", "hindichin");

        doc.addField( "url_s", "These are url handles");


        server.add(doc);
        server.commit();
    }

}
