//package twitter.api;
//
//import org.apache.solr.common.SolrDocument;
//import org.apache.solr.common.SolrDocumentList;
//
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.ListIterator;
//import java.util.Map;
//
///**
// * Created by akohli on 11/4/14.
// */
//public class KloutMain {
//    public static void main(String args[]) throws Exception
//    {
//        CloudSolrPersistenceLayer.getInstance().init();
//
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
//            Collection<Object> titleCol=(Collection<Object>)document.getFieldValue("title");
//
//            kloutLoader.populate(titleCol.toArray()[0].toString(),document.get("showID").toString(),document.get("twit"));
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
//                Collection<Object> titleCol=(Collection<Object>)document.getFieldValue("title");
//
//                kloutLoader.populate(titleCol.toArray()[0].toString(),document.get("showID").toString(),document.get("twit"));
//
//            }
//        }
//    }
//}
