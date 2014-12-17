package org.zap2it.ingester.social;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by akohli on 11/25/14.
 */
public class TestToBeDeleted {
    public static void main(String args[])
    {

        BufferedReader br = null;

        try {

            String sCurrentLine;

            br = new BufferedReader(new FileReader("/tmp/twitterhandles"));
            List<Youtube> youtubeList = new ArrayList<Youtube>();
            while ((sCurrentLine = br.readLine()) != null) {

                String[] buffer = StringUtils.split(sCurrentLine, "@", 2);
                boolean isGeneric = buffer[3].trim().toLowerCase().equals("yes") ? true : false;
                youtubeList.add(new Youtube(buffer[0].trim(), buffer[1].trim(), buffer[2].trim(), buffer[7].trim(),
                        isGeneric));

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
