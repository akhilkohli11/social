
package twitter.api;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreateTumblerFiles {
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_URL = "jdbc:mysql://localhost/social?"
            + "user=root";
    public static void main(String args[]) throws Exception{

        BufferedReader br = null;

        try {

            String sCurrentLine;
            TumblrSqlLayer tumblrSqlLayer=new TumblrSqlLayer(MYSQL_DRIVER,MYSQL_URL);
            br = new BufferedReader(new FileReader("/tmp/tumblrshowminhash.txt"));
            TumblrLoader.init();
            Map<String,String> map=new HashMap<String,String>();
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
                tumblrSqlLayer.loadPhotos("photo",showName);
                tumblrSqlLayer.loadVideos("video",showName);


            }
            // System.out.println(showName+"    "+tumblerPage);

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
    //schema blog name,postid/blog likes/follower/type of post/video audio or something/blog content
}
