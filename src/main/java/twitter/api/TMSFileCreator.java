package twitter.api;

/**
* Created by akohli on 7/7/14.
*/
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TMSFileCreator {
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_URL = "jdbc:mysql://localhost/social?"
            + "user=root";
    static String fileDirectory="/Users/akohli/akhilsocial/newtiwtterui/tumblerfile/";
    static Multimap<String,String> showToPhpt= HashMultimap.create();

    public static void main(String args[]) throws Exception
    {
        SocialMysqlLayer socialMysqlLayer=new SocialMysqlLayer(MYSQL_DRIVER,MYSQL_URL);
        TumblrSqlLayer tumblrSqlLayer=new TumblrSqlLayer(MYSQL_DRIVER,MYSQL_URL);;

        LoadApp.init();
        try {

            File fXmlFile = new File("/Users/akohli/Downloads/on_tv_showcards-db_20140616.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("showcard");

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document newDocument = docBuilder.newDocument();
            Element rootElement = newDocument.createElement("company");
            newDocument.appendChild(rootElement);

            System.out.println("----------------------------"+ nList.getLength());
            Set<String> shows=TwitterDataRetriever.getShows();
            shows.remove("Power");
            int count=0;
            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) nNode;
                    Element newElement;
                    // create a deep clone for the target document:

                    //   System.out.println("Stock Symbol: " + getValue("title", element,0));
                    String title=getValue("title", element,0);
                    String newShow=null;
                    for(String show :shows)
                    {
                        String  selectedShow=new String(show);
                        String newTitle=new String(title);
                        String parseShow=selectedShow.trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'","");
                        String parseTitle=newTitle.trim().toLowerCase().replaceAll(" ","").replaceAll("\"","").replaceAll("'", "");
                        if(parseShow.equals(parseTitle)) {
                            newShow=selectedShow;
                            break;
                        }

                    }
                    if(newShow!=null)
                    {
                        loadPhotos( Arrays.asList(getValue("title", element,0)),newShow,"tmstitle");
                        loadPhotos( Arrays.asList(getValue("desc", element,0)),newShow,"tmsdesc");
                        loadPhotos( Arrays.asList(getValue("genre", element,0)),newShow,"tmsgenre");
                        loadPhotos( Arrays.asList(getResourcesValue("resources", element, 0)),newShow,"tmsuri");
                        for(String photo : getPhotos("assets", element, 0))
                        {
                            showToPhpt.put(newShow, photo);
                        }
//
//                        System.out.println("Desc Symbol: " + getValue("desc", element,0));
//                        System.out.println("Genre Symbol: " + getValue("genre", element,0));
//                        System.out.println("uri Symbol: " + getResourcesValue("resources", element, 0));
//                        System.out.println("Photos Symbol: " + getPhotos("assets", element, 0));




                    }
                    //   System.out.println("Stock Symbol: " + getValue("ids", element,0));

                }


            }
            for(String key :showToPhpt.keySet())
            {
                loadPhotos(showToPhpt.get(key),key,"tmsphoto");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<String> getPhotos(String assets, Element element, int i) {
        List<String> photos=new ArrayList<String>();
        String uri=null;
        for (int temp = 0; temp < element.getElementsByTagName("assets").getLength(); temp++) {
            Node nNode = element.getElementsByTagName("assets").item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element newle = (Element) nNode;
                for (int newtemp = 0; newtemp < newle.getElementsByTagName("assetGroup").getLength(); newtemp++) {
                    Node newnode = newle.getElementsByTagName("assetGroup").item(newtemp);
                    if (newnode.getNodeType() == Node.ELEMENT_NODE) {
                        Element finalEli = (Element) newnode;
                        String photo=null;
                        for (int lol = 0; lol < finalEli.getElementsByTagName("asset").getLength(); lol++) {

                            Node okok = finalEli.getElementsByTagName("asset").item(lol);
                        if (okok.getNodeType() == Node.ELEMENT_NODE) {
                            Element okfinal = (Element) okok;
                            photo="http://tmsimg.com/assets/"+getValue("URI", okfinal, 0);
                        }

                        }
                        photos.add(photo);

                    }
                }

            }
        }
        return photos;
    }

    private static String getResourcesValue(String tag, Element element, int i) {
        String uri=null;
        for (int temp = 0; temp < element.getElementsByTagName(tag).getLength(); temp++) {
            Node nNode = element.getElementsByTagName(tag).item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element newle = (Element) nNode;
                uri = getValue("URI", newle, 0);
            }
        }
        return uri;
    }

        public  static void loadPhotos(Collection<String> photos,String showName,String fileName) throws Exception{

        BufferedWriter output=null;
        Connection connection=null;
        Statement statement=null;
        ResultSet rs;
        PreparedStatement preparedStatement;
        try {
            String newshow=new String(showName);
            File file = new File(fileDirectory+fileName+newshow.trim().replaceAll(" ","").replaceAll("'","")+".tsv");
           file.createNewFile();
            output = new BufferedWriter(new FileWriter(file));


            //STEP 5: Extract data from result set
            for (String photo :photos) {
                if(photo!=null) {
                    output.write(photo);
                    output.newLine();
                }
            }
            output.close();

            //  getResultSet(resultSet);

        } catch (Exception e) {
            //Handle errors for Class.forName
            e.printStackTrace();
        } finally {
            //finally block used to close resources
            try {
                if (statement != null)
                    connection.close();
            } catch (SQLException se) {
            }// do nothing
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }//end finally try

        }

    }

    private static String getValue(String tag, Element element,int num) {
        if(element!=null) {
            NodeList nodes = element.getElementsByTagName(tag).item(num).getChildNodes();
            Node node = (Node) nodes.item(num);
            return node.getNodeValue();
        }
        return null;

    }

    private static String getDescriptonValue(String tag, Element element,int num) {
        NodeList nodes = element.getElementsByTagName(tag).item(num).getChildNodes();
        Node node = (Node) nodes.item(num);
        return node.getNodeValue();

    }


}

