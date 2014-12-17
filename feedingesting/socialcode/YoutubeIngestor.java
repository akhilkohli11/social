package org.zap2it.ingester.social;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.com.google.common.base.Joiner;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.zap2it.clients.SolrClient;
import org.zap2it.util.Slug;


/**
 * Created by akohli on 8/25/14.
 */
public class YoutubeIngestor {



    public static void init() throws Exception {
        // Scopes required to access YouTube general and analytics information.
        List<String> scopes = com.google.common.collect.Lists.newArrayList(
                "https://www.googleapis.com/auth/yt-analytics.readonly",
                "https://www.googleapis.com/auth/youtube.readonly"
        );

        Credential credential = authorize(scopes);

        // YouTube object used to make all non-analytic API requests.
        youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), credential)
                .setApplicationName("youtube-analytics-api-report-example")
                .build();

    }

    /**
     * Define a global variable that identifies the name of a file that
     * contains the developer's API key.
     */
    private static final String PROPERTIES_FILENAME = "youtube.properties";

    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;

    private static List<String> allowedKeyWords=Arrays.asList( "show","clip","sea1son","series","episode","premiere","preview","recap","tease");

    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private static YouTube youtube;

    /**
     * Initialize a YouTube object to search for videos on YouTube. Then
     * display the name and thumbnail image of each video in the result set.
     *
     */
    private static Credential authorize(List<String> scopes) throws Exception {

        InputStream inputStream = new FileInputStream("/tmp/client_secrets.json");
        System.out.println(inputStream);
        Reader reader = new InputStreamReader(inputStream);
        // Load client secrets.
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(
                        new JacksonFactory(),
                        reader);


        // Set up file credential store.
        FileCredentialStore credentialStore =
                new FileCredentialStore(
                        new File(System.getProperty("user.home"),
                                ".credentials/youtube-analytics-api-report.json"),
                        new JacksonFactory()
                );

        // Set up authorization code flow.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(new NetHttpTransport(),
                        new JacksonFactory(),
                        clientSecrets,
                        scopes)
                        .setCredentialStore(credentialStore).build();

        // Authorize.
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    /**
     * Authorizes user, gets user's default channel via YouTube Data API, and gets/prints stats on
     * user's channel using the YouTube Analytics API.
     *
     */
    public static void populate( List<Youtube> youtubeList,Date after,Date before,String type,boolean isSeed) throws Exception {

        YouTube.Search.List search = youtube.search().list("id,snippet");

        int newcount = 0;
        while (newcount++ <    2) {

            search.setType("video");
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");


            for (Youtube youtubeEntry : youtubeList) {
                int initialCount=0;
                if(youtubeEntry.isGeneric())
                {
                    initialCount=1;
                }
                for(;initialCount<=1;initialCount++)
                    try {
                        String queryTerm=youtubeEntry.getQuery();
                        if(initialCount==1) {
                            search.setQ(queryTerm);
                        }
                        if(initialCount==0)
                        {
                            search.setQ(youtubeEntry.getShowName());
                        }
                        if(type!=null) {
                            search.setOrder(type);
                        }

                        if(after!=null && before!=null) {
                            search.setPublishedAfter(new com.google.api.client.util.DateTime(after));
                            search.setPublishedBefore(new com.google.api.client.util.DateTime(before));
                        }

                        // Call the API and print res
                        // ults.
                        List<String> videoIds = new ArrayList<String>();
                        SearchListResponse searchResponse = search.execute();
                        List<SearchResult> searchResultList = searchResponse.getItems();
//                        if (searchResultList != null) {
//                            prettyPrint(searchResultList.iterator(), queryTerm);
//                        }

                        if (searchResultList != null) {
                            for (SearchResult searchResult : searchResultList) {
                                videoIds.add(searchResult.getId().getVideoId());
                            }
                            Joiner stringJoiner = Joiner.on(',');
                            String videoId = stringJoiner.join(videoIds);

                            // Call the YouTube Data API's youtube.videos.list method to
                            // retrieve the resources that represent the specified videos.
                            YouTube.Videos.List listVideosRequest = youtube.videos().list("snippet, recordingDetails,statistics,player").setId(videoId);
                            VideoListResponse listResponse = listVideosRequest.execute();

                            List<Video> videoList = listResponse.getItems();

                            if (videoList != null) {
                                newprettyPrint(videoList.iterator(),youtubeEntry,isSeed);
                            }

                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                Thread.sleep(1000);
            }

        }
    }

    private static void  newprettyPrint(Iterator<Video> iteratorVideoResults,Youtube youtubeEntry,
                                        boolean isSeed) {

        System.out.println("\n=============================================================");
        System.out.println(
                "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + youtubeEntry.getQuery() + "\".");
        System.out.println("=============================================================\n");

        if (!iteratorVideoResults.hasNext()) {
            System.out.println(" There aren't any results for your query."+youtubeEntry.getQuery());
        }

        while (iteratorVideoResults.hasNext()) {
            Video singleVideo = iteratorVideoResults.next();
            try{
                if(singleVideo.getSnippet()==null)
                {
                    continue;
                }

                int likes=0;
                int dislikes=0;
                int comments=0;
                int views=0;
                if(singleVideo.getStatistics()!=null) {
                    VideoStatistics singleVideoStatistics=singleVideo.getStatistics();
                    likes=Integer.parseInt(singleVideoStatistics.getLikeCount().toString());
                    dislikes=Integer.parseInt(singleVideoStatistics.getDislikeCount().toString());
                    comments=Integer.parseInt(singleVideoStatistics.getCommentCount().toString());
                    views=Integer.parseInt(singleVideoStatistics.getViewCount().toString());


                }
                VideoSnippet videoSnippet=singleVideo.getSnippet();
                String thumbnailPic=null;
                if(videoSnippet.getThumbnails()!=null && videoSnippet.getThumbnails().getDefault()!=null &&
                        videoSnippet.getThumbnails().getDefault().getUrl()!=null )
                {
                    thumbnailPic=videoSnippet.getThumbnails().getHigh().getUrl();
                }

                SolrClient client = new SolrClient("content");
                String official=videoSnippet.getChannelTitle().toLowerCase().trim().equals(youtubeEntry.getOfficalChannel().toLowerCase().trim())?
                        "Yes":"No";
                String trimmedTitle=videoSnippet.getTitle().trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'","");
                String trimmedShowName=youtubeEntry.getShowName().trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'", "");

                if(official.equals("Yes") && (!youtubeEntry.isGeneric() || trimmedTitle.contains(trimmedShowName)))
                {
                    populateYoutubeData(client, singleVideo.getId(), youtubeEntry, videoSnippet.getTitle(),
                            likes, dislikes, views, comments, singleVideo.getPlayer().getEmbedHtml(), thumbnailPic,
                            videoSnippet.getPublishedAt(), videoSnippet.getChannelTitle(), videoSnippet.getDescription(), official);
                }

                else if(trimmedTitle.contains(trimmedShowName)) {
                    for(String keyword :allowedKeyWords) {
                        if(trimmedTitle.contains(keyword) || trimmedTitle.contains(youtubeEntry.getOfficalChannel())
                        || isMatchFound(trimmedTitle,"S[0-9][0-9]*E[0-9][0-9]*") || isMatchFound(trimmedTitle,"s[0-9][0-9]*e[0-9][0-9]*")
                                || isMatchFound(trimmedTitle,"[0-9][0-9]*x[0-9][0-9]*")) {
                            populateYoutubeData(client, singleVideo.getId(), youtubeEntry, videoSnippet.getTitle(),
                                    likes, dislikes, views, comments, singleVideo.getPlayer().getEmbedHtml(), thumbnailPic,
                                    videoSnippet.getPublishedAt(), videoSnippet.getChannelTitle(), videoSnippet.getDescription(), official);
                        }
                    }
                }


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }

    public static   void populateYoutubeData(SolrClient client,String id,Youtube youtubeEntry,String title,int likes,
                                             int dislikes,int views,int comments,String embedCodeVideo,
                                             String embedCodePic,com.google.api.client.util.DateTime createdAt,String channel,
                                             String description,String official)
    {
        String embedID=getEmbedCode(embedCodeVideo);
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField("id", "YOUTUBE" + id);
        doc.addField("document_type", "youtube_video");
        doc.addField("lang", "en");
        doc.addField("title", title);
        doc.addField("description", description);
        doc.addField("publish_date", new Date(createdAt.getValue()));
        doc.addField("slug_id", Slug.makeSlug(title)+"-youtube"+"-"+id);
        doc.addField("thumbnail", embedCodePic);
        doc.addField("author", channel);
        doc.addField("is_current", true);
        doc.addField("s_official", official);
        doc.addField("s_orig_url", embedID);
        doc.addField("recommended_shows", youtubeEntry.getTmsShowID());
        doc.addField("s_provider", "youtube");
        doc.addField("b_displayContent", true);
        doc.addField("num_ratings_likes", likes);
        doc.addField("num_ratings_dislikes", dislikes);
        doc.addField("num_ratings_views",views);
        doc.addField("num_ratings_comments",comments);

        client.postToSolr(doc);
    }

    private static String getEmbedCode(String embedCodeVideo) {
        String youtubeUrl= StringUtils.substringBetween(embedCodeVideo, "http://www.youtube.com/embed/", "'");
        String landingYoutubeUrl="http://www.youtube.com/embed/"+youtubeUrl;
        return landingYoutubeUrl;

    }

    public static boolean isMatchFound(String input,String pattern)
    {
        Pattern p = Pattern.compile("S[0-9][0-9]*E[0-9][0-9]*");
        Matcher m = p.matcher(input);
        while (m.find()) { // find next match
            String match = m.group();
            System.out.println(match);
        }
        return m.matches();
    }


}
