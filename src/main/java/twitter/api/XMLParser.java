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

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class XMLParser {
    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_URL = "jdbc:mysql://localhost/social?"
            + "user=root";
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
                        System.out.println(title+ "     "+newShow);
                        newElement = (Element) rootElement.getOwnerDocument().importNode(element, true);
                        count++;
                        rootElement.appendChild(newElement);
                        Element social =newDocument.createElement("social");
                        newElement.appendChild(social);
                        Element twitter =newDocument.createElement("twitter");
                        social.appendChild(twitter);
                        Element tumblr =newDocument.createElement("tumblr");
                        social.appendChild(tumblr);
                        Map<String,Integer> jsonList=socialMysqlLayer.getTweet(newShow);
                        for(Map.Entry<String,Integer> jsonString :jsonList.entrySet()) {
                            JSONObject o = new JSONObject(jsonString.getKey());
                            String xml = org.json.XML.toString(o);
                            xml = "<tweet>" + xml + "<sentimentalScore>"+jsonString.getValue()+"</sentimentalScore></tweet>";

                            appendXmlFragment(docBuilder, twitter, xml);
                        }

                        List<TumblrObject>  tumblrJsonList=tumblrSqlLayer.getTumblRShow(newShow);
                        for(TumblrObject tumblrObject :tumblrJsonList) {
//                            String embed=tumblrObject.getEmbedCode().replaceAll("webkitAllowFullScreen","")
//                                    .replaceAll("mozallowfullscreen","").replaceAll("allowFullScreen","").
//                                            replaceAll("webkitallowullScreen","").replaceAll("allowfullscreen","").
//                                            replaceAll("mozallowfullscreen","").replaceAll("allowFullScreen","");
                            String xml="";
                            try {
                                JSONObject obj = new JSONObject();
                                obj.put("blog",tumblrObject.getBlogName());
                                obj.put("created", tumblrObject.getCreatedOn());
                                obj.put("embedCode", tumblrObject.getEmbedCode());
                                obj.put("text", tumblrObject.getText());
                                obj.put("followers", tumblrObject.getFollowers());
                                obj.put("lastUpdated", tumblrObject.getLastUpdated() );
                                obj.put("likes", tumblrObject.getLikes());
                                obj.put("sentimentScore", tumblrObject.getSentimentalScore());
                                obj.put("type", tumblrObject.getType());
                                obj.put("url", tumblrObject.getUrl() );
                                obj.put("width", tumblrObject.getWidth());

//                                xml = "<tumblrPost>" +
//                                        "<blog>" + tumblrObject.getBlogName() + "</blog>" +
//                                        "<created>" + tumblrObject.getCreatedOn() + "</created>" +
//                                        "<embedCode>" + null + "</embedCode>" +
//                                        "<text>" + tumblrObject.getText() + "</text>" +
//                                        "<followers>" + tumblrObject.getFollowers() + "</followers>" +
//                                        "<lastUpdated>" + tumblrObject.getLastUpdated() + "</lastUpdated>" +
//                                        "<likes>" + tumblrObject.getLikes() + "</likes>" +
//                                        "<sentimentScore>" + tumblrObject.getSentimentalScore() + "</sentimentScore>" +
//                                        "<type>" + tumblrObject.getType() + "</type>" +
//                                        "<url>" + tumblrObject.getUrl() + "</url>" +
//                                        "<width>" + tumblrObject.getWidth() + "</width>" +
//                                        "</tumblrPost>";
                                 xml = org.json.XML.toString(obj);
                                xml = "<tumblrPost>" + xml + "</tumblrPost>";


                                appendXmlFragment(docBuilder, tumblr, xml);
                            }
                            catch (Exception e)
                            {
                                System.out.println(xml);
                            }
                        }
                    }
                 //   System.out.println("Stock Symbol: " + getValue("ids", element,0));

                }


            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(newDocument);
            StreamResult result = new StreamResult(new File("/Users/akohli/Downloads/a.xml"));

            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);

            transformer.transform(source, result);

            System.out.println("File saved!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void appendXmlFragment(
            DocumentBuilder docBuilder, Node parent,
            String fragment) throws Exception {
        Document doc = parent.getOwnerDocument();
        Node fragmentNode = docBuilder.parse(
                new InputSource(new StringReader(fragment)))
                .getDocumentElement();
        fragmentNode = doc.importNode(fragmentNode, true);
        parent.appendChild(fragmentNode);
    }

    private static String getValue(String tag, Element element,int num) {
        NodeList nodes = element.getElementsByTagName(tag).item(num).getChildNodes();
        Node node = (Node) nodes.item(num);
        return node.getNodeValue();

    }


}

