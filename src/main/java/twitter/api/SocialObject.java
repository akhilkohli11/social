package twitter.api;

/**
 * Created by akohli on 11/30/14.
 */
public class SocialObject {
    private String tmsShowID;
    private String showName;
    private String query;
    private String officalChannel;
    private boolean isGeneric;
    private String tumblrPage;


    public SocialObject(String tmsShowID, String showName, String query, String officalChannel, boolean isGeneric,
                  String tumblrPage) {
        this.tmsShowID = tmsShowID;
        this.showName = showName;
        this.query = query;
        this.officalChannel = officalChannel;
        this.isGeneric = isGeneric;
        this.tumblrPage=tumblrPage;
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
}
