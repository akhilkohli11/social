package twitter.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by akohli on 7/7/14.
 */
public class TwitterTweetDaeomon {
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_URL = "jdbc:mysql://localhost/social?"
            + "user=root";

    Runnable command = new Runnable() {
        public void run() {
            BufferedReader br = null;
            try {

                LoadApp.init();
                LoadApp.initialize(0,200);
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
        service.scheduleAtFixedRate(command, 0, 12, TimeUnit.HOURS);
    }


    public static void main(String args[]) throws Exception
    {
        CloudSolrPersistenceLayer.getInstance().init();

        LoadApp.init();
        LoadApp.initialize(0,200);
    }
}
