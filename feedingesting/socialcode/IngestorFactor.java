package org.zap2it.ingester.social;

/**
 * Created by akohli on 11/16/14.
 */
public class IngestorFactor {
    private   static SocialIngestor youtubeSocialIngestor =new YoutubeFileInputSocialIngestor();
    private   static SocialIngestor tumblrSocialIngestor =new TumblrFileInputSocialIngestor();
    private static SocialIngestor twitterSocialIngestor=new TwitterFileInputSocialIngestor();

    public static SocialIngestor getIngestor(String socialType)
    {
        if(socialType.equals("youtube"))
        {
            return youtubeSocialIngestor;
        }
        if(socialType.equals("tumblr"))
        {
            return tumblrSocialIngestor;
        }
        if(socialType.equals("twitter"))
        {
            return twitterSocialIngestor;
        }
        return null;
    }


}
