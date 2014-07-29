package twitter.api;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by akohli on 6/30/14.
 */
public class OptionFile {

    public static void main(String args[]) throws Exception
    {

        createLocationSql();
    }

    private static void createLocationSql() throws Exception{
        BufferedReader br = null;
        String sCurrentLine;
        br = new BufferedReader(new FileReader("/tmp/showsfinal.txt"));
        int count=1;
        //  socialMysqlLayer.readData();
        while ((sCurrentLine = br.readLine()) != null) {


            String[] buffer= StringUtils.split(sCurrentLine, "@", 2);
            System.out.println("<option>"+
                    buffer[0].trim().replaceAll(" ","").replaceAll("\"","").replaceAll("'","")+
            "</option>");


        }

    }

    private static void createShowCountSql() throws Exception{
        BufferedReader br = null;
        String sCurrentLine;
        br = new BufferedReader(new FileReader("/tmp/showsfinal.txt"));
        int count=1;
        //  socialMysqlLayer.readData();
        while ((sCurrentLine = br.readLine()) != null) {


            String[] buffer= StringUtils.split(sCurrentLine, "@", 2);
            String showTabe="SHOW_COUNT_"+buffer[0].trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'","");
            //  System.out.println("DROP TABLE "+showTabe+";");
            System.out.println("CREATE TABLE "+showTabe );
            System.out.println("(show_name VARCHAR(100) DEFAULT NULL,\n" +
                    "            type  VARCHAR(60) DEFAULT NULL ,\n" +
                    "            socialType  VARCHAR(60) DEFAULT NULL ,\n" +
                    "            count INTEGER  NOT NULL,\n" +
                    "            created_on  DATETIME NOT NULL ,\n" +
                    "            lastUpdated  DATETIME NOT NULL ,\n" +
                    "         PRIMARY KEY ( show_name,type,socialType,lastUpdated,created_on),\n" +
                    "         INDEX(show_name),\n" +
                    "        INDEX(show_name,socialType,type,created_on),\n" +
                    "        INDEX(created_on),\n" +
                    "        INDEX(show_name),\n" +
                    "        INDEX(show_name,type,created_on)); ");
        }
        String showTabe="SHOW_COUNT_SOCIAL";
        //  System.out.println("DROP TABLE "+showTabe+";");
        System.out.println("CREATE TABLE "+showTabe );
        System.out.println("(show_name VARCHAR(100) DEFAULT NULL,\n" +
                "            type  VARCHAR(60) DEFAULT NULL ,\n" +
                "            socialType  VARCHAR(60) DEFAULT NULL ,\n" +
                "            count INTEGER  NOT NULL,\n" +
                "            created_on  DATETIME NOT NULL ,\n" +
                "            lastUpdated  DATETIME NOT NULL ,\n" +
                "         PRIMARY KEY ( show_name,type,socialType,lastUpdated,created_on),\n" +
                "         INDEX(show_name),\n" +
                "        INDEX(show_name,socialType,type,created_on),\n" +
                "        INDEX(created_on),\n" +
                "        INDEX(show_name),\n" +
                "        INDEX(show_name,type,created_on)); ");
        br.close();

    }

    private static void createTumblrSql() throws Exception{
        BufferedReader br = null;
        String sCurrentLine;
        br = new BufferedReader(new FileReader("/tmp/showsfinal.txt"));
        int count=1;
        //  socialMysqlLayer.readData();
        while ((sCurrentLine = br.readLine()) != null) {


            String[] buffer= StringUtils.split(sCurrentLine, "@", 2);
            String showTabe="SHOW_TUMBLR_"+buffer[0].trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'","");
            //   System.out.println("DROP TABLE "+showTabe+";");
            System.out.println("CREATE TABLE "+showTabe );
            System.out.println("  (postID BIGINT  NOT NULL ,\n" +
                    "                    text longtext character set utf8 collate utf8_polish_ci DEFAULT NULL,\n" +
                    "                    blogName VARCHAR(100) DEFAULT NULL,\n" +
                    "                    show_name VARCHAR(100) DEFAULT NULL,\n" +
                    "                    title VARCHAR(100) DEFAULT NULL,\n" +
                    "                    official  VARCHAR(20) DEFAULT NULL ,\n" +
                    "                    type  VARCHAR(20) DEFAULT NULL ,\n" +
                    "                    sentimentalScore INT DEFAULT 1,\n" +
                    "                    likes INT DEFAULT 0,\n" +
                    "                    followers INT DEFAULT 0,\n" +
                    "                    width INT DEFAULT 0,\n" +
                    "                    embedCode VARCHAR(1500) DEFAULT NULL,\n" +
                    "                   created_on  DATETIME NOT NULL ,\n" +
                    "                       lastUpdated  DATETIME NOT NULL ,\n" +
                    "                   url VARCHAR(100) DEFAULT NULL,\n" +
                    "                   posturl VARCHAR(100) DEFAULT NULL,\n" +

                    "                 PRIMARY KEY ( postID,blogName,show_name)," +
                    "        INDEX(created_on),\n" +
                    "        INDEX(show_name),\n" +
                    "        INDEX(show_name,type,created_on));");
        }
        br.close();

    }

    private static void createTwitterSql() throws Exception{
        BufferedReader br = null;
        String sCurrentLine;
        br = new BufferedReader(new FileReader("/tmp/showsfinal.txt"));
        int count=1;
        //  socialMysqlLayer.readData();
        while ((sCurrentLine = br.readLine()) != null) {

            String[] buffer= StringUtils.split(sCurrentLine, "@", 2);
            String showTabe="SHOW_TWITTER_"+buffer[0].trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'","");
            //  System.out.println("DROP TABLE "+showTabe+";");
            System.out.println("CREATE TABLE "+showTabe );
            System.out.println("(id BIGINT  NOT NULL AUTO_INCREMENT,\n" +
                    "            tweet longtext character set utf8 collate utf8_polish_ci DEFAULT NULL,\n" +
                    "            tweetText longtext character set utf8 collate utf8_polish_ci DEFAULT NULL,\n" +
                    "            show_name VARCHAR(100) DEFAULT NULL,\n" +
                    "            type  VARCHAR(60) DEFAULT NULL ,\n" +
                    "            created_on  DATETIME NOT NULL ,\n" +
                    "            lastUpdated  DATETIME NOT NULL ,\n" +
                    "            sentimentalScore INT NOT NULL,\n" +
                    "            embedCode VARCHAR(1500) DEFAULT NULL,\n" +
                    "         PRIMARY KEY ( id ,show_name),\n" +
                    "         INDEX(show_name),\n" +
                    "        INDEX(show_name,type),\n" +
                    "        INDEX(type),\n" +
                    "        INDEX(created_on))ENGINE=InnoDB AUTO_INCREMENT=576642 DEFAULT CHARSET=latin1;");
        }
        br.close();

    }
}
