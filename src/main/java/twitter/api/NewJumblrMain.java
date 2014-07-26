package twitter.api;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by akohli on 6/20/14.
 */
public class NewJumblrMain {
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_URL = "jdbc:mysql://localhost/social?"
            + "user=root";
    public static void main(String args[]) throws Exception
    {
        NewJumblrMain.init();
    }
    static TumblrSqlLayer tumblrSqlLayer=null;

    public  static void init() throws Exception
    {
        tumblrSqlLayer=new TumblrSqlLayer(MYSQL_DRIVER,MYSQL_URL);;

        BufferedReader br = null;

        try {

            String sCurrentLine;
            br = new BufferedReader(new FileReader("/tmp/showsfinal.txt"));
            TumblrLoader.init();
            Map<String,List<String>> map=new HashMap<String,List<String>>();
            RefactoredTumblrLoader.init();
            while ((sCurrentLine = br.readLine()) != null) {

                String[] buffer= StringUtils.split(sCurrentLine, "@", 2);
                String[] newbuffer=StringUtils.split(buffer[1],"#",2);
                String[] finalbuffer=StringUtils.split(newbuffer[1], "@", 2);
                String caste[]=new String[1000];
                String hashtag[]=new String[1000];

                //  populateShowIDToShowName(socialMysqlLayer, countString,buffer[0].trim() , "@"+newbuffer[0].trim(), null, "#"+newbuffer[1].trim());
                if(finalbuffer.length>1)
                {
                    caste=("@"+finalbuffer[1].trim()).split(",");
                    hashtag=("#"+finalbuffer[0].trim()).split("#");
                }
                else
                {
                    hashtag=("#"+finalbuffer[0].trim()).split("#");
                }
                for(String hashTag : hashtag) {
                    List<String> searchObjectList = map.get(buffer[0].trim().trim());
                    if (searchObjectList==null) {
                        searchObjectList = new ArrayList<String>();
                    }
                    String topput=hashTag.replaceAll("#","").replaceAll(",","");
                    if(!topput.trim().equals("")) {
                        searchObjectList.add(hashTag.replaceAll("#", "").replaceAll(",", ""));
                        map.put(buffer[0].trim(), searchObjectList);
                    }
                }
            }
            System.out.println("TUMBLR TUMRBLRRRKLKRKRLKRLRKLRKLRKL map"+map);
            // System.out.println(showName+"    "+tumblerPage);
            RefactoredTumblrLoader.loadNewTumblrData(tumblrSqlLayer,map);

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
