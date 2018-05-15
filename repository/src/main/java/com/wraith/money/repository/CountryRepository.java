package com.wraith.money.repository;

import com.wraith.money.data.entity.Country;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * User: rowan.massey
 * Date: 24/02/13
 * Time: 16:34
 */
public interface CountryRepository extends PagingAndSortingRepository<Country, Long> {

    public List<Country> findByIso(String iso);

}
