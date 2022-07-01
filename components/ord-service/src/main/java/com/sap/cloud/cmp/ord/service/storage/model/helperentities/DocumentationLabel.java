package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@EdmIgnore
@Table(name = "ord_documentation_labels")
@Entity(name = "documentationLabel")
@IdClass(DocumentationLabel.class)
public class DocumentationLabel implements Serializable {
    @javax.persistence.Id
    @Column(name = "api_definition_id", length = 256)
    private String apiDefID;

    @javax.persistence.Id
    @Column(name = "event_definition_id", length = 256)
    private String eventDefID;

    @javax.persistence.Id
    @Column(name = "package_id", length = 256)
    private String packageID;

    @javax.persistence.Id
    @Column(name = "bundle_id", length = 256)
    private String bundleID;

    @javax.persistence.Id
    @Column(name = "vendor_id", length = 256)
    private String vendorID;

    @javax.persistence.Id
    @Column(name = "product_id", length = 256)
    private String productID;

    @javax.persistence.Id
    @Column(name = "application_id", length = 256)
    private String systemInstanceID;

    @javax.persistence.Id
    @Column(name = "key", length = Integer.MAX_VALUE)
    private String key;

    @javax.persistence.Id
    @Column(name = "value", length = Integer.MAX_VALUE)
    private String value;
}
