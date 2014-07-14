package twitter.api;

import org.joda.time.DateTime;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by akohli on 6/23/14.
 */
public class TimeProgram {
    static DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String args[]) throws Exception
    {
        String time="2014-06-20 23:56:11 GMT";
        Date date = new Date();
        Date newdaysAgo = new DateTime(date).minusHours(14).toDate();
        if(date.getTime()>df.parse(time).getTime()) {
            System.out.println(new java.sql.Date(date.getTime()));
            System.out.println(new java.sql.Date(df.parse(time).getTime()));
            System.out.println(new java.sql.Date(df.parse(time).getTime()));
            System.out.println(new Timestamp(df.parse(time).getTime()));
        }

    }
}
