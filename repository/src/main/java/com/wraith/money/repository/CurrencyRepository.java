package com.wraith.money.repository;

import com.wraith.money.data.entity.Currency;
import com.wraith.money.data.entity.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * User: rowan.massey
 * Date: 24/02/13
 */
public interface CurrencyRepository extends PagingAndSortingRepository<Currency, Long> {

    List<Currency> findByIso(@Param("currencyIso") String currencyIso);

    @RestResource
    @Query("SELECT u FROM Currency u WHERE name LIKE :name")
    List<Users> findByName(@Param("name") String name);

}
