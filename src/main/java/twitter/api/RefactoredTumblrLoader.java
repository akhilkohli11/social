package twitter.api;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.*;
import org.joda.time.DateTime;
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


    static int initalCount=0;


    public static void loadTumblr(Map<String,List<SearchObject>> searchMap) throws Exception
    {
        int limit =0;

        int newcount=0;
        while(newcount++<1) {
            Map<String, String> options = new HashMap<String, String>();
            Date date = new Date();
            long unixTime = (long) date.getTime() / 1000;

            options.put("before", String.valueOf(unixTime));
            options.put("limit", "100");
            int count = 0;
            while (count++ < 4) {
                for (Map.Entry<String, List<SearchObject>> showSearch : searchMap.entrySet()) {
                    for (SearchObject searchObject : showSearch.getValue()) {
                        if (searchObject.isBlog()) {
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


                        } else {
                            List<Post> posts = client.tagged(searchObject.getSearchTerm().trim(), options);

                            for (Post post : posts) {
                                persist( post, showSearch.getKey(), searchObject.getIsOfficial().toString(),searchObject.getID());

                            }
                        }
                    }
                    Thread.sleep(1000);
                }

                Date daysAgo = new DateTime(date).minusHours(3).toDate();
                date = daysAgo;
                unixTime = (long) daysAgo.getTime() / 1000;
                options.put("before", String.valueOf(unixTime));
            }
        }

    }


    private static void persist(Post post,String showName,String official,String showID) throws Exception{
        //text, quote, link, answer, video, audio, photo, chat
        if(post.getType().equals("photo"))
        {
            dbPhotoPost(Arrays.asList(post),showName,official,showID);

        }
        if(post.getType().equals("video"))
        {
            dbVidePost(Arrays.asList(post),showName,official,showID);
        }

        if(post.getType().equals("audio"))
        {
            dbAudioPost(Arrays.asList(post),showName,official,showID);

        }
        if(post.getType().equals("text"))
        {
            dbTextPost( Arrays.asList(post), showName, official,showID);

        }

        if(post.getType().equals("quote"))
        {
            dbQuotePost(Arrays.asList(post), showName, official,showID);

        }

    }

    private static void dbQuotePost( List<Post> posts, String showName, String official,String showID) throws Exception{
        for(Post post : posts)
        {
            String name=post.getBlogName();
            Blog blog=client.blogInfo(name);
            Long id=post.getId();
            int likes=Integer.parseInt(post.getNoteCount().toString());
            String type="video";
            QuotePost videoPostNEw=(QuotePost)post;
            String url=videoPostNEw.getSourceUrl();
            String time=post.getDateGMT();
            int followers=blog.getLikeCount();
           CloudSolrPersistenceLayer.getInstance().populateTumblrData(id,name,videoPostNEw.getText(),showName,videoPostNEw.getSourceTitle(),official,"quote",0,likes,followers,0,
                    "",time,url,post.getPostUrl(),showID);

        }
    }


    private static void dbVidePost(List<Post> videoPosts,String showName,String official,String showID) throws Exception
    {
        for(Post post : videoPosts)
        {
            String name=post.getBlogName();
            Blog blog=client.blogInfo(name);
            Long id=post.getId();
            int likes=Integer.parseInt(post.getNoteCount().toString());
            String type="video";
            VideoPost videoPostNEw=(VideoPost)post;
            String url=videoPostNEw.getThumbnailUrl();
            String time=post.getDateGMT();
            List<Note> notesList=post.getNotes();
            for(Video video : videoPostNEw.getVideos())
            {
                int width=video.getWidth();
                String embedCode=video.getEmbedCode();
                int followers=blog.getLikeCount();

                CloudSolrPersistenceLayer.getInstance().populateTumblrData(id,name,null,showName,null,official,"video",0,likes,followers,width,
                        embedCode,time,url,post.getPostUrl(),showID);
            }
        }
    }

    private static void dbPhotoPost(List<Post> photoposts,String showName,String official,String showID) throws Exception
    {
        for(Post post : photoposts)
        {
            String name=post.getBlogName();
            Blog blog=client.blogInfo(name);
            int likes=Integer.parseInt(post.getNoteCount().toString());
            Long id=post.getId();
            String type="photo";
            List<Note> notesList=post.getNotes();
            PhotoPost photoPosts=(PhotoPost)post;
            String url=photoPosts.getSourceUrl();
            String time=post.getDateGMT();
            for(Photo photo : photoPosts.getPhotos())
            {
                String embedCode=photo.getOriginalSize().getUrl();
                int followers=blog.getLikeCount();
                CloudSolrPersistenceLayer.getInstance().populateTumblrData(id,name,null,showName,null,official,"photo",0,likes,followers,photo.getOriginalSize().getWidth(),
                        embedCode,time,url,post.getPostUrl(),showID);
            }
        }
    }

    private static void dbAudioPost(List<Post> audioPosts,String showName,String official,String showID) throws Exception
    {
        for(Post post : audioPosts)
        {
            String name=post.getBlogName();
            Blog blog=client.blogInfo(name);
            int likes=Integer.parseInt(post.getNoteCount().toString());
            Long id=post.getId();
            AudioPost audioPost=(AudioPost)post;
            String url=audioPost.getSourceUrl();
            String time=post.getDateGMT();
            String embedCode=audioPost.getEmbedCode();
            int followers=blog.getLikeCount();
               CloudSolrPersistenceLayer.getInstance().populateTumblrData(id,name,null,showName,null,official,"audio",0,likes,followers,0,
                    embedCode,time,url,post.getPostUrl(),showID);

        }
    }

    private static void dbTextPost(List<Post> videoPosts,String showName,String official,String showID) throws Exception
    {
        for(Post post : videoPosts)
        {
            String name=post.getBlogName();
            Blog blog=client.blogInfo(name);
            int likes=Integer.parseInt(post.getNoteCount().toString());
            Long id=post.getId();
            String type="video";
            TextPost videoPostNEw=(TextPost)post;
            String url=videoPostNEw.getSourceUrl();
            String time=post.getDateGMT();
            int followers=blog.getLikeCount();
            List<Note> notesList=post.getNotes();
               CloudSolrPersistenceLayer.getInstance().populateTumblrData(id,name,videoPostNEw.getBody(),showName,videoPostNEw.getTitle(),official,"text",0,likes,followers,0,
                    "",time,url,post.getPostUrl(),showID);

        }
    }





//    public static void loadNewTumblrData(Map<String,List<String>> map) throws Exception{
//        Map<String,List<SearchObject>> searchList=new HashMap<String, List<SearchObject>>();
//        for(Map.Entry<String,List<String>> entry : map.entrySet()) {
//            for(String value : entry.getValue()) {
//                List<SearchObject> searchObjectList = searchList.get(entry.getKey());
//                if (searchObjectList==null) {
//                    searchObjectList = new ArrayList<SearchObject>();
//                }
//
//                searchObjectList.add(new SearchObject(value, "false", false));
//
//                searchList.put(entry.getKey(), searchObjectList);
//            }
//        }
//        loadTumblr(searchList);
//
//    }


    public static void loadNewTumblrData( Map<String, String> showsTOIDMap) throws Exception{
        Map<String,List<SearchObject>> searchList=new HashMap<String, List<SearchObject>>();
        for(Map.Entry<String,String> entry: showsTOIDMap.entrySet()) {

            List<SearchObject> searchObjectList = new ArrayList<SearchObject>();

            searchObjectList.add(new SearchObject(entry.getKey()+ "tv", "false", false,entry.getValue()));
            searchObjectList.add(new SearchObject(entry.getKey()+ "tv series", "false", false,entry.getValue()));

            searchList.put(entry.getKey(), searchObjectList);
        }

        loadTumblr(searchList);

    }

}
