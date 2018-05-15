package com.wraith.money.data.metadata;

import com.wraith.money.data.BaseEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * This class facilitates access to the screen items of a particular screen.
 * <p/>
 * User: rowan.massey
 * Date: 09/05/2015
 * Time: 15:53
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AttributeOverrides({@AttributeOverride(name = "id", column = @Column(name = "screen_id")),
        @AttributeOverride(name = "version", column = @Column(name = "screen_version"))})
public class ScreenItem extends BaseEntity implements Serializable {
}
