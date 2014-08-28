package twitter.api;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by akohli on 8/25/14.
 */
public class CreateYoutubeFiles {
    public static void main(String args[]) throws Exception
    {
        createYoutubeSql();

    }



    private static void createYoutubeSql() throws Exception{
        BufferedReader br = null;
        String sCurrentLine;
        br = new BufferedReader(new FileReader("/tmp/youtubereader"));
        int count=1;
        //  socialMysqlLayer.readData();
        while ((sCurrentLine = br.readLine()) != null) {
            if(StringUtils.isEmpty(sCurrentLine.trim()))
            {
                continue;
            }
         //   id,url,likes,dislikes,views,comment,title,embedurlvideo,embedUrlPic,tag,createdat,channel,link
            String[] buffer= sCurrentLine.split("breakbreakbreak");
            String showTabe="SHOW_YOUTUBE_"+buffer[0].trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'","");
              // System.out.println("DROP TABLE "+showTabe+";");
            System.out.println("CREATE TABLE "+showTabe );
            System.out.println("  (id VARCHAR(100)  NOT NULL ,\n" +
                    "                    show_name VARCHAR(100) DEFAULT NULL,\n" +
                    "                    title VARCHAR(100) DEFAULT NULL,\n" +
                    "                    official  VARCHAR(20) DEFAULT NULL ,\n" +
                    "                    likes INT DEFAULT 0,\n" +
                    "                    dislikes INT DEFAULT 0,\n" +
                    "                    views INT DEFAULT 0,\n" +
                    "                    comments INT DEFAULT 0,\n" +
                    "                    width INT DEFAULT 0,\n" +
                    "                    embedCodeVideo VARCHAR(700) DEFAULT NULL,\n" +
                    "                    embedCodePic VARCHAR(700) DEFAULT NULL,\n" +
                    "                    tag VARCHAR(100) DEFAULT NULL,\n" +
                    "                    created_on VARCHAR(100) DEFAULT NULL,\n" +
                    "                    channel VARCHAR(100) DEFAULT NULL,\n" +
                    "                    link VARCHAR(100) DEFAULT NULL,\n" +
 "                 PRIMARY KEY ( id,show_name)," +
                    "        INDEX(created_on),\n" +
                    "        INDEX(show_name),\n" +
                    "        INDEX(show_name,created_on));");
        }
        br.close();

    }


}
