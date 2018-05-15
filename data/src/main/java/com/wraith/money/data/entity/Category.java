package com.wraith.money.data.entity;


import com.wraith.money.data.BaseEntity;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.io.Serializable;

/**
 * This class represents the categories of the transaction in the database. e.g. Petrol, Groceries-->Bread etc
 *
 * User: rowan.massey Date: 02/09/12
 */
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "category_id")),
        @AttributeOverride(name = "version", column = @Column(name = "category_version"))})
public class Category extends BaseEntity implements Serializable {

    private Category parentCategory;
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_parent_id")
    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    @NaturalId
    @Column(name = "category_name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
