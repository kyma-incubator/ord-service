package com.sap.cloud.cmp.ord.service.storage.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OutputPort {
    @Column(name = "ord_id", length = Integer.MAX_VALUE)
    private String ordId;
}
