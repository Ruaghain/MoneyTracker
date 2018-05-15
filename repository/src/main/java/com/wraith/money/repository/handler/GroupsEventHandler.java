package com.wraith.money.repository.handler;

import com.wraith.money.data.entity.Groups;
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
 * Time: 13:14
 */
@RepositoryEventHandler(Groups.class)
public class GroupsEventHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN'))")
    @HandleBeforeCreate
    public void beforeGroupsCreate(Groups groups) {
        logger.debug(String.format("In before create for groups '%s'", groups.getName()));
    }

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN'))")
    @HandleBeforeSave
    public void beforeGroupsUpdate(Groups groups) {
        logger.debug(String.format("In before update for groups '%s'", groups.getName()));
        //Don't need to add anything to this method, the @PreAuthorize does the job.
    }

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN'))")
    @HandleBeforeDelete
    public void beforeGroupsDelete(Groups groups) {
        logger.debug(String.format("In before delete for groups '%s'", groups.getName()));
        //Don't need to add anything to this method, the @PreAuthorize does the job.
    }
}
