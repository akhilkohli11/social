package twitter.api;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.*;
import edu.stanford.nlp.util.ArrayHeap;
import org.joda.time.DateTime;

import java.util.*;

/**
 * Created by akohli on 6/19/14.
 */
public class RetreivingTextPost {
    public static void main(String args[]) throws InterruptedException {
        JumblrClient client = new JumblrClient(
                "kif6piqkn0SLiQZj2JufRjJBAgolt1eyRPLTXaLg87ZpPW8WjB",
                "wNhAqjvxuHHfKIBZpqzozMeHYosFAyvA1Kx5xBLZksR1kVFj8a"
        );
        client.setToken(
                "irpltcHJ6K3ZEXPKCJjV0B6bdHYJfMsT8oldCv6EaD2nBG87SI",
                "ZHdZH7guXA1iemJ7mpcTZSdZqYchTUciD2QxgreGQ99rLhauIf"
        );
        Map<String, String> options = new HashMap<String, String>();
        Date date = new Date();
        long unixTime = (long) date.getTime()/1000;
        options.put("before", String.valueOf(unixTime));
        options.put("limit", "100");
        Date daysAgo1 = new DateTime(date).minusDays(1).toDate();
        System.out.println(daysAgo1);
        long count=0;
        long value=89236519017L;
        Set<Long> postList=new HashSet<Long>();
        // while (true)

        while(true) {
            count++;
            List<Post> posts = client.tagged("big bang theory",options);
            posts.addAll(client.tagged("game of thrones", options));

            String id="";
            String time="";
            System.out.println("Call Posts"+posts.size());
            for(Post post : posts)
            {

               if(post.getType().equals("text"))
                {

                    TextPost post1=(TextPost)post;

                  //  System.out.println("textss   "+post1.getBody());x
                    System.out.println("textss   "+post1.getTitle());


                }

                if(post.getType().equals("quote"))
                {

                    QuotePost post1=(QuotePost)post;

                    System.out.println("textss   "+post1.getText());

                }

                long newid=post.getId()+10;

                //  System.out.println(post.getDateGMT());
                time=post.getDateGMT();
                //   System.out.println(time);
                id=String.valueOf(newid);
            }
            System.out.println("Unique Posts"+postList.size());

            Date daysAgo = new DateTime(date).minusMinutes(30).toDate();
            date=daysAgo;
            unixTime = (long) daysAgo.getTime()/1000;
            options.put("before", String.valueOf(unixTime));
//
//            if(count==3)
//            {
//                System.out.println("coming here");
//                options.put("before", "1376888900");
//                time="2013-06-02 23:57:21 GMT";
//            }
//            else if(count==4)
//            {
//                options.put("before", "1376988900");
//
//                time="2014-03-02 23:57:21 GMT";
//            }


            // options.put("offset",   );


            Thread.sleep(2000);
            // options.put("offset",String.valueOf(value+count));
        }

    }




}
