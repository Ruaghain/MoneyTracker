package com.wraith.money.repository.handler;

import com.wraith.money.data.entity.Payee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * User: rowan.massey
 * Date: 11/05/13
 * Time: 23:07
 */
@RepositoryEventHandler(Payee.class)
public class PayeeEventHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_USER'))")
    @HandleBeforeCreate
    public void beforePayeeCreate(Payee payee) {
        logger.debug(String.format("In before create for payee '%s'", payee.getName()));
    }

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_USER'))")
    @HandleBeforeSave
    public void beforePayeeUpdate(Payee payee) {
        logger.debug(String.format("In before update for payee '%s'", payee.getName()));
        //Don't need to add anything to this method, the @PreAuthorize does the job.
    }

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN'))")
    @HandleBeforeDelete
    public void beforePayeeDelete(Payee payee) {
        logger.debug(String.format("In before delete for payee '%s'", payee.getName()));
        //Don't need to add anything to this method, the @PreAuthorize does the job.
    }

}
