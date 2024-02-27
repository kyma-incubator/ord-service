package com.sap.cloud.cmp.ord.service.storage.model;

import com.sap.olingo.jpa.metadata.core.edm.annotation.EdmIgnore;
import jakarta.persistence.*;
import org.eclipse.persistence.annotations.TypeConverter;
import org.eclipse.persistence.annotations.Convert;

import java.util.UUID;

@Embeddable
public class FormationDetails {
    @Column(name = "assignment_id", length = 256)
    @Convert("uuidConverterFormationDetails")
    @TypeConverter(name = "uuidConverterFormationDetails", dataType = UUID.class, objectType = UUID.class)
    private UUID assignmentId;

    @Column(name = "fa_formation_id", length = 256)
    @Convert("uuidConverterFormationDetails")
    @TypeConverter(name = "uuidConverterFormationDetails", dataType = UUID.class, objectType = UUID.class)
    private UUID formationId;

    @Column(name = "formation_type_id", length = 256)
    @Convert("uuidConverterFormationDetails")
    @TypeConverter(name = "uuidConverterFormationDetails", dataType = UUID.class, objectType = UUID.class)
    private UUID formationTypeId;

    @EdmIgnore
    @Column(name = "target_id", length = 256)
    @Convert("uuidConverterFormationDetails")
    @TypeConverter(name = "uuidConverterFormationDetails", dataType = UUID.class, objectType = UUID.class)
    private UUID targetId;
}