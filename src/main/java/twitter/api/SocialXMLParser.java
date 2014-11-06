package twitter.api;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by akohli on 11/3/14.
 */
public class SocialXMLParser {

    public static void main(String args[]) throws Exception
    {
        init();
    }
    public static void init() throws Exception {
        CloudSolrPersistenceLayer.getInstance().init();
        File fXmlFile = new File("/tmp/social.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        //optional, but recommended
        //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
        doc.getDocumentElement().normalize();

        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

        NodeList nList = doc.getElementsByTagName("series");

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

        int count = 0;
        for (int temp = 0; temp < nList.getLength(); temp++) {

            Node nNode = nList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;
                Element newElement;
                // create a deep clone for the target document:

                //   System.out.println("Stock Symbol: " + getValue("title", element,0));
                String title = getValue("title", element, 0);
                String root = getValue("rootId", element, 0);

                System.out.println(title+"  "+root);
                getLinks("links", element,title,root);
            }
        }
    }

    private static String getValue(String tag, Element element, int num) {
        NodeList nodes = element.getElementsByTagName(tag).item(num).getChildNodes();
        Node node = (Node) nodes.item(num);
        return node.getNodeValue();

    }

    private static void getLinks(String tag, Element element,String title,String root) throws Exception{
        Map<String,String> map=new HashMap<String, String>();
        NodeList nodes = element.getElementsByTagName(tag);
        for (int temp = 0; temp < nodes.getLength(); temp++) {
            Node nNode = nodes.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element newlement = (Element) nNode;
                NodeList linknode = element.getElementsByTagName("link");
                for (int newtemp = 0; newtemp < linknode.getLength(); newtemp++) {
                    Node newlinknode = linknode.item(newtemp);
                    if (newlinknode.getNodeType() == Node.ELEMENT_NODE) {
                        Element newlinknodelet = (Element) newlinknode;
                        String host=getValue("host", newlinknodelet, 0);
                        String url=getValue("url", newlinknodelet, 0);
                        System.out.println(host);
                        System.out.println(url);
                        map.put(host,url);
                        CloudSolrPersistenceLayer.getInstance().persist(root,title,map);

                    }
                }
            }
        }
    }
}