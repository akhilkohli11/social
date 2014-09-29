package twitter.api;

import com.tumblr.jumblr.JumblrClient;
import com.tumblr.jumblr.types.*;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

import java.io.*;
import java.util.*;
import java.util.Date;

/**
 * Created by akohli on 6/20/14.
 */
public class TumblrLoader {
    static JumblrClient client;
    public static void init()
    {
        client = new JumblrClient(
                "kif6piqkn0SLiQZj2JufRjJBAgolt1eyRPLTXaLg87ZpPW8WjB",
                "wNhAqjvxuHHfKIBZpqzozMeHYosFAyvA1Kx5xBLZksR1kVFj8a"
        );
        client.setToken(
                "irpltcHJ6K3ZEXPKCJjV0B6bdHYJfMsT8oldCv6EaD2nBG87SI",
                "ZHdZH7guXA1iemJ7mpcTZSdZqYchTUciD2QxgreGQ99rLhauIf"
        );
    }
    String fileDirectory="/usr/local/apache-tomcat-7.0.47/webapps/examples/";


    public static void main(String args[]) throws Exception
    {
        init();
        String sCurrentLine;
        BufferedReader br = null;
        br = new BufferedReader(new FileReader("/tmp/showsfinal.txt"));
        TumblrLoader.init();
        RefactoredTumblrLoader.init();
        while ((sCurrentLine = br.readLine()) != null) {

            String[] buffer= StringUtils.split(sCurrentLine, "@", 2);
            String[] newbuffer=StringUtils.split(buffer[1],"#",2);
            String[] finalbuffer=StringUtils.split(newbuffer[1], "@", 2);
            String caste[]=new String[1000];
            String hashtag[]=new String[1000];

            //  populateShowIDToShowName(socialMysqlLayer, countString,buffer[0].trim() , "@"+newbuffer[0].trim(), null, "#"+newbuffer[1].trim());
          //  loadTumblr(null,buffer[0].trim());

             loadTumblr(null,buffer[0].trim());


        }
        // System.out.println(showName+"    "+tumblerPage);

    }





    public static void loadTumblr(TumblrSqlLayer tumblrSqlLayer,String showName) throws Exception
    {
        Map<String, String> options = new HashMap<String, String>();
        Date date = new Date();
        long unixTime = (long) date.getTime() / 1000;

        options.put("before", String.valueOf(unixTime));
        options.put("limit", "100");
        int count = 0;
        Map<String,Integer> tvMap=new HashMap<String, Integer>();
        Map<String,Integer> showNameMap=new HashMap<String, Integer>();
        Map<String,Integer> seasonMap=new HashMap<String, Integer>();
        Map<String,Integer> finalMap=null;
        while (count++ < 30) {

            List<Post> posts = client.tagged(showName.trim() + " TV series", options);

            for (Post post : posts) {
                persist(tumblrSqlLayer, post, showName.trim(), tvMap);

            }
            System.out.println(" TV  series");
            System.out.println(sortByValue(tvMap));
            posts = client.tagged(showName.trim()+" TV ", options);


            for (Post post : posts) {
                persist(tumblrSqlLayer, post, showName.trim(), showNameMap);

            }

            System.out.println(" TV");
            System.out.println(sortByValue(showNameMap));

            posts = client.tagged(showName.trim() + " Show", options);

            for (Post post : posts) {
                persist(tumblrSqlLayer, post, showName.trim(), seasonMap);

            }

            System.out.println(" show ");
            System.out.println(sortByValue(seasonMap));

            finalMap=new HashMap<String, Integer>();

            for(Map.Entry<String,Integer> entry:tvMap.entrySet())
            {
                if(!finalMap.containsKey(entry.getKey()))
                {
                    finalMap.put(entry.getKey(),entry.getValue());
                }
                else
                {
                    int newvalue=finalMap.get(entry.getKey())+entry.getValue();
                    finalMap.put(entry.getKey(),newvalue);
                }
            }


            for(Map.Entry<String,Integer> entry:seasonMap.entrySet())
            {
                if(!finalMap.containsKey(entry.getKey()))
                {
                    finalMap.put(entry.getKey(),entry.getValue());
                }
                else
                {
                    int newvalue=finalMap.get(entry.getKey())+entry.getValue();
                    finalMap.put(entry.getKey(),newvalue);
                }
            }

            for(Map.Entry<String,Integer> entry:showNameMap.entrySet())
            {
                if(!finalMap.containsKey(entry.getKey()))
                {
                    finalMap.put(entry.getKey(),entry.getValue());
                }
                else
                {
                    int newvalue=finalMap.get(entry.getKey())+entry.getValue();
                    finalMap.put(entry.getKey(),newvalue);
                }
            }

            System.out.println(" final map "+date);
            System.out.println(sortByValue(finalMap));


            Thread.sleep(1000);


            Date daysAgo = new DateTime(date).minusDays(4).toDate();
            date = daysAgo;
            unixTime = (long) daysAgo.getTime() / 1000;
            options.put("before", String.valueOf(unixTime));

        }
        //print maps into a file
    }

    static Map sortByValue(Map map) {
        List list = new LinkedList(map.entrySet());
        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue())
                        .compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        Map result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry)it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }


    private static void persist(TumblrSqlLayer tumblrSqlLayer,Post post,String showName,Map<String,Integer> map) throws Exception{

        dbVidePost(tumblrSqlLayer,Arrays.asList(post),showName,map);

    }

    private static void dbVidePost(TumblrSqlLayer tumblrSqlLayer,List<Post> videoPosts,String showName,Map<String,Integer> map) throws Exception
    {
        for(Post post : videoPosts)
        {
            String name=post.getBlogName();
            Blog blog=client.blogInfo(name);
           for(String tag :post.getTags())
           {
               if(map.containsKey(tag))
               {
                   int value=map.get(tag);
                   value++;
                   map.put(tag,value);
               }
               else
               {
                   map.put(tag,1);
               }
           }
        }
    }

}
