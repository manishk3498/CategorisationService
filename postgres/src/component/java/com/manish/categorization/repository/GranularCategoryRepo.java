package com.manish.categorization.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.manish.categorization.repository.entity.GranularCategoryEntity;
import com.manish.categorization.repository.entity.MCCTxnCategoryEntity;
/**
 * 
 * @author mgarg
 *
 */
public interface GranularCategoryRepo extends JpaRepository<GranularCategoryEntity, String>{

	@Query(value = "select gcm.granular_txn_cat_map_id,gc.granular_category_id,gcm.transaction_category_id,gcm.transaction_base_type_id,tc.transaction_category_name,gc.granular_category_name,gcm.is_merged_catch_all_cat,gcm.is_catch_all_cat from granular_txn_cat_map gcm,transaction_category tc,granular_category gc where gcm.granular_category_id=gc.granular_category_id and gcm.transaction_category_id=tc.transaction_category_id and tc.is_small_business=0 and tc.region_id=1 and tc.is_deleted=0 and gc.is_deleted=0 and gcm.is_deleted=0", nativeQuery = true)
	public List<GranularCategoryEntity> getGranularCategoryEntity();
	
	@Query(value = "select gcm.granular_txn_cat_map_id,gc.granular_category_id,gcm.transaction_category_id,gcm.transaction_base_type_id,tc.transaction_category_name,gc.granular_category_name,gcm.is_merged_catch_all_cat,gcm.is_catch_all_cat from granular_txn_cat_map gcm,transaction_category tc,granular_category gc where gcm.granular_category_id=gc.granular_category_id and gcm.transaction_category_id=tc.transaction_category_id and tc.is_small_business=0 and tc.region_id=1 and tc.is_deleted=0 and gc.is_deleted=0 and gcm.is_deleted=0"
			+ " and (gc.row_last_updated>sysdate-1 or gcm.row_last_updated>sysdate-1)", nativeQuery = true)
	public List<GranularCategoryEntity> getIncrementalGranularCategoryEntity();
	
}
