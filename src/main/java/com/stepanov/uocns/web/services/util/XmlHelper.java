package com.stepanov.uocns.web.services.util;

import com.stepanov.uocns.common.handling.exceptions.InternalErrorException;
import com.stepanov.uocns.web.models.entities.Topology;
import com.stepanov.uocns.web.models.entities.TopologyXml;
import com.stepanov.uocns.web.repositories.TopologyRepository;
import com.stepanov.uocns.web.repositories.TopologyXmlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class XmlHelper {

    @Value("${generator.results.path}")
    private String CONFIG_PATH;

    private final TopologyRepository topologyRepository;
    private final TopologyXmlRepository topologyXmlRepository;

    public String createXml(int[][] netlist, int[][] routing, String description, Long topologyId) throws InternalErrorException {
        try {
            StringBuilder netlistData = new StringBuilder("\n");
            for (int i = 0; i < routing.length; i++) {
                for (int j = 0; j < 4; j++) {
                    netlistData.append(netlist[i][j]).append(" ");
                }
                netlistData.append("\n");
            }

            StringBuilder routingData = new StringBuilder("\n");
            for (int[] ints : routing) {
                for (int j = 0; j < routing.length; j++) {
                    routingData.append(ints[j]).append(" ");
                }
                routingData.append("\n");
            }

            return write(netlistData.toString(), routingData.toString(), description, topologyId);
        } catch (Exception e) {
            log.error("Exception while creating XML: " + e.getMessage());
            throw new InternalErrorException("Exception while creating XML: " + e.getMessage());
        }
    }

    private String write(String netlistData, String routingData, String description, Long topologyId) throws InternalErrorException {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
            DOMImplementation impl = builder.getDOMImplementation();

            Document doc = impl.createDocument(null, "TaskOCNS", null);
            Element rootElement = doc.getDocumentElement();
            rootElement.setAttributeNS(null, "Description", description);
            rootElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            rootElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:xsd", "http://www.w3.org/2001/XMLSchema");

            // добавляем первый дочерний элемент к корневому
            Element network = doc.createElement("Network");
            rootElement.appendChild(network);

            //добавляем детей для узла
            Element netlist = doc.createElement("Netlist");
            netlist.appendChild(doc.createTextNode(netlistData));
            network.appendChild(netlist);

            Element routing = doc.createElement("Routing");
            routing.appendChild(doc.createTextNode(routingData));
            network.appendChild(routing);

            Element link = doc.createElement("Link");
            network.appendChild(link);

            Element fifosize = doc.createElement("Parameter");
            fifosize.setAttribute("FifoSize", "4");
            link.appendChild(fifosize);

            Element fifocount = doc.createElement("Parameter");
            fifocount.setAttribute("FifoCount", "4");
            link.appendChild(fifocount);

            Element traffic = doc.createElement("Traffic");
            rootElement.appendChild(traffic);

            Element flitSize = doc.createElement("Parameter");
            flitSize.setAttribute("FlitSize", "32");
            traffic.appendChild(flitSize);

            Element packetSizeAvg = doc.createElement("Parameter");
            packetSizeAvg.setAttribute("PacketSizeAvg", "10");
            traffic.appendChild(packetSizeAvg);

            Element packetSizeIsFixed = doc.createElement("Parameter");
            packetSizeIsFixed.setAttribute("PacketSizeIsFixed", "true");
            traffic.appendChild(packetSizeIsFixed);

            Element packetPeriodAvg = doc.createElement("Parameter");
            packetPeriodAvg.setAttribute("PacketPeriodAvg", "5");
            traffic.appendChild(packetPeriodAvg);

            Element simulation = doc.createElement("Simulation");
            rootElement.appendChild(simulation);

            Element countRun = doc.createElement("Parameter");
            countRun.setAttribute("CountRun", "1");
            simulation.appendChild(countRun);

            Element countPacketRx = doc.createElement("Parameter");
            countPacketRx.setAttribute("CountPacketRx", "1100");
            simulation.appendChild(countPacketRx);

            Element countPacketRxWarmUp = doc.createElement("Parameter");
            countPacketRxWarmUp.setAttribute("CountPacketRxWarmUp", "100");
            simulation.appendChild(countPacketRxWarmUp);

            Element isModeGALS = doc.createElement("Parameter");
            isModeGALS.setAttribute("IsModeGALS", "false");
            simulation.appendChild(isModeGALS);

            //создаем объект TransformerFactory для печати в консоль
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            // для красивого вывода в консоль
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource source = new DOMSource(doc);

            //печатаем в консоль или файл
            UUID uuid = UUID.randomUUID();
            File folder = new File(CONFIG_PATH + description);
            if (!folder.exists()) {
                folder.mkdir();
            }
            String configPath = CONFIG_PATH + description + "/config-" + description + "-" + uuid + ".xml";
            StreamResult file = new StreamResult(new File(configPath));

            //записываем данные
            StringWriter writer = new StringWriter();
            transformer.transform(source, new StreamResult(writer));
            String content = writer.toString();

            transformer.transform(source, file);

            Topology topology = topologyRepository.getById(topologyId);

            TopologyXml topologyXml = new TopologyXml();
            topologyXml.setTopology(topology);
            topologyXml.setName("config-" + description + ".xml");
            topologyXml.setContent(content);

            topologyXmlRepository.save(topologyXml);

            return configPath;
        } catch (Exception e) {
            log.error("Exception while generating XML-file: " + e.getMessage());
            throw new InternalErrorException("Exception while generating XML-file: " + e.getMessage());
        }
    }

}
