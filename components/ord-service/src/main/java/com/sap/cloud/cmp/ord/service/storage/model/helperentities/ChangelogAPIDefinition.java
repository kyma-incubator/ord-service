package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.*;
import java.io.Serializable;

@EdmIgnore
@Table(name = "changelog_entries_api_definitions")
@Entity(name = "changelogAPIDefinition")
@IdClass(ChangelogAPIDefinition.class)
public class ChangelogAPIDefinition implements Serializable {
    @Id
    @Column(name = "api_definition_id", length = 256)
    private String apiDefID;

    @Id
    @Column(name = "version", length = Integer.MAX_VALUE)
    private String version;

    @Id
    @Column(name = "release_status", length = Integer.MAX_VALUE)
    private String releaseStatus;

    @Id
    @Column(name = "date", length = Integer.MAX_VALUE)
    private String date;

    @Id
    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Id
    @Column(name = "url", length = Integer.MAX_VALUE)
    private String url;
}
