package com.stepanov.uocns.web.services;

import com.stepanov.uocns.common.handling.exceptions.BadRequestException;
import com.stepanov.uocns.common.handling.exceptions.CommonException;
import com.stepanov.uocns.network.TControllerOCNS;
import com.stepanov.uocns.network.common.generator.Circulant;
import com.stepanov.uocns.network.common.generator.Mesh;
import com.stepanov.uocns.network.common.generator.Torus;
import com.stepanov.uocns.web.interfaces.ISimulatorService;
import com.stepanov.uocns.web.models.dtos.simulator.CirculantRequest;
import com.stepanov.uocns.web.models.dtos.simulator.CirculantResponse;
import com.stepanov.uocns.web.models.dtos.simulator.MeshRequest;
import com.stepanov.uocns.web.models.dtos.simulator.MeshResponse;
import com.stepanov.uocns.web.models.dtos.simulator.TorusRequest;
import com.stepanov.uocns.web.models.dtos.simulator.TorusResponse;
import com.stepanov.uocns.web.models.entities.Topology;
import com.stepanov.uocns.web.models.entities.TopologyReport;
import com.stepanov.uocns.web.models.entities.TopologyTable;
import com.stepanov.uocns.web.models.entities.TopologyXml;
import com.stepanov.uocns.web.repositories.TopologyRepository;
import com.stepanov.uocns.web.services.util.XmlHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimulatorService implements ISimulatorService {

    private final String MESH_TOPOLOGY = "Mesh";
    private final String TORUS_TOPOLOGY = "Torus";
    private final String CIRCULANT_TOPOLOGY = "Circulant";
    private final String OPTIMAL_CIRCULANT_TOPOLOGY = "CirculantOpt";

    private final TControllerOCNS fControllerOCNS;

    private final XmlHelper xmlHelper;

    private final TopologyRepository topologyRepository;

    @Override
    public CirculantResponse circulant(CirculantRequest request) throws CommonException {
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

            String configPath = xmlHelper.createXml(circulantNetwork.getNetlist(), circulantNetwork.getRouting(), description, topology.getId());

            fControllerOCNS.simulate(request.getDestInjectionRate(), topology.getId(), configPath);

            Topology topologyAfter = topologyRepository.getById(topology.getId());
            TopologyReport topologyReport = topologyAfter.getTopologyReport();
            TopologyTable topologyTable = topologyAfter.getTopologyTable();
            TopologyXml topologyXml = topologyAfter.getTopologyXml();

            return CirculantResponse.builder()
                    .id(topology.getId())
                    .reportId(topologyReport.getId())
                    .tableId(topologyTable.getId())
                    .xmlId(topologyXml.getId())
                    .name(topology.getName())
                    .description(topology.getDescription())
                    .nodes(topology.getNodes())
                    .firstStep(request.getFirstStep())
                    .secondStep(request.getSecondStep())
                    .content(topologyReport.getContent())
                    .build();
        }
    }

    @Override
    public CirculantResponse optimalCirculant(CirculantRequest request) throws CommonException {
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

            String configPath = xmlHelper.createXml(circulantNetwork.getNetlist(), circulantNetwork.getRouting(), description, topology.getId());

            fControllerOCNS.simulate(request.getDestInjectionRate(), topology.getId(), configPath);

            Topology topologyAfter = topologyRepository.getById(topology.getId());
            TopologyReport topologyReport = topologyAfter.getTopologyReport();
            TopologyTable topologyTable = topologyAfter.getTopologyTable();
            TopologyXml topologyXml = topologyAfter.getTopologyXml();

            return CirculantResponse.builder()
                    .id(topology.getId())
                    .reportId(topologyReport.getId())
                    .tableId(topologyTable.getId())
                    .xmlId(topologyXml.getId())
                    .name(topology.getName())
                    .description(topology.getDescription())
                    .nodes(topology.getNodes())
                    .content(topologyReport.getContent())
                    .build();
        }
    }

    @Override
    public MeshResponse mesh(MeshRequest request) throws CommonException {
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

        String configPath = xmlHelper.createXml(meshNetwork.getNetlist(), meshNetwork.getRouting(), description, topology.getId());

        fControllerOCNS.simulate(request.getDestInjectionRate(), topology.getId(), configPath);

        Topology topologyAfter = topologyRepository.getById(topology.getId());
        TopologyReport topologyReport = topologyAfter.getTopologyReport();
        TopologyTable topologyTable = topologyAfter.getTopologyTable();
        TopologyXml topologyXml = topologyAfter.getTopologyXml();

        return MeshResponse.builder()
                .id(topology.getId())
                .reportId(topologyReport.getId())
                .tableId(topologyTable.getId())
                .xmlId(topologyXml.getId())
                .name(topology.getName())
                .description(topology.getDescription())
                .columns(topology.getColumns())
                .rows(topology.getRows())
                .content(topologyReport.getContent())
                .build();
    }

    @Override
    public TorusResponse torus(TorusRequest request) throws CommonException {
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

        String configPath = xmlHelper.createXml(torusNetwork.getNetlist(), torusNetwork.getRouting(), description, topology.getId());

        fControllerOCNS.simulate(request.getDestInjectionRate(), topology.getId(), configPath);

        Topology topologyAfter = topologyRepository.getById(topology.getId());
        TopologyReport topologyReport = topologyAfter.getTopologyReport();
        TopologyTable topologyTable = topologyAfter.getTopologyTable();
        TopologyXml topologyXml = topologyAfter.getTopologyXml();

        return TorusResponse.builder()
                .id(topology.getId())
                .reportId(topologyReport.getId())
                .tableId(topologyTable.getId())
                .xmlId(topologyXml.getId())
                .name(topology.getName())
                .description(topology.getDescription())
                .columns(topology.getColumns())
                .rows(topology.getRows())
                .content(topologyReport.getContent())
                .build();
    }

}
