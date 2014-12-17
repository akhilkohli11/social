package org.zap2it.ingester.social;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akohli on 12/4/14.
 */
public class TwitterFileInputSocialIngestor implements SocialIngestor {
    @Override
    public void ingestDataFromSource() throws Exception {
        boolean isSeed=true;
        readInputFile(isSeed);
    }

    private void readInputFile(boolean isSeed) throws Exception{

        BufferedReader br = null;

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader("/tmp/twitterHandles"));
            List<Twitter> twitterList=new ArrayList<Twitter>();
            while ((sCurrentLine = br.readLine()) != null) {

                String[] buffer= StringUtils.split(sCurrentLine, "###", 3);
                twitterList.add(new Twitter(buffer[0].trim(),buffer[1].trim(),buffer[2].trim()));


            }
            TwitterIngestor twitterIngestor=new TwitterIngestor();
            twitterIngestor.init();
            twitterIngestor.ingestData(twitterList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
