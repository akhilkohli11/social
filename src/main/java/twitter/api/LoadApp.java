
    package twitter.api;

    import org.apache.commons.lang.StringUtils;

    import java.io.BufferedReader;
    import java.io.FileReader;
    import java.io.IOException;

    public class LoadApp {
        public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
        public static final String MYSQL_URL = "jdbc:mysql://localhost/social?"
                + "user=root";
        public static void init() throws Exception{

            BufferedReader br = null;

            try {

                String sCurrentLine;
                SocialMysqlLayer socialMysqlLayer=new SocialMysqlLayer(MYSQL_DRIVER,MYSQL_URL);

                br = new BufferedReader(new FileReader("/tmp/showsfinal.txt"));
                int count=1;
                //  socialMysqlLayer.readData();
                while ((sCurrentLine = br.readLine()) != null) {

                    String[] buffer= StringUtils.split(sCurrentLine, "@", 2);
                    String[] newbuffer=StringUtils.split(buffer[1],"#",2);
                    String[] finalbuffer=StringUtils.split(newbuffer[1],"@",2);
                    String countString=String.valueOf(count);
                    String caste[]=new String[1000];
                    String hashtag[]=new String[1000];

                    //  populateShowIDToShowName(socialMysqlLayer, countString,buffer[0].trim() , "@"+newbuffer[0].trim(), null, "#"+newbuffer[1].trim());
                    if(finalbuffer.length>1)
                    {
                        caste=("@"+finalbuffer[1].trim()).split(",");
                        hashtag=("#"+finalbuffer[0].trim()).split(",");
                    }
                    else
                    {
                        hashtag=("#"+finalbuffer[0].trim()).split(",");
                    }
                    TwitterDataRetriever.populateShowIDToShowName(buffer[0].trim() ,"@"+newbuffer[0].trim(), caste, hashtag);
                    count++;
                }
                TwitterDataRetriever.getTweets(5000,socialMysqlLayer);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (br != null) br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        }
        private static void populateShowIDToShowName(SocialMysqlLayer socialMysqlLayer,String showID,String showName,String twitterHandle,String casteHandle,String hashtag) throws Exception
        {
            socialMysqlLayer.populateShowIDToShowName(showID,showName,twitterHandle,casteHandle,hashtag);

        }
    }

