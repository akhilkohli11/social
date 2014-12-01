//package twitter.api;
//
///**
// * Created by akohli on 10/28/14.
// */
//public class Main {
//    public static void main(String args[]) throws Exception
//    {
//        Klout k = new Klout("yf28t3xjef6nwpexcwtv9mcb");
//
//
//// retrieves klout id with twitter screen name
//        String[] data = k.getIdentity("crash_starz".toUpperCase(), Klout.TWITTER_SCREEN_NAME); // contains ["635263", "ks]
//
//        System.out.println(data[0]);
//
////// retrieves klout id with twitter id
////        String[] d = k.getIdentity("500042487", Klout.TWITTER); // contains ["54887627490056592", "ks"]
////
//
//// gets user with klout id
//        User u = k.getUser(data[0]);
//        System.out.println(u.score());
//        for(Topic topic:u.getTopics())
//        {
//            System.out.println(topic.name+"  "+topic.slug+" ");
//        }
//        System.out.println(u.bucket());
//        System.out.println(u.getInfluencees());
//        System.out.println(u.dayChange());
//
//        double score = u.score();
//
//        Topic[] topics = u.getTopics();
//
//        User[] influencers = u.getInfluencers();
//        User[] influencees = u.getInfluencees();
//    }
//}
