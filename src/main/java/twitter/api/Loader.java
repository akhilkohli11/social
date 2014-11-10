package twitter.api;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
/**
 * Created by akohli on 6/5/14.
 */
public class Loader extends HttpServlet {

public static void main(String args[])
{
    new Loader().init();
}

    public void init() {
        try {
            CloudSolrPersistenceLayer.getInstance().init();
            InitializePopularDocuments.init();
            Aggregator aggregator=new Aggregator();
            aggregator.delete();
//            aggregator.aggregateTrend();
//            aggregator.aggregateTotal();
//            aggregator.delete();
            aggregator.init();
//            Zap2ItSolrApi.init();
//            Thread.sleep(10000);
//            SocialXMLParser.init();
//            Thread.sleep(2000);
      //      MapSocialWebsitesToShows.init();

//            ViewsYoutubeLoader.init();
//            RefactoredTumblrLoader.init();
//            KloutDaemon kloutDaemon=new KloutDaemon();
//            kloutDaemon.init();
//            YoutubeDaemon youtubeDaemon=new YoutubeDaemon();
//            youtubeDaemon.init();
//
//            TorrentzDaemon torrentzDaemon = new TorrentzDaemon();
//            torrentzDaemon.init();
//
//
//            TumblrDaemon tumblrDaemon=new TumblrDaemon();
//            tumblrDaemon.init();
//
//
//
//
//
//            TwitterTweetDaeomon twitterTweetDaeomon=new TwitterTweetDaeomon();
//            twitterTweetDaeomon.init();
//
//
//            new TwitterOneMore().init();
//
//            FacebookDaemon facebookDaemon=new FacebookDaemon();
//            facebookDaemon.init();


        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void doGet(HttpServletRequest request, HttpServletResponse response)
                throws IOException, ServletException
        {
            System.out.println("dskjsjksdjkkdj");
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();


        }
    }
