package com.wraith.money.data.entity;

import com.wraith.money.data.BaseEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This class is the object representation of an Address in the database.
 *
 * User: rowan.massey Date: 17/08/12
 */
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "address_id")),
        @AttributeOverride(name = "version", column = @Column(name = "address_version"))})
public class Address extends BaseEntity implements Serializable {

    private String address1;
    private String address2;
    private String address3;
    private String address4;
    private String county;
    private String city;
    private Country country;

    @Column(name = "address_address1", nullable = false)
    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    @Column(name = "address_address2")
    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    @Column(name = "address_address3")
    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    @Column(name = "address_address4")
    public String getAddress4() {
        return address4;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    @Column(name = "address_county")
    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    @Column(name = "address_city", nullable = false)
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_country_id")
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }
}
