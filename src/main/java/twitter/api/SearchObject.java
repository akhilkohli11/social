package twitter.api;

/**
 * Created by akohli on 6/23/14.
 */
public class SearchObject {
    private String searchTerm;
    private String isOfficial;

    public boolean isBlog() {
        return isBlog;
    }

    private boolean isBlog;

    public SearchObject(String searchTerm, String isOfficial,boolean isBlog) {
        this.searchTerm = searchTerm;
        this.isOfficial = isOfficial;
        this.isBlog=isBlog;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public String getIsOfficial() {
        return isOfficial;
    }
}
