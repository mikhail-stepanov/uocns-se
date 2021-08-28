package com.stepanov.uocns.web.endpoints;

import com.stepanov.uocns.common.handling.exceptions.CommonException;
import com.stepanov.uocns.web.models.dtos.xmlgen.CirculantXmlRequest;
import com.stepanov.uocns.web.models.dtos.xmlgen.CirculantXmlResponse;
import com.stepanov.uocns.web.models.dtos.xmlgen.MeshXmlRequest;
import com.stepanov.uocns.web.models.dtos.xmlgen.MeshXmlResponse;
import com.stepanov.uocns.web.models.dtos.xmlgen.TorusXmlRequest;
import com.stepanov.uocns.web.models.dtos.xmlgen.TorusXmlResponse;
import com.stepanov.uocns.web.services.XmlGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/xml")
@RequiredArgsConstructor
public class XmlGeneratorEndpoint {

    private final XmlGeneratorService xmlGeneratorService;

    @CrossOrigin
    @PostMapping("/circulant")
    public CirculantXmlResponse circulant(@RequestBody CirculantXmlRequest request) throws CommonException {
        return xmlGeneratorService.circulant(request);
    }

    @CrossOrigin
    @PostMapping("/circulant/optimal")
    public CirculantXmlResponse optimalCirculant(@RequestBody CirculantXmlRequest request) throws CommonException {
        return xmlGeneratorService.optimalCirculant(request);
    }

    @CrossOrigin
    @PostMapping("/mesh")
    public MeshXmlResponse mesh(@RequestBody MeshXmlRequest request) throws CommonException {
        return xmlGeneratorService.mesh(request);
    }

    @CrossOrigin
    @PostMapping("/torus")
    public TorusXmlResponse torus(@RequestBody TorusXmlRequest request) throws CommonException {
        return xmlGeneratorService.torus(request);
    }
}
