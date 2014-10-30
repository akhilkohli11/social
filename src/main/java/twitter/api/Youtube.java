package twitter.api;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by akohli on 6/20/14.
 */
public class Youtube {
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_URL = "jdbc:mysql://localhost/social?"
            + "user=root";
    public static void main(String args[]) throws Exception
    {
        Youtube.init();
    }
    static YoutubeSqlLayer youtubeSqlLayer=null;

    public  static void init() throws Exception
    {
        youtubeSqlLayer=new YoutubeSqlLayer(MYSQL_DRIVER,MYSQL_URL);;

        BufferedReader br = null;

        try {

            String sCurrentLine;
            br = new BufferedReader(new FileReader("/tmp/youtubereader.txt"));
            Map<String,String> map=new HashMap<String,String>();
            while ((sCurrentLine = br.readLine()) != null) {

                String[] buffer= sCurrentLine.split("breakbreakbreak");
                if(buffer.length==1) {
                    map.put(buffer[0].trim(),null);
                }
                else
                {
                    map.put(buffer[0].trim(),buffer[1].trim());
                }
            }
            System.out.println("Youtube map"+map);
            // System.out.println(showName+"    "+tumblerPage);
            YoutubeLoader.init();
           // YoutubeLoader.populate(youtubeSqlLayer,map);

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

    public static YoutubeSqlLayer getYoutubeSqlLayer()
    {
        return youtubeSqlLayer;
    }

    public static Map<String, Integer> getViews(Date date) throws Exception{
        return youtubeSqlLayer.getViewsForShow(date);
    }

    public static Map<String, Integer> getComments(Date date) throws Exception{
        return youtubeSqlLayer.getCommentsForShow(date);
    }

    public static Map<String, Integer> getLikes(Date date) {
        return youtubeSqlLayer.getLikesForShow(date);
    }

    public static Map<String, Integer> getDislikes(Date date) {
        return youtubeSqlLayer.getDislikesForShow(date);
    }


    //schema blog name,postid/blog likes/follower/type of post/video audio or something/blog content
}
