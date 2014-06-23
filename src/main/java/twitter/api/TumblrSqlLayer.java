package twitter.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by akohli on 6/23/14.
 */
public class TumblrSqlLayer {

    private final String jdbcDriverStr;
    private final String jdbcURL;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String fileDirectory="/Library/Tomcat/webapps/examples/";

    private PreparedStatement preparedStatement;
    public TumblrSqlLayer(String jdbcDriverStr, String jdbcURL) {
        this.jdbcDriverStr = jdbcDriverStr;
        this.jdbcURL = jdbcURL;
    }

    public  void populateTumblrData(long postID,String blogName,String text,String showName,
                                   String title,String official,String type,int sentimentalScore,
                                   int likes,int followes,int width,String embed,
                                   String time,String url) throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        System.out.println(time);
        try {

            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            preparedStatement = connection.prepareStatement("insert into TUMBLER_DATA(postID," +
                    "blogName,text,title,official,type,sentimentalScore,likes,followers,width,embedCode," +
                    "created_on,lastUpdated,url,show_name) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setLong(1, postID);
            preparedStatement.setString(2, blogName);
            preparedStatement.setString(3, text);
            preparedStatement.setString(4, title);
            preparedStatement.setString(5, official);
            preparedStatement.setString(6, type);
            preparedStatement.setInt(7, sentimentalScore);
            preparedStatement.setInt(8, likes);
            preparedStatement.setInt(9, followes);
            preparedStatement.setInt(10, width);
            preparedStatement.setString(11, embed);
            preparedStatement.setDate(12, new java.sql.Date(df.parse(time).getTime()));
            preparedStatement.setTimestamp(13, new Timestamp(df.parse(time).getTime()));
            preparedStatement.setString(14, url);
            preparedStatement.setString(15, showName);

            preparedStatement.executeUpdate();
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
    }

    public  void loadPhotos(String fileName,String showName) throws Exception{

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
            String query = "select distinct embedCode  from TUMBLER_DATA where show_name=? and type=? order by followers desc limit 30";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,showName);
            preparedStatement.setString(2,"photo");

            rs = preparedStatement.executeQuery();
            int count=1;
            //STEP 5: Extract data from result set
            while (rs.next()) {
                output.write(rs.getString("embedCode"));
                output.newLine();
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

    }


    public  void loadVideos(String fileName,String showName) throws Exception{

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
            String query = "select distinct embedCode  from TUMBLER_DATA where show_name=? and type=? order by followers desc limit 30";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,showName);
            preparedStatement.setString(2,"video");

            rs = preparedStatement.executeQuery();
            int count=1;
            //STEP 5: Extract data from result set
            while (rs.next()) {
                output.write(rs.getString("embedCode"));
                output.newLine();
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

    }
}
