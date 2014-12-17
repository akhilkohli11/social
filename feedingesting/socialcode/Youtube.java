package org.zap2it.ingester.social;

/**
 * Created by akohli on 11/16/14.
 */
public class Youtube {
    private String tmsShowID;
    private String showName;
    private String query;
    private String officalChannel;
    private boolean isGeneric;


    public Youtube(String tmsShowID, String showName, String query, String officalChannel, boolean isGeneric) {
        this.tmsShowID = tmsShowID.trim();
        this.showName = showName.trim();
        this.query = query.trim();
        this.officalChannel = officalChannel.trim();
        this.isGeneric = isGeneric;
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
}
