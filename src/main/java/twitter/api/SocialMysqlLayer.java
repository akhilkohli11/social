package twitter.api;

import com.twitter.Extractor;
import org.joda.time.DateTime;
import org.json.JSONObject;

import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class SocialMysqlLayer {
    Map<String,HashMap<String,Integer>> showToDateToTweetMap =new HashMap<String, HashMap<String, Integer>>();
    Map<String,HashMap<String,Integer>> showToDateToTweetMapPositive =new HashMap<String, HashMap<String, Integer>>();
    Map<String,HashMap<String,Integer>> showToDateToTweetMapNegative =new HashMap<String, HashMap<String, Integer>>();
    Map<String,HashMap<String,Integer>> showToDateToTweetMapNeutral =new HashMap<String, HashMap<String, Integer>>();
    TreeMap<Integer,String> treeTweetMap=new TreeMap<Integer, String>(Collections.reverseOrder());

    Map<String,List> timeMap=new HashMap<String, List>();
    DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
      String fileDirectory="/usr/local/apache-tomcat-7.0.47/webapps/examples/";
    //String fileDirectory="/Library/Tomcat/webapps/examples/";
    private int tweetsForDayForShows;


    DateFormat newformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Object location;

    public void populatDayWiseStatsForShowForTwitter() throws Exception {

        Date date = new Date();
        int count=0;
        while (count++<7) {
            Map<String, Integer> showNameToTweet = getTweetsForDayForShows(date);
            Map<String, Integer> showNameToPhotoTweet = getPhotoTweetsForDayForShows(date);
            Map<String, Integer> showNameToLinkTweet = getLinkTweetsForDayForShows(date);
            Map<String, Integer> showNameToTextTweet = getTextTweetsForDayForShows(date);

            deleteMainTable(date);
            for (Map.Entry<String, Integer> entry : showNameToTweet.entrySet()) {
                deleteShowWiseTable(entry.getKey(), date);
                populateShowWiseTable(entry.getKey(), "twitter", "total", entry.getValue(), date);
                populateMainTable(entry.getKey(), "twitter", "total", entry.getValue(), date);
                populateShowWiseTable(entry.getKey(), "twitter", "photo", showNameToPhotoTweet.get(entry.getKey()), date);
                //populateMainTable(entry.getKey(), "twitter", "photo", showNameToPhotoTweet.get(entry.getKey()), date);
                populateShowWiseTable(entry.getKey(), "twitter", "links", showNameToLinkTweet.get(entry.getKey()), date);
                //populateMainTable(entry.getKey(), "twitter", "links", showNameToLinkTweet.get(entry.getKey()), date);
                populateShowWiseTable(entry.getKey(), "twitter", "text", showNameToTextTweet.get(entry.getKey()), date);
                //opulateMainTable(entry.getKey(), "twitter", "text", showNameToTextTweet.get(entry.getKey()), date);

            }
            date = new DateTime(date).minusDays(1).toDate();
        }


    }

    private Map<String, Integer> getTextTweetsForDayForShows(Date date) {
        Map<String,Integer> showNameToCount=new HashMap<String, Integer>();
        Set<String> shows=TwitterDataRetriever.getShows();
        for(String show : shows)
        {
            int count=getCountForType(show, date, "text");
            showNameToCount.put(show,count);
        }
        return showNameToCount;
    }


    public void populatDayWiseStatsForShowForTumblr() throws Exception {
        Date date = new Date();
        int count=0;
        while (count++<25) {
            Map<String,Integer> showNameToTweet=NewJumblrMain.getTumblrPostsForDay(date);
            Map<String,Integer> showNameToPhotoTweet= NewJumblrMain.getPhotoTumblrForDayForShows(date);
            Map<String,Integer> showNameToLinkTweet= NewJumblrMain.getVideoTumblrForDayForShows(date);
            Map<String,Integer> showNameAUDIOTweet= NewJumblrMain.getAUDIOTumblrForDayForShows(date);
            Map<String,Integer> showNameTEXTTweet= NewJumblrMain.getTEXTTumblrForDayForShows(date);

            for(Map.Entry<String,Integer> entry : showNameToTweet.entrySet())
            {
                populateShowWiseTable(entry.getKey(),"tumblr","total",entry.getValue(),date);
                populateMainTable(entry.getKey(),"tumblr","total",entry.getValue(),date);
                populateShowWiseTable(entry.getKey(),"tumblr","photo",showNameToPhotoTweet.get(entry.getKey()),date);
                //populateMainTable(entry.getKey(),"tumblr","photo",showNameToPhotoTweet.get(entry.getKey()),date);
                populateShowWiseTable(entry.getKey(),"tumblr","video",showNameToLinkTweet.get(entry.getKey()),date);
                //populateMainTable(entry.getKey(),"tumblr","video",showNameToLinkTweet.get(entry.getKey()),date);
                populateShowWiseTable(entry.getKey(),"tumblr","text",showNameTEXTTweet.get(entry.getKey()),date);
                //populateMainTable(entry.getKey(),"tumblr","text",showNameTEXTTweet.get(entry.getKey()),date);
                populateShowWiseTable(entry.getKey(),"tumblr","audio",showNameAUDIOTweet.get(entry.getKey()),date);
                int showNotesCount=NewJumblrMain.getLikesForShow(date,entry.getKey());
                populateShowWiseTable(entry.getKey(),"tumblr","likes",showNotesCount,date);
                System.out.println("Updaiting likes "+showNotesCount+" for date "+date+ "show "+entry.getKey());
                //populateMainTable(entry.getKey(),"tumblr","audio",showNameAUDIOTweet.get(entry.getKey()),date);

            }
            date = new DateTime(date).minusDays(1).toDate();
        }


    }


    public void deleteMainTable(Date date) throws Exception{
        try{
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            preparedStatement = connection.prepareStatement("delete from SHOW_COUNT_SOCIAL where created_on=?");
            preparedStatement.setDate(1, new java.sql.Date(date.getTime()));

            preparedStatement.executeUpdate();
        }
        catch (Exception e)
        {}
        finally {
            close();
        }

    }

    public void deleteShowWiseTable(String showName,Date date) throws Exception{
        try{
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String table=new String(showName);
              table=table.trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'","");
            preparedStatement = connection.prepareStatement("delete from SHOW_COUNT_"+table+" where created_on=?");
            preparedStatement.setDate(1, new java.sql.Date(date.getTime()));

            preparedStatement.executeUpdate();
        }
        catch (Exception e)
        {}
        finally {
            close();
        }

    }

    private void populateMainTable(String showName,String socialType,String type,int count,Date date) throws Exception{
        try{
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            preparedStatement = connection.prepareStatement("insert into SHOW_COUNT_SOCIAL (show_name," +
                    "type,socialType,created_on,lastUpdated,count" + ") values (?,?,?,?,?,?)");
            preparedStatement.setString(1, showName);
            preparedStatement.setString(2, type);
            preparedStatement.setString(3, socialType);
            preparedStatement.setDate(4, new java.sql.Date(date.getTime()));
            preparedStatement.setTimestamp(5, new Timestamp(date.getTime()));
            preparedStatement.setInt(6, count);

            preparedStatement.executeUpdate();
        }
        catch (Exception e)
        {}
        finally {
            close();
        }

    }

    private void populateShowWiseTable(String showName,String socialType,String type,int count,Date date) throws Exception{
        try{
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String table=new String(showName);
            table=table.trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'","");
            preparedStatement = connection.prepareStatement("insert into SHOW_COUNT_"+table+"(show_name," +
                    "type,socialType,created_on,lastUpdated,count" + ") values (?,?,?,?,?,?)");
            preparedStatement.setString(1, showName);
            preparedStatement.setString(2, type);
            preparedStatement.setString(3, socialType);
            preparedStatement.setDate(4, new java.sql.Date(date.getTime()));
            preparedStatement.setTimestamp(5, new Timestamp(date.getTime()));
            preparedStatement.setInt(6, count);

            preparedStatement.executeUpdate();
        }
        catch (Exception e)
        {}
        finally {
            close();
        }

    }

    private Map<String, Integer> getPhotoTweetsForDayForShows(Date date) {
        Map<String,Integer> showNameToCount=new HashMap<String, Integer>();
        Set<String> shows=TwitterDataRetriever.getShows();
        for(String show : shows)
        {
            int count=getCountForType(show, date, "photo");
            showNameToCount.put(show,count);
        }
        return showNameToCount;
    }

    private Map<String, Integer> getLinkTweetsForDayForShows(Date date) {
        Map<String,Integer> showNameToCount=new HashMap<String, Integer>();
        Set<String> shows=TwitterDataRetriever.getShows();
        for(String show : shows)
        {
            int count=getCountForType(show, date, "links");
            showNameToCount.put(show,count);
        }
        return showNameToCount;
    }

    private int getCountForType(String showName, Date date, String type) {
        Integer count=0;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        try {
            String table=new String(showName);
            table=table.trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'","");
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String query = "select count(*) as count  from SHOW_GEO_TWITTER_"+table+" where created_on =? and type=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1,new java.sql.Date(date.getTime()));
            preparedStatement.setString(2,type);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String daycount = rs.getString("count");
                count=Integer.parseInt(daycount);
            }
            //  getResultSet(resultSet);

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (statement != null)
                    connection.close();
            } catch (SQLException se) {
            }// do nothing
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try

        }
        return count;
    }

    public Map<String,Integer> getTweetsForDayForShows(Date date) throws Exception{
        Map<String,Integer> showNameToCount=new HashMap<String, Integer>();
        Set<String> shows=TwitterDataRetriever.getShows();
        for(String show : shows)
        {
            int count=getCount(show,date);
            showNameToCount.put(show,count);
        }
        return showNameToCount;
    }

    public void loadComparisionGraphTwitter(String[] showNames,  String bottomtime,String uppertime, String id) throws Exception{
        BufferedWriter statsOutput=null;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        String newFileName="comparetwittergraph"+id+".tsv";
        try {
            File newFile = new File(fileDirectory+newFileName);
            newFile.createNewFile();

            statsOutput = new BufferedWriter(new FileWriter(newFile));


            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            int loopcount=0;
            Map<String,Map<String,Integer>> datahsowCountMap=new LinkedHashMap<String, Map<String, Integer>>();
            for(String show : showNames) {
                String showName = TwitterDataRetriever.getShowToTableName().get(show.toLowerCase());
                loopcount++;

                String table = new String(showName);
                table = table.trim().toLowerCase().replaceAll(" ", "").replaceAll("\"", "").replaceAll("'", "");
                String query = "select count,created_on  from SHOW_COUNT_" + table + " where   socialType=? and created_on >=? and created_on<=? and type=? order by created_on asc";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, "twitter");
                preparedStatement.setString(2, bottomtime);
                preparedStatement.setString(3, uppertime);
                preparedStatement.setString(4, "total");
                rs = preparedStatement.executeQuery();
                int count = 1;
                //STEP 5: Extract data from result set
                int newcount = 0;
                String oldCreate = "";

                while (rs.next()) {
                    String createdOn=rs.getString("created_on");
                    int total=Integer.parseInt(rs.getString("count"));
                    Map<String,Integer> showCountMap;
                    if(datahsowCountMap.get(createdOn)!=null)
                    {
                        showCountMap=datahsowCountMap.get(createdOn);
                    }
                    else
                    {
                         showCountMap=new LinkedHashMap<String, Integer>();

                    }
                    showCountMap.put(show,total);
                    datahsowCountMap.put(createdOn,showCountMap);

                }


            }

            statsOutput.write("date" + "\t");
            for(String show :showNames) {
                statsOutput.write(show+"\t");
            }
            statsOutput.newLine();

            for(Map.Entry<String,Map<String,Integer>> entry:datahsowCountMap.entrySet())
            {
                statsOutput.write(entry.getKey().replaceAll("00:00:00.0", "").replaceAll(" ", "").replaceAll("-", "")+"\t");
                Map<String,Integer> map=entry.getValue();
                for(String show : showNames)
                {
                    if(map.containsKey(show)) {
                        statsOutput.write(map.get(show) + "\t");
                    }
                    else
                    {
                        statsOutput.write("0" + "\t");

                    }
                }
                statsOutput.newLine();
            }


        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (statement != null)
                    connection.close();
            } catch (SQLException se) {
            }// do nothing
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
            statsOutput.close();

        }
    }



        public void loadComparision(String[] showNames,  String bottomtime,String uppertime, String id) throws Exception{
        BufferedWriter statsOutput=null;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        String newFileName="comparetwitter"+id+".tsv";
        try {
            File newFile = new File(fileDirectory+newFileName);
            newFile.createNewFile();

            statsOutput = new BufferedWriter(new FileWriter(newFile));


            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            int loopcount=0;
            for(String show : showNames) {
                String showName = TwitterDataRetriever.getShowToTableName().get(show.toLowerCase());
                loopcount++;

                String table = new String(showName);
                table = table.trim().toLowerCase().replaceAll(" ", "").replaceAll("\"", "").replaceAll("'", "");
                String query = "select type,count,created_on  from SHOW_COUNT_" + table + " where   socialType=? and created_on >=? and created_on<=? order by created_on asc";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, "twitter");
                preparedStatement.setString(2, bottomtime);
                preparedStatement.setString(3, uppertime);
                rs = preparedStatement.executeQuery();
                int count = 1;
                //STEP 5: Extract data from result set
                int newcount = 0;
                String oldCreate = "";
                int linkCount = 0;
                int textCount = 0;
                int photoCount = 0;
                int totalCount = 0;
                int rowCount = 0;
                int inlopp = 0;
                statsOutput.write("newline");
                statsOutput.newLine();
                statsOutput.write(showName);
                statsOutput.newLine();
                while (rs.next()) {
                    inlopp = 1;
                    String create = rs.getString("created_on");
                    if (!oldCreate.equals(create)) {
                        if (!oldCreate.equals("")) {
                            statsOutput.write(oldCreate.replaceAll("00:00:00.0", "").replaceAll(" ", "").replaceAll("-", "") + "break" + totalCount + "break" + photoCount + "break" + textCount + "break" + linkCount);
                            statsOutput.newLine();
                            linkCount = 0;
                            textCount = 0;
                            photoCount = 0;
                            totalCount = 0;
                        }
                        oldCreate = create;
                    }

                    String type = rs.getString("type");
                    if (type.equals("link")) {
                        linkCount = Integer.parseInt(rs.getString("count"));
                    }

                    if (type.equals("photo")) {
                        photoCount = Integer.parseInt(rs.getString("count"));
                    }
                    if (type.equals("text") || type.equals("quote")) {
                        textCount = Integer.parseInt(rs.getString("count"));
                    }
                    if(type.equals("total"))
                    {
                        totalCount = Integer.parseInt(rs.getString("count"));

                    }
                    //                if(newcount>30 && textCount!=videCount && videCount!=photoCount && photoCount!=audioCount && textCount>0&&
//                        photoCount>0 && videCount>0)
//                    break;

                }
                if (inlopp == 1) {
                    statsOutput.write(oldCreate.replaceAll("00:00:00.0", "").replaceAll(" ", "").replaceAll("-", "") + "break" + totalCount + "break" + photoCount + "break" + textCount + "break" + linkCount);
                    if(loopcount<showNames.length) {
                        statsOutput.newLine();
                    }
                }
            }


        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (statement != null)
                    connection.close();
            } catch (SQLException se) {
            }// do nothing
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
            statsOutput.close();

        }

    }


    public void loadTrendsTwitter( String bottomtime,String uppertime, String id) throws Exception{
        BufferedWriter statsOutput=null;

        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        String newFileName="trendstwitter"+id+".tsv";
        try {
            File newFile = new File(fileDirectory+newFileName);
            newFile.createNewFile();

            statsOutput = new BufferedWriter(new FileWriter(newFile));


            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
             String query = "select count,show_name  from SHOW_COUNT_SOCIAL where   socialType=? and created_on >=? and created_on<=? and type=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,"twitter");
            preparedStatement.setString(2,bottomtime);
            preparedStatement.setString(3,uppertime);
            preparedStatement.setString(4,"total");

            rs = preparedStatement.executeQuery();
            Map<String,Integer> trends=new HashMap<String, Integer>();

            while (rs.next()) {
                String show = rs.getString("show_name");
                int totalCount=Integer.parseInt(rs.getString("count"));
                if(trends.containsKey(show))
                {
                    int count=trends.get(show);
                    count+=totalCount;
                    trends.put(show,count);
                }
                else
                {
                    trends.put(show,totalCount);
                }


            }
            int count1=0;
            int numbers[]=new int[10000];
            Map<Integer,String> trends1=new HashMap<Integer, String>();

            for(Map.Entry<String,Integer> entry : trends.entrySet())
            {
                trends1.put(entry.getValue(),entry.getKey());
            }
            for(Map.Entry<String,Integer> entry : trends.entrySet())
            {
                numbers[count1++] =entry.getValue();
            }


            int temp;

            for(int i = 0; i < numbers.length; i++)
            {
                for(int j = 1; j < (numbers.length-i); j++)
                {
                    //if numbers[j-1] < numbers[j], swap the elements
                    if(numbers[j-1] < numbers[j])
                    {
                        temp = numbers[j-1];
                        numbers[j-1]=numbers[j];
                        numbers[j]=temp;
                    }
                }
            }
            List<String> persisted=new ArrayList<String>();
            int count=0;
            for(int i = 0; i < numbers.length; i++)
            {
                count++;
                if(!persisted.contains(trends1.get(numbers[i])) && trends1.get(numbers[i])!=null)
                {
                    persisted.add(trends1.get(numbers[i]));
                    statsOutput.write(String.valueOf(trends1.get(numbers[i])+"newvalue"+numbers[i]));
                    if(count<numbers.length-1) {
                        statsOutput.newLine();
                    }
                }
            }


        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (statement != null)
                    connection.close();
            } catch (SQLException se) {
            }// do nothing
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
            statsOutput.close();

        }

    }



    public void loadTrendsTumblr(  String bottomtime,String uppertime, String id) throws Exception{
        BufferedWriter statsOutput=null;

        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        String newFileName="trendstumblr"+id+".tsv";
        try {
            File newFile = new File(fileDirectory+newFileName);
            newFile.createNewFile();

            statsOutput = new BufferedWriter(new FileWriter(newFile));


            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String query = "select count,show_name  from SHOW_COUNT_SOCIAL where   socialType=? and created_on >=? and created_on<=? and type=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,"tumblr");
            preparedStatement.setString(2,bottomtime);
            preparedStatement.setString(3,uppertime);
            preparedStatement.setString(4,"total");

            rs = preparedStatement.executeQuery();
            Map<String,Integer> trends=new HashMap<String, Integer>();

            while (rs.next()) {
                String show = rs.getString("show_name");
                int totalCount=Integer.parseInt(rs.getString("count"));
                if(trends.containsKey(show))
                {
                    int count=trends.get(show);
                    count+=totalCount;
                    trends.put(show,count);
                }
                else
                {
                    trends.put(show,totalCount);
                }


            }
            int count1=0;
            int numbers[]=new int[10000];
            Map<Integer,String> trends1=new HashMap<Integer, String>();

            for(Map.Entry<String,Integer> entry : trends.entrySet())
            {
                trends1.put(entry.getValue(),entry.getKey());
            }
            for(Map.Entry<String,Integer> entry : trends.entrySet())
            {
                numbers[count1++] =entry.getValue();
            }


            int temp;

            for(int i = 0; i < numbers.length; i++)
            {
                for(int j = 1; j < (numbers.length-i); j++)
                {
                    //if numbers[j-1] < numbers[j], swap the elements
                    if(numbers[j-1] < numbers[j])
                    {
                        temp = numbers[j-1];
                        numbers[j-1]=numbers[j];
                        numbers[j]=temp;
                    }
                }
            }
            List<String> persisted=new ArrayList<String>();
            int count=0;
            for(int i = 0; i < numbers.length; i++)
            {
                if(!persisted.contains(trends1.get(numbers[i])) && trends1.get(numbers[i])!=null)
                {
                    persisted.add(trends1.get(numbers[i]));
                    statsOutput.write(String.valueOf(trends1.get(numbers[i])+"newvalue"+numbers[i]));
                    if(count<numbers.length-1) {
                        statsOutput.newLine();
                    }
                }
            }


        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (statement != null)
                    connection.close();
            } catch (SQLException se) {
            }// do nothing
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
            statsOutput.close();

        }

    }



    enum TestTableColumns {
        show_id, show_name;
    }

    private final String jdbcDriverStr;
    private final String jdbcURL;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private PreparedStatement preparedStatement;
    Map<String,String> tagToshowID=new HashMap<String, String>();
    public SocialMysqlLayer(String jdbcDriverStr, String jdbcURL) {
        this.jdbcDriverStr = jdbcDriverStr;
        this.jdbcURL = jdbcURL;
    }

    public Integer getCount(String showName,Date date) throws Exception {
        Integer count=0;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        try {
            String table=new String(showName);
            table=table.trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'","");
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String query = "select count(*) as count  from SHOW_GEO_TWITTER_"+table+" where created_on =?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1,new java.sql.Date(date.getTime()));
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String daycount = rs.getString("count");
                count=Integer.parseInt(daycount);
            }
            //  getResultSet(resultSet);

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (statement != null)
                    connection.close();
            } catch (SQLException se) {
            }// do nothing
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try

        }
        return count;

    }

    public Map<String,Integer> getTweet(String showName) throws Exception {
        Integer count=0;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        Map<String,Integer> tweetMap=new HashMap<String, Integer>();
        List<String> tweets=new ArrayList<String>();
        try {
            String table=new String(showName);
            table=table.trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'","");
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String query = "select tweet,sentimentalScore  from SHOW_TWEET where show_name = ? limit 2";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,showName);
            rs = preparedStatement.executeQuery();
            String tweet=null;
            while (rs.next()) {
                tweet = rs.getString("tweet");
                int sentimentalScore=Integer.parseInt(rs.getString("sentimentalScore"));
                tweetMap.put(tweet,sentimentalScore);
            }
            return tweetMap;
            //  getResultSet(resultSet);

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (statement != null)
                    connection.close();
            } catch (SQLException se) {
            }// do nothing
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try

        }
        return null;

    }

    public  int populateTweetData(String tweet,String tweetText,String showName,
                                  String createdOn,String persistTime,int sentimentalScore,String type,String embedCode,String selectedCity,String selectedCountry,
                                  String selectedState) throws Exception
    {
        try{
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String table=new String(showName);
            tweetText  = tweetText.replaceAll("[^\\u0000-\\uFFFF]", "");
            System.out.println("Twitrer "+showName);
            table=table.trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'","");
            preparedStatement = connection.prepareStatement("insert into SHOW_TWITTER_"+table+"(tweet," +
                    "tweetText,show_name,created_on,sentimentalScore,embedCode,type,lastUpdated" + ") values (?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, tweet);
            preparedStatement.setString(2, tweetText);
            preparedStatement.setString(3, showName);
            preparedStatement.setDate(4, new java.sql.Date(df.parse(createdOn).getTime()));
            preparedStatement.setInt(5, sentimentalScore);
            preparedStatement.setString(6, embedCode);
            preparedStatement.setString(7, type);
            preparedStatement.setTimestamp(8, new Timestamp(df.parse(createdOn).getTime()));

            preparedStatement.executeUpdate();


            preparedStatement = connection.prepareStatement("insert into SHOW_GEO_TWITTER_"+table+"(city," +
                    "tweetText,show_name,created_on,sentimentalScore,embedCode,type,lastUpdated,country,state" + ") values (?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, selectedCity);
            preparedStatement.setString(2, tweetText);
            preparedStatement.setString(3, showName);
            preparedStatement.setDate(4, new java.sql.Date(df.parse(createdOn).getTime()));
            preparedStatement.setInt(5, sentimentalScore);
            preparedStatement.setString(6, embedCode);
            preparedStatement.setString(7, type);
            preparedStatement.setTimestamp(8, new Timestamp(df.parse(createdOn).getTime()));
            preparedStatement.setString(9, selectedCountry);
            preparedStatement.setString(10, selectedState);

            return preparedStatement.executeUpdate();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            close();
        }
        return 0;
    }

    public  String loadUSGeography(String showName,String bottomtime,String uppertime,String id) throws Exception{

        BufferedWriter output=null;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        String newshow=new String(showName);
        String fileName="geotwitter"+id+newshow.trim().replaceAll(" ","").replaceAll("'","")+".tsv";
        try {
            File file = new File(fileDirectory+fileName);
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file));
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String table=new String(showName);
            table=table.trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'","");
            String query = "select state  from SHOW_GEO_TWITTER_"+table+" where  created_on >=? and created_on<=?";
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, bottomtime);
            preparedStatement.setString(2, uppertime);

            rs = preparedStatement.executeQuery();
            int count=1;
            //STEP 5: Extract data from result set
            Map<String,Integer> countMap=new HashMap<String, Integer>();
            while (rs.next()) {
                String state=rs.getString("state");
                if(state==null)
                    continue;;
                if(countMap.get(state)!=null)
                {
                    int newcount=countMap.get(state);
                    newcount++;
                    countMap.put(state,newcount);

                }
                else
                {
                    countMap.put(state,1);
                }
            }
            int newcount=0;
            for(Map.Entry<String,Integer> entry :countMap.entrySet())
            {
                output.write("US-"+entry.getKey()+"newvalue"+entry.getValue());
                newcount++;
                if(newcount<countMap.size()) {
                    output.newLine();
                }
            }
            output.close();

            //  getResultSet(resultSet);

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (statement != null)
                    connection.close();
            } catch (SQLException se) {
            }// do nothing
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try

        }
        return fileName;
    }


    public List<String> showPositiveTweetText(String showName,String fileName,String bottomtime,String uppertime) throws Exception {

        BufferedWriter output=null;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        try {
            String newshow=new String(showName);
            File file = new File(fileDirectory+fileName+newshow.trim().replaceAll(" ","").replaceAll("'","")+"positivetext.tsv");
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file));
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String query = "select distinct tweet  from SHOW_TWEET where created_on >=? and created_on<=? and show_name=? and sentimentalScore<=? limit 100";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,bottomtime);
            preparedStatement.setString(2,uppertime);
            preparedStatement.setString(3,showName);
            preparedStatement.setInt(4, 0);

            rs = preparedStatement.executeQuery();
            int count=1;
            //STEP 5: Extract data from result set
            while (rs.next()) {
                output.write(rs.getString("tweet"));
                output.newLine();
            }
            //  getResultSet(resultSet);

        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (statement != null)
                    connection.close();
            } catch (SQLException se) {
            }// do nothing
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
            output.close();

        }
        return null;
    }


    public String loadGRaphs(String showName,String bottomtime,String uppertime,String id) throws Exception{

        BufferedWriter output=null;
        BufferedWriter statsOutput=null;

        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        String newshow=new String(showName);
        String fileName="graphtwitter"+id+newshow.trim().replaceAll(" ","").replaceAll("'","")+".tsv";
        String newFileName="statstwitter"+id+newshow.trim().replaceAll(" ","").replaceAll("'","")+".tsv";
        try {
            File file = new File(fileDirectory+fileName);
            file.createNewFile();

            File newFile = new File(fileDirectory+newFileName);
            newFile.createNewFile();

            output = new BufferedWriter(new FileWriter(file));

            statsOutput = new BufferedWriter(new FileWriter(newFile));


            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String table=new String(showName);
            table=table.trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'","");
            String query = "select type,count,created_on  from SHOW_COUNT_"+table+" where   socialType=? and created_on >=? and created_on<=? order by created_on asc";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,"twitter");
            preparedStatement.setString(2,bottomtime);
            preparedStatement.setString(3,uppertime);
            rs = preparedStatement.executeQuery();
            int count=1;
            //STEP 5: Extract data from result set
            output.write("date"+"\t"+"TO"+"\t"+"P"+"\t"+"TE"+"\t"+"L");
            int newcount=0;
            String oldCreate="";
            int linkCount=0;
            int textCount=0;
            int photoCount=0;
            int totalCount=0;
            int rowCount=0;
            int inlopp=0;
            while (rs.next()) {
                inlopp=1;
                String create = rs.getString("created_on");
                if(!oldCreate.equals(create)) {
                    if(!oldCreate.equals("")) {
                        output.write(oldCreate.replaceAll("00:00:00.0", "").replaceAll(" ", "").replaceAll("-", "") + "\t" + totalCount + "\t" + photoCount + "\t" + textCount + "\t" + linkCount);
                        statsOutput.write(oldCreate.replaceAll("00:00:00.0", "").replaceAll(" ", "").replaceAll("-", "") + "break" + totalCount + "break" + photoCount + "break" + textCount + "break" + linkCount);
                        statsOutput.newLine();
                        linkCount = 0;
                        textCount = 0;
                        photoCount = 0;
                        totalCount = 0;
                    }
                    oldCreate=create;
                    output.newLine();
                }

                String type = rs.getString("type");
                if(type.equals("link"))
                {
                    linkCount=Integer.parseInt(rs.getString("count"));
                }

                if(type.equals("photo"))
                {
                    photoCount=Integer.parseInt(rs.getString("count"));
                }
                if(type.equals("text") || type.equals("quote"))
                {
                    textCount=Integer.parseInt(rs.getString("count"));
                }
                if(type.equals("total"))
                {
                    totalCount=Integer.parseInt(rs.getString("count"));
                }
                //                if(newcount>30 && textCount!=videCount && videCount!=photoCount && photoCount!=audioCount && textCount>0&&
//                        photoCount>0 && videCount>0)
//                    break;

            }
            if(inlopp==1)
            {
                output.write(oldCreate.replaceAll("00:00:00.0", "").replaceAll(" ", "").replaceAll("-", "") + "\t" + totalCount + "\t" + photoCount + "\t" + textCount + "\t" + linkCount);
                statsOutput.write( oldCreate.replaceAll("00:00:00.0", "").replaceAll(" ", "").replaceAll("-", "")+"break"+totalCount + "break" + photoCount + "break" + textCount + "break" + linkCount);
            }


        } catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (statement != null)
                    connection.close();
            } catch (SQLException se) {
            }// do nothing
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try
            output.close();
            statsOutput.close();

        }
        return fileName;
    }





    public Set<String> getCityList() throws Exception{
        BufferedWriter output=null;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        Set<String> cityList=new HashSet<String>();
        try {

            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String query = "select city from cities";
            preparedStatement = connection.prepareStatement(query);
            rs = preparedStatement.executeQuery();
            //STEP 5: Extract data from result set
            while (rs.next()) {
                cityList.add(rs.getString("city"));
            }

        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (statement != null)
                    connection.close();
            } catch (SQLException se) {
            }// do nothing
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try

        }
        return cityList;
    }








    public Map<String,String> getCityToStateMap() throws Exception{
        BufferedWriter output=null;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        Map<String,String> cityToStateMap=new HashMap<String, String>();
        try {

            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String query = "select city, state_code  from cities";
            preparedStatement = connection.prepareStatement(query);
            rs = preparedStatement.executeQuery();
            //STEP 5: Extract data from result set
            while (rs.next()) {
                cityToStateMap.put(rs.getString("city"),rs.getString("state_code"));
            }

        }catch (SQLException se) {
            //Handle errors for JDBC
            se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (statement != null)
                    connection.close();
            } catch (SQLException se) {
            }// do nothing
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try

        }
        return cityToStateMap;
    }








    private void close() {
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (Exception e) {
        }
    }


    public double compare(final String s1, final String s2)
    {
        double retval = 0.0;
        final int n = s1.length();
        final int m = s2.length();
        if (0 == n)
        {
            retval = m;
        }
        else if (0 == m)
        {
            retval = n;
        }
        else
        {
            retval = 1.0 - (compare(s1, n, s2, m) / (Math.max(n, m)));
        }
        return retval;
    }

    private double compare(final String s1, final int n,
                           final String s2, final int m)
    {
        int matrix[][] = new int[n + 1][m + 1];
        for (int i = 0; i <= n; i++)
        {
            matrix[i][0] = i;
        }
        for (int i = 0; i <= m; i++)
        {
            matrix[0][i] = i;
        }

        for (int i = 1; i <= n; i++)
        {
            int s1i = s1.codePointAt(i - 1);
            for (int j = 1; j <= m; j++)
            {
                int s2j = s2.codePointAt(j - 1);
                final int cost = s1i == s2j ? 0 : 1;
                matrix[i][j] = min3(matrix[i - 1][j] + 1,
                        matrix[i][j - 1] + 1,
                        matrix[i - 1][j - 1] + cost);
            }
        }
        return matrix[n][m];
    }

    private int min3(final int a, final int b, final int c)
    {
        return Math.min(Math.min(a, b), c);
    }

    //    public List<String> formFile(String showName,String fileName,String bottomtime,String uppertime) throws Exception {
//
//        BufferedWriter output=null;
//        Connection connection=null;
//        Statement statement=null;
//        ResultSet rs;
//        PreparedStatement preparedStatement;
//        try {
//            String newshow=new String(showName);
//            File file = new File(fileDirectory+fileName+newshow.trim().replaceAll(" ","").replaceAll("'","")+".tsv");
//            file.createNewFile();
//            output = new BufferedWriter(new FileWriter(file));
//            Class.forName(jdbcDriverStr);
//            connection = DriverManager.getConnection(jdbcURL);
//            statement = connection.createStatement();
//            String query = "select distinct created_on  from SHOW_TWEET where created_on >=? and created_on<=? and show_name=?";
//            preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setString(1,bottomtime);
//            preparedStatement.setString(2,uppertime);
//            preparedStatement.setString(3,showName);
//            rs = preparedStatement.executeQuery();
//            int count=1;
//            //STEP 5: Extract data from result set
//            output.write("date"+"\t"+"totalTweet"+"\t"+"positive"+"\t"+"negative"+"\t"+"neutral");
//            int finalcount=0;
//            while (rs.next()) {
//                output.newLine();
//                String create = rs.getString("created_on");
//                query = "select sentimentalScore from SHOW_TWEET where created_on=? and show_name=?";
//                preparedStatement = connection.prepareStatement(query);
//                preparedStatement.setString(1,create);
//                preparedStatement.setString(2,showName);
//                ResultSet resultSet1=preparedStatement.executeQuery();
//                int negCount=0;
//                int poscount=0;
//                int neutralcount=0;
//                while (resultSet1.next())
//                {
//                    String score = resultSet1.getString("sentimentalScore");
//                    if(Integer.parseInt(score)==1)
//                    {
//                        neutralcount++;
//                    }
//                    if(Integer.parseInt(score)>1)
//                    {
//                        poscount++;
//                    }
//                    if(Integer.parseInt(score)<1)
//                    {
//                        negCount++;
//                    }
//                    finalcount++;
//                }
//                //output.write(create.replaceAll("00:00:00.0","").replaceAll(" ","").replaceAll("-","")+"\t"+finalcount+"\t"+poscount+"\t"+negCount+"\t"+neutralcount);
//                System.out.println(showName + create + "    " + finalcount);
//            }
//            trends.put(showName,finalcount);
//
//            //  getResultSet(resultSet);
//
//        } catch (SQLException se) {
//            //Handle errors for JDBC
//            se.printStackTrace();
//        } catch (Exception e) {
//            //Handle errors for Class.forName
//            e.printStackTrace();
//        } finally {
//            //finally block used to close resources
//            try {
//                if (statement != null)
//                    connection.close();
//            } catch (SQLException se) {
//            }// do nothing
//            try {
//                if (connection != null)
//                    connection.close();
//            } catch (SQLException se) {
//                se.printStackTrace();
//            }//end finally try
//            output.close();
//
//        }
//        return null;
//    }

    //
//    public List<String> showAllShowsTweets(List<String> showNames,String fileName,String bottomtime,String uppertime) throws Exception {
//
//        BufferedWriter output=null;
//        Connection connection=null;
//        Statement statement=null;
//        ResultSet rs;
//        PreparedStatement preparedStatement;
//        try {
//            File file = new File(fileDirectory+fileName+".tsv");
//            file.createNewFile();
//            output = new BufferedWriter(new FileWriter(file));
//            Class.forName(jdbcDriverStr);
//            connection = DriverManager.getConnection(jdbcURL);
//            statement = connection.createStatement();
//            String query = "select distinct created_on  from SHOW_TWEET where created_on >=? and created_on<=?";
//            preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setString(1, bottomtime);
//            preparedStatement.setString(2,uppertime);
//            rs = preparedStatement.executeQuery();
//            int count=1;
//            //STEP 5: Extract data from result set
//            showNames=new ArrayList<String>();
//            int newcount=0;
//            for(Map.Entry<Integer,String> entry:treeTweetMap.entrySet()) {
//                if(newcount++==5)
//                    break;
//                showNames.add(entry.getValue());
//
//            }
//                output.write("date" + "\t time \t");
//                for (String showName : showNames) {
//                    output.write(showName.replaceAll(" ","").replaceAll("'","") + "\t");
//                }
//                output.newLine();
//
//                while (rs.next()) {
//                    String create = rs.getString("created_on");
//                    output.write(create.replaceAll("00:00:00.0", "").replaceAll(" ", "").replaceAll("-", "") + "\t" + 0 + "\t");
//
//                    for (String showName : showNames) {
//                        int finalcount=0;
//                        try {
//                            finalcount = showToDateToTweetMap.get(showName).get(create);
//                        }catch (Exception e)
//                        {}
//                        output.write(finalcount + "\t");
//                        System.out.println(showName + create + "    " + finalcount);
//                    }
//                    output.newLine();
//
//                }
//
//            //  getResultSet(resultSet);
//
//        } catch (SQLException se) {
//            //Handle errors for JDBC
//            se.printStackTrace();
//        } catch (Exception e) {
//            //Handle errors for Class.forName
//            e.printStackTrace();
//        } finally {
//            //finally block used to close resources
//            try {
//                if (statement != null)
//                    connection.close();
//            } catch (SQLException se) {
//            }// do nothing
//            try {
//                if (connection != null)
//                    connection.close();
//            } catch (SQLException se) {
//                se.printStackTrace();
//            }//end finally try
//            output.close();
//
//        }
//        return null;
//    }



//    public void getLocation() throws Exception{
//        BufferedWriter output=null;
//        Connection connection=null;
//        Statement statement=null;
//        ResultSet rs;
//        PreparedStatement preparedStatement;
//        try {
//            String newshow = "file";
//            File file = new File(fileDirectory + newshow + ".tsv");
//            file.createNewFile();
//            output = new BufferedWriter(new FileWriter(file));
//            Class.forName(jdbcDriverStr);
//            connection = DriverManager.getConnection(jdbcURL);
//            statement = connection.createStatement();
//            String query = "select city, state_code  from cities";
//            preparedStatement = connection.prepareStatement(query);
//            rs = preparedStatement.executeQuery();
//            //STEP 5: Extract data from result set
//            while (rs.next()) {
//                cities.add(rs.getString("city"));
//            }
//            long id = 0;
//            int countnotfound = 0;
//            while (true) {
//
//                query = "select id, tweet  from SHOW_TWEET WHERE ID>=?  limit 10000";
//                preparedStatement = connection.prepareStatement(query);
//                preparedStatement.setLong(1, id);
//
//
//                rs = preparedStatement.executeQuery();
//                int count = 1;
//                //STEP 5: Extract data from result set
//                while (rs.next()) {
//                    long newid = Long.parseLong(rs.getString("id"));
//                    if (id > newid) {
//                        id = newid;
//                    }
//
//                    JSONObject jsonObject = new JSONObject(rs.getString("tweet"));
//                    String newloc = jsonObject.get("user").toString();
//
//
//                    JSONObject user = new JSONObject(newloc);
//                    if (user.has("location")) {
//
//                        String location = user.get("location").toString();
//                        if (location.trim().equals("")) {
//                            continue;
//                        }
//                        int found = 0;
//                        for (String city : cities) {
//                            city = city.trim();
//                            location = location.trim();
//                            if (city.contains(location)) {
//                                System.out.println("location " + location + " city " + city);
//                                found = 1;
//                                break;
//                                //output.write(location1);
//                                //output.newLine();
//                            }
//                        }
//                        if (found == 1)
//                            continue;
//                        for (String city : cities) {
//                            city = city.trim();
//                            location = location.trim();
//                            if (location.contains(city)) {
//                                System.out.println("location " + location +" city "+ city);
//                                found = 1;
//                                break;
//                                //output.write(location1);
//                                //output.newLine();
//                            }
//
//                        }
////                        if (found == 0) {
////                            countnotfound++;
////                            if (countnotfound % 8 == 0) {
////                                for (String city : cities) {
////                                    city = city.trim();
////                                    location = location.trim();
////                                    if (compare(city, location) > .0065) {
////                                        System.out.println(location + "   " + city);
////                                        break;
////                                    }
////                                }
////                            }
////
////                        }
//                    }
//                }
//                //  getResultSet(resultSet);
//
//            }
//        }catch (SQLException se) {
//            //Handle errors for JDBC
//            se.printStackTrace();
//        } catch (Exception e) {
//            //Handle errors for Class.forName
//            e.printStackTrace();
//        } finally {
//            //finally block used to close resources
//            try {
//                if (statement != null)
//                    connection.close();
//            } catch (SQLException se) {
//            }// do nothing
//            try {
//                if (connection != null)
//                    connection.close();
//            } catch (SQLException se) {
//                se.printStackTrace();
//            }//end finally try
//            output.close();
//
//        }
//    }



}
