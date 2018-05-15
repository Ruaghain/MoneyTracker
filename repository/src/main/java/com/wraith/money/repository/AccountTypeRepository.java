package com.wraith.money.repository;

import com.wraith.money.data.entity.AccountType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * User: rowan.massey
 * Date: 24/02/13
 * Time: 22:56
 */
public interface AccountTypeRepository extends PagingAndSortingRepository<AccountType, Long> {

    @RestResource
    @Query("SELECT u FROM AccountType u WHERE name LIKE :name")
    List<AccountType> findByName(@Param("name") String name);
}
