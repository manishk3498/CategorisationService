package com.manish.categorization.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.manish.categorization.repository.entity.MCCTxnCategoryEntity;
/**
 * 
 * @author mgarg
 *
 */
public interface MCCTxnCatMapRepo extends JpaRepository<MCCTxnCategoryEntity, String>{

	@Query(value = "select mtcm.mcc_txn_category_map_id,mtcm.mc_code,tc.transaction_category_name from mcc_txn_category_map mtcm left join transaction_category tc on mtcm.transaction_category_id=tc.transaction_category_id", nativeQuery = true)
	public List<MCCTxnCategoryEntity> getMCCTxnCategoryEntity();

	@Query(value = "select mtcm.mcc_txn_category_map_id,mtcm.mc_code,tc.transaction_category_name from mcc_txn_category_map mtcm left join transaction_category tc on mtcm.transaction_category_id=tc.transaction_category_id where mtcm.row_last_updated>sysdate-1", nativeQuery = true)
	public List<MCCTxnCategoryEntity> getIncrementalMCCTxnCategoryEntity();
}
