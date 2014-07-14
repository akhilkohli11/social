package twitter.api;

/**
 * Created by akohli on 7/7/14.
 */
public class TumblrObject {
    private String postID;
    private String text;
    private String blogName;
    private String showName;
    private String type;
    private String likes;
    private String followers;
    private String width;
    private String embedCode;

    public String getSentimentalScore() {
        return sentimentalScore;
    }

    private String sentimentalScore;

    public String getPostID() {
        return postID;
    }

    public String getText() {
        return text;
    }

    public String getBlogName() {
        return blogName;
    }

    public String getShowName() {
        return showName;
    }

    public String getType() {
        return type;
    }

    public String getLikes() {
        return likes;
    }

    public String getFollowers() {
        return followers;
    }

    public String getWidth() {
        return width;
    }

    public String getEmbedCode() {
        return embedCode;
    }

    public String getUrl() {
        return url;
    }

    public String getCreatedOn() {
        return createdOn;
    }
    public String getLastUpdated() {
        return lastUpdated;
    }

    private String url;
    private String createdOn;
    private String lastUpdated;


    public TumblrObject(String postID, String text, String blogName, String showName, String type, String followers, String likes, String width, String embedCode, String sentimentalScore, String url, String createdOn,
                        String lastUpdated) {
        this.postID = postID;
        this.text = text;
        this.blogName = blogName;
        this.showName = showName;
        this.type = type;
        this.followers = followers;
        this.likes = likes;
        this.width = width;
        this.embedCode = embedCode;
        this.sentimentalScore = sentimentalScore;
        this.url = url;
        this.createdOn = createdOn;
        this.lastUpdated=lastUpdated;
    }


}
