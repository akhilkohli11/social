package twitter.api;

import com.twitter.Extractor;
import org.json.JSONObject;

import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class SocialMysqlLayer {
    Map<String,HashMap<String,Integer>> showToDateToTweetMap =new HashMap<String, HashMap<String, Integer>>();
    Map<String,HashMap<String,Integer>> showToDateToTweetMapPositive =new HashMap<String, HashMap<String, Integer>>();
    Map<String,HashMap<String,Integer>> showToDateToTweetMapNegative =new HashMap<String, HashMap<String, Integer>>();
    Map<String,HashMap<String,Integer>> showToDateToTweetMapNeutral =new HashMap<String, HashMap<String, Integer>>();
    TreeMap<Integer,String> treeTweetMap=new TreeMap<Integer, String>(Collections.reverseOrder());

    Map<String,List> timeMap=new HashMap<String, List>();
    DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
    String fileDirectory="/usr/local/apache-tomcat-7.0.47/webapps/examples/";
    public void updateTime(long newid) throws Exception{
        DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");


        BufferedWriter output=null;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        File file = new File(fileDirectory+"time.tsv");
        file.createNewFile();
        try {
            output = new BufferedWriter(new FileWriter(file));
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            int count=0;
             rs = statement.executeQuery("select tweetText,ID from SHOW_TWEET where id>"+newid+" limit 1");
            int newcount=0;
            while (rs.next()) {
                if(newcount++>600000)
                {
                    break;
                }
                 newid=Long.parseLong(rs.getString("ID"));
             //   String tweet="";
                String tweetText="";
                try {
                 //    tweet = rs.getString("tweet");
                     tweetText=rs.getString("tweetText");
                }

                catch (Exception e)
                {
                    rs = statement.executeQuery("select * from SHOW_TWEET where id>"+newid+" limit 1");
                    System.out.println(newid);
                       continue;
                }
                Extractor extractor = new Extractor();

                String newTweet=new String(tweetText);
                List<String> removeList=new ArrayList<String>();
                removeList.addAll(extractor.extractURLs(newTweet));
                removeList.addAll(extractor.extractHashtags(newTweet));
                removeList.add(extractor.extractReplyScreenname(newTweet));
                removeList.addAll(Arrays.asList("http:/","http://","http:"));
                removeList.addAll(extractor.extractMentionedScreennames(newTweet));
                for(String remove : removeList)
                {
                    if(remove!=null) {
                        newTweet = newTweet.replace(remove, "");
                    }
                }
          //      JSONObject jsonObject = new JSONObject(tweet);
            //    String createdAt=jsonObject.get("created_at").toString();
                count++;
//              preparedStatement = connection.prepareStatement("update SHOW_TWEET set lastUpdated= ? where id=?");
//                preparedStatement.setTimestamp(1, new Timestamp(df.parse(createdAt).getTime()));
//                preparedStatement.setLong(2, newid);

            //     preparedStatement.executeUpdate();

                preparedStatement = connection.prepareStatement("update SHOW_TWEET set sentimentalScore= ? where id=?");
                preparedStatement.setInt(1, TwitterDataRetriever.findSentiment(newTweet));
                preparedStatement.setLong(2, newid);

                preparedStatement.executeUpdate();

                rs = statement.executeQuery("select * from SHOW_TWEET where id>"+newid+" limit 1");
                output.write(String.valueOf(newid));
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
    }

    public List<String> showAllShowsTweets(List<String> showNames,String fileName,String bottomtime,String uppertime) throws Exception {

        BufferedWriter output=null;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        try {
            File file = new File(fileDirectory+fileName+".tsv");
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file));
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String query = "select distinct created_on  from SHOW_TWEET where created_on >=? and created_on<=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, bottomtime);
            preparedStatement.setString(2,uppertime);
            rs = preparedStatement.executeQuery();
            int count=1;
            //STEP 5: Extract data from result set
            showNames=new ArrayList<String>();
            int newcount=0;
            for(Map.Entry<Integer,String> entry:treeTweetMap.entrySet()) {
                if(newcount++==5)
                    break;
                showNames.add(entry.getValue());

            }
                output.write("date" + "\t time \t");
                for (String showName : showNames) {
                    output.write(showName.replaceAll(" ","").replaceAll("'","") + "\t");
                }
                output.newLine();

                while (rs.next()) {
                    String create = rs.getString("created_on");
                    output.write(create.replaceAll("00:00:00.0", "").replaceAll(" ", "").replaceAll("-", "") + "\t" + 0 + "\t");

                    for (String showName : showNames) {
                        int finalcount=0;
                        try {
                            finalcount = showToDateToTweetMap.get(showName).get(create);
                        }catch (Exception e)
                        {}
                        output.write(finalcount + "\t");
                        System.out.println(showName + create + "    " + finalcount);
                    }
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

    public List<String> readTweets(String showName,String fileName,String bottomtime,String uppertime) throws Exception {

        BufferedWriter output=null;
         Connection connection=null;
         Statement statement=null;
         ResultSet rs;
         PreparedStatement preparedStatement;
        try {
            String newshow=new String(showName);
            File file = new File(fileDirectory+fileName+newshow.trim().replaceAll(" ","").replaceAll("'","")+".tsv");
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file));
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String query = "select distinct created_on  from SHOW_TWEET where created_on >=? and created_on<=? and show_name=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, bottomtime);
            preparedStatement.setString(2,uppertime);
            preparedStatement.setString(3,showName);
            rs = preparedStatement.executeQuery();
            int count=1;
            //STEP 5: Extract data from result set
            output.write("date"+"\t"+showName+"\t"+"time");
            int totaltweetCount=0;
            while (rs.next()) {
                output.newLine();
                String create = rs.getString("created_on");
                query = "select created_on from SHOW_TWEET where created_on=? and show_name=?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,create);
                preparedStatement.setString(2,showName);
                ResultSet resultSet1=preparedStatement.executeQuery();
                int finalcount=0;
                while (resultSet1.next())
                {
                    finalcount++;
                }
                HashMap<String,Integer> timetoTweetMap= showToDateToTweetMap.get(showName);
                if(timetoTweetMap==null)
                {
                    showToDateToTweetMap.put(showName, new HashMap<String, Integer>());
                }
                showToDateToTweetMap.get(showName).put(create,finalcount);
                output.write(create.replaceAll("00:00:00.0","").replaceAll(" ","").replaceAll("-","")+"\t"+finalcount+"\t"+0);
                totaltweetCount+=finalcount;
                System.out.println(showName + create + "    " + finalcount);
            }
            treeTweetMap.put(totaltweetCount,showName);
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



    public List<String> showPositive(String showName,String fileName,String bottomtime,String uppertime) throws Exception {

        BufferedWriter output=null;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        try {
            String newshow=new String(showName);
            File file = new File(fileDirectory+fileName+newshow.trim().replaceAll(" ","").replaceAll("'","")+"positive.tsv");
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file));
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String query = "select distinct created_on  from SHOW_TWEET where created_on >=? and created_on<=? and show_name=? and sentimentalScore>=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,bottomtime);
            preparedStatement.setString(2,uppertime);
            preparedStatement.setString(3,showName);
            preparedStatement.setInt(4, 2);
            rs = preparedStatement.executeQuery();
            int count=1;
            //STEP 5: Extract data from result set
            output.write("date"+"\t"+showName+"\t"+"time");
            while (rs.next()) {
                output.newLine();
                String create = rs.getString("created_on");
                query = "select created_on from SHOW_TWEET where created_on=? and show_name=? and sentimentalScore>=?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,create);
                preparedStatement.setString(2,showName);
                preparedStatement.setInt(3,2);

                ResultSet resultSet1=preparedStatement.executeQuery();
                int finalcount=0;
                while (resultSet1.next())
                {
                    finalcount++;
                }
                HashMap<String,Integer> timetoTweetMap= showToDateToTweetMapPositive.get(showName);
                if(timetoTweetMap==null)
                {
                    showToDateToTweetMapPositive.put(showName, new HashMap<String, Integer>());
                }
                showToDateToTweetMapPositive.get(showName).put(create,finalcount);
                output.write(create.replaceAll("00:00:00.0","").replaceAll(" ","").replaceAll("-","")+"\t"+finalcount+"\t"+0);
                System.out.println("positive" + showName + create + "    " + finalcount);
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

    public List<String> showNegative(String showName,String fileName,String bottomtime,String uppertime) throws Exception {

        BufferedWriter output=null;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        try {
            String newshow=new String(showName);
            File file = new File(fileDirectory+fileName+newshow.trim().replaceAll(" ","").replaceAll("'","")+"negative.tsv");
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file));
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String query = "select distinct created_on  from SHOW_TWEET where created_on >=? and created_on<=? and show_name=? and sentimentalScore<=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,bottomtime);
            preparedStatement.setString(2,uppertime);
            preparedStatement.setString(3,showName);
            preparedStatement.setInt(4, 0);

            rs = preparedStatement.executeQuery();
            int count=1;
            //STEP 5: Extract data from result set
            output.write("date"+"\t"+showName+"\t"+"time");
            while (rs.next()) {
                output.newLine();
                String create = rs.getString("created_on");
                query = "select created_on from SHOW_TWEET where created_on=? and show_name=? and sentimentalScore<=?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,create);
                preparedStatement.setString(2,showName);
                preparedStatement.setInt(3,0);
                ResultSet resultSet1=preparedStatement.executeQuery();
                int finalcount=0;
                while (resultSet1.next())
                {
                    finalcount++;
                }
                HashMap<String,Integer> timetoTweetMap= showToDateToTweetMapNegative.get(showName);
                if(timetoTweetMap==null)
                {
                    showToDateToTweetMapNegative.put(showName, new HashMap<String, Integer>());
                }
                showToDateToTweetMapNegative.get(showName).put(create,finalcount);
                output.write(create.replaceAll("00:00:00.0","").replaceAll(" ","").replaceAll("-","")+"\t"+finalcount+"\t"+0);
                System.out.println("nagative" + showName + create + "    " + finalcount);
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




    public List<String> showNegativeTweetText(String showName,String fileName,String bottomtime,String uppertime) throws Exception {

        BufferedWriter output=null;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        try {
            String newshow=new String(showName);
            File file = new File(fileDirectory+fileName+newshow.trim().replaceAll(" ","").replaceAll("'","")+"negativetext");
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file));
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String query = "select tweet  from SHOW_TWEET where created_on >=? and created_on<=? and show_name=? and sentimentalScore<=? limit 30";
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
            String query = "select tweet  from SHOW_TWEET where created_on >=? and created_on<=? and show_name=? and sentimentalScore<=? limit 30";
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

    public List<String> showAllTweetText(String showName,String fileName,String bottomtime,String uppertime) throws Exception {

        BufferedWriter output=null;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        try {
            String newshow=new String(showName);
            File file = new File(fileDirectory+fileName+newshow.trim().replaceAll(" ","").replaceAll("'","")+"alltext.tsv");
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file));
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String query = "select tweet  from SHOW_TWEET where created_on >=? and created_on<=? and show_name=? order by created_on desc limit 30";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,bottomtime);
            preparedStatement.setString(2,uppertime);
            preparedStatement.setString(3,showName);


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

    public List<String> showNeutral(String showName,String fileName,String bottomtime,String uppertime) throws Exception {

        BufferedWriter output=null;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        try {
            String newshow=new String(showName);
            File file = new File(fileDirectory+fileName+newshow.trim().replaceAll(" ","").replaceAll("'","")+"neutral");
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file));
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String query = "select distinct created_on  from SHOW_TWEET where created_on >=? and created_on<=? and show_name=? and sentimentalScore=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,bottomtime);
            preparedStatement.setString(2,uppertime);
            preparedStatement.setString(3,showName);
            preparedStatement.setInt(4, 1);

            rs = preparedStatement.executeQuery();
            int count=1;
            //STEP 5: Extract data from result set
            output.write("date"+"\t"+showName+"\t"+"time");
            while (rs.next()) {
                output.newLine();
                String create = rs.getString("created_on");
                query = "select created_on from SHOW_TWEET where created_on=? and show_name=? and sentimentalScore=?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,create);
                preparedStatement.setString(2,showName);
                preparedStatement.setInt(3,1);
                ResultSet resultSet1=preparedStatement.executeQuery();
                int finalcount=0;
                while (resultSet1.next())
                {
                    finalcount++;
                }
                HashMap<String,Integer> timetoTweetMap= showToDateToTweetMapNeutral.get(showName);
                if(timetoTweetMap==null)
                {
                    showToDateToTweetMapNeutral.put(showName, new HashMap<String, Integer>());
                }
                showToDateToTweetMapNeutral.get(showName).put(create,finalcount);
                output.write(create.replaceAll("00:00:00.0","").replaceAll(" ","").replaceAll("-","")+"\t"+finalcount+"\t"+0);
                System.out.println("neutral " + showName + create + "    " + finalcount);
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

    public List<String> negativepositiveneutralAll(String showName,String fileName,String bottomtime,String uppertime) throws Exception {

        BufferedWriter output=null;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        try {
            String newshow=new String(showName);
            File file = new File(fileDirectory+fileName+newshow.trim().replaceAll(" ","").replaceAll("'","")+"all.tsv");
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file));
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String query = "select distinct created_on  from SHOW_TWEET where created_on >=? and created_on<=? and show_name=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,bottomtime);
            preparedStatement.setString(2,uppertime);
            preparedStatement.setString(3,showName);
            rs = preparedStatement.executeQuery();
            int count=1;
            //STEP 5: Extract data from result set
            output.write("date"+"\t"+"totalTweet"+"\t"+"positive"+"\t"+"negative"+"\t"+"neutral"+"\t"+"time");
            while (rs.next()) {
                output.newLine();
                String create = rs.getString("created_on");
                query = "select sentimentalScore from SHOW_TWEET where created_on=? and show_name=?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,create);
                preparedStatement.setString(2,showName);
                ResultSet resultSet1=preparedStatement.executeQuery();
                int finalcount=0;
                int negCount=0;
                int poscount=0;
                int neutralcount=0;
                while (resultSet1.next())
                {
                    String score = resultSet1.getString("sentimentalScore");
                    if(Integer.parseInt(score)==1)
                    {
                        neutralcount++;
                    }
                    if(Integer.parseInt(score)>1)
                    {
                        poscount++;
                    }
                    if(Integer.parseInt(score)<1)
                    {
                        negCount++;
                    }
                    finalcount++;
                }
                output.write(create.replaceAll("00:00:00.0","").replaceAll(" ","").replaceAll("-","")+"\t"+finalcount+"\t"+poscount+"\t"+negCount+"\t"+neutralcount+"\t"+0);
                System.out.println(showName + create + "    " + finalcount);
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




        public void showTrends() throws Exception{

        BufferedWriter output=null;
        try {
            File file = new File("/Users/akohli/IdeaProjects/app/files/"+"trends");
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file));
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select tweet,show_name,sentimentalScore from SHOW_TWEET ");
            int count=1;
            int postiveCount=0;
            int neutralcount=0;
            int negativecount=0;
            Map<String,Integer> trends=new HashMap<String, Integer>();
            //STEP 5: Extract data from result set
            while (rs.next()) {
                String tweet = rs.getString("tweet");
                String show = rs.getString("show_name");
                Integer number=trends.get(show);
                if(number!=null)
                {
                    number++;
                    trends.put(show,number);
                }
                else
                {
                    trends.put(show,1);
                }
                // System.out.println(show+"      "+showName);


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
            for(int i = 0; i < numbers.length; i++)
            {
                if(!persisted.contains(trends1.get(numbers[i])) && trends1.get(numbers[i])!=null)
                {
                    persisted.add(trends1.get(numbers[i]));
                output.write(String.valueOf(trends1.get(numbers[i])));
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

//    public List<String> getShowTweets(String showName) throws Exception
//    {
//        try {
//            Class.forName(jdbcDriverStr);
//            connection = DriverManager.getConnection(jdbcURL);
//            statement = connection.createStatement();
//            resultSet = statement.executeQuery("select count(*) from SHOW_TWEET;");
//            //  getResultSet(resultSet);
//
//        } finally {
//            close();
//    }

    public  int populateShowIDToShowName(String showID,String showName,String twitterHandle,String casteHandle,String hashtag) throws Exception
    {
        try{
              Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            preparedStatement = connection.prepareStatement("insert into SHOW_META_DETA(show_id,show_name," +
                    "show_twitter_handle_id,show_caste_handle_id,hashtag" + ") values (?,?,?,?,?)");
            preparedStatement.setString(1, showID);
            preparedStatement.setString(2, showName);
            preparedStatement.setString(3, twitterHandle);
            preparedStatement.setString(4, casteHandle);
            preparedStatement.setString(5, hashtag);


            return preparedStatement.executeUpdate();
        } finally {
            close();
        }
    }



    public  int populateTweetData(String tweet,String tweetText,String showName,
                                  String createdOn,String persistTime,int sentimentalScore) throws Exception
    {
        try{
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            preparedStatement = connection.prepareStatement("insert into SHOW_TWEET(tweet," +
                    "tweetText,show_name,created_on,sentimentalScore" + ") values (?,?,?,?,?)");
            preparedStatement.setString(1, tweet);
            preparedStatement.setString(2, tweetText);
            preparedStatement.setString(3, showName);
            preparedStatement.setDate(4, new java.sql.Date(df.parse(createdOn).getTime()));
            preparedStatement.setInt(5, sentimentalScore);

            return preparedStatement.executeUpdate();
        } finally {
            close();
        }
    }

//    public  void populateShowIDToShowName(String showName) throws Exception
//    {
//        try{
//            Class.forName(jdbcDriverStr);
//            connection = DriverManager.getConnection(jdbcURL);
//            statement = connection.createStatement();
//            preparedStatement = connection.prepareStatement("insert into SHOW_META_DETA(show_name) values (?)");
//            preparedStatement.setString(1, showName);
//            preparedStatement.executeUpdate();
//        } finally {
//            close();
//        }
//    }

//    private void getResultSet(ResultSet resultSet) throws Exception {
//            Integer id = resultSet.get
//            String text = resultSet.getString(TestTableColumns.show_name.toString());
//            System.out.println("id: " + id);
//            System.out.println("text: " + text);
//        }
//    }

    private void close() {
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        } catch (Exception e) {
        }
    }
}
