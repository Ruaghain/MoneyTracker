package com.wraith.money.data.entity;

import com.wraith.money.data.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This class maps a given payee description from an upload, to a specific payee in the payee table. Should be used exclusively
 * for data uploads.
 * <p/>
 * User: rowan.massey Date: 09/09/2014
 */
@Entity
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "payeemapping_id")),
        @AttributeOverride(name = "version", column = @Column(name = "payeemapping_version"))})
public class PayeeMapping extends BaseEntity implements Serializable {

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "payee_id")
    private Payee payee;
    private String payeeDescription;

    public Payee getPayee() {
        return payee;
    }

    public void setPayee(Payee payee) {
        this.payee = payee;
    }

    public String getPayeeDescription() {
        return payeeDescription;
    }

    public void setPayeeDescription(String payeeDescription) {
        this.payeeDescription = payeeDescription;
    }
}
