package twitter.api;

import java.util.Map;

/**
 * Created by akohli on 10/31/14.
 */
public class TumblrForAllShows {
    public  static void init() throws Exception
    {
        try {
            ShowLoader showLoader= ShowLoader.getShowLoader();
            Map<String, String> showsTOIDMap=showLoader.getShowTOIDMap();
            RefactoredTumblrLoader.loadNewTumblrData(showsTOIDMap);


        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
