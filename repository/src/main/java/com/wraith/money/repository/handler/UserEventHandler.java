package com.wraith.money.repository.handler;

import com.wraith.money.data.entity.Groups;
import com.wraith.money.data.entity.Users;
import com.wraith.money.repository.CountryRepository;
import com.wraith.money.repository.GroupsRepository;
import com.wraith.money.repository.encoding.Encoding;
import com.wraith.money.repository.exception.RepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.inject.Inject;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

/**
 * User: rowan.massey
 * Date: 24/02/13
 * Time: 21:00
 */
@RepositoryEventHandler(Users.class)
public class UserEventHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Inject
    CountryRepository countryRepository;
    @Inject
    private Encoding encoding;
    @Inject
    private GroupsRepository groupsRepository;

    @HandleBeforeCreate
    public void beforeUserCreate(Users user) {
        logger.debug(String.format("In before create for user '%s'", user.getFullName()));
        user.setEnabled(1);
        user.setGroups(getUserRoleGroup());
        user.setPassword(getCreatedUserPassword(user.getUserName(), user.getPassword()));
    }

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #user.getUserName() == authentication.name))")
    @HandleBeforeSave
    public void beforeUserUpdate(Users user) {
        //Don't need to add anything to this method, the @PreAuthorize does the job.
    }

    @PreAuthorize("isAuthenticated() and (hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #user.getUserName() == authentication.name))")
    @HandleBeforeDelete
    public void beforeUserDelete(Users user) {
        //TODO: Need to have at least ONE admin user available. Check for it here, and bail on the delete.
        //Don't need to add anything to this method, the @PreAuthorize does the job.
    }

    /**
     * This method encodes the user's password
     *
     * @param userName The userName of the created user.
     * @param password The original password of the create user.
     */
    private String getCreatedUserPassword(String userName, String password) {
        logger.debug(String.format("Encoding password for user '%s'", userName));
        try {
            return encoding.encodePassword(password);
        } catch (NoSuchAlgorithmException e) {
            logger.error(String.format("Error encoding user password. Error was '%s'", e.getMessage()), e);
            throw new RepositoryException(String.format("There was an error encoding the user password for user '%s'", userName));
        }
    }

    /**
     * This method searches for the "Users" group in the groups repository.
     *
     * @return A set, containing the found users group.
     */
    private Set<Groups> getUserRoleGroup() {
        Set<Groups> userGroupsSet = new HashSet<>();
        Groups userGroups = groupsRepository.findByName("Users").get(0);
        if (userGroups.getName().isEmpty()) {
            throw new RepositoryException("No group called 'Users' is defined for the user to be assigned to.");
        }
        userGroupsSet.add(userGroups);
        return userGroupsSet;
    }
}
