package twitter.api;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.*;
import edu.stanford.nlp.util.ArrayHeap;
import org.joda.time.DateTime;

import java.util.*;

/**
 * Created by akohli on 6/19/14.
 */
public class TumblerTests {
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

            Map<String, String> newoptions = new HashMap<String, String>();
            newoptions.put("type", "video");
            List<Post> videoPosts = client.blogPosts("cwthe100", newoptions);

            for (Post videoPost : videoPosts) {
                VideoPost post1 = (VideoPost) videoPost;

                for (Video video : post1.getVideos()) {
                        System.out.println("vide   "+video.getEmbedCode());
                        System.out.println("vide   "+video.getWidth());
                }

            }


            System.out.println("Unique Posts" + postList.size());

            Date daysAgo = new DateTime(date).minusMinutes(30).toDate();
            date = daysAgo;
            unixTime = (long) daysAgo.getTime() / 1000;
            options.put("before", String.valueOf(unixTime));
        }

        }

    }

