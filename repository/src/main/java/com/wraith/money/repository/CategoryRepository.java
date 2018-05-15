package com.wraith.money.repository;

import com.wraith.money.data.entity.Category;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * This class provides for the database repository access for the Category entity.
 *
 * User: rowan.massey
 * Date: 24/02/13
 */
public interface CategoryRepository extends PagingAndSortingRepository<Category, Long> {

	public List<Category> findByName(String categoryName);

}
