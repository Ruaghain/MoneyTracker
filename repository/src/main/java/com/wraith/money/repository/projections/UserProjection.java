package com.wraith.money.repository.projections;

import com.wraith.money.data.entity.Address;
import com.wraith.money.data.entity.Users;
import org.springframework.data.rest.core.config.Projection;

import java.util.Date;

/**
 * User: rowan.massey
 * Date: 05/07/2015
 * Time: 15:00
 */
@Projection(name = "userProjection", types = Users.class)
public interface UserProjection {

    Integer getId();

    String getUserName();

    String getFirstName();

    String getLastName();

    String getEnabled();

    Date getDateOfBirth();

    Address getAddress();

    String getFullName();
}
