package com.manish.categorization.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.manish.categorization.repository.entity.RegionEntity;
/**
 * 
 * @author mgarg
 *
 */
public interface RegionRepo extends JpaRepository<RegionEntity, String>{

	@Query(value = "select region_id,region_name,tde_services from region", nativeQuery = true)
	public List<RegionEntity> getRegionEntity();

}