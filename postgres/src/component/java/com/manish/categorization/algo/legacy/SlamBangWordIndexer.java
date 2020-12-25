package com.manish.categorization.algo.legacy;


import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.reflect.TypeToken;
import com.manish.categorization.db.SlamBangWord;
import com.manish.categorization.repository.SlamBangWordRepo;
import com.manish.categorization.repository.entity.SlamBangWordEntity;


@Component
public final class SlamBangWordIndexer {

	private static final Logger logger = LogManager.getLogger(SlamBangWordIndexer.class);
	
	@Inject
	private SlamBangWordRepo slamBangWordRepo;
	
	@Autowired
	Directory luceneDirectory;

	@PostConstruct
	public void init() {/*
		if(logger.isInfoEnabled())
			logger.info("Initiaizing the SlamBangWordIndexer.");
		final Path dirIndexPath = Paths.get(indexDirPath);
		if (!Files.isReadable(dirIndexPath)) {
			String message = "Document directory '" + dirIndexPath.toAbsolutePath()
					+ "' does not exist or is not readable, please check the path";
			logger.error(message);
			throw new RuntimeException(message);
		}
		indexSlamBangWords(Boolean.TRUE);
		if(logger.isInfoEnabled())
			logger.info("Completed the SlamBangWordIndexer.");
	*/}

	public void indexSlamBangWords(boolean fullLoad) {

		Type listType = new TypeToken<List<SlamBangWord>>() {
		}.getType();
		
		List<SlamBangWord> slamBangWords = new ArrayList<SlamBangWord>();;
		SlamBangWord sbw = null;
		List<SlamBangWordEntity> slamBangWordsEntity = null;
		//TODO  check for slam_bang_word from db or json
			// Code from db
			if(fullLoad){
				slamBangWordsEntity=slamBangWordRepo.getSlamBangWordEntity();
			}else{
				slamBangWordsEntity=slamBangWordRepo.getIncrementalSlamBangWords();
			}
			for (Iterator iterator = slamBangWordsEntity.iterator(); iterator
					.hasNext();) {
				SlamBangWordEntity slamBangWordEntity = (SlamBangWordEntity) iterator
						.next();
				sbw = new SlamBangWord();
				sbw.setSlamBangWordId(slamBangWordEntity.getSlamBangWordId());
				sbw.setSlamBangWord(slamBangWordEntity.getSlamBangWord());
				sbw.setMerchantId(slamBangWordEntity.getMerchantId()!=null?slamBangWordEntity.getMerchantId():null);
				sbw.setMerchantName(slamBangWordEntity.getMerchantName()!=null?slamBangWordEntity.getMerchantName():"");
				sbw.setSumInfoId(slamBangWordEntity.getSumInfoId()!=null?slamBangWordEntity.getSumInfoId():null);
				sbw.setTransactionCategoryName(slamBangWordEntity.getTransactionCategoryName());
				sbw.setTransactionType(slamBangWordEntity.getTransactionType());
				sbw.setRegionId(slamBangWordEntity.getRegionId()!=null?slamBangWordEntity.getRegionId():null);
				sbw.setPriority(slamBangWordEntity.getPriority());
				sbw.setIsDummyMerchant(slamBangWordEntity.getIsDummyMerchant());
				slamBangWords.add(sbw);
			}
		try {
			//Directory dir = FSDirectory.open(Paths.get(indexDirPath));
			Analyzer analyzer = new StandardAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			if(fullLoad)
				iwc.setOpenMode(OpenMode.CREATE);
			else
				iwc.setOpenMode(OpenMode.CREATE_OR_APPEND);
			IndexWriter writer = new IndexWriter(luceneDirectory, iwc);
			for (SlamBangWord slamBangWord : slamBangWords) {
				Document document = createDocument(slamBangWord);
				if(document != null){
					writer.addDocument(document);
				}
					
			}
			writer.forceMerge(1);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Document createDocument(SlamBangWord slamBangWord) {
		Document document = new Document();
		document.add(new StringField("slamBangWordId", String.valueOf(slamBangWord.getSlamBangWordId()), Store.YES));
		document.add(new StringField("slamBangWord", slamBangWord.getSlamBangWord().toLowerCase(), Store.YES));
		if(slamBangWord.getSlamBangWord().trim().equals("")){
			return null;
		}
		String merchantId = slamBangWord.getMerchantId() != null ? String.valueOf(slamBangWord.getMerchantId()) : "";
		String sumInfoId = slamBangWord.getSumInfoId() != null ? String.valueOf(slamBangWord.getSumInfoId()) : "";
		document.add(new StringField("merchantId", merchantId, Store.YES));
		String merchantName = slamBangWord.getMerchantName()!=null?slamBangWord.getMerchantName():"";
		document.add(new StringField("merchant", merchantName , Store.YES));
		document.add(new StringField("sumInfoId", sumInfoId, Store.YES));
		document.add(new StringField("transactionCategoryName", slamBangWord.getTransactionCategoryName(), Store.YES));
		document.add(new StringField("transactionType", slamBangWord.getTransactionType(), Store.YES));
		String regionId = slamBangWord.getRegionId() != null ? String.valueOf(slamBangWord.getRegionId()) : "";
		document.add(new StringField("regionId", regionId, Store.YES));
		document.add(new StringField("priority", String.valueOf(slamBangWord.getPriority()), Store.YES));
		Long isDummyMerchant = slamBangWord.getIsDummyMerchant();
		String isDummyMerchantStr = isDummyMerchant != null ? String.valueOf(isDummyMerchant) : "0";
		document.add(new StringField("isDummy", isDummyMerchantStr, Store.YES));
		return document;
	}

	@PreDestroy
	public void cleanup() {
	}

    /**
    * This method is used to remove special characters and
    * replacing with the blank space
    * 
     * @param description
    * @return
    */
    public static String removeSpecialCharacters(String description) {
           String spaceSeparatedString = "";
           if (description == null || (description.trim()).length() == 0) {
                  return spaceSeparatedString;
           }

           String[] words = description.toLowerCase().split("[^\\(0-9)(a-z)(A-Z)]+");
           for (int i = 0; i < words.length; i++) {
                  if (i == 0) {
                        spaceSeparatedString += words[i];
                  } else {
                        spaceSeparatedString += " " + words[i];
                  }
           }
           return spaceSeparatedString.trim();
    }
}
