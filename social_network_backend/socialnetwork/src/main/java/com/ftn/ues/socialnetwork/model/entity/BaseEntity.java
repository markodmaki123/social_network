package com.ftn.ues.socialnetwork.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "deleted" , nullable = false)
    boolean deleted;

    public boolean isDeleted() {
        return deleted;
    }
}

