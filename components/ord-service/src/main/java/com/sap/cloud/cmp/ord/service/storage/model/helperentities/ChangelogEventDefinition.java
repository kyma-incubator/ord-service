package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import java.io.Serializable;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@EdmIgnore
@Table(name = "changelog_entries_event_definitions")
@Entity(name = "changelogEventDefinition")
@IdClass(ChangelogEventDefinition.class)
public class ChangelogEventDefinition implements Serializable {
    @Id
    @Column(name = "event_definition_id", length = 256)
    private String eventDefID;

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
