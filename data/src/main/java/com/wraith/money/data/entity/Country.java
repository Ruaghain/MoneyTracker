package com.wraith.money.data.entity;


import com.wraith.money.data.BaseEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * This class represents the country table in the database.
 *
 * User: rowan.massey Date: 17/08/12
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "country_id")),
        @AttributeOverride(name = "version", column = @Column(name = "country_version"))})
public class Country extends BaseEntity implements Serializable {

    private String name;
    private String iso;

    @NaturalId
    @Column(name = "country_name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "country_iso", nullable = false)
    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }
}
