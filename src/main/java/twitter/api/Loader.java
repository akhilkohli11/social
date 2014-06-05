package twitter.api;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
/**
 * Created by akohli on 6/5/14.
 */
public class Loader extends HttpServlet {


        int accesses = 0;

    public void init() {
        try {
            System.out.println(": initialised dsdsdsdssd");
            LoadApp.init();
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

            accesses++;
            out.print("Number of times this servlet has been accessed:" + accesses);
        }
    }
