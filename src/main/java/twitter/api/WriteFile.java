package twitter.api;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by akohli on 6/4/14.
 */
public class WriteFile {
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_URL = "jdbc:mysql://localhost/social?"
            + "user=root";
    public static void main(String args[]) throws Exception {
        SocialMysqlLayer socialMysqlLayer = new SocialMysqlLayer(MYSQL_DRIVER, MYSQL_URL);
        BufferedReader br = null;

        try {

            String sCurrentLine;
            br = new BufferedReader(new FileReader("/Users/akohli/Downloads/ak.txt"));
            while ((sCurrentLine = br.readLine()) != null) {
                String[] buffer = StringUtils.split(sCurrentLine, "@", 2);
                String showName = buffer[0].trim();
                socialMysqlLayer.readTweets(showName);
                socialMysqlLayer.showPostiveTweets(showName);
               socialMysqlLayer.showNegativeTweets(showName);
                socialMysqlLayer.showNeutralTweets(showName);
                socialMysqlLayer.showSentimentalStats(showName);
            }
            socialMysqlLayer.showTrends();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        }
    }
}
