package com.stepanov.uocns.web.models.dtos.xmlgen;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TorusXmlResponse {

    private Long id;

    private String name;

    private String content;
}
