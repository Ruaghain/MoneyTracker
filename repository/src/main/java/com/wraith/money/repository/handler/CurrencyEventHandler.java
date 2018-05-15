package com.wraith.money.repository.handler;

import com.wraith.money.data.entity.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * User: rowan.massey
 * Date: 06/05/13
 * Time: 11:16
 */
@RepositoryEventHandler(Currency.class)
public class CurrencyEventHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN'))")
    @HandleBeforeCreate
    public void beforeCurrencyCreate(Currency currency) {
        logger.debug(String.format("In before create for currency '%s'", currency.getName()));
    }

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN'))")
    @HandleBeforeSave
    public void beforeCurrencyUpdate(Currency currency) {
        logger.debug(String.format("In before update for currency '%s'", currency.getName()));
        //Don't need to add anything to this method, the @PreAuthorize does the job.
    }

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN'))")
    @HandleBeforeDelete
    public void beforeCurrencyDelete(Currency currency) {
        logger.debug(String.format("In before delete for currency '%s'", currency.getName()));
        //Don't need to add anything to this method, the @PreAuthorize does the job.
    }

}
