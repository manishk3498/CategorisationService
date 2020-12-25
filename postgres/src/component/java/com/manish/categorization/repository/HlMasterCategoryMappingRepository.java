package com.manish.categorization.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.manish.categorization.repository.entity.HLMasterMappingEntity;

/**
 * 
 * @author mgarg
 *
 */
public interface HlMasterCategoryMappingRepository extends JpaRepository<HLMasterMappingEntity, String>{

	@Query(value = "SELECT hlmc.HL_MASTER_CATEGORY_MAPPING_ID as HL_MASTER_CATEGORY_MAPPING_ID,hlmc.HL_TRANSACTION_CATEGORY_ID as HL_TRANSACTION_CATEGORY_ID, "
			+ " hlmc.TRANSACTION_CATEGORY_ID as TRANSACTION_CATEGORY_ID,hltc.HL_TRANSACTION_CATEGORY_NAME as HL_TXN_CATEGORY_NAME  "
			+ " from HL_MASTER_CATEGORY_MAPPING hlmc INNER JOIN HL_TRANSACTION_CATEGORY hltc ON hlmc.HL_TRANSACTION_CATEGORY_ID =hltc.HL_TRANSACTION_CATEGORY_ID "
			+ "  where hltc.IS_DELETED=0 and hlmc.IS_DELETED=0 order by hlmc.HL_MASTER_CATEGORY_MAPPING_ID", nativeQuery = true)
	public List<HLMasterMappingEntity> getHlMasterMappingEntity();
	
}