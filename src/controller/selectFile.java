package controller;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

/**
 * Created by Adriel on 4/9/2017.
 */
public class selectFile extends JFrame {
    public  String filepath;
    public Document doc;


    public selectFile() {
        super("File Chooser Test Frame");
        setSize(260, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Container c = getContentPane();
        c.setLayout(new FlowLayout());

        JButton selectButton = new JButton("Select File");
        JButton runButton = new JButton("Run");
        final JLabel statusbar = new JLabel("select your file");

        // Create a file chooser that opens up as an Open dialog
        selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                JFileChooser chooser = new JFileChooser();
                chooser.setMultiSelectionEnabled(true);
                int option = chooser.showOpenDialog(selectFile.this);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File[] sf = chooser.getSelectedFiles();
                    String filelist = "nothing";
                    if (sf.length > 0) filelist = sf[0].getName();
                    for (int i = 1; i < sf.length; i++) {
                        filelist += ", " + sf[i].getName();
                    }
                    statusbar.setText("You chose " + filelist);

                    try {
                        filepath = filelist;
                        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                        doc = docBuilder.parse(filepath);

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
                                int getNum = Integer.parseInt(xmlVal.substring(18, 20));
                                Integer IdNum = getNum;
                                IdNum = IdNum + 1;

                                numberChange.setCharAt(18, (char)(IdNum + '0'));

                                System.out.println(getNum);
                                System.out.println(IdNum);

                                node.setTextContent("000000000000000000"+IdNum +"0000000000000000000000000");
                            }
                        }

                    } catch (ParserConfigurationException | IOException | SAXException pce) {
                        pce.printStackTrace();
                    }
                }
                else {
                    statusbar.setText("You canceled.");
                }
            }
        });

        // Create a file chooser that opens up as a Save dialog
        runButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {

                try {
                    // write the content into xml file
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(new File(filepath));
                    if (result == null){
                        System.out.println("no file select");
                    }
                    transformer.transform(source, result);

                    System.out.println("Done");
                    statusbar.setText("Done");

                } catch (TransformerException e) {
                    e.printStackTrace();
                    statusbar.setText("Error");
                }
                statusbar.setText("Done");
            }
        });

        c.add(selectButton);
        c.add(runButton);
        c.add(statusbar);
    }

}
