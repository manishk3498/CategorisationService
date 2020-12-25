package com.manish.categorization.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.manish.categorization.repository.entity.TransactionClassificationEntity;


public interface TransactionClassificationRepo extends JpaRepository<TransactionClassificationEntity, String> {
	
	@Query(value ="select mtt.meerkat_txn_type_id, mtt.type_name meerkat_txn_type, mtst.meerkat_txn_sub_type_id, mtst.type_name meerkat_txn_subtype, mtc.meerkat_txn_classifier_id, mtc.classification "
				+ "from meerkat_txn_classifier mtc "
				+ "left join meerkat_txn_type mtt on mtc.meerkat_txn_type_id=mtt.meerkat_txn_type_id  "
				+ "left join meerkat_txn_sub_type mtst  on mtc.meerkat_txn_sub_type_id=mtst.meerkat_txn_sub_type_id", nativeQuery = true)
	public List<TransactionClassificationEntity> getTransactionClassificationInfo();
	
	@Query(value ="select mtt.meerkat_txn_type_id, mtt.type_name meerkat_txn_type, mtst.meerkat_txn_sub_type_id, mtst.type_name meerkat_txn_subtype, mtc.meerkat_txn_classifier_id, mtc.classification "
			+ "from meerkat_txn_classifier mtc "
			+ "left join meerkat_txn_type mtt on mtc.meerkat_txn_type_id=mtt.meerkat_txn_type_id  "
			+ "left join meerkat_txn_sub_type mtst  on mtc.meerkat_txn_sub_type_id=mtst.meerkat_txn_sub_type_id where mtc.row_last_updated>sysdate-1", nativeQuery = true)
	public List<TransactionClassificationEntity> getIncrementalTransactionClassificationInfo();
	
}
