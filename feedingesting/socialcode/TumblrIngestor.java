package org.zap2it.ingester.social;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.*;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.zap2it.beans.Article;
import org.zap2it.clients.AwsS3Client;
import org.zap2it.clients.SolrClient;
import org.zap2it.util.ArticleUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by akohli on 6/20/14.
 */
public class TumblrIngestor {
    static DateFormat tumblrDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
    private static List<String> allowedKeyWords=Arrays.asList("show","clip","season","series","episode","premiere","preview","recap","finale");




    public static void loadTumblr(List<Tumblr> tumblrList) throws Exception
    {

        int newcount=0;
        Map<String, String> options = new HashMap<String, String>();
        Date date = new Date();
        long unixTime = (long) date.getTime() / 1000;

        options.put("before", String.valueOf(unixTime));
        options.put("limit", "100");

        int treshold=20;
        int increase=0;
        for (Tumblr tumblr : tumblrList) {
            try {
                List<Post> blogPosts = client.blogPosts(tumblr.getBlogName());
                for (Post post : blogPosts) {
                    persist(post, tumblr, treshold, true);
                }
                Thread.sleep(100);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        while(newcount++<7*10) {

            int count = 0;
            while (count++ < 2) {
                for (Tumblr tumblr : tumblrList) {
                    List<Post> posts = client.tagged(tumblr.getQuery().trim(), options);
                    for (Post post : posts) {
                        persist( post, tumblr,treshold,false);

                    }
                }
            }
            Thread.sleep(2000);

            Date daysAgo = new DateTime(date).minusHours(7).toDate();
            date = daysAgo;
            unixTime = (long) daysAgo.getTime() / 1000;
            options.put("before", String.valueOf(unixTime));
            increase++;
            if(increase%5==0 && increase<50)
            {
                treshold+=40;
            }
            else if(increase<50)
            {
                treshold+=20;
            }
            if(increase%15==0 && increase>50)
            {
                treshold+=40;
            }

        }

    }




    private static void persist(Post post,Tumblr tumblr,int threshold,boolean official) throws Exception{
        int notes=Integer.parseInt(post.getNoteCount().toString());
        if(!official && notes<threshold)
        {
            System.out.println("Rejecting post "+post.getPostUrl()+" Notes "+notes);
            return;
        }
        //text, quote, link, answer, video, audio, photo, chat
        if(post.getType().equals("photo"))
        {
            dbPhotoPost(Arrays.asList(post),tumblr,official);

        }
        if(post.getType().equals("video"))
        {
            dbVidePost(Arrays.asList(post),tumblr,official);
        }


        if(post.getType().equals("text"))
        {
            dbTextPost( Arrays.asList(post),tumblr,official);

        }

//        if(post.getType().equals("audio"))
//        {
//            dbAudioPost(Arrays.asList(post),tumblr);
//
//        }

    }




    private static void dbVidePost(List<Post> videoPosts,Tumblr tumblr,boolean official) throws Exception
    {
        for(Post post : videoPosts)
        {
            String name=post.getBlogName();
            Blog blog=client.blogInfo(name);
            Long id=post.getId();
            int likes=Integer.parseInt(post.getNoteCount().toString());
            VideoPost videoPost=(VideoPost)post;
            String time=post.getDateGMT();
            int followers=blog.getLikeCount();
            List<String> tags=post.getTags();
            boolean validShowNameTag=false;
            boolean validChannelTag=false;
            boolean validTag=false;
            String author=blog.getName();
            for(String tag : tags) {
                String formattedTag = tag.trim().toLowerCase().replaceAll(" ", "");
                String showName = tumblr.getShowName().trim().toLowerCase().replaceAll(" ", "");
                String channelName = tumblr.getOfficalChannel().trim().toLowerCase().replaceAll(" ", "");
                String hashtag=tumblr.getHashtag().replace("#","");
                if (formattedTag.contains(hashtag)) {
                    validShowNameTag = true;
                }
                if (formattedTag.contains(showName)) {
                    validShowNameTag = true;
                }
                if (formattedTag.contains(channelName)) {
                    validChannelTag = true;
                }
                for (String keyword : allowedKeyWords) {
                    if(tag.equals(keyword))
                    {
                        validTag=true;
                    }
                }
            }

            if(official||(validShowNameTag && (!tumblr.isGeneric()||validTag || validChannelTag)))
            {
                SolrClient client = new SolrClient("content");

                SolrInputDocument doc = new SolrInputDocument();
                String caption=videoPost.getCaption();
                if(StringUtils.isEmpty(caption))
                {
                    caption=post.getSlug().replace("-", " ").toUpperCase();
                }
                if(StringUtils.isNotEmpty(videoPost.getThumbnailUrl()) && StringUtils.isNotEmpty(post.getSlug()) ) {
                    System.out.println("TumblrWriting "+tumblr.getShowName()+"  "+post.getTags());
                    doc.addField("id", "TUMBLRPOST" + id);
                    doc.addField("document_type", "tumblr_video");
                    doc.addField("lang", "en");
                    doc.addField("title", post.getSlug().replace("-", " ").toUpperCase());
                    doc.addField("description", caption);
                    doc.addField("publish_date", new Date(tumblrDF.parse(time).getTime()));
                    doc.addField("slug_id", post.getSlug() + "-tumblr"+"-"+post.getId());
                    doc.addField("tag", post.getTags());
                    doc.addField("author", author+".tumblr.com");
                    doc.addField("is_current", true);
                    doc.addField("s_orig_url", videoPost.getPostUrl());
                    doc.addField("thumbnail", videoPost.getThumbnailUrl());
                    doc.addField("s_provider", "tumblr");
                    doc.addField("recommended_shows", tumblr.getTmsShowID());
                    doc.addField("num_ratings_followers", followers);
                    doc.addField("num_ratings_likes", likes);
                    doc.addField("s_current", "current");
                    doc.addField("b_displayContent", true);
                    if(official)
                    {
                        doc.addField("b_official", true);
                    }

                    client.postToSolr(doc);

                }
                else
                {
                    System.out.println("Failed "+post.getPostUrl());
                }

            }
            else
            {
                System.out.println("Failed tags "+post.getTags());
            }

        }
    }


    private  static void dbPhotoPost(List<Post> photoposts,Tumblr tumblr,boolean official) throws Exception
    {
        for(Post post : photoposts)
        {
            String name=post.getBlogName();
            Blog blog=client.blogInfo(name);
            int likes=Integer.parseInt(post.getNoteCount().toString());
            Long id=post.getId();
            PhotoPost photoPosts=(PhotoPost)post;
            String time=post.getDateGMT();
            int followers=blog.getLikeCount();
            List<String> tags=post.getTags();
            boolean validShowNameTag=false;
            boolean validChannelTag=false;
            boolean validTag=false;
            String author=blog.getName();
            for(String tag : tags) {
                String formattedTag = tag.trim().toLowerCase().replaceAll(" ", "");
                String showName = tumblr.getShowName().trim().toLowerCase().replaceAll(" ", "");
                String channelName = tumblr.getOfficalChannel().trim().toLowerCase().replaceAll(" ", "");
                String hashtag=tumblr.getHashtag().replace("#","");
                if (formattedTag.contains(hashtag)) {
                    validShowNameTag = true;
                }
                if (formattedTag.contains(showName)) {
                    validShowNameTag = true;
                }
                if (formattedTag.contains(channelName)) {
                    validChannelTag = true;
                }
                for (String keyword : allowedKeyWords) {
                    if(tag.equals(keyword))
                    {
                        validTag=true;
                    }
                }
            }

            String thumbnail=null;

            for(Photo photo : photoPosts.getPhotos())
            {

                thumbnail=photo.getOriginalSize().getUrl();
                break;

            }
            if(official||(validShowNameTag && (!tumblr.isGeneric()||validTag || validChannelTag))) {

                System.out.println("TumblrWriting "+tumblr.getShowName()+"  "+post.getTags());
                SolrClient client = new SolrClient("content");

                SolrInputDocument doc = new SolrInputDocument();
                String caption=((PhotoPost) post).getCaption();
                if(StringUtils.isEmpty(caption))
                {
                    caption=post.getSlug().replace("-", " ").toUpperCase();
                }
                if(StringUtils.isNotEmpty(thumbnail) && StringUtils.isNotEmpty(post.getSlug())) {

                    doc.addField("id", "TUMBLRPOST" + id);
                    doc.addField("document_type", "tumblr_photogallery");
                    doc.addField("lang", "en");
                    doc.addField("title", post.getSlug().replace("-", " ").toUpperCase());
                    doc.addField("description", caption);
                    doc.addField("publish_date", new Date(tumblrDF.parse(time).getTime()));
                    doc.addField("thumbnail", thumbnail);
                    doc.addField("s_current", "current");
                    doc.addField("slug_id", post.getSlug()+ "-tumblr"+"-"+post.getId());
                    doc.addField("author", author+".tumblr.com");
                    doc.addField("is_current", true);
                    doc.addField("s_orig_url", photoPosts.getPostUrl());
                    doc.addField("s_provider", "tumblr");
                    doc.addField("recommended_shows", tumblr.getTmsShowID());
                    doc.addField("num_ratings_followers", followers);
                    doc.addField("num_ratings_likes", likes);
                    doc.addField("tag", post.getTags());
                    doc.addField("b_displayContent", true);
                    if(official)
                    {
                        doc.addField("b_official", true);
                    }


                    client.postToSolr(doc);
                }
                else
                {
                    System.out.println("Failed "+post.getPostUrl());
                }
            }
            else
            {
                System.out.println("Failed tags "+post.getTags());
            }
        }
    }




    private static void dbTextPost(List<Post> videoPosts,Tumblr tumblr,boolean official) throws Exception
    {
        for(Post post : videoPosts)
        {
            String name=post.getBlogName();
            Blog blog=client.blogInfo(name);
            int likes=Integer.parseInt(post.getNoteCount().toString());
            Long id=post.getId();
            TextPost textPost=(TextPost)post;
            String url=textPost.getSourceUrl();
            String time=post.getDateGMT();
            int followers=blog.getLikeCount();
            List<String> tags=post.getTags();
            boolean validShowNameTag=false;
            boolean validChannelTag=false;
            boolean validTag=false;
            String author=blog.getName();
            for(String tag : tags) {
                String formattedTag = tag.trim().toLowerCase().replaceAll(" ", "");
                String showName = tumblr.getShowName().trim().toLowerCase().replaceAll(" ", "");
                String channelName = tumblr.getOfficalChannel().trim().toLowerCase().replaceAll(" ", "");
                String hashtag=tumblr.getHashtag().replace("#","");
                if (formattedTag.contains(hashtag)) {
                    validShowNameTag = true;
                }
                if (formattedTag.contains(showName)) {
                    validShowNameTag = true;
                }

                if (formattedTag.contains(showName)) {
                    validShowNameTag = true;
                }
                if (formattedTag.contains(channelName)) {
                    validChannelTag = true;
                }
                for (String keyword : allowedKeyWords) {
                    if(tag.equals(keyword))
                    {
                        validTag=true;
                    }
                }
            }
            String thumbnail= getImageForTextPost(textPost.getBody());

            if(official||(validShowNameTag && (!tumblr.isGeneric()||validTag || validChannelTag))) {
                System.out.println("TumblrWriting "+tumblr.getShowName()+"  "+post.getTags());
                SolrClient client = new SolrClient("content");
                if( post.getSlug()!=null && ((TextPost) post).getTitle()!=null && StringUtils.isNotEmpty(thumbnail) &&
                        StringUtils.isNotEmpty(textPost.getBody()) && textPost.getBody().length()>800) {

                    SolrInputDocument doc = new SolrInputDocument();
                    doc.addField("id", "TUMBLRPOST" + id);
                    doc.addField("document_type", "tumblr_article");
                    doc.addField("lang", "en");
                    doc.addField("title", textPost.getTitle());

                    doc.addField("description", textPost.getBody());
                    doc.addField("publish_date", new Date(tumblrDF.parse(time).getTime()));
                    doc.addField("slug_id", post.getSlug()+ "-tumblr"+"-"+post.getId());
                    doc.addField("author", author+".tumblr.com");
                    doc.addField("is_current", true);
                    doc.addField("s_orig_url", textPost.getPostUrl());
                    doc.addField("thumbnail", thumbnail);
                    doc.addField("s_provider", "tumblr");
                    doc.addField("s_current", "current");
                    doc.addField("b_displayContent", true);
                    doc.addField("recommended_shows", tumblr.getTmsShowID());
                    doc.addField("num_ratings_followers", followers);
                    doc.addField("num_ratings_likes", likes);
                    doc.addField("tag", post.getTags());
                    if(official)
                    {
                        doc.addField("b_official", true);
                    }


                    client.postToSolr(doc);
                }
                else
                {
                    System.out.println("Failed "+post.getPostUrl());
                }
            }
            else
            {
                System.out.println("Failed tags "+post.getTags());
            }
        }
    }

    public static String getImageForTextPost(String body)
    {
        if(StringUtils.isEmpty(body))
        {
            return null;
        }
        Pattern p=null;
        Matcher m= null;
        p= Pattern.compile(".*<img[^>]*src=\"([^\"]*)",Pattern.CASE_INSENSITIVE);
        m= p.matcher(body);
        String image=null;
        while (m.find())
        {
            image=m.group(1);
            System.out.println("text image"+image.toString());
        }
        return image;
    }

//
//    private static void dbAudioPost(List<Post> audioPosts,String showName,String official,String showID) throws Exception
//    {
//        for(Post post : audioPosts)
//        {
//            String name=post.getBlogName();
//            Blog blog=client.blogInfo(name);
//            int likes=Integer.parseInt(post.getNoteCount().toString());
//            Long id=post.getId();
//            AudioPost audioPost=(AudioPost)post;
//            String url=audioPost.getSourceUrl();
//            String time=post.getDateGMT();
//            String embedCode=audioPost.getEmbedCode();
//            int followers=blog.getLikeCount();
//            CloudSolrPersistenceLayer.getInstance().populateTumblrData(id,name,null,showName,null,official,"audio",0,likes,followers,0,
//                    embedCode,time,url,post.getPostUrl(),showID);
//
//        }
//    }


}
