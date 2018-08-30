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
public class Standard {

    public static void main(String[] args) {
        try {

            XPathFactory xpf = XPathFactory.newInstance();
            XPath xpath = xpf.newXPath();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document markupFile = builder.parse("miccfg.xml");
            markupFile.getDocumentElement().normalize();

            File outputFile = new File("test.txt");
            try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(outputFile))) {
                NodeList tasks = markupFile.getElementsByTagName("Task");
                
                for (int i = 0; i < tasks.getLength(); i++) {
                    Node task = tasks.item(i);
                    
                    Element singleTask = (Element) task;
                    NodeList days = singleTask.getElementsByTagName("DayOfWeek");
                    NodeList sources = singleTask.getElementsByTagName("Source");
                    NodeList destinations = singleTask.getElementsByTagName("Destination");
                    
                    if (task.getNodeType() == Node.ELEMENT_NODE) {
                        
                        String taskName = (String) xpath.evaluate("/Settings/Tasks/Task[" + i + "]/@Name", markupFile, XPathConstants.STRING);
                        String taskStatus = (String) xpath.evaluate("/Settings/Tasks/Task[" + i + "]/@Active", markupFile, XPathConstants.STRING);
                        
                        List<String> taskDays = new ArrayList<>();
                        for (int j = 0; j <= days.getLength(); j++) {
                            String day = (String) xpath.evaluate("/Settings/Tasks/Task[" + i + "]/Schedules/Schedule/Days/DayOfWeek[" + j + "]", markupFile, XPathConstants.STRING);
                            taskDays.add(day);
                        }
                        String collectedDays = Arrays.toString(taskDays.toArray()).replace(",", " ");
                        String finalDays = collectedDays.replaceAll("\\[", "").replaceAll("\\]", "");
                        
                        List<String> taskSources = new ArrayList<>();
                        for (int j = 0; j < sources.getLength(); j++) {
                            Element sourceItem = (Element) sources.item(j);
                            String sourceHost = sourceItem.getAttribute("HostID");
                            String sourcePath = sourceItem.getAttribute("Path");
                            //if(sourceHost == null || "".equals(sourceHost)){sourceHost = "Void";}
                            //if(sourcePath == null || "".equals(sourcePath)){sourcePath = "Void";}
                            //String sourceHost = (String) xpath.evaluate("/Settings/Tasks/Task[" + i + "]/Source[" + j + "]/@HostID", markupFile, XPathConstants.STRING);
                            //String sourcePath = (String) xpath.evaluate("/Settings/Tasks/Task[" + i + "]/Source[" + j + "]/@Path", markupFile, XPathConstants.STRING);
                            taskSources.add(sourceHost + "@" + sourcePath + " ");
                        }
                        String collectedSources = Arrays.toString(taskSources.toArray()).replace(",", " ");
                        String finalSources = collectedSources.replaceAll("\\[", "").replaceAll("\\]", "");
                        
                        List<String> taskDestinations = new ArrayList<>();
                        for (int j = 0; j < destinations.getLength(); j++) {
                            Element destinationItem = (Element) destinations.item(j);
                            String destinationHost = destinationItem.getAttribute("HostID");
                            String destinationPath = destinationItem.getAttribute("Path");
                            //if(destinationHost == null || "".equals(destinationHost)){destinationHost = "Void";}
                            //if(destinationPath == null || "".equals(destinationPath)){destinationPath = "Void";}
                            //String sourceHost = (String) xpath.evaluate("/Settings/Tasks/Task[" + i + "]/Source[" + j + "]/@HostID", markupFile, XPathConstants.STRING);
                            //String sourcePath = (String) xpath.evaluate("/Settings/Tasks/Task[" + i + "]/Source[" + j + "]/@Path", markupFile, XPathConstants.STRING);
                            taskDestinations.add(destinationHost + "@" + destinationPath + " ");
                        }
                        String collectedDestinations = Arrays.toString(taskDestinations.toArray()).replace(",", " ");
                        String finalDestinations = collectedDestinations.replaceAll("\\[", "").replaceAll("\\]", "");
                        
                        String startTime = (String) xpath.evaluate("/Settings/Tasks/Task[" + i + "]/Schedules/Schedule/Frequency/Interval/@StartTime", markupFile, XPathConstants.STRING);
                        String endTime = (String) xpath.evaluate("/Settings/Tasks/Task[" + i + "]/Schedules/Schedule/Frequency/Interval/@EndTime", markupFile, XPathConstants.STRING);
                        String interval = startTime + " - " + endTime;
                        
                        String fileMask = (String) xpath.evaluate("/Settings/Tasks/Task[" + i + "]/Source/@FileMask", markupFile, XPathConstants.STRING);
                        //String sourceHost = (String) xpath.evaluate("/Settings/Tasks/Task[" + i + "]/Source/@HostID", markupFile, XPathConstants.STRING);
                        //String sourcePath = (String) xpath.evaluate("/Settings/Tasks/Task[" + i + "]/Source/@Path", markupFile, XPathConstants.STRING);
                        
                        String fileName = (String) xpath.evaluate("/Settings/Tasks/Task[" + i + "]/Destination/@FileName", markupFile, XPathConstants.STRING);
                        //String destinationHost = (String) xpath.evaluate("/Settings/Tasks/Task[" + i + "]/Destination/@HostID", markupFile, XPathConstants.STRING);
                        //String destinationPath = (String) xpath.evaluate("/Settings/Tasks/Task[" + i + "]/Destination/@Path", markupFile, XPathConstants.STRING);
                        
                        String extractedTask = taskName + "|" + taskStatus + "|" + finalDays + "|" + interval + "|" + fileMask + "|" + finalSources + "|" + fileName + "|" + finalDestinations;
                        //String finalTask = extractedTask.replaceAll(",,", ",VOID,");
                        
                        fileWriter.write(extractedTask + "\n");
                        //fileWriter.write(finalTask + "\n");
                    }
                }
            }

        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            e.printStackTrace();
        }
    }
}
