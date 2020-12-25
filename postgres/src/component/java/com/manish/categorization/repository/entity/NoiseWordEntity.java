package com.manish.categorization.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
/**
 * 
 * @author mgarg
 *
 */
@Entity
public class NoiseWordEntity implements Serializable{
	
	@Id
	@Column(name="noise_word_ext_id")
	private Long noiseWordExtId;

	@Column(name="noise_word")
	private String noiseWord;

	public Long getNoiseWordExtId() {
		return noiseWordExtId;
	}

	public void setNoiseWordExtId(Long noiseWordExtId) {
		this.noiseWordExtId = noiseWordExtId;
	}

	public String getNoiseWord() {
		return noiseWord;
	}

	public void setNoiseWord(String noiseWord) {
		this.noiseWord = noiseWord;
	}
	
	
	
}
