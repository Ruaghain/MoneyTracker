package com.wraith.money.data.entity;

import com.wraith.money.data.BaseEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is a representation of the groups entity, it manages all of the groups that the relevant user belongs to.
 *
 * User: rowan.massey
 * Date: 12/08/12
 * Time: 20:53
 */
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "group_id")),
        @AttributeOverride(name = "version", column = @Column(name = "group_version"))})
public class Groups extends BaseEntity implements Serializable {

    private String name;
    private Set<Authorities> authorities = new HashSet<>();

    public Groups() {
    }

    @Column(name = "group_name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "Group_Authorities", joinColumns = {@JoinColumn(name = "group_authorities_group_id", referencedColumnName = "group_id")},
            inverseJoinColumns = {@JoinColumn(name = "group_authorities_authorities_id", referencedColumnName = "authorities_id")})
    public Set<Authorities> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authorities> authorities) {
        this.authorities = authorities;
    }
}
