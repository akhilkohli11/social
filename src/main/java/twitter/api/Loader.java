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
            System.out.println(": initialised dsdsdsdssd");

            TwitterTweetDaeomon twitterTweetDaeomon=new TwitterTweetDaeomon();
            twitterTweetDaeomon.init();
            YoutubeDaemon youtubeDaemon=new YoutubeDaemon();
            youtubeDaemon.init();



            TwitterTumblrDaemon twitterTumblrDaemon=new TwitterTumblrDaemon();
            twitterTumblrDaemon.init();
            new TwitterOneMore().init();

            TumblrDaemon tumblrDaemon=new TumblrDaemon();
            tumblrDaemon.init();


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
