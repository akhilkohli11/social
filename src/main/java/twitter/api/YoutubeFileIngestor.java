package twitter.api;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;


import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by akohli on 11/16/14.
 */
public class YoutubeFileIngestor  {


    private void readInputFile() throws Exception{

        BufferedReader br = null;

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader("/tmp/socialingestor"));

            while ((sCurrentLine = br.readLine()) != null) {

                String[] buffer= StringUtils.split(sCurrentLine, "###", 8);
//                for(int i=0;i<buffer.length;i++)
//                {
//                    System.out.print(buffer[i] + "       ");
//                }
                System.out.println(buffer[0].trim()+ "       "+buffer[1].trim()
                        + "       "+buffer[2].trim()+ "       "+buffer[7].trim()+
                         "       "+buffer[3].trim());

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String args[]) throws Exception
    {
        YoutubeFileIngestor youtubeFileInputSocialIngestor=new YoutubeFileIngestor();
        youtubeFileInputSocialIngestor.readInputFile();
    }
}
