package twitter.api;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by akohli on 10/30/14.
 */
public class YoutubeForAllShowsInitializer {

    public  static void init() throws Exception
    {
        try {
            ShowLoader showLoader= ShowLoader.getShowLoader();
            Map<String, Integer> showsTOIDMap=showLoader.getShowTOIDMap();
            YoutubeLoader.init();
            YoutubeLoader.populate(showsTOIDMap);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
