package com.manish.categorization.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.manish.categorization.repository.entity.SiteInfoEntity;

public interface SiteNameRepo extends JpaRepository<SiteInfoEntity, String> {
	
	@Query(value ="select si.sum_info_id, si.site_id, st.name from sum_info si, site st where si.site_id=st.site_id", nativeQuery = true)
	public List<SiteInfoEntity> getSiteName();
	
	@Query(value ="select si.sum_info_id, si.site_id, st.name from sum_info si, site st where si.site_id=st.site_id and st.row_last_updated>sysdate-1", nativeQuery = true)
	public List<SiteInfoEntity> getIncrementalSiteName();
	
	
}
