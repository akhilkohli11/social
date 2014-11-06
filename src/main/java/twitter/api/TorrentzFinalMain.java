package twitter.api;

import java.util.Map;

/**
 * Created by akohli on 11/4/14.
 */
public class TorrentzFinalMain {
    public static void main(String args[]) throws Exception
    {
        CloudSolrPersistenceLayer.getInstance().init();
        TorrentzInitializer.init();
    }
}
