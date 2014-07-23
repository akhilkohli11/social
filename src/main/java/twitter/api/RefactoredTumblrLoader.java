package twitter.api;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.*;
import org.joda.time.DateTime;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by akohli on 6/20/14.
 */
public class RefactoredTumblrLoader {
    static JumblrClient client;
    public static void init()
    {
        client = new JumblrClient(
                "kif6piqkn0SLiQZj2JufRjJBAgolt1eyRPLTXaLg87ZpPW8WjB",
                "wNhAqjvxuHHfKIBZpqzozMeHYosFAyvA1Kx5xBLZksR1kVFj8a"
        );
        client.setToken(
                "irpltcHJ6K3ZEXPKCJjV0B6bdHYJfMsT8oldCv6EaD2nBG87SI",
                "ZHdZH7guXA1iemJ7mpcTZSdZqYchTUciD2QxgreGQ99rLhauIf"
        );
    }
    String fileDirectory="/usr/local/apache-tomcat-7.0.47/webapps/examples/";





    public static void loadTumblr(TumblrSqlLayer tumblrSqlLayer,Map<String,List<SearchObject>> searchMap) throws Exception
    {
        Map<String, String> options = new HashMap<String, String>();
        Date date = new Date();
        long unixTime = (long) date.getTime()/1000;
        options.put("before", String.valueOf(unixTime));
        options.put("limit", "100");
        int count=0;
        while(count++<24) {
            for(Map.Entry<String,List<SearchObject>> showSearch : searchMap.entrySet()) {
                for(SearchObject searchObject : showSearch.getValue()) {
                    if(searchObject.isBlog()) {
//
//                        Map<String, String> newoptions = new HashMap<String, String>();
//                        newoptions.put("type", "video");
//                        System.out.println(searchObject.getSearchTerm());
//                        List<Post> videoPosts = client.blogPosts(searchObject.getSearchTerm().trim(), newoptions);
//                        dbVidePost(tumblrSqlLayer,videoPosts,showSearch.getKey(),"true");
//                        newoptions.put("type", "photo");
//                        List<Post> photoposts = client.blogPosts(searchObject.getSearchTerm(), newoptions);
//
//
//                        newoptions.put("type", "audio");
//                        List<Post> audioPosts = client.blogPosts(searchObject.getSearchTerm(), newoptions);
//
//
//                        newoptions.put("type", "text");
//                        List<Post> textPosts = client.blogPosts(searchObject.getSearchTerm(), newoptions);
//



                    }
                    else {
                        List<Post> posts = client.tagged(searchObject.getSearchTerm().trim(), options);
                        for (Post post : posts) {
                            persist(tumblrSqlLayer,post,showSearch.getKey(),searchObject.getIsOfficial().toString());

                        }
                    }
                }
                Thread.sleep(1000);
            }

            Date daysAgo = new DateTime(date).minusHours(1).toDate();
            date = daysAgo;
            unixTime = (long) daysAgo.getTime() / 1000;
            options.put("before", String.valueOf(unixTime));
        }


    }


    private static void persist(TumblrSqlLayer tumblrSqlLayer,Post post,String showName,String official) throws Exception{
        //text, quote, link, answer, video, audio, photo, chat
        if(post.getType().equals("photo"))
        {
            dbPhotoPost(tumblrSqlLayer,Arrays.asList(post),showName,official);

        }
        if(post.getType().equals("video"))
        {
            dbVidePost(tumblrSqlLayer,Arrays.asList(post),showName,official);
        }

        if(post.getType().equals("audio"))
        {
            dbAudioPost(tumblrSqlLayer,Arrays.asList(post),showName,official);

        }
        if(post.getType().equals("text"))
        {
            dbTextPost(tumblrSqlLayer, Arrays.asList(post), showName, official);

        }

        if(post.getType().equals("quote"))
        {
            dbQuotePost(tumblrSqlLayer, Arrays.asList(post), showName, official);

        }

    }

    private static void dbQuotePost(TumblrSqlLayer tumblrSqlLayer, List<Post> posts, String showName, String official) throws Exception{
        for(Post post : posts)
        {
            String name=post.getBlogName();
            Blog blog=client.blogInfo(name);
            Long id=post.getId();
            String type="video";
            QuotePost videoPostNEw=(QuotePost)post;
            String url=videoPostNEw.getSourceUrl();
            String time=post.getDateGMT();
            int likes=blog.getFollowersCount();
            int followers=blog.getLikeCount();
            tumblrSqlLayer.populateTumblrData(id,name,videoPostNEw.getText(),showName,videoPostNEw.getSourceTitle(),official,"quote",0,likes,followers,0,
                    "",time,url);

        }
    }


    private static void dbVidePost(TumblrSqlLayer tumblrSqlLayer,List<Post> videoPosts,String showName,String official) throws Exception
    {
        for(Post post : videoPosts)
        {
            String name=post.getBlogName();
            Blog blog=client.blogInfo(name);
            Long id=post.getId();
            String type="video";
            VideoPost videoPostNEw=(VideoPost)post;
            String url=videoPostNEw.getThumbnailUrl();
            String time=post.getDateGMT();
            for(Video video : videoPostNEw.getVideos())
            {
                int width=video.getWidth();
                String embedCode=video.getEmbedCode();
                int likes=blog.getFollowersCount();
                int followers=blog.getLikeCount();
                tumblrSqlLayer.populateTumblrData(id,name,null,showName,null,official,"video",0,likes,followers,width,
                        embedCode,time,url);
            }
        }
    }

    private static void dbPhotoPost(TumblrSqlLayer tumblrSqlLayer,List<Post> photoposts,String showName,String official) throws Exception
    {
        for(Post post : photoposts)
        {
            String name=post.getBlogName();
            Blog blog=client.blogInfo(name);
            Long id=post.getId();
            String type="photo";
            PhotoPost photoPosts=(PhotoPost)post;
            String url=photoPosts.getSourceUrl();
            String time=post.getDateGMT();
            for(Photo photo : photoPosts.getPhotos())
            {
                String embedCode=photo.getOriginalSize().getUrl();
                int likes=blog.getFollowersCount();
                int followers=blog.getLikeCount();
                tumblrSqlLayer.populateTumblrData(id,name,null,showName,null,official,"photo",0,likes,followers,photo.getOriginalSize().getWidth(),
                        embedCode,time,url);
            }
        }
    }

    private static void dbAudioPost(TumblrSqlLayer tumblrSqlLayer,List<Post> audioPosts,String showName,String official) throws Exception
    {
        for(Post post : audioPosts)
        {
            String name=post.getBlogName();
            Blog blog=client.blogInfo(name);
            Long id=post.getId();
            AudioPost audioPost=(AudioPost)post;
            String url=audioPost.getSourceUrl();
            String time=post.getDateGMT();

            String embedCode=audioPost.getEmbedCode();
            int likes=blog.getFollowersCount();
            int followers=blog.getLikeCount();
            tumblrSqlLayer.populateTumblrData(id,name,null,showName,null,official,"audio",0,likes,followers,0,
                    embedCode,time,url);

        }
    }

    private static void dbTextPost(TumblrSqlLayer tumblrSqlLayer,List<Post> videoPosts,String showName,String official) throws Exception
    {
        for(Post post : videoPosts)
        {
            String name=post.getBlogName();
            Blog blog=client.blogInfo(name);
            Long id=post.getId();
            String type="video";
            TextPost videoPostNEw=(TextPost)post;
            String url=videoPostNEw.getSourceUrl();
            String time=post.getDateGMT();
            int likes=blog.getFollowersCount();
            int followers=blog.getLikeCount();
            tumblrSqlLayer.populateTumblrData(id,name,videoPostNEw.getBody(),showName,videoPostNEw.getTitle(),official,"text",0,likes,followers,0,
                    "",time,url);

        }
    }


    public static void loadTumblrData(TumblrSqlLayer tumblrSqlLayer,Map<String,String> map) throws Exception{
        Map<String,List<SearchObject>> searchList=new HashMap<String, List<SearchObject>>();
        for(Map.Entry<String,String> entry : map.entrySet()) {
            List<SearchObject> searchObjectList = new ArrayList<SearchObject>();
            if (entry.getValue() == null) {
                searchObjectList.add(new SearchObject(entry.getKey(), "true", false));
            } else {
                searchObjectList.add(new SearchObject(entry.getKey(), "false", false));
                searchObjectList.add(new SearchObject(entry.getValue(), "true", true));
            }
            searchList.put(entry.getKey(), searchObjectList);
        }
        loadTumblr(tumblrSqlLayer,searchList);

    }




}
