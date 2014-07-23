package twitter.api;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by akohli on 6/20/14.
 */
public class JumblrMain {
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_URL = "jdbc:mysql://localhost/social?"
            + "user=root";
    public static void main(String args[]) throws Exception
    {
        JumblrMain.init();
    }
     static TumblrSqlLayer tumblrSqlLayer=null;

    public  static void init() throws Exception
    {
        tumblrSqlLayer=new TumblrSqlLayer(MYSQL_DRIVER,MYSQL_URL);;

        BufferedReader br = null;

        try {

            String sCurrentLine;
            br = new BufferedReader(new FileReader("/tmp/tumblrshowminhash.txt"));
            TumblrLoader.init();
            Map<String,String> map=new HashMap<String,String>();
            RefactoredTumblrLoader.init();
            while ((sCurrentLine = br.readLine()) != null) {

                String[] buffer = StringUtils.splitByWholeSeparator(sCurrentLine, "http");
                String showName = "";
                String tumblerPage = null;

                //  populateShowIDToShowName(socialMysqlLayer, countString,buffer[0].trim() , "@"+newbuffer[0].trim(), null, "#"+newbuffer[1].trim());
                showName = buffer[0].trim();
                if (buffer.length > 1) {
                    String temp = buffer[1].trim().substring(3);
                    String buf[] = StringUtils.splitByWholeSeparator(temp, ".");
                    tumblerPage = buf[0].trim();
                    map.put(showName,tumblerPage);
                }
                else {
                    map.put(showName,null);
                }

            }
            // System.out.println(showName+"    "+tumblerPage);
            RefactoredTumblrLoader.loadTumblrData(tumblrSqlLayer,map);

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

    public static TumblrSqlLayer getTumblrSqlLayer()
    {
        return tumblrSqlLayer;
    }

    public static Map<String, Integer> getTumblrPostsForDay(Date date) throws Exception{
        return tumblrSqlLayer.getTumblrForDayForShows(date);
    }

    public static Map<String, Integer> getPhotoTumblrForDayForShows(Date date) throws Exception{
        return tumblrSqlLayer.getPhotoTumblrForDayForShows(date);
    }

    public static Map<String, Integer> getVideoTumblrForDayForShows(Date date) {
        return tumblrSqlLayer.getVideoTumblrForDayForShows(date);
    }

    public static Map<String, Integer> getAUDIOTumblrForDayForShows(Date date) {
        return tumblrSqlLayer.getAudioTumblr(date);
    }

    public static Map<String, Integer> getTEXTTumblrForDayForShows(Date date) {
        return tumblrSqlLayer.getTextTumblr(date);
    }
    //schema blog name,postid/blog likes/follower/type of post/video audio or something/blog content
}
