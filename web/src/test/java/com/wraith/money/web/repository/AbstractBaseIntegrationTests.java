package com.wraith.money.web.repository;

import com.wraith.money.repository.*;
import com.wraith.money.web.ApplicationConfig;
import com.wraith.money.web.helper.EntityRepositoryHelper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.inject.Inject;
import java.util.Collection;

/**
 * This is the base class for integration testing. It uses the testing configuration.
 *
 * @author rowan.massey
 */
@ActiveProfiles("testing")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ApplicationConfig.class)
@WebAppConfiguration
public abstract class AbstractBaseIntegrationTests extends AbstractTransactionalJUnit4SpringContextTests {

	public static final String CONTENT_TYPE = "application/hal+json";

	@Inject
	protected AuthenticationManager authenticationManager;
	protected Authentication admin;

	@Inject
	@Qualifier("repositoryExporterHandlerAdapter")
	protected RequestMappingHandlerAdapter handlerAdapter;

	@Inject
	@Qualifier("repositoryExporterHandlerMapping")
	protected RequestMappingHandlerMapping handlerMapping;

	@Inject
	protected UsersRepository usersRepository;

	@Inject
	protected CategoryRepository categoryRepository;

	@Inject
	protected CurrencyRepository currencyRepository;

	@Inject
	protected PayeeRepository payeeRepository;

	@Inject
	protected AccountRepository accountRepository;

	protected EntityRepositoryHelper entityRepositoryHelper;

	@Before
	public void setUp() {
		admin = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("Admin", "Passw0rd"));
		entityRepositoryHelper = new EntityRepositoryHelper(handlerMapping, handlerAdapter);
	}

	protected void authenticate(String userName, String password) {
		authenticate(userName, password, null);
	}

	protected void authenticate(String userName, String password, Collection<GrantedAuthority> authorities) {
		Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName,
				password, authorities));
		SecurityContextHolder.getContext().setAuthentication(authenticate);
	}
}
