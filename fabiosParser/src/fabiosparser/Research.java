/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fabiosparser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.xpath.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author FabioL
 */
public class Research {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        try {

            XPathFactory xpf = XPathFactory.newInstance();
            XPath xpath = xpf.newXPath();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document markupFile = builder.parse("miccfg.xml");
            markupFile.getDocumentElement().normalize();

            File outputFile = new File("test2.txt");
            try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(outputFile))) {
                NodeList tasks = markupFile.getElementsByTagName("Task");
                
                List<String> test = new ArrayList<>();

                for (int i = 0; i < tasks.getLength(); i++) {
                    Node task = tasks.item(i);

                    Element singleTask = (Element) task;
                    NodeList destinations = singleTask.getElementsByTagName("Destination");

                    if (task.getNodeType() == Node.ELEMENT_NODE) {

                        String destinationHost;
                        String destinationPath;
                        
                        List<String> hostsScenarioI = new ArrayList<>();
                        List<String> hostsScenarioII = new ArrayList<>();
                        
                        List<String> destinationsScenarioI = new ArrayList<>();
                        List<String> destinationsScenarioII = new ArrayList<>();

                        if (destinations.getLength() == 0) {

                            String hostScenarioI = (String) xpath.evaluate("/Settings/Tasks/Task[" + i + "]/For/Destination/@HostID", markupFile, XPathConstants.STRING);
                            String hostScenarioII = (String) xpath.evaluate("/Settings/Tasks/Task[" + i + "]/For/If/When/Destination/@HostID", markupFile, XPathConstants.STRING);

                            String pathScenarioI = (String) xpath.evaluate("/Settings/Tasks/Task[" + i + "]/For/Destination/@Path", markupFile, XPathConstants.STRING);
                            String pathScenarioII = (String) xpath.evaluate("/Settings/Tasks/Task[" + i + "]/For/If/When/Destination/@Path", markupFile, XPathConstants.STRING);
                            
                            
                            test.add(pathScenarioI);

                            if (hostScenarioI == null || "".equals(hostScenarioI)) {
                                fileWriter.write(hostScenarioII + "|" + pathScenarioII + "\n");
                            } else if (hostScenarioII == null || "".equals(hostScenarioII)) {
                                fileWriter.write(hostScenarioI + "|" + pathScenarioI + "\n");
                            }

                        } else {

                            for (int j = 0; j < destinations.getLength(); j++) {
                                Element destinationItem = (Element) destinations.item(j);
                                destinationHost = destinationItem.getAttribute("HostID");
                                destinationPath = destinationItem.getAttribute("Path");

                                fileWriter.write(destinationHost + "," + destinationPath + "\n");
                            }
                        }
                    }
                }
                
                System.out.println(test.size());
                
                for (int j = 0; j < test.size(); j++){
                    
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            e.printStackTrace();
        }
    }
}
