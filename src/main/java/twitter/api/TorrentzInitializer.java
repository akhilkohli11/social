package twitter.api;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Created by akohli on 10/31/14.
 */
public class TorrentzInitializer {

    public static void init() throws Exception {

        Map<String, String> showsTOIDMap = ShowLoader.getShowLoader().getShowTOIDMap();
        for (Map.Entry<String, String> showSearch : showsTOIDMap.entrySet()) {
            try {


                populateSeedersLeechers(showSearch.getKey(),showSearch.getKey() + " tv", showSearch.getValue());
            //    populateSeedersLeechers(showSearch.getKey() + " season", showSearch.getValue());
                populateSeedersLeechers(showSearch.getKey(),showSearch.getKey() + " show", showSearch.getValue());
            //    populateSeedersLeechers(showSearch.getKey() + " series", showSearch.getValue());


                populateTorrentz(showSearch.getKey(),showSearch.getKey() + " tv", showSearch.getValue());
                // populateTorrentz(showSearch.getKey() + " season", showSearch.getValue());
                populateTorrentz(showSearch.getKey(),showSearch.getKey() + " show", showSearch.getValue());
                //  populateTorrentz(showSearch.getKey() + " series", showSearch.getValue());


                Thread.sleep(1000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    public static void populateTorrentz(String show_name,String query, String showID) {

        URL u;
        InputStream is = null;
        DataInputStream dis;
        String s;
        String show = new String(query);
        show = show.trim().toLowerCase().replaceAll(" ", "+");
        try {
            for (int i = 1; i < 10; i++) {

                try {
                    String url = "http://torrentz.com/search?f=" + show.toString() + "&p=" + String.valueOf(i);
                    System.out.println(url);

                    u = new URL(url);

                    is = u.openStream();         // throws an IOException

                    dis = new DataInputStream(new BufferedInputStream(is));

                    //------------------------------------------------------------//
                    // Step 5:                                                    //
                    //------------------------------------------------------------//
                    // Now just read each record of the input stream, and print   //
                    // it out.  Note that it's assumed that this problem is run   //
                    // from a command-line, not from an application or applet.    //
                    //------------------------------------------------------------//

                    while ((s = dis.readLine()) != null) {
                        String[] output = s.split("span class=\"d\">");
                        if (output.length > 1) {
                            String newoutput[] = output[1].split("<");
                            System.out.println("torrentz"+ query+"  "+Integer.parseInt(newoutput[0]));
                            CloudSolrPersistenceLayer.getInstance().populateTorrentzData(i,Integer.parseInt(newoutput[0]),
                                    show_name, showID,"torrentz");
                        }

                    }

                }  catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {

            try {
                is.close();
            } catch (Exception ioe) {
                ioe.printStackTrace();
            }

        }


    }






    public static void populateSeedersLeechers(String show_name,String query, String showID) {

        URL u;
        InputStream is = null;
        DataInputStream dis;
        String s;
        String show = new String(query);
        show = show.trim().toLowerCase().replaceAll(" ", "+");
        try {
            for (int i = 1; i < 10; i++) {

                try {
                    u = new URL("http://www.torrenthound.com/search/game+of+thrones");

                    String url = "http://torrenthound.com/search/"+String.valueOf(i)+"/" + show.toString() ;
                    System.out.println(url);

                    u = new URL(url);

                    is = u.openStream();         // throws an IOException

                    dis = new DataInputStream(new BufferedInputStream(is));

                    //------------------------------------------------------------//
                    // Step 5:                                                    //
                    //------------------------------------------------------------//
                    // Now just read each record of the input stream, and print   //
                    // it out.  Note that it's assumed that this problem is run   //
                    // from a command-line, not from an application or applet.    //
                    //------------------------------------------------------------//

                    while ((s = dis.readLine()) != null) {
                        String[] output= s.split("span class=\"seeds\">");
                        String[] leechoutput= s.split("span class=\"leeches\">");

                        if(output.length>1) {
                            String newoutput[]=output[1].split("<");
                            String newoutput1[]=leechoutput[1].split("<");

                            String leechers=newoutput[0].replaceAll("&uarr;","").trim();
                            String seeders=newoutput1[0].replaceAll("&darr;","").trim();
                            System.out.println("torrenthound"+ query+"  "+Integer.parseInt(leechers)+"  "+
                                    Integer.parseInt(seeders));
                            CloudSolrPersistenceLayer.getInstance().populateTorrentzData(i,Integer.parseInt(leechers)+
                            Integer.parseInt(seeders),show_name,showID,"torrenthound");
                        }

                    }


                }  catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } finally {

            try {
                is.close();
            } catch (Exception ioe) {
                ioe.printStackTrace();
            }

        }
    }
}
