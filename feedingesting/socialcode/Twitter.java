package org.zap2it.ingester.social;

/**
 * Created by akohli on 12/4/14.
 */
    public class Twitter {
    private String tmsShowID;
    private String showName;
    private String handle;


    public Twitter(String tmsShowID, String showName, String handle) {
        this.tmsShowID = tmsShowID.trim();
        this.showName = showName.trim();
        this.handle=handle;
    }

    public String getShowName() {
        return showName;
    }

    public String getTmsShowID() {
        return tmsShowID;
    }

    public String getHandle() {
        return handle;
    }

}
