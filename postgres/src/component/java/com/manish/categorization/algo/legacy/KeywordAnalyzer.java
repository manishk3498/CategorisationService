package com.manish.categorization.algo.legacy;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import org.apache.commons.collections4.Trie;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import com.manish.categorization.db.SlamBangWord;
import com.manish.categorization.util.Constants;

public class KeywordAnalyzer extends Analyzer {

	private static Logger logger = LogManager.getLogger(KeywordAnalyzer.class);
	
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		return new TokenStreamComponents(new WhitespaceTokenizer());
	}
	
	private List<String> tokensInternal(String text) {
		List<String> tokens = new ArrayList<String>();
		List<String> words = new ArrayList<String>();
		TokenStream stream = tokenStream("field", new StringReader(text));
		CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
		try {
			stream.reset();
			while (stream.incrementToken()) {
				String token = termAtt.toString();
				words.add(token);
			}
			stream.end();
		} catch (IOException e) {
			if(logger.isInfoEnabled()){
				logger.info("Exceptions while creating tokens ",e);
			}
		} finally {
			try {
				stream.close();
			} catch (IOException e) {
				if(logger.isInfoEnabled()){
					logger.info("Exceptions while creating tokens ",e);
				}
			}
		}
		tokens.addAll(words);
		for (int i = 0; i < words.size(); i++) {
			String word = words.get(i);
			StringBuffer sb = new StringBuffer(word);
			for (int j = i + 1; j < words.size(); j++) {
				sb.append(Constants.SPACE);
				sb.append(words.get(j));
				tokens.add(sb.toString());
			}

		}
		return tokens;
	}

	public Set<String> tokens(String text) {
		Set<String> tokensSet = new HashSet<String>();
		text = text.toLowerCase();
		String cleanText = clean(text);
		List<String> tokens = tokensInternal(text);
		if(tokens != null && !tokens.isEmpty()){
			tokensSet.addAll(tokens);
		}
		if(!text.equals(cleanText)){
			List<String> cleanedTokens = tokensInternal(cleanText);
			if(cleanedTokens != null && !cleanedTokens.isEmpty()){
				tokensSet.addAll(cleanedTokens);
			}
		}
		return tokensSet;
	}

	public String clean(String description) {
		String spaceSeparatedString = "";
		if (description == null || (description.trim()).length() == 0) {
			return spaceSeparatedString;
		}
		String[] words = description.split("[^\\p{L}]+");
		return String.join(Constants.SPACE, words);
	}
}
