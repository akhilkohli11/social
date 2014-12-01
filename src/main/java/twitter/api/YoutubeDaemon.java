package twitter.api;


import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by akohli on 7/7/14.
 */
public class YoutubeDaemon {


    Runnable command = new Runnable() {
        public void run() {
            try {

                Date before = new Date();
                Date after = new org.joda.time.DateTime(before).minusHours(3).toDate();
                Map<String, String> map = ShowLoader.getShowLoader().getShowTOIDMap();

//                ViewsYoutubeLoader.populate(map,null,null,"viewCount");
//                ViewsYoutubeLoader.populate(map,null,null,"relevance");
                int count = 0;
                while (count++ <= 3) {
                    before = after;
                    after = new org.joda.time.DateTime(before).minusHours(3).toDate();
                    ViewsYoutubeLoader.populate(map, after, before, "relevance");
                    ViewsYoutubeLoader.populate(map, after, before, "viewCount");
                    ViewsYoutubeLoader.populate(map, after, before, null);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    public  void init()

    {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(command, 0, 30, TimeUnit.HOURS);    }


}

