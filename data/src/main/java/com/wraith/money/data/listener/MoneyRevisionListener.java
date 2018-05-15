package com.wraith.money.data.listener;

import com.wraith.money.data.audit.Revision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import java.net.InetAddress;
import java.util.Calendar;

/**
 * User: rowan.massey
 * Date: 29/03/13
 */
public class MoneyRevisionListener implements org.hibernate.envers.RevisionListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void newRevision(Object revisionEntity) {
        logger.debug("Adding new revision to the database.");

        SecurityContext securityContext = SecurityContextHolder.getContext();
        String currentUser = "Unknown User";
        String currentIPAddress = getServerIPAddress();

        if (securityContext.getAuthentication() != null) {
            logger.debug("Authentication information exists. Retrieving required information.");

            currentUser = ((User) securityContext.getAuthentication().getPrincipal()).getUsername();
            WebAuthenticationDetails details = (WebAuthenticationDetails) securityContext.getAuthentication().getDetails();
            currentIPAddress = details.getRemoteAddress();
        }

        Revision revision = ((Revision) revisionEntity);
        revision.setIpAddress(currentIPAddress);
        revision.setRevisionDate(Calendar.getInstance().getTime());
        revision.setUserName(currentUser);
    }

    /**
     * This method retrieves the current servers IP address.
     *
     * @return A string containing the IP address of the server.
     */
    private String getServerIPAddress() {
        String ipAddress = "";
        try {
            ipAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            logger.warn("There was a problem retrieving the current servers IP address.");
        }
        return ipAddress;
    }

}
