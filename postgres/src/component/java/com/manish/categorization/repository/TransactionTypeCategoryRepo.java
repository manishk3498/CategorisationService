package com.manish.categorization.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.manish.categorization.repository.entity.TransactionTypeCategoryEntity;
/**
 * 
 * @author mgarg
 *
 */
public interface TransactionTypeCategoryRepo extends JpaRepository<TransactionTypeCategoryEntity, String> {
	
	@Query(value ="select ittcm.inv_trans_type_cat_map_id as inv_trans_type_cat_map_id,ittcm.transaction_category_id as transaction_category_id,tt.transaction_type as transaction_type,tc.transaction_category_name as transaction_category_name from inv_trans_type_cat_map ittcm,transaction_type tt,transaction_category tc where ittcm.transaction_type_id=tt.transaction_type_id and ittcm.transaction_category_id=tc.transaction_category_id", nativeQuery = true)
	public List<TransactionTypeCategoryEntity> getTransactionTypeCategoryEntity();
}