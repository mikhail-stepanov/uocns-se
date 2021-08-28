package com.stepanov.uocns.web.services;

import com.stepanov.uocns.common.handling.exceptions.BadRequestException;
import com.stepanov.uocns.common.handling.exceptions.CommonException;
import com.stepanov.uocns.network.common.generator.Circulant;
import com.stepanov.uocns.network.common.generator.Mesh;
import com.stepanov.uocns.network.common.generator.Torus;
import com.stepanov.uocns.web.interfaces.IXmlGeneratorService;
import com.stepanov.uocns.web.models.dtos.xmlgen.CirculantXmlRequest;
import com.stepanov.uocns.web.models.dtos.xmlgen.CirculantXmlResponse;
import com.stepanov.uocns.web.models.dtos.xmlgen.MeshXmlRequest;
import com.stepanov.uocns.web.models.dtos.xmlgen.MeshXmlResponse;
import com.stepanov.uocns.web.models.dtos.xmlgen.TorusXmlRequest;
import com.stepanov.uocns.web.models.dtos.xmlgen.TorusXmlResponse;
import com.stepanov.uocns.web.models.entities.Topology;
import com.stepanov.uocns.web.models.entities.TopologyXml;
import com.stepanov.uocns.web.repositories.TopologyRepository;
import com.stepanov.uocns.web.services.util.XmlHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class XmlGeneratorService implements IXmlGeneratorService {

    private final String MESH_TOPOLOGY = "Mesh";
    private final String TORUS_TOPOLOGY = "Torus";
    private final String CIRCULANT_TOPOLOGY = "Circulant";
    private final String OPTIMAL_CIRCULANT_TOPOLOGY = "CirculantOpt";

    private final XmlHelper xmlHelper;

    private final TopologyRepository topologyRepository;

    @Override
    public CirculantXmlResponse circulant(CirculantXmlRequest request) throws CommonException {
        if (request.getNodes() < 5) {
            log.error("Invalid number of nodes for optimal circulant");
            throw new BadRequestException("Invalid number of nodes for optimal circulant");
        } else {
            Topology topology = new Topology();
            topology.setName(request.getName());
            topology.setNodes(request.getNodes());

            Circulant circulantNetwork = new Circulant(request.getNodes(), request.getFirstStep(), request.getSecondStep());
            circulantNetwork.createNetlist();
            circulantNetwork.createRouting(circulantNetwork.adjacencyMatrix(circulantNetwork.getNetlist(), request.getNodes()),
                    circulantNetwork.getNetlist());

            String description = CIRCULANT_TOPOLOGY + "-(" + request.getNodes() + ", " + request.getFirstStep() + ", " + request.getSecondStep() + ")";
            topology.setDescription(description);
            topologyRepository.save(topology);

            xmlHelper.createXml(circulantNetwork.getNetlist(), circulantNetwork.getRouting(), description, topology.getId());

            TopologyXml topologyXml = topologyRepository.getById(topology.getId()).getTopologyXml();

            return CirculantXmlResponse.builder()
                    .id(topologyXml.getId())
                    .name(topologyXml.getName())
                    .content(topologyXml.getContent())
                    .build();
        }
    }

    @Override
    public CirculantXmlResponse optimalCirculant(CirculantXmlRequest request) throws CommonException {
        if (request.getNodes() < 5) {
            log.error("Invalid number of nodes for optimal circulant");
            throw new BadRequestException("Invalid number of nodes for optimal circulant");
        } else {
            Topology topology = new Topology();
            topology.setName(request.getName());
            topology.setNodes(request.getNodes());

            Circulant circulantNetwork = new Circulant(request.getNodes());
            circulantNetwork.createNetlist();
            circulantNetwork.createRouting(circulantNetwork.adjacencyMatrix(circulantNetwork.getNetlist(), request.getNodes()),
                    circulantNetwork.getNetlist());

            String description = OPTIMAL_CIRCULANT_TOPOLOGY + "-(" + request.getNodes() + ", " + circulantNetwork.s1 + ", " + circulantNetwork.s2 + ")";
            topology.setDescription(description);
            topologyRepository.save(topology);

            xmlHelper.createXml(circulantNetwork.getNetlist(), circulantNetwork.getRouting(), description, topology.getId());

            TopologyXml topologyXml = topologyRepository.getById(topology.getId()).getTopologyXml();

            return CirculantXmlResponse.builder()
                    .id(topologyXml.getId())
                    .name(topologyXml.getName())
                    .content(topologyXml.getContent())
                    .build();
        }
    }

    @Override
    public MeshXmlResponse mesh(MeshXmlRequest request) throws CommonException {
        Topology topology = new Topology();
        topology.setName(request.getName());
        topology.setColumns(request.getColumns());
        topology.setRows(request.getRows());

        Mesh meshNetwork = new Torus(request.getColumns(), request.getRows());
        meshNetwork.createNetlist();
        meshNetwork.createRouting();

        String description = MESH_TOPOLOGY + "-(" + request.getColumns() + ", " + request.getRows() + ")";
        topology.setDescription(description);
        topologyRepository.save(topology);

        xmlHelper.createXml(meshNetwork.getNetlist(), meshNetwork.getRouting(), description, topology.getId());

        TopologyXml topologyXml = topologyRepository.getById(topology.getId()).getTopologyXml();

        return MeshXmlResponse.builder()
                .id(topologyXml.getId())
                .name(topologyXml.getName())
                .content(topologyXml.getContent())
                .build();
    }

    @Override
    public TorusXmlResponse torus(TorusXmlRequest request) throws CommonException {
        Topology topology = new Topology();
        topology.setName(request.getName());
        topology.setColumns(request.getColumns());
        topology.setRows(request.getRows());

        Torus torusNetwork = new Torus(request.getColumns(), request.getRows());
        torusNetwork.createNetlist();
        torusNetwork.createRouting();

        String description = TORUS_TOPOLOGY + "-(" + request.getColumns() + ", " + request.getRows() + ")";
        topology.setDescription(description);
        topologyRepository.save(topology);

        xmlHelper.createXml(torusNetwork.getNetlist(), torusNetwork.getRouting(), description, topology.getId());

        TopologyXml topologyXml = topologyRepository.getById(topology.getId()).getTopologyXml();

        return TorusXmlResponse.builder()
                .id(topologyXml.getId())
                .name(topologyXml.getName())
                .content(topologyXml.getContent())
                .build();
    }
}
