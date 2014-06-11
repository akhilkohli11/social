package twitter.api;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by akohli on 6/9/14.
 */
public class MainJava {
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_URL = "jdbc:mysql://localhost/social?"
            + "user=root";

    public static void main(String args[])
    {
        SocialMysqlLayer socialMysqlLayer = new SocialMysqlLayer(MYSQL_DRIVER, MYSQL_URL);
        BufferedReader br = null;
        try {
            SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");

            // Tab delimited file will be written to data with the name tab-file.csv
            String time="Mon Jun 09 01:25:14 +0000 2014";
            System.out.println(new Timestamp(df.parse(time).getTime()));
            socialMysqlLayer.updateTime(1);
        } catch (Exception e) {
            System.out.println("Error Printing Tab Delimited File");
        }
    }
}
