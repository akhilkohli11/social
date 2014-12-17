package twitter.api;

import facebook4j.*;
import facebook4j.auth.AccessToken;
import org.joda.time.DateTime;

import java.net.URL;
import java.util.Date;
import java.util.Map;

/**
 * Created by akohli on 11/2/14.
 */
public class FacebookInitializer {
    static Facebook facebook;

    public static void main(String args[]) throws Exception
    {
        init();

    }

    public static void init() throws Exception {
        facebook = new FacebookFactory().getInstance();
        facebook.setOAuthAppId("716670628425125", "c2647b471fc740abd80c30838b041218");
        // facebook.setOAuthPermissions(commaSeparetedPermissions);
        facebook.setOAuthAccessToken(new AccessToken("716670628425125|K8Q1ZVxbMVAbgvXdskRe29L3o7M", null));
    }


    public static void populate( Map<String,String> searchMap,String query) throws Exception {


        int newcount = 0;
        while (newcount++ < 2) {


            for (Map.Entry<String, String> showSearch : searchMap.entrySet()) {
                try {
                    int count=0;
                    ResponseList<facebook4j.Page> pages = facebook.searchPages(showSearch.getKey()+query);
                    for (Page page : pages ) {
                        try {
                            System.out.println("Page" + page.getLink() + page.getLikes() + "  " + page.getTalkingAboutCount()
                                    + page.getLink() + page.getName());
                            Reading reading1 = new Reading();
                            reading1.filter(page.getId());
                            ResponseList<Post> feed = facebook.getFeed(page.getId());
                            for (Post post : feed) {
                                if (post != null) {
                                    CloudSolrPersistenceLayer.getInstance().populateFacebook(post.getId(), post.getSharesCount(),
                                            post.getScheduledPublishTime(), showSearch.getKey(), showSearch.getValue());
                                }
                            }
                            if(count++>3)
                            {
                                break;
                            }
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                }catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }
        Thread.sleep(1000*30);
    }


}
