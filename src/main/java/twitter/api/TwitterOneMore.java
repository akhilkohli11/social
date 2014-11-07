package twitter.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by akohli on 7/7/14.
 */
public class TwitterOneMore {
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_URL = "jdbc:mysql://localhost/social?"
            + "user=root";

    Runnable command = new Runnable() {
        public void run() {
            BufferedReader br = null;
            try {

                TwitterDataRetriever.getTweets(5000, LoadApp.getSocialMysqlLayer(),201,405);

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
        service.scheduleAtFixedRate(command, 2, 60*80, TimeUnit.MINUTES);
    }
}
