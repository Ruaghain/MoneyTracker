package com.wraith.money.repository;

import com.wraith.money.data.entity.Authorities;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

/**
 * User: rowan.massey Date: 17/03/13 Time: 15:40
 */
@RestResource(rel = "authorities", path = "/authorities")
public interface AuthoritiesRepository extends PagingAndSortingRepository<Authorities, Long> {

	public List<Authorities> findByAuthority(@Param("authorityName") String authorityName);

}
