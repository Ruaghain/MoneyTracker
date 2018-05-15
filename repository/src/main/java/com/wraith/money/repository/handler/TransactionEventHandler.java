package com.wraith.money.repository.handler;

import com.wraith.money.data.entity.Transaction;
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
 * Time: 23:57
 */
@RepositoryEventHandler(Transaction.class)
public class TransactionEventHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_USER'))")
    @HandleBeforeCreate
    public void beforeTransactionCreate(Transaction transaction) {
        logger.debug(String.format("In before create for a transaction with a category of '%s' and a payee of '%s'", transaction.getCategory(), transaction.getPayee()));
    }

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_USER'))")
    @HandleBeforeSave
    public void beforeTransactionUpdate(Transaction transaction) {
        logger.debug(String.format("In before update for a transaction with a category of '%s' and a payee of '%s'", transaction.getCategory(), transaction.getPayee()));
        //Don't need to add anything to this method, the @PreAuthorize does the job.
    }

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_USER'))")
    @HandleBeforeDelete
    public void beforeTransactionDelete(Transaction transaction) {
        logger.debug(String.format("In before delete for a transaction with a category of '%s' and a payee of '%s'", transaction.getCategory(), transaction.getPayee()));
        //Don't need to add anything to this method, the @PreAuthorize does the job.
    }

}
