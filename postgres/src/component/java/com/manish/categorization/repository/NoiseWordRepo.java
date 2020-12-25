package com.manish.categorization.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.manish.categorization.repository.entity.NoiseWordEntity;
/**
 * 
 * @author mgarg
 *
 */
public interface NoiseWordRepo extends JpaRepository<NoiseWordEntity, String>{

	@Query(value = "select noise_word_ext_id,noise_word from noise_word_ext where attribute_type_id=6 and is_deleted=0", nativeQuery = true)
	public List<NoiseWordEntity> getNoiseEntity();

	@Query(value = "select noise_word_ext_id,noise_word from noise_word_ext where attribute_type_id=6 and row_last_updated>sysdate-1 and is_deleted=0", nativeQuery = true)
	public List<NoiseWordEntity> getIncrementalNoiseWords();

}
