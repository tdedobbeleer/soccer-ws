package com.soccer.ws.model;

import com.soccer.ws.utils.GeneralUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.joda.time.DateTime;

import javax.persistence.*;

/**
 * Created by u0090265 on 08/06/16.
 */
@MappedSuperclass
public class BaseClass {
    protected Long id;
    private DateTime created;
    private DateTime modified;

    @PreUpdate
    @PrePersist
    public void updateTimeStamps() {
        modified = new DateTime();
        if (created == null) {
            created = new DateTime();
        }
    }

    @Id
    @GeneratedValue(generator = "sequence-generator")
    @GenericGenerator(
            name = "sequence-generator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "hibernate_sequences"),
                    @Parameter(name = "prefer_sequence_per_entity", value = "true"),
            }
    )
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "modified")
    @org.hibernate.annotations.Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getModified() {
        return modified;
    }

    public void setModified(DateTime modified) {
        this.modified = modified;
    }

    @Column(name = "created")
    @org.hibernate.annotations.Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    @Transient
    public String getStringCreated() {
        return GeneralUtils.convertToStringDateTime(this.created);
    }

    @Transient
    public String getStringModfied() {
        return GeneralUtils.convertToStringDateTime(this.modified);
    }
}
