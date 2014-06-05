//package twitter.api;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by akohli on 6/2/14.
// */
//public class PopulateSocialDataMain {
//    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
//    public static final String MYSQL_URL = "jdbc:mysql://localhost/social?"
//            + "user=root";
//    static Map<String,String> tagToshowID=new HashMap<String, String>();
//    static List<String> followTerms=new ArrayList<String>();
//
//    public static void main(String args[])
//            throws Exception
//    {
//      //  System.out.println(TwitterDataRetriever.getTweets("america"));
//        SocialMysqlLayer socialMysqlLayer=new SocialMysqlLayer(MYSQL_DRIVER,MYSQL_URL);
//       populateShowIDToShowName(socialMysqlLayer, "1", "The 100", "@cwthe100", null, "#the100");
//       populateShowIDToShowName(socialMysqlLayer, "2", "America's Got Talent", "@nbcagt", "@heidiklum, @howiemandel, @HowardStern, @OfficialMelB, @NickCannon",
//               "#agt, #GoldenBuzzer, #TodaysGotTalent, #SummerStartsNow, #NickPrank, #TurnUpTheTalent, #AmericasGotTalent");
//       populateShowIDToShowName(socialMysqlLayer, "3", "The Bachelorette", "@BacheloretteABC", "@Clare_Crawley, @AndiDorfman, @Nikki_Ferrell, @JuanPaGalavis",
//               "#thebachelorette, #BleachableMoment, #VoteBachelorCraig, #VoteBachelorJosh, #VoteBachelorJJ, #VoteBachelorAndrew");
//
//       populateShowIDToShowName(socialMysqlLayer, "4", "Beauty and the Beast", "@cwbatb", null, "#batb, #Vincat, #beasties");
//        populateShowIDToShowName(socialMysqlLayer, "5", "Bet on Your Baby", "@betonyourbaby", null, "#BetOnYourBaby");
//        populateShowIDToShowName(socialMysqlLayer, "6", "Crossbones", "@NBCCrossbones", null, "#blackbeard, #crossbones, #johnmalkovich");
//
//        TwitterDataRetriever.populateShowIDToShowName( "1", "The 100", "@cwthe100", null, "#the100");
//        TwitterDataRetriever.populateShowIDToShowName( "2", "America's Got Talent", "@nbcagt", "@heidiklum, @howiemandel, @HowardStern, @OfficialMelB, @NickCannon",
//                "#agt, #GoldenBuzzer, #TodaysGotTalent, #SummerStartsNow, #NickPrank, #TurnUpTheTalent, #AmericasGotTalent");
//        TwitterDataRetriever.populateShowIDToShowName( "3", "The Bachelorette", "@BacheloretteABC", "@Clare_Crawley, @AndiDorfman, @Nikki_Ferrell, @JuanPaGalavis",
//                "#thebachelorette, #BleachableMoment, #VoteBachelorCraig, #VoteBachelorJosh, #VoteBachelorJJ, #VoteBachelorAndrew");
//
//        TwitterDataRetriever.populateShowIDToShowName( "4", "Beauty and the Beast", "@cwbatb", null, "#batb, #Vincat, #beasties");
//        TwitterDataRetriever.populateShowIDToShowName( "5", "Bet on Your Baby", "@betonyourbaby", null, "#BetOnYourBaby");
//        TwitterDataRetriever.populateShowIDToShowName( "6", "Crossbones", "@NBCCrossbones", null, "#blackbeard, #crossbones, #johnmalkovich");
//
//
//       // TwitterDataRetriever.getTweets();
//     //   socialMysqlLayer.readData();
//    }
//
//    private static void populateShowIDToShowName(SocialMysqlLayer socialMysqlLayer,String showID,String showName,String twitterHandle,String casteHandle,String hashtag) throws Exception
//    {
//       // socialMysqlLayer.populateShowIDToShowName(showID,showName,twitterHandle,casteHandle,hashtag);
//
//    }
//}
