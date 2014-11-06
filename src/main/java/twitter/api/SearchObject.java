package twitter.api;

/**
 * Created by akohli on 6/23/14.
 */
public class SearchObject {
    private String searchTerm;
    private String isOfficial;
    private String id;

    public boolean isBlog() {
        return isBlog;
    }

    private boolean isBlog;

    public SearchObject(String searchTerm, String isOfficial,boolean isBlog,String id) {
        this.searchTerm = searchTerm;
        this.isOfficial = isOfficial;
        this.isBlog=isBlog;
        this.id=id;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public String getID()
    {
        return id;
    }
    public String getIsOfficial() {
        return isOfficial;
    }
}
