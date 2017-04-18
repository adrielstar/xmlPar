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
import java.util.ArrayList;

/**
 * Created by Adriel on 4/9/2017.
 */
public class selectFile extends JFrame {
    private String filepath;
    private Document doc;
    private StringBuilder filelist = new StringBuilder("nothing");

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
                File workingDirectory = new File(System.getProperty("user.dir"));
                chooser.setCurrentDirectory(workingDirectory);
                chooser.setMultiSelectionEnabled(true);
                int option = chooser.showOpenDialog(selectFile.this);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File[] sf = chooser.getSelectedFiles();

                    if (sf.length > 0) filelist = new StringBuilder(sf[0].getName());
                    for (int i = 1; i < sf.length; i++) {
                        filelist.append(",").append(sf[i].getName());
                    }
                    statusbar.setText("You chose " + filelist);

                    ArrayList<String> dataFile = openMultiple(filelist.toString());
                    for (String aDataFile : dataFile) {
                        System.out.println(aDataFile);
                        try {
                            filepath = aDataFile;
                            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                            doc = docBuilder.parse(aDataFile);

                            Document docs = documentUpload(aDataFile);

                            // Get the staff element by tag name directly
                            Node staff = doc.getElementsByTagName("staff").item(0);
                            // loop the staff child node
                            NodeList list = staff.getChildNodes();

                            for (int i = 0; i < list.getLength(); i++) {
                                Node node = list.item(i);

                                // get the salary element, and update the value
                                if ("salary".equals(node.getNodeName())) {
                                    String xmlVal = node.getTextContent();
                                    //get the 18 number of the list
                                    int replaceChart = Integer.parseInt(xmlVal.substring(18, 22));
                                    int counter = replaceChart + 1;
                                    StringBuilder stringBuilder = new StringBuilder(xmlVal);
                                    stringBuilder.replace(18, 22, String.format("%04d", counter));
                                    String newMyName = stringBuilder.toString();

                                    System.out.println(newMyName);
                                    node.setTextContent(newMyName);
                                }
                            }

                        } catch (ParserConfigurationException | IOException | SAXException pce) {
                            pce.printStackTrace();
                        }
                }} else {
                    statusbar.setText("You canceled.");
                }
            }
        });

        // Create a file chooser that opens up as a Save dialog
        runButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                ArrayList<String> dataFile = openMultiple(filelist.toString());
                for (String aDataFile : dataFile) {
                try {
                    // write the content into xml file
                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(new File(aDataFile));
                    if (result == null) {
                        System.out.println("no file select");
                    }
                    transformer.transform(source, result);

                    System.out.println(aDataFile +" Was successfully Done");
                    statusbar.setText(dataFile.size() +"Was successfully Done");

                } catch (TransformerException e) {
                    e.printStackTrace();
                    statusbar.setText("Error");
                }}
            }
        });

        c.add(selectButton);
        c.add(runButton);
        c.add(statusbar);
    }

    private ArrayList<String> openMultiple(String file) {
        String[] fields = file.split(",");
        ArrayList<String> myData = new ArrayList<>();
        for (String field : fields) {
            file = field;
            myData.add(file);
        }
//        System.out.println(myData);
        return myData;
    }

    private Document documentUpload(String file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        doc = docBuilder.parse(file);
        return doc;
    }
}
