package twitter.api;

import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by akohli on 7/7/14.
 */
public class KloutDaemon {
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_URL = "jdbc:mysql://localhost/social?"
            + "user=root";

    Runnable command = new Runnable() {
        public void run() {
            BufferedReader br = null;

            try {

             //   initlize();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        }
    };
    public  void init()
    {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(command, 5, 60, TimeUnit.MINUTES);
    }

//    public void initlize() throws Exception
//    {
//        KloutLoader kloutLoader=new KloutLoader();
//        kloutLoader.init();
//        Map<String,Object> params = new HashMap<String,Object>();
//        int start=0;
//        int fetchSize=50;
//        params.put("q", "category:shows");
//        params.put("start", start);
//        params.put("rows",fetchSize);
//        SolrDocumentList documents = CloudSolrPersistenceLayer.getInstance().getSolrDocumentsWithPagination(params, start, fetchSize);
//        ListIterator<SolrDocument> newiterator=documents.listIterator();
//
//        while (newiterator.hasNext())
//        {
//            SolrDocument document=newiterator.next();
//            String title=document.getFieldValue("title").toString();
//
//            kloutLoader.populate(title,document.get("showID").toString(),document.get("twit"));
//        }
//        SolrDocumentList allDocs = new SolrDocumentList();
//        while(documents.size() > 0) {
//            allDocs.addAll(documents);
//            start += fetchSize;
//            params.put("start",fetchSize);
//            documents = CloudSolrPersistenceLayer.getInstance().getSolrDocumentsWithPagination(params, start, fetchSize);
//            ListIterator<SolrDocument> iterator=documents.listIterator();
//
//            while (iterator.hasNext())
//            {
//                SolrDocument document=iterator.next();
//                String title=document.getFieldValue("title").toString();
//
//                kloutLoader.populate(title,document.get("showID").toString(),document.get("twit"));
//
//            }
//        }
//    }
    }

