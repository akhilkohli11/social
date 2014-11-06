package twitter.api;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by akohli on 7/1/14.
 */
public class Aggregator {

    Runnable command = new Runnable() {
        public void run() {
            BufferedReader br = null;

            try {

                // socialMysqlLayer.populateLocationDataForTwitter();
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
    //    service.scheduleAtFixedRate(command, 60*3, 60*4, TimeUnit.MINUTES);
    }

    public static void main(String args[]) throws Exception
    {
        CloudSolrPersistenceLayer.getInstance().init();
        Aggregator aggregator=new Aggregator();
        aggregator.aggregate(null);
    }
    public void aggregate(Date date) throws Exception
    {
        //Also delete docs
        Map<String, String> showNameToIDMap= ShowLoader.getShowLoader().getShowTOIDMap();

        for(Map.Entry<String,String> entry :showNameToIDMap.entrySet())
        {
            double youtubeviews=CloudSolrPersistenceLayer.getInstance().getViewsTotal(entry.getValue(), "youtube", "views");
            System.out.println(entry.getKey()+"   "+entry.getValue()+"   "+youtubeviews);
            CloudSolrPersistenceLayer.getInstance().deleteDocuments("category:youtube");
        }

    }
}
