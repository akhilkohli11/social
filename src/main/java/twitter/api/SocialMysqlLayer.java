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
    private int tweetsForDayForShows;

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

     DateFormat newformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void populatDayWiseStatsForShowForTwitter() throws Exception {
        Date date = new Date();

        Map<String,Integer> showNameToTweet=getTweetsForDayForShows(date);
        Map<String,Integer> showNameToPhotoTweet= getPhotoTweetsForDayForShows(date);
        Map<String,Integer> showNameToLinkTweet= getLinkTweetsForDayForShows(date);
        for(Map.Entry<String,Integer> entry : showNameToTweet.entrySet())
        {
            populateShowWiseTable(entry.getKey(),"twitter","total",entry.getValue(),date);
            populateMainTable(entry.getKey(),"twitter","total",entry.getValue(),date);
            populateShowWiseTable(entry.getKey(),"twitter","photo",showNameToPhotoTweet.get(entry.getKey()),date);
            populateMainTable(entry.getKey(),"twitter","photo",showNameToPhotoTweet.get(entry.getKey()),date);
            populateShowWiseTable(entry.getKey(),"twitter","links",showNameToLinkTweet.get(entry.getKey()),date);
            populateMainTable(entry.getKey(),"twitter","links",showNameToLinkTweet.get(entry.getKey()),date);

        }


    }


    public void populatDayWiseStatsForShowForTumblr() throws Exception {
        Date date = new Date();

        Map<String,Integer> showNameToTweet=JumblrMain.getTumblrPostsForDay(date);
        Map<String,Integer> showNameToPhotoTweet= JumblrMain.getPhotoTumblrForDayForShows(date);
        Map<String,Integer> showNameToLinkTweet= JumblrMain.getVideoTumblrForDayForShows(date);
        for(Map.Entry<String,Integer> entry : showNameToTweet.entrySet())
        {
            populateShowWiseTable(entry.getKey(),"tumblr","total",entry.getValue(),date);
            populateMainTable(entry.getKey(),"tumblr","total",entry.getValue(),date);
            populateShowWiseTable(entry.getKey(),"tumblr","photo",showNameToPhotoTweet.get(entry.getKey()),date);
            populateMainTable(entry.getKey(),"tumblr","photo",showNameToPhotoTweet.get(entry.getKey()),date);
            populateShowWiseTable(entry.getKey(),"tumblr","video",showNameToLinkTweet.get(entry.getKey()),date);
            populateMainTable(entry.getKey(),"tumblr","video",showNameToLinkTweet.get(entry.getKey()),date);

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
        } finally {
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
            String query = "select count(*) as count  from SHOW_TWITTER_"+table+" where created_on =? and type=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1,new java.sql.Date(date.getTime()));
            preparedStatement.setString(2,type);
            rs = preparedStatement.executeQuery();
            System.out.println("result   "+rs);
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
            String query = "select count(*) as count  from SHOW_TWITTER_"+table+" where created_on =?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1,new java.sql.Date(date.getTime()));
            rs = preparedStatement.executeQuery();
            System.out.println("result   "+rs);
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
            System.out.println("result   "+rs);
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
                                  String createdOn,String persistTime,int sentimentalScore,String type,String embedCode) throws Exception
    {
        try{
            System.out.println(showName);
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String table=new String(showName);
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

            return preparedStatement.executeUpdate();
        } finally {
            close();
        }
    }


//    public List<String> showPositive(String showName,String fileName,String bottomtime,String uppertime) throws Exception {
//
//        BufferedWriter output=null;
//        Connection connection=null;
//        Statement statement=null;
//        ResultSet rs;
//        PreparedStatement preparedStatement;
//        try {
//            String newshow=new String(showName);
//            File file = new File(fileDirectory+fileName+newshow.trim().replaceAll(" ","").replaceAll("'","")+"positive.tsv");
//            file.createNewFile();
//            output = new BufferedWriter(new FileWriter(file));
//            Class.forName(jdbcDriverStr);
//            connection = DriverManager.getConnection(jdbcURL);
//            statement = connection.createStatement();
//            String query = "select distinct created_on  from SHOW_TWEET where created_on >=? and created_on<=? and show_name=? and sentimentalScore>=?";
//            preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setString(1,bottomtime);
//            preparedStatement.setString(2,uppertime);
//            preparedStatement.setString(3,showName);
//            preparedStatement.setInt(4, 2);
//            rs = preparedStatement.executeQuery();
//            int count=1;
//            //STEP 5: Extract data from result set
//            output.write("date"+"\t"+showName+"\t"+"time");
//            while (rs.next()) {
//                output.newLine();
//                String create = rs.getString("created_on");
//                query = "select created_on from SHOW_TWEET where created_on=? and show_name=? and sentimentalScore>=?";
//                preparedStatement = connection.prepareStatement(query);
//                preparedStatement.setString(1,create);
//                preparedStatement.setString(2,showName);
//                preparedStatement.setInt(3,2);
//
//                ResultSet resultSet1=preparedStatement.executeQuery();
//                int finalcount=0;
//                while (resultSet1.next())
//                {
//                    finalcount++;
//                }
//                HashMap<String,Integer> timetoTweetMap= showToDateToTweetMapPositive.get(showName);
//                if(timetoTweetMap==null)
//                {
//                    showToDateToTweetMapPositive.put(showName, new HashMap<String, Integer>());
//                }
//                showToDateToTweetMapPositive.get(showName).put(create,finalcount);
//                output.write(create.replaceAll("00:00:00.0","").replaceAll(" ","").replaceAll("-","")+"\t"+finalcount+"\t"+0);
//                System.out.println("positive" + showName + create + "    " + finalcount);
//            }
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
//    public List<String> showNegative(String showName,String fileName,String bottomtime,String uppertime) throws Exception {
//
//        BufferedWriter output=null;
//        Connection connection=null;
//        Statement statement=null;
//        ResultSet rs;
//        PreparedStatement preparedStatement;
//        try {
//            String newshow=new String(showName);
//            File file = new File(fileDirectory+fileName+newshow.trim().replaceAll(" ","").replaceAll("'","")+"negative.tsv");
//            file.createNewFile();
//            output = new BufferedWriter(new FileWriter(file));
//            Class.forName(jdbcDriverStr);
//            connection = DriverManager.getConnection(jdbcURL);
//            statement = connection.createStatement();
//            String query = "select distinct created_on  from SHOW_TWEET where created_on >=? and created_on<=? and show_name=? and sentimentalScore<=?";
//            preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setString(1,bottomtime);
//            preparedStatement.setString(2,uppertime);
//            preparedStatement.setString(3,showName);
//            preparedStatement.setInt(4, 0);
//
//            rs = preparedStatement.executeQuery();
//            int count=1;
//            //STEP 5: Extract data from result set
//            output.write("date"+"\t"+showName+"\t"+"time");
//            while (rs.next()) {
//                output.newLine();
//                String create = rs.getString("created_on");
//                query = "select created_on from SHOW_TWEET where created_on=? and show_name=? and sentimentalScore<=?";
//                preparedStatement = connection.prepareStatement(query);
//                preparedStatement.setString(1,create);
//                preparedStatement.setString(2,showName);
//                preparedStatement.setInt(3,0);
//                ResultSet resultSet1=preparedStatement.executeQuery();
//                int finalcount=0;
//                while (resultSet1.next())
//                {
//                    finalcount++;
//                }
//                HashMap<String,Integer> timetoTweetMap= showToDateToTweetMapNegative.get(showName);
//                if(timetoTweetMap==null)
//                {
//                    showToDateToTweetMapNegative.put(showName, new HashMap<String, Integer>());
//                }
//                showToDateToTweetMapNegative.get(showName).put(create,finalcount);
//                output.write(create.replaceAll("00:00:00.0","").replaceAll(" ","").replaceAll("-","")+"\t"+finalcount+"\t"+0);
//                System.out.println("nagative" + showName + create + "    " + finalcount);
//            }
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
//
//
//
//    public List<String> showNegativeTweetText(String showName,String fileName,String bottomtime,String uppertime) throws Exception {
//
//        BufferedWriter output=null;
//        Connection connection=null;
//        Statement statement=null;
//        ResultSet rs;
//        PreparedStatement preparedStatement;
//        try {
//            String newshow=new String(showName);
//            File file = new File(fileDirectory+fileName+newshow.trim().replaceAll(" ","").replaceAll("'","")+"negativetext.tsv");
//            file.createNewFile();
//            output = new BufferedWriter(new FileWriter(file));
//            Class.forName(jdbcDriverStr);
//            connection = DriverManager.getConnection(jdbcURL);
//            statement = connection.createStatement();
//            String query = "select distinct tweet  from SHOW_TWEET where created_on >=? and created_on<=? and show_name=? and sentimentalScore<=? ";
//            preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setString(1,bottomtime);
//            preparedStatement.setString(2,uppertime);
//            preparedStatement.setString(3,showName);
//            preparedStatement.setInt(4, 0);
//
//            rs = preparedStatement.executeQuery();
//            int count=1;
//            //STEP 5: Extract data from result set
//            while (rs.next()) {
//                output.write(rs.getString("tweet"));
//                output.newLine();
//            }
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
//    public List<String> showPositiveTweetText(String showName,String fileName,String bottomtime,String uppertime) throws Exception {
//
//        BufferedWriter output=null;
//        Connection connection=null;
//        Statement statement=null;
//        ResultSet rs;
//        PreparedStatement preparedStatement;
//        try {
//            String newshow=new String(showName);
//            File file = new File(fileDirectory+fileName+newshow.trim().replaceAll(" ","").replaceAll("'","")+"positivetext.tsv");
//            file.createNewFile();
//            output = new BufferedWriter(new FileWriter(file));
//            Class.forName(jdbcDriverStr);
//            connection = DriverManager.getConnection(jdbcURL);
//            statement = connection.createStatement();
//            String query = "select distinct tweet  from SHOW_TWEET where created_on >=? and created_on<=? and show_name=? and sentimentalScore<=? limit 100";
//            preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setString(1,bottomtime);
//            preparedStatement.setString(2,uppertime);
//            preparedStatement.setString(3,showName);
//            preparedStatement.setInt(4, 0);
//
//            rs = preparedStatement.executeQuery();
//            int count=1;
//            //STEP 5: Extract data from result set
//            while (rs.next()) {
//                output.write(rs.getString("tweet"));
//                output.newLine();
//            }
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
//    public List<String> showAllTweetText(String showName,String fileName,String bottomtime,String uppertime) throws Exception {
//
//        BufferedWriter output=null;
//        Connection connection=null;
//        Statement statement=null;
//        ResultSet rs;
//        PreparedStatement preparedStatement;
//        try {
//            String newshow=new String(showName);
//            File file = new File(fileDirectory+fileName+newshow.trim().replaceAll(" ","").replaceAll("'","")+"alltext.tsv");
//            file.createNewFile();
//            output = new BufferedWriter(new FileWriter(file));
//            Class.forName(jdbcDriverStr);
//            connection = DriverManager.getConnection(jdbcURL);
//            statement = connection.createStatement();
//            String query = "select distinct tweet  from SHOW_TWEET where created_on >=? and created_on<=? and show_name=? order by created_on desc limit 100";
//            preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setString(1,bottomtime);
//            preparedStatement.setString(2,uppertime);
//            preparedStatement.setString(3,showName);
//
//
//            rs = preparedStatement.executeQuery();
//            int count=1;
//            //STEP 5: Extract data from result set
//            while (rs.next()) {
//                output.write(rs.getString("tweet"));
//                output.newLine();
//            }
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
//    public List<String> negativepositiveneutralAll(String showName,String fileName,String bottomtime,String uppertime) throws Exception {
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
//            output.write("date"+"\t"+"totalTweet"+"\t"+"positive"+"\t"+"negative"+"\t"+"neutral"+"\t"+"time");
//            while (rs.next()) {
//                output.newLine();
//                String create = rs.getString("created_on");
//                query = "select sentimentalScore from SHOW_TWEET where created_on=? and show_name=?";
//                preparedStatement = connection.prepareStatement(query);
//                preparedStatement.setString(1,create);
//                preparedStatement.setString(2,showName);
//                ResultSet resultSet1=preparedStatement.executeQuery();
//                int finalcount=0;
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
//                output.write(create.replaceAll("00:00:00.0","").replaceAll(" ","").replaceAll("-","")+"\t"+finalcount+"\t"+poscount+"\t"+negCount+"\t"+neutralcount+"\t"+0);
//                System.out.println(showName + create + "    " + finalcount);
//            }
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
//
//
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

    Map<String,Integer> trends=new HashMap<String, Integer>();

        public void showTrends(String bottomtime,String uppertime) throws Exception{

        BufferedWriter output=null;
        try {
            File file = new File(fileDirectory+"trends.tsv");
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file));
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
//            String query ="select show_name from SHOW_TWEET where created_on >=? and created_on<=?";
//            preparedStatement = connection.prepareStatement(query);
//            preparedStatement.setString(1,bottomtime);
//            preparedStatement.setString(2,uppertime);
//            ResultSet rs=preparedStatement.executeQuery();
//            int count=1;
//            int postiveCount=0;
//            int neutralcount=0;
//            int negativecount=0;
//            Map<String,Integer> trends=new HashMap<String, Integer>();
//            //STEP 5: Extract data from result set
//            while (rs.next()) {
//                String show = rs.getString("show_name");
//                Integer number=trends.get(show);
//                if(number!=null)
//                {
//                    number++;
//                    trends.put(show,number);
//                }
//                else
//                {
//                    trends.put(show,1);
//                }
//                // System.out.println(show+"      "+showName);
//
//
//            }
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
            for(int i = 0; i < numbers.length; i++)
            {
                if(!persisted.contains(trends1.get(numbers[i])) && trends1.get(numbers[i])!=null)
                {
                    persisted.add(trends1.get(numbers[i]));
                output.write(String.valueOf(trends1.get(numbers[i])+"\t"+numbers[i]));
                output.newLine();
                }
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

    }








    private void close() {
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (Exception e) {
        }
    }
}
