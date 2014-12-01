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
                String[] buffer= StringUtils.split(sCurrentLine, "###", 9);
                boolean isGeneric=buffer[3].trim().toLowerCase().equals("yes")?true:false;
                System.out.println(buffer[0].trim()+"  "+buffer[1].trim()+"  "+buffer[2].trim()+"  "+buffer[5].trim()+"  "+
                        isGeneric+"  "+buffer[8].trim());
                CloudSolrPersistenceLayer.getInstance().loadPopularDocuments(buffer[0].trim());
                SocialObject socialObject=new SocialObject(buffer[0].trim(),buffer[1].trim(),buffer[1].trim()+" tv",buffer[5].trim(),
                        isGeneric,buffer[6].trim());
                CloudSolrPersistenceLayer.getInstance().loadSocialObject(buffer[0].trim(),socialObject);

                count++;
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
