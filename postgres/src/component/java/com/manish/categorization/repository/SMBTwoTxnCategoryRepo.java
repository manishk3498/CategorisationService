package com.manish.categorization.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.manish.categorization.repository.entity.SMBTwoTxnCategoryEntity;
/**
 * 
 * @author smohanty
 *
 */
public interface SMBTwoTxnCategoryRepo extends JpaRepository<SMBTwoTxnCategoryEntity, String>{

	@Query(value = "SELECT SMB2_TXN_CATEGORY_ID,SMB2_TXN_CATEGORY_NAME FROM SMB2_TXN_CATEGORY WHERE IS_DELETED=0 ", nativeQuery = true)
	public List<SMBTwoTxnCategoryEntity> getSMBTwoTxnCategoryEntity();

}
