package twitter.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by akohli on 7/7/14.
 */
public class YoutubeDaemon {
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_URL = "jdbc:mysql://localhost/social?"
            + "user=root";

    Runnable command = new Runnable() {
        public void run() {
            BufferedReader br = null;

            try {

                Date before = new Date();
                Date after =  new org.joda.time.DateTime(before).minusHours(3).toDate();
                Map<String,String> map=ShowLoader.getShowLoader().getShowTOIDMap();

                ViewsYoutubeLoader.populate(map,null,null,"viewCount");
                ViewsYoutubeLoader.populate(map,null,null,"relevance");
                int count=0;
                while (count++<=2)
                {
                    before=after;
                    after =  new org.joda.time.DateTime(before).minusHours(6).toDate();
                    ViewsYoutubeLoader.populate(map,after,before,"relevance");
                    ViewsYoutubeLoader.populate(map,after,before,"viewCount");
                    ViewsYoutubeLoader.populate(map,after,before,null);

                }

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
        service.scheduleAtFixedRate(command, 0, 60*60, TimeUnit.MINUTES);
    }


    public static void main(String args[]) throws Exception
    {
        CloudSolrPersistenceLayer.getInstance().init();
        ViewsYoutubeLoader.init();
        Date before = new Date();
        Date after =  new org.joda.time.DateTime(before).minusHours(3).toDate();
        Map<String,String> map=ShowLoader.getShowLoader().getShowTOIDMap();

        ViewsYoutubeLoader.populate(map,null,null,"viewCount");
        ViewsYoutubeLoader.populate(map,null,null,"relevance");
        int count=0;
        while (count++<=2)
        {
            before=after;
            after =  new org.joda.time.DateTime(before).minusHours(6).toDate();
            ViewsYoutubeLoader.populate(map,after,before,"relevance");
            ViewsYoutubeLoader.populate(map,after,before,"viewCount");
            ViewsYoutubeLoader.populate(map,after,before,null);

        }
    }
}
