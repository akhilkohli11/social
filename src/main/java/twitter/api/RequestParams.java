package twitter.api;

/**
 * Created by akohli on 7/23/14.
 */
    public class RequestParams {

        private String maxNumberOfItems;
        private String criteria;
    public RequestParams()
    {}

    public RequestParams(String maxNumberOfItems, String criteria) {
        this.maxNumberOfItems = maxNumberOfItems;
        this.criteria = criteria;
    }

    public String getMaxNumberOfItems() {
        return maxNumberOfItems;
    }

    public void setMaxNumberOfItems(String maxNumberOfItems) {
        this.maxNumberOfItems = maxNumberOfItems;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }
// getters and setters

    }
