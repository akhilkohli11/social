package twitter.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

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
  //  String fileDirectory="/usr/local/apache-tomcat-7.0.47/webapps/examples/";
    String fileDirectory="/Library/Tomcat/webapps/examples/";


    private PreparedStatement preparedStatement;
    public TumblrSqlLayer(String jdbcDriverStr, String jdbcURL) {
        this.jdbcDriverStr = jdbcDriverStr;
        this.jdbcURL = jdbcURL;
    }

    public Map<String, Integer> getPhotoTumblrForDayForShows(java.util.Date date) {
        Map<String,Integer> showNameToCount=new HashMap<String, Integer>();
        Set<String> shows=TwitterDataRetriever.getShows();
        for(String show : shows)
        {
            int count=getCountForType(show, date, "photo");
            showNameToCount.put(show,count);
        }
        return showNameToCount;
    }

    public Map<String, Integer> getVideoTumblrForDayForShows(java.util.Date date) {
        Map<String,Integer> showNameToCount=new HashMap<String, Integer>();
        Set<String> shows=TwitterDataRetriever.getShows();
        for(String show : shows)
        {
            int count=getCountForType(show, date, "video");
            showNameToCount.put(show,count);
        }
        return showNameToCount;
    }

    public Map<String, Integer> getAudioTumblr(Date date) {
        Map<String,Integer> showNameToCount=new HashMap<String, Integer>();
        Set<String> shows=TwitterDataRetriever.getShows();
        for(String show : shows)
        {
            int count=getCountForType(show, date, "audio");
            showNameToCount.put(show,count);
        }
        return showNameToCount;    }

    public Map<String, Integer> getTextTumblr(Date date) {
        Map<String,Integer> showNameToCount=new HashMap<String, Integer>();
        Set<String> shows=TwitterDataRetriever.getShows();
        for(String show : shows)
        {
            int count=getCountForType(show, date, "text");
            int quotecount=getCountForType(show, date, "quote");
            count+=quotecount;
            showNameToCount.put(show,count);
        }
        return showNameToCount;    }

    public Map<String,Integer> getTumblrForDayForShows(java.util.Date date) throws Exception{
        Map<String,Integer> showNameToCount=new HashMap<String, Integer>();
        Set<String> shows=TwitterDataRetriever.getShows();
        for(String show : shows)
        {
            int count=getCount(show,date);
            showNameToCount.put(show,count);
        }
        return showNameToCount;
    }


    private int getCountForType(String showName, java.util.Date date, String type) {
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
            String query = "select count(*) as count  from SHOW_TUMBLR_"+table+" where created_on =? and type=?";
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

    public Integer getCount(String showName, java.util.Date date) throws Exception {
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
            String query = "select count(*) as count  from SHOW_TUMBLR_"+table+" where created_on =?";
            preparedStatement = connection.prepareStatement(query);
            java.sql.Date queryDate=new java.sql.Date(date.getTime());
            preparedStatement.setDate(1, queryDate);
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

    public List<TumblrObject> getTumblRShow(String showName) throws Exception {
        Integer count=0;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        List<TumblrObject> tumblrObjectList=new ArrayList<TumblrObject>();
        try {
            String table=new String(showName);
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String query = "select *  from TUMBLER_DATA where show_name = ? limit 2";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,showName);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String postID=rs.getString("postID");
                String text=rs.getString("text")==null?null:rs.getString("text");
                String blogName=rs.getString("blogName")==null?null:rs.getString("blogName");
                String show_name=rs.getString("show_name")==null?null:rs.getString("show_name");
                String type=rs.getString("type")==null?null:rs.getString("type");
                String sentimentalScore=String.valueOf(randInt(-3,10));

                String likes=rs.getString("likes")==null?null:rs.getString("likes");
                String followers=rs.getString("followers")==null?null:rs.getString("followers");
                String width=rs.getString("width")==null?null:rs.getString("width");
                String embedCode=rs.getString("embedCode")==null?null:rs.getString("embedCode");
                String created_on=rs.getString("created_on")==null?null:rs.getString("created_on");
                String lastUpdated=rs.getString("lastUpdated")==null?null:rs.getString("lastUpdated");
                String url=rs.getString("url")==null?null:rs.getString("url");

                tumblrObjectList.add(new TumblrObject(postID,text,blogName,showName,type,followers,likes,width,embedCode,sentimentalScore,url,created_on,
                        lastUpdated));
            }
            return tumblrObjectList;
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

    public static int randInt(int min, int max) {

        // Usually this should be a field rather than a method variable so
        // that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public  void populateTumblrData(long postID,String blogName,String text,String showName,
                                    String title,String official,String type,int sentimentalScore,
                                    int likes,int followes,int width,String embed,
                                    String time,String url,String postURL) throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        try {

            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            String table=new String(showName);
            System.out.println("tumblr "+showName);
            table=table.trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'","");
            preparedStatement = connection.prepareStatement("insert into SHOW_TUMBLR_"+table+"(postID," +
                    "blogName,text,title,official,type,sentimentalScore,likes,followers,width,embedCode," +
                    "created_on,lastUpdated,url,show_name,posturl) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
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
            preparedStatement.setString(16, postURL);

            preparedStatement.executeUpdate();
        } catch (SQLException se) {
            //Handle errors for JDBC
            //   se.printStackTrace();
        } catch (Exception e) {
            //Handle errors for Class.forName
            // e.printStackTrace();
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




//    public  void populateTumblrData(long postID,String blogName,String text,String showName,
//                                    String title,String official,String type,int sentimentalScore,
//                                    int likes,int followes,int width,String embed,
//                                    String time,String url) throws Exception {
//        Connection connection = null;
//        Statement statement = null;
//        ResultSet rs;
//        PreparedStatement preparedStatement;
//        System.out.println(time);
//        try {
//
//            Class.forName(jdbcDriverStr);
//            connection = DriverManager.getConnection(jdbcURL);
//            System.out.println(showName);
//            String table=new String(showName);
//            table=table.trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'","");
//            preparedStatement = connection.prepareStatement("insert into SHOW_TUMBLR_"+table+"(postID," +
//                    "blogName,text,title,official,type,sentimentalScore,likes,followers,width,embedCode," +
//                    "created_on,lastUpdated,url,show_name) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
//            preparedStatement.setLong(1, postID);
//            preparedStatement.setString(2, blogName);
//            preparedStatement.setString(3, text);
//            preparedStatement.setString(4, title);
//            preparedStatement.setString(5, official);
//            preparedStatement.setString(6, type);
//            preparedStatement.setInt(7, sentimentalScore);
//            preparedStatement.setInt(8, likes);
//            preparedStatement.setInt(9, followes);
//            preparedStatement.setInt(10, width);
//            preparedStatement.setString(11, embed);
//            preparedStatement.setDate(12, new java.sql.Date(df.parse(time).getTime()));
//            preparedStatement.setTimestamp(13, new Timestamp(df.parse(time).getTime()));
//            preparedStatement.setString(14, url);
//            preparedStatement.setString(15, showName);
//
//            preparedStatement.executeUpdate();
//        } catch (SQLException se) {
//            //Handle errors for JDBC
//            //   se.printStackTrace();
//        } catch (Exception e) {
//            //Handle errors for Class.forName
//            // e.printStackTrace();
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
//
//        }
//    }

    public  String loadPhotos(String showName,String bottomtime,String uppertime,String id) throws Exception{

        BufferedWriter output=null;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        String newshow=new String(showName);
        String fileName=id+"phototumblr"+newshow.trim().replaceAll(" ","").replaceAll("'","")+".tsv";
        try {
            File file = new File(fileDirectory+fileName);
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file));
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String table=new String(showName);
            table=table.trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'","");
            String query = "select distinct embedCode, blogName,followers,posturl from SHOW_TUMBLR_"+table+" where show_name=? and type=? and  created_on >=? and created_on<=? order by followers desc limit 15";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,showName);
            preparedStatement.setString(2,"photo");
            preparedStatement.setString(3, bottomtime);
            preparedStatement.setString(4, uppertime);

            rs = preparedStatement.executeQuery();
            int count=1;
            List<String> embedCodeList=new ArrayList<String>();
            //STEP 5: Extract data from result set
            while (rs.next()) {
                embedCodeList.add(rs.getString("embedCode"));
                output.write(rs.getString("blogName")+"break"+rs.getString("followers")+"break"+rs.getString("posturl")+"break"+rs.getString("embedCode"));
                output.newLine();
            }
            if(!embedCodeList.isEmpty())
            {
                output.write("photos");
                output.newLine();
                for(String code : embedCodeList)
                {
                    output.write(code);
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


    public  String loadVideos(String showName,String bottomtime,String uppertime,String id) throws Exception{
        BufferedWriter output=null;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        String newshow=new String(showName);
        String fileName="videotumblr"+id+newshow.trim().replaceAll(" ","").replaceAll("'","")+".tsv";
        try {
            File file = new File(fileDirectory+fileName);
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file));
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String table=new String(showName);
            table=table.trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'","");
            String query = "select distinct embedCode, blogName,followers,posturl from SHOW_TUMBLR_"+table+" where show_name=? and type=? and  created_on >=? and created_on<=? order by followers desc limit 15";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,showName);
            preparedStatement.setString(2,"video");
            preparedStatement.setString(3, bottomtime);
            preparedStatement.setString(4, uppertime);

            rs = preparedStatement.executeQuery();
            int count=1;
            List<String> embedCodeList=new ArrayList<String>();
            //STEP 5: Extract data from result set
            while (rs.next()) {
                embedCodeList.add(rs.getString("embedCode"));
                output.write(rs.getString("blogName")+"break"+rs.getString("followers")+"break"+rs.getString("posturl")+"break"+rs.getString("embedCode"));
                output.newLine();
            }
            if(!embedCodeList.isEmpty())
            {
                output.write("photos");
                output.newLine();
                for(String code : embedCodeList)
                {
                    output.write(code);
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

    public  String loadTextPost(String showName,String bottomtime,String uppertime,String id) throws Exception{

        BufferedWriter output=null;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        String newshow=new String(showName);
        String fileName="texttumblr"+id+newshow.trim().replaceAll(" ","").replaceAll("'","")+".tsv";
        try {
            File file = new File(fileDirectory+fileName);
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file));
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String table=new String(showName);
            table=table.trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'", "");
            String query = "select distinct text, blogName,followers,posturl  from SHOW_TUMBLR_"+table+"  where show_name=? and type=? and  created_on >=? and created_on<=? order by followers desc limit 20";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,showName);
            preparedStatement.setString(2,"text");
            preparedStatement.setString(3, bottomtime);
            preparedStatement.setString(4, uppertime);


            rs = preparedStatement.executeQuery();

            List<String> embedCodeList=new ArrayList<String>();
            //STEP 5: Extract data from result set
            while (rs.next()) {
                embedCodeList.add(rs.getString("text"));
                output.write(rs.getString("blogName")+"break"+rs.getString("followers")+"break"+rs.getString("posturl"));
                output.newLine();
            }
            if(!embedCodeList.isEmpty())
            {
                output.write("photos");
                output.newLine();
                for(String code : embedCodeList)
                {
                    output.write(code);
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

    public String loadAudioPosts(String showName,String bottomtime,String uppertime,String id) throws Exception{

        BufferedWriter output=null;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        String newshow=new String(showName);
        String fileName="audiotumblr"+id+newshow.trim().replaceAll(" ","").replaceAll("'","")+".tsv";
        try {
            File file = new File(fileDirectory+fileName);
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file));
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String table=new String(showName);
            table=table.trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'","");
            String query = "select distinct embedCode, blogName,followers,posturl from SHOW_TUMBLR_"+table+" where show_name=? and type=? and  created_on >=? and created_on<=? order by followers desc limit 15";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,showName);
            preparedStatement.setString(2,"audio");
            preparedStatement.setString(3, bottomtime);
            preparedStatement.setString(4, uppertime);

            rs = preparedStatement.executeQuery();
            int count=1;
            //STEP 5: Extract data from result set
            List<String> embedCodeList=new ArrayList<String>();
            //STEP 5: Extract data from result set
            while (rs.next()) {
                embedCodeList.add(rs.getString("embedCode"));
                output.write(rs.getString("blogName")+"break"+rs.getString("followers")+"break"+rs.getString("posturl"));
                output.newLine();
            }
            if(!embedCodeList.isEmpty())
            {
                output.write("photos");
                output.newLine();
                for(String code : embedCodeList)
                {
                    output.write(code);
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


    public String loadGRaphs(String showName,String bottomtime,String uppertime,String id) throws Exception{

        BufferedWriter output=null;
        BufferedWriter statsOutput=null;

        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        String newshow=new String(showName);
        String fileName="graphtumblr"+id+newshow.trim().replaceAll(" ","").replaceAll("'","")+".tsv";
        String newFileName="statstumblr"+id+newshow.trim().replaceAll(" ","").replaceAll("'","")+".tsv";
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
            table=table.trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'", "");
            String query = "select type,count,created_on  from SHOW_COUNT_"+table+" where   socialType=? and created_on >=? and created_on<=? order by created_on asc";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,"tumblr");
            preparedStatement.setString(2,bottomtime);
            preparedStatement.setString(3,uppertime);
            rs = preparedStatement.executeQuery();
            int count=1;
            //STEP 5: Extract data from result set
            output.write("date"+"\t"+"TO"+"\t"+"V"+"\t"+"A"+"\t"+"TE"+"\t"+"P");
            int newcount=0;
            String oldCreate="";
            int videCount=0;
            int audioCount=0;
            int textCount=0;
            int photoCount=0;
            int totalCount=0;
            int rowCount=0;
            int inlopp=0;
            System.out.println(rs.getRow()+" rrow");
            while (rs.next()) {
                System.out.println("comingnngngngn");
                inlopp=1;
                String create = rs.getString("created_on");
                if(!oldCreate.equals(create)) {
                    System.out.println("turning");
                    if(!oldCreate.equals("")) {
                        output.write(oldCreate.replaceAll("00:00:00.0", "").replaceAll(" ", "").replaceAll("-", "") + "\t" + totalCount + "\t" + videCount + "\t" + audioCount + "\t" + textCount + "\t" +
                                photoCount);
                        statsOutput.write( oldCreate.replaceAll("00:00:00.0", "").replaceAll(" ", "").replaceAll("-", "")+"break"+totalCount + "break" + videCount + "break" + audioCount + "break" + textCount + "break" +
                                photoCount);
                        statsOutput.newLine();

                        videCount = 0;
                        audioCount = 0;
                        textCount = 0;
                        photoCount = 0;
                        totalCount = 0;
                    }
                    oldCreate=create;
                    output.newLine();
                }

                String type = rs.getString("type");
                if(type.equals("video"))
                {
                    videCount=Integer.parseInt(rs.getString("count"));
                    totalCount+=videCount;
                }
                if(type.equals("audio"))
                {
                    audioCount=Integer.parseInt(rs.getString("count"));
                    totalCount+=audioCount;
                }
                if(type.equals("photo"))
                {
                    photoCount=Integer.parseInt(rs.getString("count"));
                    totalCount+=photoCount;
                }
                if(type.equals("text") || type.equals("quote"))
                {
                    textCount=Integer.parseInt(rs.getString("count"));
                    totalCount+=textCount;
                }
                //                if(newcount>30 && textCount!=videCount && videCount!=photoCount && photoCount!=audioCount && textCount>0&&
//                        photoCount>0 && videCount>0)
//                    break;

            }
            if(inlopp==1)
            {
                output.write(oldCreate.replaceAll("00:00:00.0", "").replaceAll(" ", "").replaceAll("-", "") + "\t" + totalCount + "\t" + videCount + "\t" + audioCount + "\t" + textCount + "\t" +
                        photoCount);
                statsOutput.write( oldCreate.replaceAll("00:00:00.0", "").replaceAll(" ", "").replaceAll("-", "")+"break"+totalCount + "break" + videCount + "break" + audioCount + "break" + textCount + "break" +
                        photoCount);
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


}
