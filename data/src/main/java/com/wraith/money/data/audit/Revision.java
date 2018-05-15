package com.wraith.money.data.audit;

import com.wraith.money.data.listener.MoneyRevisionListener;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

/**
 * User: rowan.massey
 * Date: 29/03/13
 * Time: 14:44
 */

@Entity
@RevisionEntity(MoneyRevisionListener.class)
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "revision_id")),
        @AttributeOverride(name = "timestamp", column = @Column(name = "revision_timestamp"))})
public class Revision extends DefaultRevisionEntity {

    @Column(name = "revision_date", nullable = false)
    private Date revisionDate;
    @Column(name = "revision_username")
    private String userName;
    @Column(name = "revision_ipaddress")
    private String ipAddress;

    public Date getRevisionDate() {
        return revisionDate;
    }

    public void setRevisionDate(Date revisionDate) {
        this.revisionDate = revisionDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
