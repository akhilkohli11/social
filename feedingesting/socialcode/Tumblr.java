package org.zap2it.ingester.social;

import org.apache.commons.lang.StringUtils;

/**
 * Created by akohli on 11/17/14.
 */
public class Tumblr {
    private String tmsShowID;
    private String showName;
    private String query;
    private String officalChannel;
    private boolean isGeneric;
    private String tumblrPage;


    private String hashtag;
    private String blogName;

    public Tumblr(String tmsShowID, String showName, String query, String officalChannel, boolean isGeneric,
                  String tumblrPage,String hashtag) {
        this.tmsShowID = tmsShowID;
        this.showName = showName;
        this.query = query;
        this.officalChannel = officalChannel;
        this.isGeneric = isGeneric;
        this.tumblrPage=tumblrPage;
        this.hashtag=hashtag;
    }

    public String getShowName() {
        return showName;
    }

    public String getTmsShowID() {
        return tmsShowID;
    }

    public String getQuery() {
        return query;
    }

    public String getOfficalChannel() {
        return officalChannel;
    }

    public boolean isGeneric() {
        return isGeneric;
    }


    public String getTumblrPage() {
        return tumblrPage;
    }


    public String getHashtag() {
        return hashtag;
    }

    public String getBlogName() {
        blogName=StringUtils.substringBetween(tumblrPage, "http://", ".tumblr");
        return blogName;
    }
}
