package com.manish.categorization.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.manish.categorization.repository.entity.SlamBangWordEntity;

public interface SlamBangWordRepo extends JpaRepository<SlamBangWordEntity, String> {
	
	@Query(value ="SELECT sbw.SLAM_BANG_WORD_ID as SLAM_BANG_WORD_ID,  sbw.SLAM_BANG_WORD as SLAM_BANG_WORD,  sbw.MERCHANT_ID as MERCHANT_ID, m.MERCHANT_NAME as MERCHANT_NAME, "
			+ "sbw.SUM_INFO_ID as SUM_INFO_ID,  tc.TRANSACTION_CATEGORY_NAME as TRANSACTION_CATEGORY_NAME, tt.TRANSACTION_TYPE as TRANSACTION_TYPE,  sbw.REGION_ID as REGION_ID, "
			+ "sbw.PRIORITY as PRIORITY,m.IS_DUMMY as IS_DUMMY,sbw.IS_DELETED as IS_DELETED,temp.containers as CONTAINERS, "
			+ "m.IS_DELETED as IS_MERCHANT_DELETED FROM SLAM_BANG_WORD sbw "
			+ "LEFT JOIN MERCHANT m on m.MERCHANT_ID=sbw.MERCHANT_ID "
			+ "LEFT JOIN TRANSACTION_CATEGORY tc on tc.TRANSACTION_CATEGORY_ID=sbw.TRANSACTION_CATEGORY_ID "
			+ "LEFT JOIN TRANSACTION_TYPE tt on tt.TRANSACTION_TYPE_id=sbw.TRANSACTION_TYPE_id "
			+ "LEFT JOIN (SELECT sbwtm.slam_bang_word_id,LISTAGG(t.tag, ',') WITHIN GROUP (ORDER BY t.tag) AS containers "
			+ "FROM slam_bang_word_tag_map sbwtm,tag t WHERE sbwtm.tag_id=t.tag_id AND sbwtm.is_deleted=0 GROUP BY sbwtm.slam_bang_word_id) temp"
			+ " ON sbw.slam_bang_word_id=temp.slam_bang_word_id "
			+ "WHERE tc.is_small_business = 0 and sbw.IS_DELETED=0", nativeQuery = true)
	public List<SlamBangWordEntity> getSlamBangWordEntity();
	
	@Query(value ="SELECT sbw.SLAM_BANG_WORD_ID as SLAM_BANG_WORD_ID,  sbw.SLAM_BANG_WORD as SLAM_BANG_WORD,  sbw.MERCHANT_ID as MERCHANT_ID, m.MERCHANT_NAME as MERCHANT_NAME, "
			+ "sbw.SUM_INFO_ID as SUM_INFO_ID,  tc.TRANSACTION_CATEGORY_NAME as TRANSACTION_CATEGORY_NAME, tt.TRANSACTION_TYPE as TRANSACTION_TYPE,  sbw.REGION_ID as REGION_ID, "
			+ "sbw.PRIORITY as PRIORITY,m.IS_DUMMY as IS_DUMMY,sbw.IS_DELETED as IS_DELETED,temp.containers as CONTAINERS FROM SLAM_BANG_WORD sbw "
			+ "LEFT JOIN MERCHANT m on m.MERCHANT_ID=sbw.MERCHANT_ID "
			+ "LEFT JOIN TRANSACTION_CATEGORY tc on tc.TRANSACTION_CATEGORY_ID=sbw.TRANSACTION_CATEGORY_ID "
			+ "LEFT JOIN TRANSACTION_TYPE tt on tt.TRANSACTION_TYPE_id=sbw.TRANSACTION_TYPE_id "
			+ "LEFT JOIN (SELECT sbwtm.slam_bang_word_id,LISTAGG(t.tag, ',') WITHIN GROUP (ORDER BY t.tag) AS containers "
			+ "FROM slam_bang_word_tag_map sbwtm,tag t WHERE sbwtm.tag_id=t.tag_id AND sbwtm.is_deleted=0 GROUP BY sbwtm.slam_bang_word_id) temp"
			+ " ON sbw.slam_bang_word_id=temp.slam_bang_word_id "
			+ "WHERE sbw.row_last_updated>sysdate-1 and tc.is_small_business = 0", nativeQuery = true)
	public List<SlamBangWordEntity> getIncrementalSlamBangWords();
	
}
