package twitter.api;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by akohli on 10/31/14.
 */
public class PirateBayUrl {

    public static void main (String[] args) {

        //-----------------------------------------------------//
        //  Step 1:  Start creating a few objects we'll need.
        //-----------------------------------------------------//

        URL u;
        InputStream is = null;
        DataInputStream dis;
        String s;

        try {

            //------------------------------------------------------------//
            // Step 2:  Create the URL.                                   //
            //------------------------------------------------------------//
            // Note: Put your real URL here, or better yet, read it as a  //
            // command-line arg, or read it from a file.                  //
            //------------------------------------------------------------//

                u = new URL("http://www.torrenthound.com/search/game+of+thrones");

            //----------------------------------------------//
            // Step 3:  Open an input stream from the url.  //
            //----------------------------------------------//

            is = u.openStream();         // throws an IOException

            //-------------------------------------------------------------//
            // Step 4:                                                     //
            //-------------------------------------------------------------//
            // Convert the InputStream to a buffered DataInputStream.      //
            // Buffering the stream makes the reading faster; the          //
            // readLine() method of the DataInputStream makes the reading  //
            // easier.                                                     //
            //-------------------------------------------------------------//

            dis = new DataInputStream(new BufferedInputStream(is));

            //------------------------------------------------------------//
            // Step 5:                                                    //
            //------------------------------------------------------------//
            // Now just read each record of the input stream, and print   //
            // it out.  Note that it's assumed that this problem is run   //
            // from a command-line, not from an application or applet.    //
            //------------------------------------------------------------//

            while ((s = dis.readLine()) != null) {
              //  System.out.println(s);
                String[] output= s.split("span class=\"seeds\">");
                String[] leechoutput= s.split("span class=\"leeches\">");

                if(output.length>1) {
                    String newoutput[]=output[1].split("<");
                    String newoutput1[]=leechoutput[1].split("<");

                    System.out.println(newoutput[0].replaceAll("&uarr;","")+"   "+newoutput1[0].replaceAll("&darr;",""));
                }

                        }

        } catch (MalformedURLException mue) {

            System.out.println("Ouch - a MalformedURLException happened.");
            mue.printStackTrace();
            System.exit(1);

        } catch (IOException ioe) {

            System.out.println("Oops- an IOException happened.");
            ioe.printStackTrace();
            System.exit(1);

        } finally {

            //---------------------------------//
            // Step 6:  Close the InputStream  //
            //---------------------------------//

            try {
                is.close();
            } catch (IOException ioe) {
                // just going to ignore this one
            }

        } // end of 'finally' clause

    }  // end of main
}
