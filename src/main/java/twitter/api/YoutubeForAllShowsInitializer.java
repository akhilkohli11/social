package twitter.api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by akohli on 10/30/14.
 */
public class YoutubeForAllShowsInitializer {

    public  static void init() throws Exception
    {
        try {

            Map<String, String> map=ShowLoader.getShowLoader().getShowTOIDMap();
            Date before = new Date();
            Date after =  new org.joda.time.DateTime(before).minusHours(3).toDate();


            ViewsYoutubeLoader.populate(map,null,null,"viewCount");
            ViewsYoutubeLoader.populate(map,null,null,"relevance");
            int count=0;
            while (count++<=4)
            {
                before=after;
                after =  new org.joda.time.DateTime(before).minusHours(3).toDate();
                ViewsYoutubeLoader.populate(map,after,before,"relevance");
                ViewsYoutubeLoader.populate(map,after,before,"viewCount");
                ViewsYoutubeLoader.populate(map,after,before,null);

            }        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
