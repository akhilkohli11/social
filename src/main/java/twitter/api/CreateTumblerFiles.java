
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
        Map<String,String> lowerGraph=new HashMap<String, String>();
        Map<String,String> upperGraph=new HashMap<String, String>();
        lowerGraph.put("jan","2014-01-01 00:00:00");
        lowerGraph.put("feb","2014-02-01 00:00:00");
        lowerGraph.put("mar","2014-03-01 00:00:00");
        lowerGraph.put("apr","2014-04-01 00:00:00");
        lowerGraph.put("may","2014-05-01 00:00:00");
        lowerGraph.put("jun","2014-06-01 00:00:00");

        upperGraph.put("jan","2014-01-31 00:00:00");
        upperGraph.put("feb","2014-02-31 00:00:00");
        upperGraph.put("mar","2014-03-31 00:00:00");
        upperGraph.put("apr","2014-04-31 00:00:00");
        upperGraph.put("may","2014-05-31 00:00:00");
        upperGraph.put("jun","2014-06-18 00:00:00");


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
                for(Map.Entry<String,String> entry :lowerGraph.entrySet()) {
//                    tumblrSqlLayer.loadPhotos(entry.getKey()+"tumblerPhoto", showName,entry.getValue(),
//                            upperGraph.get(entry.getKey()));
//                    tumblrSqlLayer.loadVideos(entry.getKey()+"tumblerVideo", showName,entry.getValue(),
//                            upperGraph.get(entry.getKey()));
//                    tumblrSqlLayer.loadTextPosts(entry.getKey()+"tumblerText", showName,entry.getValue(),
//                            upperGraph.get(entry.getKey()));
//                    tumblrSqlLayer.loadAudioPosts(entry.getKey()+"tumbleraudio", showName,entry.getValue(),
//                            upperGraph.get(entry.getKey()));
//                    tumblrSqlLayer.loadGRaphs(entry.getKey()+"tumblergraph", showName,entry.getValue(),
//                            upperGraph.get(entry.getKey()));
                }


            }

           // tumblrSqlLayer.loadTrending();
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
