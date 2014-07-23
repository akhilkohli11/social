
package twitter.api;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateFiles {
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_URL = "jdbc:mysql://localhost/social?"
            + "user=root";
    public static void main(String args[]) throws Exception{
        SocialMysqlLayer socialMysqlLayer = new SocialMysqlLayer(MYSQL_DRIVER, MYSQL_URL);
        BufferedReader br = null;

        try {

            String sCurrentLine;
            List<String> showNames = new ArrayList<String>();

        }

        finally {

        }
        }
}

