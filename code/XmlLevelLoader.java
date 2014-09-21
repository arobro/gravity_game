import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.File;
import java.util.*;

/** Class XmlLevelLoader loads the level texture and the maps item coordinates */
 
public class XmlLevelLoader
{
   private static HashMap<String, String> itemList;
    
   public XmlLevelLoader(){
        try {
            itemList = new HashMap<String, String >();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File("files\\levels.xml"));
            NodeList items = doc.getElementsByTagName("item");
    
            itemList.put("levelBackground",((Element)doc.getElementsByTagName("level").item(0)).getAttribute("background"));
    
            for (int i = 0; i < items.getLength(); i++) {  
                Node nNode = items.item(i);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    itemList.put(i+"x", eElement.getElementsByTagName("x").item(0).getTextContent());
                    itemList.put(i+"y", eElement.getElementsByTagName("y").item(0).getTextContent());
                }
            } 
           } catch (Exception e) {
               e.printStackTrace();
           } 
        }
    public int itemGetX(int id) {
         return Integer.parseInt(itemList.get(id + "x"));
    }
    public int  itemGetY(int id) {
         return Integer.parseInt(itemList.get(id + "y"));
    }    
    public String getCustomCommand(String id) {
         return itemList.get( id );
    }        
}