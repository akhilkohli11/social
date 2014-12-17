package org.zap2it.ingester.social;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by akohli on 11/16/14.
 */
public class SocialInitializer {
    public static Logger logger = LoggerFactory.getLogger(SocialInitializer.class);
    public static void main(String[] args) throws Exception {
        YoutubeDaemon youtubeDaemon=new YoutubeDaemon();
        youtubeDaemon.init();
        TumblrDaemon tumblrDaemon=new TumblrDaemon();
        tumblrDaemon.init();
       TwitterDaemon twitterDaemon=new TwitterDaemon();
        twitterDaemon.init();
    }

}
