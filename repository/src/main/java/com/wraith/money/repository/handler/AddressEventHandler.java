package com.wraith.money.repository.handler;

import com.wraith.money.data.entity.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * User: rowan.massey
 * Date: 30/04/13
 * Time: 19:09
 */
@RepositoryEventHandler(Address.class)
public class AddressEventHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN'))")
    @HandleBeforeCreate
    public void beforeAddressCreate(Address address) {
        logger.debug(String.format("In before create for address '%s'", address.toString()));
    }

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN'))")
    @HandleBeforeSave
    public void beforeAddressUpdate(Address address) {
        logger.debug(String.format("In before update for address '%s'", address.toString()));
        //Don't need to add anything to this method, the @PreAuthorize does the job.
    }

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN'))")
    @HandleBeforeDelete
    public void beforeAddressDelete(Address address) {
        logger.debug(String.format("In before delete for address '%s'", address.toString()));
        //Don't need to add anything to this method, the @PreAuthorize does the job.
    }
}
