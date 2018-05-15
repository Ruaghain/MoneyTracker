package com.wraith.money.web.security;

import com.wraith.money.data.entity.Authorities;
import com.wraith.money.data.entity.Groups;
import com.wraith.money.data.entity.Users;
import com.wraith.money.repository.UsersRepository;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Collection;

/**
 * User: rowan.massey
 * Date: 12/08/12
 */
@Component
public class MoneyUserDetailsService extends JdbcDaoSupport implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    private UsersRepository usersRepository;

    @Inject
    public MoneyUserDetailsService(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug(String.format("Loading user with user name '%s'", username));

        UserDetails user = findUserByUsername(username);
        if (user == null) {
            String error = String.format("Query for user '%s' failed. User not found.", username);
            logger.error(error);
            throw new UsernameNotFoundException(error);
        }

        return user;
    }

    protected UserDetails findUserByUsername(String username) {
        logger.debug(String.format("Finding user with user name '%s'", username));

        Users user = usersRepository.findByUserName(username).get(0);

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        for (Groups group : user.getGroups()) {
            for (Authorities authority : group.getAuthorities()) {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(authority.getAuthority());
                authorities.add(grantedAuthority);
            }
        }

        return new User(user.getUserName(), user.getPassword(), BooleanUtils.toBoolean(user.getEnabled()), true, true, true, authorities);
    }
}
