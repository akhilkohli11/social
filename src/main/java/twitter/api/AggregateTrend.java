package twitter.api;

/**
 * Created by akohli on 12/3/14.
 */
public class AggregateTrend {
    public static void main(String args[]) throws Exception
    {
        CloudSolrPersistenceLayer.getInstance().init();
        InitializePopularDocuments.init();
        Aggregator aggregator=new Aggregator();

            int count=0;
            while (count++<3) {
                aggregator.aggregateTrend();
                Thread.sleep(20 * 1000);
            }
        Thread.sleep(30 * 1000);

        count=0;
            while(count++<3) {
                aggregator.delete();
                Thread.sleep(20 * 1000);
            }
            Thread.sleep(120*1000);
            System.exit(0);

    }
}
