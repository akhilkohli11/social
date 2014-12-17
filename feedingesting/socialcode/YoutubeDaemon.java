package org.zap2it.ingester.social;

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

                SocialIngestor socialIngestor = IngestorFactor.getIngestor("youtube");
                socialIngestor.ingestDataFromSource();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    public  void init()
    {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleAtFixedRate(command, 0, 30*60, TimeUnit.MINUTES);
    }

}

