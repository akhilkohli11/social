package twitter.api;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.*;
import edu.stanford.nlp.util.ArrayHeap;
import org.joda.time.DateTime;

import java.util.*;

/**
 * Created by akohli on 6/19/14.
 */
public class TumblerMain {
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
//                postList.add(post.getId());
//
//             //   System.out.println(post.getId());
//           //     System.out.println(post.getPostUrl());
//             //   System.out.println(post.getFormat());
//                System.out.println(post.getType());
//
                Map<String,String> newoptions=new HashMap<String, String>();
                newoptions.put("type","video");
                //newoptions.put("tag","cwthe100");
                List<Post> videoPosts=client.blogPosts("cwthe100",newoptions);

                for(Post videoPost : videoPosts)
                {
                    VideoPost post1=(VideoPost)videoPost;

                    for(Video video : post1.getVideos())
                    {
//                        System.out.println("vide   "+video.getEmbedCode());
//                        System.out.println("vide   "+video.getWidth());
                    }

                }

                newoptions.put("type","photo");
                //newoptions.put("tag","cwthe100");
                List<Post> photo=client.blogPosts("cwthe100",newoptions);

                for(Post videoPost : photo)
                {
                    PhotoPost post1=(PhotoPost)videoPost;

                    for(Photo phot1: post1.getPhotos())
                    {
                        for(PhotoSize photoSize:phot1.getSizes())
                        {
//                            System.out.println("phpt   "+photoSize.getUrl());
//                            System.out.println("phpt   "+photoSize.getWidth());
//                            System.out.println("phpt   "+photoSize.getHeight());
                        }
                    }

                }

                newoptions.put("type","text");
                newoptions.put("tag","cwthe100");
                List<Post> textPosts=client.blogPosts("cwthe100",newoptions);
                for(Post videoPost : textPosts)
                {

                    TextPost post1=(TextPost)videoPost;

                    System.out.println("textss   "+post1.getBody());

                }

                newoptions.put("type","quote");
                newoptions.put("tag","cwthe100");
                List<Post> linkPosts=client.blogPosts("cwthe100",newoptions);
                for(Post videoPost : linkPosts)
                {
                    QuotePost post1=(QuotePost)linkPosts;

                    System.out.println("quot   "+post1.getText());

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
