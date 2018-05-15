package com.wraith.money.data.entity;

import com.wraith.money.data.BaseEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * This class is the object representation of an account type in the database. They include "Current", "Wallet" etc
 *
 * User: rowan.massey
 * Date: 15/08/12
 * Time: 22:11
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "acctype_id")),
        @AttributeOverride(name = "version", column = @Column(name = "acctype_version"))})
public class AccountType extends BaseEntity implements Serializable {
    private String name;

    @Column(name = "acctype_name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
