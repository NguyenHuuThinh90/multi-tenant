package com.app.common;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;

@MappedSuperclass
public class ItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence_id")
    @GenericGenerator(name = "sequence_id", strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_hibernate", value = "sequence_id"),
                    @Parameter(name = "initial_value", value = "1"),
                    @Parameter(name = "optimizer", value = "sequence_id")
            }
    )
    private Long id;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
