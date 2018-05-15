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
 * This class represents the currency used in the transaction, in the database. User: rowan.massey Date: 15/08/12
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "currency_id")),
        @AttributeOverride(name = "version", column = @Column(name = "currency_version"))})
public class Currency extends BaseEntity implements Serializable {

    private String name;
    private String iso;

    @NaturalId
    @Column(name = "currency_iso", nullable = false)
    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    @Column(name = "currency_name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
