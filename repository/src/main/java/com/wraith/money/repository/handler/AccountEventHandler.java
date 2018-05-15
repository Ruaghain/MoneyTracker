package com.wraith.money.repository.handler;

import com.wraith.money.data.entity.Account;
import com.wraith.money.data.entity.Users;
import com.wraith.money.repository.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.inject.Inject;

/**
 * This class handles all of the events around the the account entity, and facilitates any business rules around each of
 * the create, update and delete actions.
 *
 * @author rowan.massey
 */
@RepositoryEventHandler(Account.class)
public class AccountEventHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    UsersRepository usersRepository;

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_USER'))")
    @HandleBeforeCreate
    public void beforeAccountCreate(Account account) {
        logger.debug(String.format("In before create for account '%s'", account.getName()));

        Authentication authorization = SecurityContextHolder.getContext().getAuthentication();
        Users currentUser = usersRepository.findByUserName(authorization.getName()).get(0);
        account.setUser(currentUser);
    }

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN'))")
    @HandleBeforeSave
    public void beforeAccountUpdate(Account account) {
        logger.debug(String.format("In before update for account '%s'", account.getName()));
        //Don't need to add anything to this method, the @PreAuthorize does the job.
    }

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN'))")
    @HandleBeforeDelete
    public void beforeAccountDelete(Account account) {
        logger.debug(String.format("In before delete for account '%s'", account.getName()));
        //Don't need to add anything to this method, the @PreAuthorize does the job.
    }

}
