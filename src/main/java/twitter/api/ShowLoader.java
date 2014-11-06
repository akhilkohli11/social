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
    private Map<String, String> showTOIDMap=new HashMap<String, String>();
    private Map<String, String> popularShowTOIDMap;


    public static ShowLoader getShowLoader()
    {

        return showLoader;
    }

    public Map<String, String> getShowTOIDMap() throws Exception{

       if(showTOIDMap.isEmpty())
       {
           showTOIDMap=CloudSolrPersistenceLayer.getInstance().getPopular100ShowMap();
       }
        return showTOIDMap;

    }


}
