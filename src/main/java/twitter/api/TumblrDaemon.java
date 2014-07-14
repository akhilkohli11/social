package twitter.api;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by akohli on 7/7/14.
 */
public class TumblrDaemon {
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_URL = "jdbc:mysql://localhost/social?"
            + "user=root";

    Runnable command = new Runnable() {
        public void run() {
            BufferedReader br = null;

            try {

                JumblrMain.init();

            } catch (Exception e) {
               // e.printStackTrace();
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
        Thread thread = new Thread(command);
        thread.start();
    }
}
