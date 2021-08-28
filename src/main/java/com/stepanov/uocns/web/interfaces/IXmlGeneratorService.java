package com.stepanov.uocns.web.interfaces;

import com.stepanov.uocns.common.handling.exceptions.CommonException;
import com.stepanov.uocns.web.models.dtos.xmlgen.CirculantXmlRequest;
import com.stepanov.uocns.web.models.dtos.xmlgen.CirculantXmlResponse;
import com.stepanov.uocns.web.models.dtos.xmlgen.MeshXmlRequest;
import com.stepanov.uocns.web.models.dtos.xmlgen.MeshXmlResponse;
import com.stepanov.uocns.web.models.dtos.xmlgen.TorusXmlRequest;
import com.stepanov.uocns.web.models.dtos.xmlgen.TorusXmlResponse;

public interface IXmlGeneratorService {

    CirculantXmlResponse circulant(CirculantXmlRequest request) throws CommonException;

    CirculantXmlResponse optimalCirculant(CirculantXmlRequest request) throws CommonException;

    MeshXmlResponse mesh(MeshXmlRequest request) throws CommonException;

    TorusXmlResponse torus(TorusXmlRequest request) throws CommonException;
}
