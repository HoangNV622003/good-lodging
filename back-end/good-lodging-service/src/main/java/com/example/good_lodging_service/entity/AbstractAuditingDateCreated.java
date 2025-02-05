package com.example.good_lodging_service.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import org.springframework.data.annotation.CreatedDate;

import java.io.Serializable;
import java.time.Instant;

public abstract class AbstractAuditingDateCreated implements Serializable {

    @CreatedDate
    @Column(name = "date_created", updatable = false)
    @JsonIgnore
    private Instant dateCreated;
}
