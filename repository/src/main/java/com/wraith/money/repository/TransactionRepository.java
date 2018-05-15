package com.wraith.money.repository;

import com.wraith.money.data.entity.Transaction;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * User: rowan.massey
 * Date: 24/02/13
 * Time: 15:48
 */
public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {
}
