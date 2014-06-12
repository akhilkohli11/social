package twitter.api;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by akohli on 6/11/14.
 */
public class NewService {

    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_URL = "jdbc:mysql://localhost/social?"
            + "user=root";
    Runnable command = new Runnable() {
        public void run() {
            try {
                TwitterDataRetriever.init();
                SocialMysqlLayer socialMysqlLayer = new SocialMysqlLayer(MYSQL_DRIVER, MYSQL_URL);
                socialMysqlLayer.updateTime(155900);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            // update database
        }
    };
    public  void init()
    {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.execute(command);
    }


}
