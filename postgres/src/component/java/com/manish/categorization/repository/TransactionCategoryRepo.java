package com.manish.categorization.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.manish.categorization.repository.entity.TransactionCategoryEntity;

public interface TransactionCategoryRepo extends JpaRepository<TransactionCategoryEntity, String> {
	
	@Query(value ="select tc.transaction_category_id, tc.transaction_category_name, tct.transaction_category_type, tc.region_id, tc.merged_txn_cat_id, tc.merged_txn_cat_name, tc.is_small_business, tc.categorisation_rule "
			+ "from transaction_category tc left join transaction_category_type tct on  tc.transaction_category_type_id=tct.transaction_category_type_id where tc.is_small_business=0", nativeQuery = true)
	public List<TransactionCategoryEntity> getTransactionCategoryEntity();
}