package twitter.api;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by akohli on 6/23/14.
 */
public class TimeProgram {
    static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String args[]) throws Exception
    {
        String time="2014-06-20 23:56:11 GMT";
        System.out.println(new java.sql.Date(df.parse(time).getTime()));
        System.out.println(new Timestamp(df.parse(time).getTime()));

    }
}
