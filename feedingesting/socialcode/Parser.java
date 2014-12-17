package org.zap2it.ingester.social;

import javax.swing.text.html.HTMLEditorKit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by akohli on 11/20/14.
 */
public class Parser {
        public static void main(String args[]) throws Exception {


            Pattern p=null;
            Matcher m= null;
            String txt=    "<p>I’m stuck in a thread on G+ where people are complaining about Fox, which I can normally get behind, but in this case they’re upset about Hieroglyph being canceled before it even aired. </p>\n<p><a href=\"http://www.imdb.com/title/tt3416922/\" target=\"_blank\">Hieroglyph</a>: An ancient Egyptian fantasy show.</p>\n<blockquote>\n<p>Set in the world of ancient Egypt, <em>Hieroglyph</em> follows a notorious thief who is plucked from prison to serve the Pharaoh, forcing him to navigate palace intrigue, seductive concubines, criminal underbellies and divine sorcerers, as he races to stop the downfall of one of history’s greatest civilizations. (<a href=\"http://www.hollywoodreporter.com/live-feed/fox-pulls-plug-hieroglyph-715938\" target=\"_blank\">X</a>)</p>\n</blockquote>\n<p>Good premise. I’d want to watch it. </p>\n<p>Extremely white casting. </p>\n<p>I’m not sure how you can have a token black person on a show like this, but they somehow managed.</p>\n<p><img src=\"https://31.media.tumblr.com/c59cdfb0ac8d8cda52b842405ab1569f/tumblr_inline_n830s3fTka1qkt98p.jpg\"/></p>\n\n<p>I don’t blame Fox one bit. Well, except that they should have done the casting right to begin with. Though they say it was <a href=\"http://insidetv.ew.com/2014/06/30/fox-cancels-hieroglyph/\" target=\"_blank\">canceled for creative reasons</a>.</p>\n<blockquote>\n<p><span>Only the first episode had been filmed on the pricey </span><em>Hieroglyph</em><span> before writers began breaking scripts and stories, which weren’t meeting a certain level creatively, for the rest of the season. Filming on the remaining 12 episodes was expected to start later this year. </span><em>Hieroglyph</em><span> was expected to premiere on Fox in early 2015.</span></p>\n</blockquote>\n<p><span>It’s great to know they have their priorities straight.</span></p>";
            p= Pattern.compile(".*<img[^>]*src=\"([^\"]*)",Pattern.CASE_INSENSITIVE);
            m= p.matcher(txt);
            String word0=null;
            while (m.find())
            {
                word0=m.group(1);
                System.out.println(word0.toString());
            }



        }
}
