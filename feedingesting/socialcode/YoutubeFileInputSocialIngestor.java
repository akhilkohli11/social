package org.zap2it.ingester.social;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by akohli on 11/16/14.
 */
public class YoutubeFileInputSocialIngestor implements SocialIngestor {
    @Override
    public void ingestDataFromSource() throws Exception {
        boolean isSeed=true;
        YoutubeIngestor.init();
        readInputFile(isSeed);
    }

    private void readInputFile(boolean isSeed) throws Exception{

        BufferedReader br = null;

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader("/tmp/socialingestor"));
            List<Youtube> youtubeList=new ArrayList<Youtube>();
            while ((sCurrentLine = br.readLine()) != null) {

                String[] buffer= StringUtils.split(sCurrentLine, "###",8);
                boolean isGeneric=buffer[3].trim().toLowerCase().equals("yes")?true:false;
                youtubeList.add(new Youtube(buffer[0].trim(),buffer[1].trim(),buffer[2].trim(),buffer[7].trim(),
                        isGeneric));

            }
            Date before = new Date();
            Date after =  new org.joda.time.DateTime(before).minusHours(3).toDate();

            if(isSeed) {
                YoutubeIngestor.populate(youtubeList, null, null, "viewCount",isSeed);
                YoutubeIngestor.populate(youtubeList, null, null, "relevance",isSeed);
            }
//            int count=0;
//            while (count++<=4)
//            {
//                before=after;
//                after =  new org.joda.time.DateTime(before).minusHours(24).toDate();
//                YoutubeIngestor.populate(youtubeList,after,before,"relevance",true);
//                YoutubeIngestor.populate(youtubeList,after,before,"viewCount",true);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public static void main(String args[]) throws Exception
    {
        YoutubeIngestor.init();
        YoutubeFileInputSocialIngestor youtubeFileInputSocialIngestor=new YoutubeFileInputSocialIngestor();
        youtubeFileInputSocialIngestor.readInputFile(true);
    }
}
