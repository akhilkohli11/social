/*
* Copyright (c) 2012 Google Inc.
*
* Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
* in compliance with the License. You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software distributed under the License
* is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
* or implied. See the License for the specific language governing permissions and limitations under
* the License.
*/

package twitter.api;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.repackaged.com.google.common.base.Joiner;
import com.google.api.client.util.Lists;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import com.google.api.client.util.DateTime;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;

import java.io.*;
import java.util.*;

/**
* Print a list of videos matching a search term.
*
* @author Jeremy Walker
*/
public class YoutubeResearch {

    /**
     * Define a global variable that identifies the name of a file that
     * contains the developer's API key.
     */
    private static final String PROPERTIES_FILENAME = "youtube.properties";

    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;

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

        InputStream  inputStream=new FileInputStream("/tmp/client_secrets.json");
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
                        new JacksonFactory());

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
     * @param args command line args (not used).
     */
    public static void main(String[] args) {

        // Scopes required to access YouTube general and analytics information.
        List<String> scopes = com.google.common.collect.Lists.newArrayList(
                "https://www.googleapis.com/auth/yt-analytics.readonly",
                "https://www.googleapis.com/auth/youtube.readonly"
        );

        try {
            Credential credential = authorize(scopes);

            // YouTube object used to make all non-analytic API requests.
            youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), credential)
                    .setApplicationName("youtube-analytics-api-report-example")
                    .build();
                // Authorize the request.
               // Credential credential = Auth.authorize(scopes, "createbroadcast");


                // Authorize the request.

//            youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
//
//                //   new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer()  {
//            public void initialize(HttpRequest request) throws IOException {
//                }
//            }).setApplicationName("akhilkohli11").build();

            String queryTerm = getInputQuery();

            // Define the API request for retrieving search results.
            YouTube.Search.List search = youtube.search().list("id,snippet");

            // Set your developer key from the Google Developers Console for
            // non-authenticated requests. See:
            // https://console.developers.google.com/

          //  search.setKey(apiKey);
           // search.setOauthToken("OswRKDKyb_g6YadfkQSUNQBm");
            //search.set

            search.setQ(queryTerm);

            // Restrict the search results to only include videos. See:
            // https://developers.google.com/youtube/v3/docs/search/list#type
            search.setType("video");

            // To increase efficiency, only retrieve the fields that the
            // application uses.
            long number=50;
            Date date = new Date();
            Date daysAgo = new org.joda.time.DateTime(date).minusDays(5).toDate();
        while (true)
            {
            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(number);
            search.setPublishedAfter(new DateTime(daysAgo));
                search.setPublishedBefore(new DateTime(date));
                 date = new org.joda.time.DateTime(date).minusDays(1).toDate();
                 daysAgo = new org.joda.time.DateTime(date).minusDays(1).toDate();


                // Call the API and print res
            // ults.
                List<String> videoIds = new ArrayList<String>();
                SearchListResponse searchResponse = search.execute();
                List<SearchResult> searchResultList = searchResponse.getItems();
                if (searchResultList != null) {
                    prettyPrint(searchResultList.iterator(), queryTerm);
                }

                if (searchResultList != null) {
                    for (SearchResult searchResult : searchResultList) {
                        videoIds.add(searchResult.getId().getVideoId());
                    }
                }
                Joiner stringJoiner = Joiner.on(',');
                String videoId = stringJoiner.join(videoIds);

                // Call the YouTube Data API's youtube.videos.list method to
                // retrieve the resources that represent the specified videos.
                YouTube.Videos.List listVideosRequest = youtube.videos().list("snippet, recordingDetails,statistics,player").setId(videoId);
                VideoListResponse listResponse = listVideosRequest.execute();

                List<Video> videoList = listResponse.getItems();

                if (videoList != null) {
                    newprettyPrint(videoList.iterator(), queryTerm);
                }

                Thread.sleep(10000);
            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /*
     * Prompt the user to enter a query term and return the user-specified term.
     */
    private static String getInputQuery() throws IOException {


        return "orange is new black";
    }

    /*
     * Prints out all results in the Iterator. For each result, print the
     * title, video ID, and thumbnail.
     *
     * @param iteratorSearchResults Iterator of SearchResults to print
     *
     * @param query Search query (String)
     */
    static Set<String> id=new HashSet<String>();

    private static void newprettyPrint(Iterator<Video> iteratorVideoResults, String query) {

        System.out.println("\n=============================================================");
        System.out.println(
                "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");
        System.out.println("=============================================================\n");

        if (!iteratorVideoResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
        }

        while (iteratorVideoResults.hasNext()) {

            Video singleVideo = iteratorVideoResults.next();
        //    singleVideo.get
        try{
            Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
            if(singleVideo.getSnippet().getChannelTitle().toLowerCase().contains("netflix")) {
//            GeoPoint location = singleVideo.getRecordingDetails().getLocation();
                System.out.println(" Video Id" + singleVideo.getId());
                System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
                System.out.println(" Embed: " + singleVideo.getPlayer().getEmbedHtml());
                System.out.println(" Etag: " + singleVideo.getEtag());
                System.out.println(" published: " + singleVideo.getSnippet().getPublishedAt());
                System.out.println(" snippet tags: " + singleVideo.getSnippet().getTags());
                System.out.println(" Chanel Id" + singleVideo.getSnippet().getChannelId());

                System.out.println(" Chanel Id" + singleVideo.getSnippet().getChannelTitle());
            }

            }
        catch (Exception e)
        {
            e.printStackTrace();
        }
            //   System.out.println(" Location: " + location.getLatitude() + ", " + location.getLongitude());
//            System.out.println(" Thumbnail: " + thumbnail.getUrl());
//            if(singleVideo.getStatistics()!=null) {
//                System.out.println(singleVideo.getStatistics().getCommentCount() + "   " +
//                        singleVideo.getStatistics().getLikeCount() + "   " +singleVideo.getStatistics().getDislikeCount()+" "+ singleVideo.getStatistics().getViewCount());
//            }
//            System.out.println("\n-------------------------------------------------------------\n");
        }
    }

    private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

        System.out.println("\n=============================================================");
        System.out.println(
                "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");
        System.out.println("=============================================================\n");

        if (!iteratorSearchResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
        }

        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();


            // Confirm that the result represents a video. Otherwise, the
            // item will not contain a video ID.
            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
               // System.out.println(" Video Id" + rId.getVideoId() +" size "+id.size());
                id.add(rId.getVideoId());
               //     System.out.println(" Thumbnail: " + thumbnail.getUrl());
              //  System.out.println("\n-------------------------------------------------------------\n");
            }
        }
    }
}