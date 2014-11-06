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

    Runnable command = new Runnable() {
        public void run() {
            BufferedReader br = null;

            try {

                TorrentzInitializer.init();

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
        service.scheduleAtFixedRate(command, 0, 6*60, TimeUnit.MINUTES);
    }
}
