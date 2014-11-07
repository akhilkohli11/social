package twitter.api;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by akohli on 11/6/14.
 */
public class InitializePopularDocuments {

    public static void  init() throws Exception{

        BufferedReader br = null;

        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader("/tmp/okbro"));
            int count=1;
            //  socialMysqlLayer.readData();
            while ((sCurrentLine = br.readLine()) != null) {

                String[] buffer= StringUtils.split(sCurrentLine, "#", 2);
                if(buffer.length>1) {
                    String[] hashtag = ("#" + buffer[1].trim()).split("#");
                    CloudSolrPersistenceLayer.getInstance().loadPopularDocuments(buffer[0].trim(),hashtag);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
}
