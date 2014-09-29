package twitter.api;


import facebook4j.*;
import facebook4j.auth.AccessToken;
import org.joda.time.DateTime;

import java.util.Date;

/**
* Created by akohli on 9/19/14.
*/
public class FacebookAPI {

    public static void main(String args[]) throws Exception
    {

        Facebook facebook = new FacebookFactory().getInstance();
        facebook.setOAuthAppId("716670628425125", "c2647b471fc740abd80c30838b041218");
       // facebook.setOAuthPermissions(commaSeparetedPermissions);
        facebook.setOAuthAccessToken(new AccessToken("CAACEdEose0cBAGSwq3QLdwGLdC3Ja3Cy77BnTZCn89EtwaDmd4o5JIF3JwqCDOXdK4hhrZAzJjJwFDbojULtfiMkdpahrDaWdYr3URUfpcvAIhy9HWaCPByswX55hgYT1w166ZAKs5R0Rq9mF11PCkNGjgpgi9ZC5fuJ70XP3uSpODBlIDldKhbeWPrF3DrXlwGaiERn0u8t3EyZADY4Q", null));
        Date date = new Date();
        while (true) {
            Reading reading=new Reading();
            Date since = new DateTime(date).minusDays(4).toDate();
            Date until = new DateTime(date).minusDays(1).toDate();
            date = until;
            reading.since(since.toString());
            reading.until(until.toString());
            ResponseList<Post> results = facebook.searchPosts("#OITNB", reading);
            System.out.println(results.size());
            for (Post post : results) {
                System.out.println(post.getLink() + "   " + post.getCreatedTime());

            }
            Thread.sleep(2000);
        }


    }
}
