package com.manish.categorization.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.manish.categorization.repository.entity.TxnMerchantTypeEntity;
/**
 * 
 * @author smohanty
 *
 */
public interface TxnMerchantTypeRepo extends JpaRepository<TxnMerchantTypeEntity, String>{

	@Query(value = "select txn_merchant_type_id,txn_merchant_type_name from txn_merchant_type", nativeQuery = true)
	public List<TxnMerchantTypeEntity> getTxnMerchantTypeEntity();

}
