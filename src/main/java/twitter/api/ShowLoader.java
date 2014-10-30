package twitter.api;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by akohli on 10/30/14.
 */
public class ShowLoader {

    static ShowLoader showLoader=new ShowLoader();
    private Map<String, Integer> showTOIDMap=new HashMap<String, Integer>();

    public  List<String> getShows()
    {
        //todo read shows from solr
        return Arrays.asList("Game of Thrones","Big Bang Theory","Friends");
    }

    public static ShowLoader getShowLoader()
    {
        return showLoader;
    }

    public Map<String, Integer> getShowTOIDMap() {
        showTOIDMap=new HashMap<String, Integer>();
       showTOIDMap.put("Game of Thrones",1);
        showTOIDMap.put("Big Bang Theory",2);
        showTOIDMap.put("Friends",3);
        return showTOIDMap;

    }
}
