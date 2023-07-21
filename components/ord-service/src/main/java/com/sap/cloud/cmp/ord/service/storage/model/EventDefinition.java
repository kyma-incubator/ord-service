package com.sap.cloud.cmp.ord.service.storage.model;

import com.sap.cloud.cmp.ord.service.storage.model.converter.UrlConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;

@Embeddable
public class EventDefinition {
    @Column(name = "type", length = Integer.MAX_VALUE)
    private String type;

    @Column(name = "custom_type", length = Integer.MAX_VALUE)
    private String customType;

    @Column(name = "media_type", length = Integer.MAX_VALUE)
    private String mediaType;

    @Convert(converter = UrlConverter.class)
    @Column(name = "url", length = Integer.MAX_VALUE)
    private String url;
}
