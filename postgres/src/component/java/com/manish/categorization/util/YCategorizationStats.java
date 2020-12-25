package com.manish.categorization.util;
/**
 * This class is used to gather categorization stats which will be logged in access logs.
 * @author mgarg
 *
 */
public class YCategorizationStats {

	/**
	 * No of txns being sent in the categorisation request
	 */
	private long txnsCount;
	/**
	 * Total time taken for categorization for processing the txns
	 */
	private long meerkatTimeTaken;
	/**
	 * Time taken by Legacy Keywords based logic for processing the txns
	 */
	private long totalTimeTaken;	
	/**
	 * UniqueTrackingId for very request
	 */
	
	private Long cobrandId;
	
	private Long regionId;
	
	private String uniqueTrackingId;
	public long getTxnsCount() {
		return txnsCount;
	}
	public void setTxnsCount(long txnsCount) {
		this.txnsCount = txnsCount;
	}
	public long getMeerkatTimeTaken() {
		return meerkatTimeTaken;
	}
	public void setMeerkatTimeTaken(long meerkatTimeTaken) {
		this.meerkatTimeTaken = meerkatTimeTaken;
	}
	
	public long getTotalTimeTaken() {
		return totalTimeTaken;
	}
	public void setTotalTimeTaken(long totalTimeTaken) {
		this.totalTimeTaken = totalTimeTaken;
	}
	public String getUniqueTrackingId() {
		return uniqueTrackingId;
	}
	public void setUniqueTrackingId(String uniqueTrackingId) {
		this.uniqueTrackingId = uniqueTrackingId;
	}
	
	public Long getCobrandId() {
		return cobrandId;
	}
	public void setCobrandId(Long cobrandId) {
		this.cobrandId = cobrandId;
	}	

	public Long getRegionId() {
		return regionId;
	}
	public void setRegionId(Long regionId) {
		this.regionId = regionId;
	}
	
	@Override
	public String toString() {
		return "cobrandId= " + cobrandId +", regionId="+ regionId + ", txnsCount="+txnsCount + ", meerkatTimeTaken=" + meerkatTimeTaken + ",  totalTimeTaken=" + totalTimeTaken ;
	}
	
}
