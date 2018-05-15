package com.wraith.money.web.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.wraith.money.data.entity.Account;
import com.wraith.money.data.entity.AccountType;
import com.wraith.money.data.entity.Currency;
import com.wraith.money.data.entity.Users;
import com.wraith.money.web.exception.MoneyException;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * User: rowan.massey Date: 24/02/13 Time: 20:07
 */
@Configuration
public class ApplicationRestConfig extends RepositoryRestMvcConfiguration {

    @Override
    protected void configureJacksonObjectMapper(ObjectMapper objectMapper) {
        //The following code displays all dates in ISO8601 format in the JSON payload.
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        objectMapper.setDateFormat(new ISO8601DateFormat());
    }

    @Override
    protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        super.configureRepositoryRestConfiguration(config);
        try {
            config.setBaseUri(new URI("/api"));
            config.exposeIdsFor(Users.class, Currency.class, AccountType.class, Account.class);
        } catch (URISyntaxException e) {
            throw new MoneyException(e);
        }
    }
}
