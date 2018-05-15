package com.wraith.money.repository;

import com.wraith.money.data.entity.Account;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * User: rowan.massey
 * Date: 24/01/13
 * Time: 21:56
 */
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {

    public List<Account> findByName(@Param("accountName") String accountName);

}
