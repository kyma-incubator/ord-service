package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@EdmIgnore
@Table(name = "changelog_entries_api_definitions")
@Entity(name = "changelogAPIDefinition")
@IdClass(ChangelogAPIDefinition.class)
public class ChangelogAPIDefinition implements Serializable {
    @javax.persistence.Id
    @Column(name = "api_definition_id", length = 256)
    private String apiDefID;

    @javax.persistence.Id
    @Column(name = "version", length = Integer.MAX_VALUE)
    private String version;

    @javax.persistence.Id
    @Column(name = "release_status", length = Integer.MAX_VALUE)
    private String releaseStatus;

    @javax.persistence.Id
    @Column(name = "date", length = Integer.MAX_VALUE)
    private String date;

    @javax.persistence.Id
    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @javax.persistence.Id
    @Column(name = "url", length = Integer.MAX_VALUE)
    private String url;
}
