
package twitter.api;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateFiles {
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_URL = "jdbc:mysql://localhost/social?"
            + "user=root";
    public static void main(String args[]) throws Exception{
        SocialMysqlLayer socialMysqlLayer = new SocialMysqlLayer(MYSQL_DRIVER, MYSQL_URL);
        BufferedReader br = null;

        try {

            String sCurrentLine;
            br = new BufferedReader(new FileReader("/tmp/showsfinal.txt"));
            List<String> showNames=new ArrayList<String>();
            while ((sCurrentLine = br.readLine()) != null) {
                String[] buffer = StringUtils.split(sCurrentLine, "@", 2);
               String showName = buffer[0].trim();
//                socialMysqlLayer.readTweets(showName,"jun7to10","2014-06-07 00:00:00","2014-06-09 00:00:00");
//                socialMysqlLayer.showPositive(showName,"jun7to10","2014-06-05 00:00:00","2014-06-09 00:00:00");
//                socialMysqlLayer.showNeutral(showName,"jun7to10","2014-06-07 00:00:00","2014-06-09 00:00:00");
//                socialMysqlLayer.showNegative(showName,"jun7to10","2014-06-07 00:00:00","2014-06-09 00:00:00");
                socialMysqlLayer.negativepositiveneutralAll(showName,"jun7to14","2014-06-07 00:00:00","2014-06-09 00:00:00");
                socialMysqlLayer.showPositiveTweetText(showName,"jun7to10","2014-06-07 00:00:00","2014-06-09 00:00:00");
                socialMysqlLayer.showAllTweetText(showName,"jun7to10","2014-06-07 00:00:00","2014-06-09 00:00:00");
                socialMysqlLayer.showNegativeTweetText(showName,"jun7to10","2014-06-07 00:00:00","2014-06-09 00:00:00");

                showNames.add(showName);

            }
          //  socialMysqlLayer.showAllShowsTweets(showNames,"jun7to10combinationtweet","2014-06-07 00:00:00","2014-06-09 00:00:00");


            br = new BufferedReader(new FileReader("/tmp/showsfinal.txt"));
            showNames=new ArrayList<String>();
            while ((sCurrentLine = br.readLine()) != null) {
                String[] buffer = StringUtils.split(sCurrentLine, "@", 2);
                String showName = buffer[0].trim();
                socialMysqlLayer.readTweets(showName,"jun10to14","2014-06-10 00:00:00","2014-06-14 00:00:00");
                socialMysqlLayer.showPositive(showName,"jun10to14","2014-06-10 00:00:00","2014-06-14 00:00:00");
//                socialMysqlLayer.showNeutral(showName,"jun10to14","2014-06-10 00:00:00","2014-06-14 00:00:00");
//                socialMysqlLayer.showNegative(showName,"jun10to14","2014-06-10 00:00:00","2014-06-14 00:00:00");
//                socialMysqlLayer.negativepositiveneutralAll(showName,"jun10to14","2014-06-10 00:00:00","2014-06-14 00:00:00");
             //   socialMysqlLayer.showNeutralTweetText(showName,"jun10to14","2014-06-10 00:00:00","2014-06-14 00:00:00");
                socialMysqlLayer.showNegativeTweetText(showName,"jun10to14","2014-06-10 00:00:00","2014-06-14 00:00:00");
                socialMysqlLayer.showPositiveTweetText(showName,"jun10to14","2014-06-10 00:00:00","2014-06-14 00:00:00");
                socialMysqlLayer.showAllTweetText(showName,"jun10to14","2014-06-10 00:00:00","2014-06-14 00:00:00");
                socialMysqlLayer.negativepositiveneutralAll(showName,"jun10to14","2014-06-10 00:00:00","2014-06-14 00:00:00");

                showNames.add(showName);

            }
            socialMysqlLayer.showAllShowsTweets(showNames,"jun10to14combinationtweet","2014-06-10 00:00:00","2014-06-14 00:00:00");

        }
        finally {

        }
        }
}

