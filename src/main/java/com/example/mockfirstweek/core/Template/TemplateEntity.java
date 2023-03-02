package com.example.mockfirstweek.core.Template;

import javax.persistence.*;
import javax.persistence.MappedSuperclass;
import org.javers.core.metamodel.annotation.DiffIgnore;

import java.io.Serializable;


@MappedSuperclass
public class TemplateEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @DiffIgnore
    private Long created;
    private Long updated;
    @DiffIgnore
    private String createdBy;
    private String updatedBy;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
