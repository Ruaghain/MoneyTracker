package com.wraith.money.repository.handler;

import com.wraith.money.data.entity.Authorities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * User: rowan.massey
 * Date: 01/05/13
 * Time: 19:41
 */
@RepositoryEventHandler(Authorities.class)
public class AuthoritiesEventHandler {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN'))")
    @HandleBeforeCreate
    public void beforeAuthorityCreate(Authorities authority) {
        logger.debug(String.format("In before create for authority '%s'", authority.getAuthority()));
    }

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN'))")
    @HandleBeforeSave
    public void beforeAuthorityUpdate(Authorities authority) {
        logger.debug(String.format("In before update for authority '%s'", authority.getAuthority()));
        //Don't need to add anything to this method, the @PreAuthorize does the job.
    }

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN'))")
    @HandleBeforeDelete
    public void beforeAuthorityDelete(Authorities authority) {
        logger.debug(String.format("In before delete for authority '%s'", authority.getAuthority()));
        //Don't need to add anything to this method, the @PreAuthorize does the job.
    }
}
