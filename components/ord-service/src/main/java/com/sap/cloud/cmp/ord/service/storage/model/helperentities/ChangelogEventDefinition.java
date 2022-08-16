package com.sap.cloud.cmp.ord.service.storage.model.helperentities;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;

import javax.persistence.*;
import java.io.Serializable;

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
