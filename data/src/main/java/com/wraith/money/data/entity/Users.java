package com.wraith.money.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wraith.money.data.BaseEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents the users table in the database.
 *
 * User: rowan.massey
 * Date: 06/07/12
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
@Audited
@AuditTable(value = "Users_Audit")
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "users_id")),
        @AttributeOverride(name = "version", column = @Column(name = "users_version"))})
public class Users extends BaseEntity implements Serializable {

    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    @JsonIgnore
    private int enabled;
    private Address address;
    private Set<Groups> groups = new HashSet<>();
    private Set<Account> accounts = new HashSet<>();

    public Users() {

    }

    @Column(name = "users_firstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "users_lastName")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @NaturalId
    @Column(name = "users_userName")
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    //    @JsonIgnore
    @Column(name = "users_password")
    public String getPassword() {
        return password;
    }

    //    @JsonProperty("password")
    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "users_enabled", nullable = false)
    public int getEnabled() {
        return enabled;
    }

    public void setEnabled(int enabled) {
        this.enabled = enabled;
    }

    @Column(name = "users_dateofbirth")
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @NotAudited
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "users_address_id")
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @NotAudited
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinTable(name = "Users_Groups", joinColumns = {@JoinColumn(name = "users_groups_user_id", referencedColumnName = "users_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "users_groups_group_id", referencedColumnName = "group_id", nullable = false)})
    public Set<Groups> getGroups() {
        return groups;
    }

    public void setGroups(Set<Groups> groups) {
        this.groups = groups;
    }

    @NotAudited
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinTable(name = "Users_Account", joinColumns = {@JoinColumn(name = "users_account_user_id", referencedColumnName = "users_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "users_account_account_id", referencedColumnName = "account_id", nullable = false)})
    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    @Transient
    public String getFullName() {
        return this.getFirstName().concat(" ").concat(getLastName());
    }
}
