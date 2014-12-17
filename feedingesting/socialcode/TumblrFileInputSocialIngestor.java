package org.zap2it.ingester.social;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akohli on 11/16/14.
 */
public class TumblrFileInputSocialIngestor implements SocialIngestor {
    @Override
    public void ingestDataFromSource() throws Exception {
        boolean isSeed=true;
        TumblrIngestor.init();
        readInputFile(isSeed);
    }

    private void readInputFile(boolean isSeed) throws Exception{

        BufferedReader br;

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader("/tmp/socialingestor"));
            List<Tumblr> tumblrList=new ArrayList<Tumblr>();
            while ((sCurrentLine = br.readLine()) != null) {

                String[] buffer= StringUtils.split(sCurrentLine, "###", 9);
                boolean isGeneric=buffer[3].trim().toLowerCase().equals("yes")?true:false;
                tumblrList.add(new Tumblr(buffer[0].trim(),buffer[1].trim(),buffer[1].trim()+" tv",buffer[5].trim(),
                        isGeneric,buffer[6].trim(),buffer[8].trim()));
                tumblrList.add(new Tumblr(buffer[0].trim(),buffer[1].trim(),buffer[1].trim()+" show",buffer[5].trim(),
                        isGeneric,buffer[6].trim(),buffer[8].trim()));
                tumblrList.add(new Tumblr(buffer[0].trim(),buffer[1].trim(),buffer[1].trim()+" series",buffer[5].trim(),
                        isGeneric,buffer[6].trim(),buffer[8].trim()));
                tumblrList.add(new Tumblr(buffer[0].trim(),buffer[1].trim(),buffer[2].trim(),buffer[5].trim(),
                        false,buffer[6].trim(),buffer[8].trim()));
                tumblrList.add(new Tumblr(buffer[0].trim(),buffer[1].trim(),buffer[1].trim(),buffer[5].trim(),
                        false,buffer[6].trim(),buffer[8].trim()));

            }
             if(isSeed) {
                TumblrIngestor.loadTumblr(tumblrList);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
