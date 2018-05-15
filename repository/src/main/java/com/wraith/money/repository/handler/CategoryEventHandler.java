package com.wraith.money.repository.handler;

import com.wraith.money.data.entity.Category;
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
 * Time: 20:31
 */
@RepositoryEventHandler(Category.class)
public class CategoryEventHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @HandleBeforeCreate
    public void beforeCategoryCreate(Category category) {
        logger.debug(String.format("In before create for category '%s'", category.getName()));
    }

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN'))")
    @HandleBeforeSave
    public void beforeCategoryUpdate(Category category) {
        logger.debug(String.format("In before update for category '%s'", category.getName()));
        //Don't need to add anything to this method, the @PreAuthorize does the job.
    }

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN'))")
    @HandleBeforeDelete
    public void beforeCategoryDelete(Category category) {
        logger.debug(String.format("In before delete for category '%s'", category.getName()));
        //Don't need to add anything to this method, the @PreAuthorize does the job.
    }
}
