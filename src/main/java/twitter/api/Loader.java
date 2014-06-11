package twitter.api;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
/**
 * Created by akohli on 6/5/14.
 */
public class Loader extends HttpServlet {



    public void init() {
        try {
            System.out.println(": initialised dsdsdsdssd");
            NewService newService=new NewService();
            newService.init();
            FileService fileService=new FileService();
            fileService.init();
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


        }
    }
