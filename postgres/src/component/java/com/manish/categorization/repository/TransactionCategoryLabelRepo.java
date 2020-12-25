package com.manish.categorization.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.manish.categorization.repository.entity.TransactionCategoryLabelEntity;

public interface TransactionCategoryLabelRepo extends JpaRepository<TransactionCategoryLabelEntity, String> {
	
	@Query(value ="select tclm.txn_cate_label_map_id,tclm.transaction_category_id, cl.label_name label from txn_cate_label_map tclm, category_label cl where tclm.category_label_id=cl.category_label_id and tclm.is_deleted=0 and cl.is_deleted=0 and tclm.label_set_id in (select label_set_id from txn_cate_label_map group by label_set_id having count(*)=1)", nativeQuery = true)
	public List<TransactionCategoryLabelEntity> getTransactionCategoryLabelEntity();
	
	
}