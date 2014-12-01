package twitter.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by akohli on 10/31/14.
 */
public class TorrentzDaemon {


    public  void init()
    {
        try {
            TorrentzInitializer.init();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
