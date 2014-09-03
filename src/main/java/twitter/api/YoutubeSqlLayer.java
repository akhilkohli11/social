package twitter.api;

import com.google.api.client.util.DateTime;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by akohli on 8/25/14.
 */
public class YoutubeSqlLayer {
    private final String jdbcDriverStr;
    private final String jdbcURL;

    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String fileDirectory="/usr/local/apache-tomcat-7.0.47/webapps/examples/";
   // String fileDirectory="/Library/Tomcat/webapps/examples/";


    private PreparedStatement preparedStatement;
    public YoutubeSqlLayer(String jdbcDriverStr, String jdbcURL) {
        this.jdbcDriverStr = jdbcDriverStr;
        this.jdbcURL = jdbcURL;
    }


    public  void populateYoutubeData(String id,String showName,String title,String official,int likes,
                                     int dislikes,int views,int comments,String embedCodeVideo,
                                  String embedCodePic,String tag,DateTime createdAt,String channel,String link) throws Exception {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs;
        PreparedStatement preparedStatement;
          try {
              String table=new String(showName);
              table=table.trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'","");
              Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            System.out.println("youtube "+showName+ table+"    "+createdAt+"   "+new java.sql.Date(createdAt.getValue()));
            preparedStatement = connection.prepareStatement("insert into SHOW_YOUTUBE_"+table+"(id," +
                    "show_name,title,official,likes,dislikes,views,comments,embedCodeVideo,embedCodePic," +
                    "tag,created_on,channel,link) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, showName);
            preparedStatement.setString(3, title);
            preparedStatement.setString(4, official);
            preparedStatement.setInt(5, likes);
            preparedStatement.setInt(6, dislikes);
            preparedStatement.setInt(7, views);
            preparedStatement.setInt(8, comments);
            preparedStatement.setString(9, embedCodeVideo);
            preparedStatement.setString(10, embedCodePic);
            preparedStatement.setString(11, tag);
            preparedStatement.setDate(12, new java.sql.Date(createdAt.getValue()));
            preparedStatement.setString(13, channel);
            preparedStatement.setString(14, link);



            preparedStatement.executeUpdate();
        } catch (SQLException se) {
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


    public void loadComparision(String[] showNames,  String bottomtime,String uppertime, String id) throws Exception{


        BufferedWriter statsOutput=null;

        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        String newFileName="compareyoutube"+id+".tsv";
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
                preparedStatement.setString(1, "youtube");
                preparedStatement.setString(2, bottomtime);
                preparedStatement.setString(3, uppertime);
                rs = preparedStatement.executeQuery();
                int count = 1;
                //STEP 5: Extract data from result set
                int newcount = 0;
                String oldCreate = "";
                int views=0;
                int comments=0;
                int likes=0;
                int dislikes=0;
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
                            statsOutput.write(oldCreate.replaceAll("00:00:00.0", "").replaceAll(" ", "").replaceAll("-", "") + "break" + views + "break" + likes + "break" + dislikes + "break" + comments);
                            statsOutput.newLine();

                            views = 0;
                            comments = 0;
                            likes = 0;
                            dislikes = 0;

                        }
                        oldCreate = create;
                    }

                    String type = rs.getString("type");
                    if (type.equals("views")) {
                        views = Integer.parseInt(rs.getString("count"));
                    }
                    if (type.equals("likes")) {
                        likes= Integer.parseInt(rs.getString("count"));
                    }
                    if (type.equals("dislikes")) {
                        dislikes = Integer.parseInt(rs.getString("count"));
                    }
                    if (type.equals("comments")) {
                        comments = Integer.parseInt(rs.getString("count"));
                    }

                    //                if(newcount>30 && textCount!=videCount && videCount!=photoCount && photoCount!=audioCount && textCount>0&&
//                        photoCount>0 && videCount>0)
//                    break;

                }
                if (inlopp == 1) {

                    statsOutput.write(oldCreate.replaceAll("00:00:00.0", "").replaceAll(" ", "").replaceAll("-", "") + "break" + views + "break" + likes + "break" + dislikes + "break" + comments );
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

    public void loadComparisionGraphTumblr(String[] showNames,  String bottomtime,String uppertime, String id) throws Exception{
        BufferedWriter statsOutput=null;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        String newFileName="compareyoutubegraph"+id+".tsv";
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
                preparedStatement.setString(1, "youtube");
                preparedStatement.setString(2, bottomtime);
                preparedStatement.setString(3, uppertime);
                preparedStatement.setString(4, "views");
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


    public Map<String, Integer> getViewsForShow(java.util.Date date) {
        Map<String,Integer> showNameToCount=new HashMap<String, Integer>();
        Set<String> shows=TwitterDataRetriever.getShows();
        for(String show : shows)
        {
            int count=getCountForType(show, date, "views");
            showNameToCount.put(show,count);
        }
        return showNameToCount;
    }

    public Map<String, Integer> getLikesForShow(java.util.Date date) {
        Map<String,Integer> showNameToCount=new HashMap<String, Integer>();
        Set<String> shows=TwitterDataRetriever.getShows();
        for(String show : shows)
        {
            int count=getCountForType(show, date, "likes");
            showNameToCount.put(show,count);
        }
        return showNameToCount;
    }

    public Map<String, Integer> getCommentsForShow(java.util.Date date) {
        Map<String,Integer> showNameToCount=new HashMap<String, Integer>();
        Set<String> shows=TwitterDataRetriever.getShows();
        for(String show : shows)
        {
            int count=getCountForType(show, date, "comments");
            showNameToCount.put(show,count);
        }
        return showNameToCount;
    }

    public Map<String, Integer> getDislikesForShow(java.util.Date date) {
        Map<String,Integer> showNameToCount=new HashMap<String, Integer>();
        Set<String> shows=TwitterDataRetriever.getShows();
        for(String show : shows)
        {
            int count=getCountForType(show, date, "dislikes");
            showNameToCount.put(show,count);
        }
        return showNameToCount;    }




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
            String query = "select "+type+"  from SHOW_YOUTUBE_"+table+" where created_on =?";
            System.out.println(query + new java.sql.Date(date.getTime()));
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, new java.sql.Date(date.getTime()));
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String totcount = rs.getString(type);
                count+=Integer.parseInt(totcount);
            }
            System.out.println(count);
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

    public void loadBarGraph(String[] showNames, String bottomtime,String uppertime, String id) throws Exception{
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        String csvFileName="compareyoutubegraph"+id+".csv";
        BufferedWriter csvOutput=null;

        try {

            File csvFile = new File(fileDirectory + csvFileName);
            csvFile.createNewFile();

            csvOutput = new BufferedWriter(new FileWriter(csvFile));

            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            int loopcount = 0;
            Map<String, Integer> datahsowCountMap = new LinkedHashMap<String, Integer>();
            for (String show : showNames) {
                String showName = TwitterDataRetriever.getShowToTableName().get(show.toLowerCase());
                loopcount++;

                String table = new String(showName);
                table = table.trim().toLowerCase().replaceAll(" ", "").replaceAll("\"", "").replaceAll("'", "");
                String query = "select count from SHOW_COUNT_" + table + " where   socialType=? and created_on >=? and created_on<=? and type=? order by created_on asc";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, "youtube");
                preparedStatement.setString(2, bottomtime);
                preparedStatement.setString(3, uppertime);
                preparedStatement.setString(4, "views");
                rs = preparedStatement.executeQuery();
                int count = 1;
                //STEP 5: Extract data from result set
                int newcount = 0;
                String oldCreate = "";
                int total = 0;
                while (rs.next()) {
                    total += Integer.parseInt(rs.getString("count"));

                    datahsowCountMap.put(show, total);

                }


            }

            csvOutput.write("State,");
            for (String show : showNames) {
                csvOutput.write(show + ",");
            }
            csvOutput.newLine();


            csvOutput.write("Total" + ",");
            for (String show : showNames) {

                if (datahsowCountMap.containsKey(show)) {
                    csvOutput.write(datahsowCountMap.get(show) + ",");

                } else {
                    csvOutput.write(0 + ",");
                }
            }
            csvOutput.newLine();



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
            csvOutput.close();

        }
    }

    public void loadMostViewed(String showName,String bottomtime,String uppertime,String id,String type) throws Exception{

        BufferedWriter output=null;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        String newshow=new String(showName);
        String fileName=type+"youtube"+id+newshow.trim().replaceAll(" ","").replaceAll("'","")+".tsv";
        try {
            File file = new File(fileDirectory+fileName);
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file));
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            String table=new String(showName);
            table=table.trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'","");
            String query = "select distinct embedCodeVideo, title,likes,dislikes,views,comments from SHOW_YOUTUBE_"+table+" where show_name=? and   created_on >=? and created_on<=? and title LIKE ? order by "+type+" desc limit 10";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,showName);
            preparedStatement.setString(2, bottomtime);
            preparedStatement.setString(3, uppertime);
            preparedStatement.setString(4, "%" + showName + "%");

            rs = preparedStatement.executeQuery();
            int count=1;
            List<String> embedCodeList=new ArrayList<String>();
            //STEP 5: Extract data from result set
            while (rs.next()) {
                embedCodeList.add(rs.getString("embedCodeVideo"));
                output.write(rs.getString("title")+"break"+rs.getString("views")+"break"+rs.getString("likes")+"break"+rs.getString("dislikes")+"break"
                                +rs.getString("comments")+"break"+rs.getString("embedCodeVideo")
                );
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
