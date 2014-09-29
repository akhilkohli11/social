package twitter.api;

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

/**
 * Created by akohli on 8/25/14.
 */
public class YoutubeLoader {

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

    private static int goback=6*30;

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

        // Checks that the defaults have been replaced (Default = "Enter X here").
//        if (clientSecrets.getDetails().getClientId().startsWith("Enter")
//                || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
//            System.err.println(
//                    "Enter Client ID and Secret from https://code.google.com/apis/console/?api=youtube"
//                            + "into youtube-analytics-cmdline-report-sample/src/main/resources/client_secrets.json");
//            System.exit(1);
//        }

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
    public static void populate(YoutubeSqlLayer youtubeSqlLayer, Map<String,String> searchMap) throws Exception {

        YouTube.Search.List search = youtube.search().list("id,snippet");

        int newcount = 0;
        while (newcount++ < 500) {
            int count = 0;

            search.setType("video");

            long number = 50;
            Date date = new Date();
            Date daysAgo =  new org.joda.time.DateTime(date).minusHours(4).toDate();
            while (count++ < goback) {
                search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
                search.setMaxResults(number);
                search.setPublishedAfter(new com.google.api.client.util.DateTime(daysAgo));
                search.setPublishedBefore(new com.google.api.client.util.DateTime(date));

                date = new org.joda.time.DateTime(date).minusHours(1).toDate();
                daysAgo = new org.joda.time.DateTime(date).minusHours(4).toDate();
                for (Map.Entry<String, String> showSearch : searchMap.entrySet()) {
                    try {
                        String queryTerm=showSearch.getKey()+" tv series american   ";
                        search.setQ(queryTerm);


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
                                newprettyPrint(videoList.iterator(), queryTerm,youtubeSqlLayer,showSearch.getValue(),
                                        showSearch.getKey());
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            }
            Thread.sleep(1000*60*300);
        }
    }


    static Set<String> id=new HashSet<String>();

    private static void newprettyPrint(Iterator<Video> iteratorVideoResults, String query,YoutubeSqlLayer youtubeSqlLayer,
                                       String officialChannel,String showName) {

        System.out.println("\n=============================================================");
        System.out.println(
                "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");
        System.out.println("=============================================================\n");

        if (!iteratorVideoResults.hasNext()) {
            System.out.println(" There aren't any results for your query."+query);
        }

        while (iteratorVideoResults.hasNext()) {
            String official="false";
            Video singleVideo = iteratorVideoResults.next();
            try{
                if(singleVideo.getSnippet()==null)
                {
                    continue;
                }
                if(singleVideo.getSnippet().getChannelTitle().toLowerCase().contains(officialChannel)) {
                    official="true";
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

                    youtubeSqlLayer.populateYoutubeData(singleVideo.getId(),showName,videoSnippet.getTitle(),
                            official,likes,dislikes,views,comments,singleVideo.getPlayer().getEmbedHtml(),null,null,
                            videoSnippet.getPublishedAt(),videoSnippet.getChannelTitle(),null);
                CloudSolrPersistenceLayer.getInstance().populateYoutubeData(singleVideo.getId(),showName,videoSnippet.getTitle(),
                        official,likes,dislikes,views,comments,singleVideo.getPlayer().getEmbedHtml(),null,null,
                        videoSnippet.getPublishedAt(),videoSnippet.getChannelTitle(),null);


            }
            catch (Exception e)
            {
              //  e.printStackTrace();
            }

        }
    }





//    private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {
//
//        System.out.println("\n=============================================================");
//        System.out.println(
//                "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");
//        System.out.println("=============================================================\n");
//
//        if (!iteratorSearchResults.hasNext()) {
//            System.out.println(" There aren't any results for your query.");
//        }
//
//        while (iteratorSearchResults.hasNext()) {
//
//            SearchResult singleVideo = iteratorSearchResults.next();
//            ResourceId rId = singleVideo.getId();
//
//
//            // Confirm that the result represents a video. Otherwise, the
//            // item will not contain a video ID.
//            if (rId.getKind().equals("youtube#video")) {
//                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
//                // System.out.println(" Video Id" + rId.getVideoId() +" size "+id.size());
//                id.add(rId.getVideoId());
//                //     System.out.println(" Thumbnail: " + thumbnail.getUrl());
//                //  System.out.println("\n-------------------------------------------------------------\n");
//            }
//        }
//    }
}
