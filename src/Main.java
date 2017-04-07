/**
 * Created by walterad on 7-4-2017.
 */
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String argv[]) {
        try {
            String filepath = "data.xml";
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filepath);

            // Get the root element
            Node company = doc.getFirstChild();

            // Get the staff element by tag name directly
            Node staff = doc.getElementsByTagName("staff").item(0);
            // loop the staff child node
            NodeList list = staff.getChildNodes();

            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);

                // get the salary element, and update the value
                if ("salary".equals(node.getNodeName())) {
                    String xmlVal = node.getTextContent();
                    StringBuilder numberChange = new StringBuilder(xmlVal);

                    //get the 18 number of the list
                    int getNum = Integer.parseInt(xmlVal.substring(18, 19));
                    char number = (char)(getNum + '0');
                    number++;

                    numberChange.setCharAt(18, number);

//                    System.out.println(getNum);
//                    System.out.println(number);

                    node.setTextContent(String.valueOf(numberChange));
                }
            }

            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filepath));
            transformer.transform(source, result);

            System.out.println("Done");

        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SAXException sae) {
            sae.printStackTrace();
        }
    }
    public static String[] getSubStrings(String string){
        List<String> result = new ArrayList<String>();
            result.add(string);
        return result.toArray(new String[result.size()]);
    }
}