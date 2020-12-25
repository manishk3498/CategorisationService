package com.manish.categorization.algo.legacy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.manish.categorization.db.SlamBangWord;
import com.manish.categorization.util.Constants;


/**
 * This class defines the rule for Categorization of Transaction description
 */
public class CategorizationPattern implements Serializable {
	
	private int merchant;
	private int sumInfo;
	private int region;
	private int globalRegion;
	private int priority;
	private String categorizationSource;
	
	public CategorizationPattern(int merchant, int sumInfo, int region, int globalRegion, int priority, String categorizationSource) {
		super();
		this.merchant = merchant;
		this.sumInfo = sumInfo;
		this.region = region;
		this.globalRegion = globalRegion;
		this.priority = priority;
		this.categorizationSource=categorizationSource;
	}

	public CategorizationPattern(int merchant, int sumInfo, int region, 
			 int globalRegion) {
		super();
		this.merchant = merchant;
		this.sumInfo = sumInfo;
		this.region = region;
		this.globalRegion = globalRegion;
	}
	public CategorizationPattern() {
		
	}

	public static List<CategorizationPattern> patterns = new ArrayList<CategorizationPattern>();
	
	
	static {
		setPatterns();
	}
	
	/**
	 * This method sets the rules in order based on the priority
	 */
	private static void setPatterns() {
		//merchant sum_info, region, global_region
		patterns.add(new CategorizationPattern(1, 1, 0, 1, 1,"4")); //M:S:GR
		patterns.add(new CategorizationPattern(0, 1, 0, 1, 2,"4")); //S:GR
		patterns.add(new CategorizationPattern(1, 1, 1, 0, 3,"4")); //M:S:R
		patterns.add(new CategorizationPattern(0, 1, 1, 0, 4,"4")); //S:R
	
		

		//Gloabl rules
		patterns.add(new CategorizationPattern(1, 0, 0, 1, 5,"23")); //M:GR
		patterns.add(new CategorizationPattern(0, 0, 0, 1, 6,"23")); //GR
		patterns.add(new CategorizationPattern(1, 0, 1, 0, 7,"5")); //M:R
		patterns.add(new CategorizationPattern(0, 0, 1, 0, 8,"5")); //R
	}
	
	/**
	 * This method returns categorization pattern for the given set of inputs 
	 * @param slamBangWord
	 * @param sumInfo
	 * @param region
	 * @return
	 */
	public static CategorizationPattern getPattern(SlamBangWord slamBangWord, Long sumInfo,
			Long region){
		int merchant = slamBangWord.getMerchantId() != null ? 1 : 0;
		int sumInfoFlag = slamBangWord.getSumInfoId() != null && sumInfo != null && slamBangWord.getSumInfoId().equals(sumInfo) ? 1 : 0;
		int regionFlag = region != null && slamBangWord.getRegionId() != null && !slamBangWord.getRegionId().equals(Constants.ZERO_LONG) && slamBangWord.getRegionId().equals(region) ? 1 : 0;
		int globalRegionFlag = slamBangWord.getRegionId() != null && slamBangWord.getRegionId().equals(Constants.ZERO_LONG)?1:0; 
		if (sumInfoFlag==1) {
			if(regionFlag == 1) {
				globalRegionFlag = 0;
			}
			return new CategorizationPattern(merchant, sumInfoFlag, regionFlag, globalRegionFlag);
		}
		if(regionFlag == 1) {
			globalRegionFlag = 0;
		}
		return new CategorizationPattern(merchant, sumInfoFlag, regionFlag, globalRegionFlag);
	}
	


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + merchant;
		result = prime * result + sumInfo;
		result = prime * result + region;
		result = prime * result + globalRegion;
		result = prime * result + priority;
		return result;
	}

	@Override
	public boolean equals(Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (getClass() != object.getClass())
			return false;
		CategorizationPattern otherObject = (CategorizationPattern) object;
		if (merchant != otherObject.merchant)
			return false;
		if (sumInfo != otherObject.sumInfo)
			return false;
		if (region != otherObject.region)
			return false;
		if (globalRegion != otherObject.globalRegion)
			return false;
		return true;
	}
	
	public int getPriority() {
		return priority;
	}

	public String getCategorizationSource() {
		return categorizationSource;
	}
	
}
