package com.wraith.money.repository;

import com.wraith.money.data.entity.DataUpload;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RestResource;

/**
 * This interface facilitates database access to the DataUpload table.
 * <p/>
 * User: rowan.massey
 * Date: 26/09/2014
 * Time: 21:48
 */
@RestResource(rel = "dataUpload", path = "/dataUploads")
public interface DataUploadRepository extends PagingAndSortingRepository<DataUpload, Long> {

}
