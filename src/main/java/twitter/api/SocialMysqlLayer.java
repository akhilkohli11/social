package twitter.api;

import com.google.common.primitives.Ints;

import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class SocialMysqlLayer {

    DateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
    public void showPostiveTweets(String showName) throws Exception{
        BufferedWriter output=null;
        try {
            String newshow=new String(showName);
            File file = new File("/Users/akohli/IdeaProjects/app/files/"+newshow.trim().replaceAll(" ","").replaceAll("'","")+"positive");
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
            //STEP 5: Extract data from result set
            while (rs.next()) {
                String tweet = rs.getString("tweet");
                String show = rs.getString("show_name");
                int score = Integer.parseInt(rs.getString("sentimentalScore"));
                // System.out.println(show+"      "+showName);

                    if(show.trim().equals(showName.trim()) && score>1) {
                        count++;
                        output.write(tweet);
                        output.newLine();
                    }

                }




            System.out.println(count +"  "+showName);
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
//        return null;



    }

    public void showNegativeTweets(String showName) throws Exception{

        BufferedWriter output=null;
        try {
            String newshow=new String(showName);
            File file = new File("/Users/akohli/IdeaProjects/app/files/"+newshow.trim().replaceAll(" ","").replaceAll("'","")+"negative");
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
            //STEP 5: Extract data from result set
            while (rs.next()) {
                String tweet = rs.getString("tweet");
                String show = rs.getString("show_name");
                int score = Integer.parseInt(rs.getString("sentimentalScore"));
                // System.out.println(show+"      "+showName);

                if(show.trim().equals(showName.trim()) && score<1) {
                    count++;
                    output.write(tweet);
                    output.newLine();
                }

            }




            System.out.println(count +"  "+showName);
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

    public void showNeutralTweets(String showName) throws Exception{

        BufferedWriter output=null;
        try {
            String newshow=new String(showName);
            File file = new File("/Users/akohli/IdeaProjects/app/files/"+newshow.trim().replaceAll(" ","").replaceAll("'","")+"neutral");
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
            //STEP 5: Extract data from result set
            while (rs.next()) {
                String tweet = rs.getString("tweet");
                String show = rs.getString("show_name");
                int score = Integer.parseInt(rs.getString("sentimentalScore"));
                // System.out.println(show+"      "+showName);

                if(show.trim().equals(showName.trim()) && score==1) {
                    count++;
                    output.write(tweet);
                    output.newLine();
                }

            }




            System.out.println(count +"  "+showName);
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

    public List<String> readTweets(String showName) throws Exception {
        BufferedWriter output=null;
        try {
            String newshow=new String(showName);
            File file = new File("/Users/akohli/IdeaProjects/app/files/"+newshow.trim().replaceAll(" ","").replaceAll("'",""));
            file.createNewFile();
            output = new BufferedWriter(new FileWriter(file));
            Class.forName(jdbcDriverStr);
            connection = DriverManager.getConnection(jdbcURL);
            statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select tweet,show_name from SHOW_TWEET ");
            int count=1;
            //STEP 5: Extract data from result set
            while (rs.next()) {
                String tweet = rs.getString("tweet");
                String show = rs.getString("show_name");
               // System.out.println(show+"      "+showName);
                if(show.trim().equals(showName.trim())) {
                    count++;
                    output.write(tweet);
                    output.newLine();
                }
            }
            System.out.println(count +"  "+showName);
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


    public void showSentimentalStats(String showName) throws Exception{

        BufferedWriter output=null;
        try {
            String newshow=new String(showName);
            File file = new File("/Users/akohli/IdeaProjects/app/files/"+newshow.trim().replaceAll(" ","").replaceAll("'","")+"sentiment");
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
            //STEP 5: Extract data from result set
            while (rs.next()) {
                String tweet = rs.getString("tweet");
                String show = rs.getString("show_name");
                int score = Integer.parseInt(rs.getString("sentimentalScore"));
                // System.out.println(show+"      "+showName);
                if(show.trim().equals(showName.trim())) {
                    count++;
//                    output.write(tweet);
//                    output.newLine();
                    if(score>1)
                        postiveCount++;
                    if(score==1)
                        neutralcount++;
                    if(score<1)
                        negativecount++;

                }


            }
            output.write(String.valueOf(postiveCount));
            output.newLine();
            output.write(String.valueOf(neutralcount));
            output.newLine();
            output.write(String.valueOf(negativecount));
            System.out.println(count +"  "+showName);
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
//        return null;


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
