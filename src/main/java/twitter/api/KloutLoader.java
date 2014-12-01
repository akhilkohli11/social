//package twitter.api;
//
//
//import java.util.Map;
//
///**
// * Created by akohli on 11/4/14.
// */
//public class KloutLoader {
//    Klout k = null;
//
//    public void init() {
//        k = new Klout("yf28t3xjef6nwpexcwtv9mcb");
//    }
//
//    public  void populate(String show,String showID,Object twitterPage) throws Exception {
//              if(twitterPage!=null) {
//                  String newTwit=twitterPage.toString();
//                  try {
//                      newTwit=newTwit.replaceAll("http://twitter.com/", "");
//                      newTwit=newTwit.replaceAll("https://www.twitter.com/", "");
//
//                      newTwit=newTwit.replaceAll("https://twitter.com/", "");
//                      newTwit=newTwit.replaceAll("http://www.twitter.com/", "");
//
//                      String[] data = k.getIdentity(newTwit, Klout.TWITTER_SCREEN_NAME); // contains ["635263", "ks]
//                      User u = k.getUser(data[0]);
//                      CloudSolrPersistenceLayer.getInstance().populateKloutScore(show, showID,
//                              u.score(), u.dayChange(), u.weekChange(), u.monthChange());
//                  }
//                  catch (Exception e)
//                  {
//                      e.printStackTrace();
//                  }
//
//
//
//        }
//    }
//}
