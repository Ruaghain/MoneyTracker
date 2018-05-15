package com.wraith.money.repository;

import com.wraith.money.data.entity.Payee;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * User: rowan.massey
 * Date: 24/02/13
 * Time: 13:28
 */
public interface PayeeRepository extends PagingAndSortingRepository<Payee, Long> {

    public List<Payee> findByName(@Param("payeeName") String payeeName);

}
