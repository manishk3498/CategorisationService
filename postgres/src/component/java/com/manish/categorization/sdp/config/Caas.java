package com.manish.categorization.sdp.config;

import com.yodlee.sdp.config.mapping.ConfigProperty;

/**
 * 
 * @author smohanty
 *
 */
public class Caas {

	@ConfigProperty("COM.YODLEE.CAAS.SPRING_HTTP_MULTIPART_MAX_FILE_SIZE")
	private String springMultipartMaxFileSize;
	
	@ConfigProperty("COM.YODLEE.CAAS.SPRING_HTTP_MULTIPART_MAX_REQUEST_SIZE")
	private String springMultipartMaxRequestSize;
	
	@ConfigProperty("COM.YODLEE.CAAS.PROXY_HOST")
	private String proxyHost;
	
	@ConfigProperty("COM.YODLEE.CAAS.PROXY_PORT")
	private int proxyPort;
	
	@ConfigProperty("COM.YODLEE.CAAS.PROXY_ENABLED")
	private boolean proxyEnabled;
	
	@ConfigProperty("COM.YODLEE.CAAS.BATCH_SIZE")
	private String batchSize;
	
	@ConfigProperty("COM.YODLEE.CAAS.CONNECTION_TIMEOUT")
	private int connectionTimeout;
	
	@ConfigProperty("COM.YODLEE.CAAS.SOCKET_TIMEOUT")
	private int socketTimeout;
	
	@ConfigProperty("COM.YODLEE.CAAS.MAXIMUM_CONNECTIONS")
	private int maximumConnectionsPerRoute;
	
	@ConfigProperty("COM.YODLEE.CAAS.MAXIMUM_CONNECTIONS_PER_ROUTE")
	private int maxConnections;
	
	@ConfigProperty("COM.YODLEE.CAAS.INACTIVITY_TIME")
	private int inactivityTimeInms;
	
	@ConfigProperty("COM.YODLEE.CAAS.MEERKAT_URL")
	private String meerkatUrl;

	@ConfigProperty("COM.YODLEE.CAAS.SERVER_COMPRESSION_ENABLED")
	private boolean serverCompressionEnabled;

	@ConfigProperty("COM.YODLEE.CAAS.SERVER_COMPRESSION_MIME_TYPES")
	private String serverCompressionMimeTypes;

	@ConfigProperty("COM.YODLEE.CAAS.LUCENE_SLAMBANG_DIR")
	private String luceneSlambangDir;

	@ConfigProperty("COM.YODLEE.CAAS.TEMP_DIR")
	private String tempDir;

	@ConfigProperty("COM.YODLEE.CAAS.READ_FROM_DB")
	private boolean readFromDb;

	@ConfigProperty("COM.YODLEE.CAAS.SPEND_INCLUSION")
	private String spendInclusion;

	@ConfigProperty("COM.YODLEE.CAAS.NON_SPEND_INCLUSION")
	private String nonSpendInclusion;
	
	@ConfigProperty("COM.YODLEE.CAAS.DEBUG_THRESHOLD_CATEGORY_SCORE")
	private String thresholdCategoryScore;
	
	@ConfigProperty("COM.YODLEE.CAAS.DEBUG_THRESHOLD_SUBTYPE_SCORE")
	private String thresholdSubtypeScore;
	
	@ConfigProperty("COM.YODLEE.CAAS.DEBUG_THRESHOLD_MERCHANT_SCORE")
	private String thresholdMerchantScore;
	
	@ConfigProperty("COM.YODLEE.CAAS.SPRING_DATASOURCE_URL")
	private String springDataSourceUrl;
	
	@ConfigProperty("COM.YODLEE.CAAS.SPRING_DATASOURCE_USERNAME")
	private String springDataSourceUserName;
	
	@ConfigProperty("COM.YODLEE.CAAS.SPRING_DATASOURCE_PASSWORD")
	private String springDataSourcePassword;
	
	@ConfigProperty("COM.YODLEE.CAAS.SPRING_DATASOURCE_DRIVER_CLASS_NAME")
	private String springDataSourceDriverClassName;
	
	@ConfigProperty("COM.YODLEE.CAAS.SPRING_JPA_DATABASE_PLATFORM")
	private String springJpaDatabasePlatform;
	
	@ConfigProperty("COM.YODLEE.CAAS.SPRING_JPA_SHOW_SQL")
	private String springJpaShowSQL;
	
	@ConfigProperty("COM.YODLEE.CAAS.MEERKAT_THREADS_COUNT")
	private int numberOfMeerkatThreads;
	
	@ConfigProperty("COM.YODLEE.CAAS.LEGACY_THREADS_COUNT")
	private int numberOfLegacyThreads;
	
	@ConfigProperty("COM.YODLEE.CAAS.MEERKAT_THREAD_TIMEOUT")
	private long meerkatThreadTimeout;
	
	@ConfigProperty("COM.YODLEE.CAAS.PARALLEL_CATEGORIZATION_ENABLED")
	private boolean parallelEnabled;
	
	@ConfigProperty("COM.YODLEE.CAAS.LEGACY_THREAD_TIMEOUT")
	private long legacyThreadTimeout;
	
	@ConfigProperty("COM.YODLEE.CAAS.LEGACY_BATCH_SIZE")
	private int legacyBatchSize;
	
	@ConfigProperty("COM.YODLEE.CAAS.QUEUE_THREADS_LIMIT")
	private int queueThreadsLimit;
	
	@ConfigProperty("COM.YODLEE.CAAS.EXTRA_THREAD_DELAY")
	private long extraThreadDelay;
	
	@ConfigProperty("COM.YODLEE.CAAS.INCREMENTAL_CACHE_RELOAD_TIMEINTERVAL")
	private String incrementalCacheReloadTimeInterval;

	@ConfigProperty("COM.YODLEE.CAAS.CORE_SUPRESS_ZERO_IN_CHECK_NUM_FROM_CHECK_DESC")
	private String supressZeroInCheckNumFromCheckDesc;

	@ConfigProperty("COM.YODLEE.CAAS.CORE_CHECK_NUMBER_LENGTH_IN_CHECK_DESC")
	private String checkNumberLengthInCheckDesc;

	@ConfigProperty("COM.YODLEE.CAAS.CORE_APPENDED_ZERO_IN_CHECK_NUMBER_MIN_COUNT")
	private String appendedZeroInCheckNumberMinCount;

	@ConfigProperty("COM.YODLEE.CAAS.USE_TRIE_SLAMBANG_CACHE")
	private String useTriesSlambangCache;

	@ConfigProperty("COM.YODLEE.CAAS.USE_JSON_GRANULAR_CATEGORY_MAPPINGS")
	private String useJsonForLoadingGranularCategories;

	@ConfigProperty("COM.YODLEE.CAAS.LOGFILE_LOCATION")
	private String logFile;

	@ConfigProperty("COM.YODLEE.CAAS.LOGFILE_NAME_PREFIX")
	private String logFileNamePrefix;

	@ConfigProperty("COM.YODLEE.CAAS.LOGFILE_MAXSIZE")
	private String maxSize;

	@ConfigProperty("COM.YODLEE.CAAS.LOGFILE_MAXBACKUPINDEX")
	private String maxBackup;

	@ConfigProperty("COM.YODLEE.CAAS.INSTANCE")
	private String instance;

	@ConfigProperty("COM.YODLEE.CAAS.TDE_URL_REGION_1")
	private String meerkatUrlRegion1;
	
	@ConfigProperty("COM.YODLEE.CAAS.TDE_URL_REGION_2")
	private String meerkatUrlRegion2;
	
	@ConfigProperty("COM.YODLEE.CAAS.TDE_URL_REGION_3")
	private String meerkatUrlRegion3;
	
	@ConfigProperty("COM.YODLEE.CAAS.TDE_URL_REGION_4")
	private String meerkatUrlRegion4;

	@ConfigProperty("COM.YODLEE.CAAS.TDE_URL_REGION_7")
	private String meerkatUrlRegion7;

	@ConfigProperty("COM.YODLEE.CAAS.LOAN_TYPES")
	private String loanTypes;

	@ConfigProperty("COM.YODLEE.CAAS.INVESTMENT_TRANSACTION_TYPES")
	private String investmentTransactionTypes;

	@ConfigProperty("COM.YODLEE.CAAS.INVESTMENT_ACCOUNT_TYPES")
	private String investmentAccountTypes;

	@ConfigProperty("COM.YODLEE.CAAS.SUPPORTED_REGIONS_LOAN_AND_INVESTMENT")
	private String supportedRegionsForLoanAndInvestment;
	
	@ConfigProperty("COM.YODLEE.CAAS.SIMPLEDESC_VERSION")
	private String simpleDescriptionVersion;

	@ConfigProperty("COM.YODLEE.CAAS.POPULATE_ADDITIONAL_FIELDS")
	private boolean populateAdditionalFields;

	@ConfigProperty("COM.YODLEE.CAAS.POPULATE_ISPHYSICAL_FIELD")
	private boolean populateIsPhysical;

	@ConfigProperty("COM.YODLEE.CAAS.POPULATE_INTERMEDIARY_FIELD")
	private boolean populateIntermediary;

	@ConfigProperty("COM.YODLEE.CAAS.POPULATE_TXNMERCHANTTYPE_FIELD")
	private boolean populateTxnMerchantType;
	
	@ConfigProperty("COM.YODLEE.CAAS.POPULATE_LOGOENDPOINT_FIELD")
	private boolean populateLogoEndpoint;
	
	@ConfigProperty("COM.YODLEE.CAAS.POPULATE_DIGITAL_FIELD_FOR_LOAN_AND_INVESTMENT")
	private boolean populateDigitalFieldForLoanAndInvestment;
	
	@ConfigProperty("COM.YODLEE.CAAS.CONFIDENCE_SCORE_THRESHOLD")
	private int confidenceScoreThreshold;
	
	@ConfigProperty("COM.YODLEE.CAAS.CUSTOM_REST_CONNECTION_CONNECT_TIMEOUT")
	private int customRestConnectionTimeout;
	
	@ConfigProperty("COM.YODLEE.CAAS.CUSTOM_REST_CONNECTION_CONNECTION_REQUEST_TIMEOUT")
	private int customRestRequestTimeout;
	
	@ConfigProperty("COM.YODLEE.CAAS.CUSTOM_REST_CONNECTION_READ_TIMEOUT")
	private int customRestReadTimeout;
	
	@ConfigProperty("COM.YODLEE.CAAS.PARALLELISM")
	private int parallelism;
	
	
	public String getSpringMultipartMaxFileSize() {
		return springMultipartMaxFileSize;
	}
	
	public void setSpringMultipartMaxFileSize(String springMultipartMaxFileSize) {
		this.springMultipartMaxFileSize = springMultipartMaxFileSize;
	}
	
	public String getSpringMultipartMaxRequestSize() {
		return springMultipartMaxRequestSize;
	}
	
	public void setSpringMultipartMaxRequestSize(String springMultipartMaxRequestSize) {
		this.springMultipartMaxRequestSize = springMultipartMaxRequestSize;
	}
	
	public String getProxyHost() {
		return proxyHost;
	}
	
	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}
	
	public int getProxyPort() {
		return proxyPort;
	}
	
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}
	
	public boolean isProxyEnabled() {
		return proxyEnabled;
	}
	
	public void setProxyEnabled(boolean proxyEnabled) {
		this.proxyEnabled = proxyEnabled;
	}
	
	public String getBatchSize() {
		return batchSize;
	}
	
	public void setBatchSize(String batchSize) {
		this.batchSize = batchSize;
	}
	
	public int getConnectionTimeout() {
		return connectionTimeout;
	}
	
	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}
	
	public int getMaximumConnectionsPerRoute() {
		return maximumConnectionsPerRoute;
	}
	
	public void setMaximumConnectionsPerRoute(int maximumConnectionsPerRoute) {
		this.maximumConnectionsPerRoute = maximumConnectionsPerRoute;
	}
	
	public int getMaxConnections() {
		return maxConnections;
	}
	
	public void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}
	
	public int getInactivityTimeInms() {
		return inactivityTimeInms;
	}
	
	public void setInactivityTimeInms(int inactivityTimeInms) {
		this.inactivityTimeInms = inactivityTimeInms;
	}
	
	public String getMeerkatUrl() {
		return meerkatUrl;
	}
	
	public void setMeerkatUrl(String meerkatUrl) {
		this.meerkatUrl = meerkatUrl;
	}
	
	public boolean isServerCompressionEnabled() {
		return serverCompressionEnabled;
	}
	
	public void setServerCompressionEnabled(boolean serverCompressionEnabled) {
		this.serverCompressionEnabled = serverCompressionEnabled;
	}
	
	public String getServerCompressionMimeTypes() {
		return serverCompressionMimeTypes;
	}
	
	public void setServerCompressionMimeTypes(String serverCompressionMimeTypes) {
		this.serverCompressionMimeTypes = serverCompressionMimeTypes;
	}
	
	public String getLuceneSlambangDir() {
		return luceneSlambangDir;
	}
	
	public void setLuceneSlambangDir(String luceneSlambangDir) {
		this.luceneSlambangDir = luceneSlambangDir;
	}
	
	public String getTempDir() {
		return tempDir;
	}
	
	public void setTempDir(String tempDir) {
		this.tempDir = tempDir;
	}
	
	public boolean isReadFromDb() {
		return readFromDb;
	}
	
	public void setReadFromDb(boolean readFromDb) {
		this.readFromDb = readFromDb;
	}
	
	public String getSpendInclusion() {
		return spendInclusion;
	}
	
	public void setSpendInclusion(String spendInclusion) {
		this.spendInclusion = spendInclusion;
	}
	
	public String getNonSpendInclusion() {
		return nonSpendInclusion;
	}
	
	public void setNonSpendInclusion(String nonSpendInclusion) {
		this.nonSpendInclusion = nonSpendInclusion;
	}
	
	public String getThresholdCategoryScore() {
		return thresholdCategoryScore;
	}
	
	public void setThresholdCategoryScore(String thresholdCategoryScore) {
		this.thresholdCategoryScore = thresholdCategoryScore;
	}
	
	public String getThresholdSubtypeScore() {
		return thresholdSubtypeScore;
	}
	
	public void setThresholdSubtypeScore(String thresholdSubtypeScore) {
		this.thresholdSubtypeScore = thresholdSubtypeScore;
	}
	
	public String getThresholdMerchantScore() {
		return thresholdMerchantScore;
	}
	
	public void setThresholdMerchantScore(String thresholdMerchantScore) {
		this.thresholdMerchantScore = thresholdMerchantScore;
	}
	
	public String getSpringDataSourceUrl() {
		return springDataSourceUrl;
	}
	
	public void setSpringDataSourceUrl(String springDataSourceUrl) {
		this.springDataSourceUrl = springDataSourceUrl;
	}
	
	public String getSpringDataSourceUserName() {
		return springDataSourceUserName;
	}
	
	public void setSpringDataSourceUserName(String springDataSourceUserName) {
		this.springDataSourceUserName = springDataSourceUserName;
	}
	
	public String getSpringDataSourcePassword() {
		return springDataSourcePassword;
	}
	
	public void setSpringDataSourcePassword(String springDataSourcePassword) {
		this.springDataSourcePassword = springDataSourcePassword;
	}
	
	public String getSpringDataSourceDriverClassName() {
		return springDataSourceDriverClassName;
	}
	
	public void setSpringDataSourceDriverClassName(String springDataSourceDriverClassName) {
		this.springDataSourceDriverClassName = springDataSourceDriverClassName;
	}
	
	public String getSpringJpaDatabasePlatform() {
		return springJpaDatabasePlatform;
	}
	
	public void setSpringJpaDatabasePlatform(String springJpaDatabasePlatform) {
		this.springJpaDatabasePlatform = springJpaDatabasePlatform;
	}
	
	public String getSpringJpaShowSQL() {
		return springJpaShowSQL;
	}
	
	public void setSpringJpaShowSQL(String springJpaShowSQL) {
		this.springJpaShowSQL = springJpaShowSQL;
	}
	
	public int getNumberOfMeerkatThreads() {
		return numberOfMeerkatThreads;
	}
	
	public void setNumberOfMeerkatThreads(int numberOfMeerkatThreads) {
		this.numberOfMeerkatThreads = numberOfMeerkatThreads;
	}
	
	public int getNumberOfLegacyThreads() {
		return numberOfLegacyThreads;
	}
	
	public void setNumberOfLegacyThreads(int numberOfLegacyThreads) {
		this.numberOfLegacyThreads = numberOfLegacyThreads;
	}
	
	public long getMeerkatThreadTimeout() {
		return meerkatThreadTimeout;
	}
	
	public void setMeerkatThreadTimeout(long meerkatThreadTimeout) {
		this.meerkatThreadTimeout = meerkatThreadTimeout;
	}
	
	public boolean isParallelEnabled() {
		return parallelEnabled;
	}
	
	public void setParallelEnabled(boolean parallelEnabled) {
		this.parallelEnabled = parallelEnabled;
	}
	
	public long getLegacyThreadTimeout() {
		return legacyThreadTimeout;
	}
	
	public void setLegacyThreadTimeout(long legacyThreadTimeout) {
		this.legacyThreadTimeout = legacyThreadTimeout;
	}
	
	public int getLegacyBatchSize() {
		return legacyBatchSize;
	}
	
	public void setLegacyBatchSize(int legacyBatchSize) {
		this.legacyBatchSize = legacyBatchSize;
	}
	
	public int getQueueThreadsLimit() {
		return queueThreadsLimit;
	}
	
	public void setQueueThreadsLimit(int queueThreadsLimit) {
		this.queueThreadsLimit = queueThreadsLimit;
	}
	
	public long getExtraThreadDelay() {
		return extraThreadDelay;
	}
	
	public void setExtraThreadDelay(long extraThreadDelay) {
		this.extraThreadDelay = extraThreadDelay;
	}
	
	public String getIncrementalCacheReloadTimeInterval() {
		return incrementalCacheReloadTimeInterval;
	}
	
	public void setIncrementalCacheReloadTimeInterval(String incrementalCacheReloadTimeInterval) {
		this.incrementalCacheReloadTimeInterval = incrementalCacheReloadTimeInterval;
	}
	
	public String getSupressZeroInCheckNumFromCheckDesc() {
		return supressZeroInCheckNumFromCheckDesc;
	}
	
	public void setSupressZeroInCheckNumFromCheckDesc(String supressZeroInCheckNumFromCheckDesc) {
		this.supressZeroInCheckNumFromCheckDesc = supressZeroInCheckNumFromCheckDesc;
	}
	
	public String getCheckNumberLengthInCheckDesc() {
		return checkNumberLengthInCheckDesc;
	}
	
	public void setCheckNumberLengthInCheckDesc(String checkNumberLengthInCheckDesc) {
		this.checkNumberLengthInCheckDesc = checkNumberLengthInCheckDesc;
	}
	
	public String getAppendedZeroInCheckNumberMinCount() {
		return appendedZeroInCheckNumberMinCount;
	}
	
	public void setAppendedZeroInCheckNumberMinCount(String appendedZeroInCheckNumberMinCount) {
		this.appendedZeroInCheckNumberMinCount = appendedZeroInCheckNumberMinCount;
	}
	public String getUseTriesSlambangCache() {
		return useTriesSlambangCache;
	}
	
	public void setUseTriesSlambangCache(String useTriesSlambangCache) {
		this.useTriesSlambangCache = useTriesSlambangCache;
	}
	
	public String getUseJsonForLoadingGranularCategories() {
		return useJsonForLoadingGranularCategories;
	}
	
	public void setUseJsonForLoadingGranularCategories(String useJsonForLoadingGranularCategories) {
		this.useJsonForLoadingGranularCategories = useJsonForLoadingGranularCategories;
	}
	
	public String getLogFile() {
		return logFile;
	}
	
	public void setLogFile(String logFile) {
		this.logFile = logFile;
	}
	
	public String getLogFileNamePrefix() {
		return logFileNamePrefix;
	}
	
	public void setLogFileNamePrefix(String logFileNamePrefix) {
		this.logFileNamePrefix = logFileNamePrefix;
	}
	
	public String getMaxSize() {
		return maxSize;
	}
	
	public void setMaxSize(String maxSize) {
		this.maxSize = maxSize;
	}
	
	public String getMaxBackup() {
		return maxBackup;
	}
	
	public void setMaxBackup(String maxBackup) {
		this.maxBackup = maxBackup;
	}
	
	public String getInstance() {
		return instance;
	}
	
	public void setInstance(String instance) {
		this.instance = instance;
	}
	
	public String getMeerkatUrlRegion1() {
		return meerkatUrlRegion1;
	}
	
	public void setMeerkatUrlRegion1(String meerkatUrlRegion1) {
		this.meerkatUrlRegion1 = meerkatUrlRegion1;
	}
	
	public String getMeerkatUrlRegion2() {
		return meerkatUrlRegion2;
	}
	
	public void setMeerkatUrlRegion2(String meerkatUrlRegion2) {
		this.meerkatUrlRegion2 = meerkatUrlRegion2;
	}
	
	public String getMeerkatUrlRegion3() {
		return meerkatUrlRegion3;
	}
	
	public void setMeerkatUrlRegion3(String meerkatUrlRegion3) {
		this.meerkatUrlRegion3 = meerkatUrlRegion3;
	}
	
	public String getMeerkatUrlRegion4() {
		return meerkatUrlRegion4;
	}
	
	public void setMeerkatUrlRegion4(String meerkatUrlRegion4) {
		this.meerkatUrlRegion4 = meerkatUrlRegion4;
	}
	
	public String getMeerkatUrlRegion7() {
		return meerkatUrlRegion7;
	}
	
	public void setMeerkatUrlRegion7(String meerkatUrlRegion7) {
		this.meerkatUrlRegion7 = meerkatUrlRegion7;
	}
	
	public String getLoanTypes() {
		return loanTypes;
	}
	
	public void setLoanTypes(String loanTypes) {
		this.loanTypes = loanTypes;
	}
	
	public String getInvestmentTransactionTypes() {
		return investmentTransactionTypes;
	}
	
	public void setInvestmentTransactionTypes(String investmentTransactionTypes) {
		this.investmentTransactionTypes = investmentTransactionTypes;
	}
	
	public String getInvestmentAccountTypes() {
		return investmentAccountTypes;
	}
	
	public void setInvestmentAccountTypes(String investmentAccountTypes) {
		this.investmentAccountTypes = investmentAccountTypes;
	}
	
	public String getSupportedRegionsForLoanAndInvestment() {
		return supportedRegionsForLoanAndInvestment;
	}
	
	public void setSupportedRegionsForLoanAndInvestment(String supportedRegionsForLoanAndInvestment) {
		this.supportedRegionsForLoanAndInvestment = supportedRegionsForLoanAndInvestment;
	}
	
	public int getSocketTimeout() {
		return socketTimeout;
	}
	
	public void setSocketTimeout(int socketTimeout) {
		this.socketTimeout = socketTimeout;
	}
	
	public String getSimpleDescriptionVersion() {
		return simpleDescriptionVersion;
	}
	
	public void setSimpleDescriptionVersion(String simpleDescriptionVersion) {
		this.simpleDescriptionVersion = simpleDescriptionVersion;
	}
	
	public boolean isPopulateAdditionalFields() {
		return populateAdditionalFields;
	}
	
	public void setPopulateAdditionalFields(boolean populateAdditionalFields) {
		this.populateAdditionalFields = populateAdditionalFields;
	}
	
	public boolean isPopulateIsPhysical() {
		return populateIsPhysical;
	}
	
	public void setPopulateIsPhysical(boolean populateIsPhysical) {
		this.populateIsPhysical = populateIsPhysical;
	}
	
	public boolean isPopulateIntermediary() {
		return populateIntermediary;
	}
	
	public void setPopulateIntermediary(boolean populateIntermediary) {
		this.populateIntermediary = populateIntermediary;
	}
	
	public boolean isPopulateTxnMerchantType() {
		return populateTxnMerchantType;
	}
	
	public void setPopulateTxnMerchantType(boolean populateTxnMerchantType) {
		this.populateTxnMerchantType = populateTxnMerchantType;
	}
	
	public boolean isPopulateLogoEndpoint() {
		return populateLogoEndpoint;
	}
	
	public void setPopulateLogoEndpoint(boolean populateLogoEndpoint) {
		this.populateLogoEndpoint = populateLogoEndpoint;
	}
	
	public boolean isPopulateDigitalFieldForLoanAndInvestment() {
		return populateDigitalFieldForLoanAndInvestment;
	}
	
	public void setPopulateDigitalFieldForLoanAndInvestment(boolean populateDigitalFieldForLoanAndInvestment) {
		this.populateDigitalFieldForLoanAndInvestment = populateDigitalFieldForLoanAndInvestment;
	}
	
	public int getConfidenceScoreThreshold() {
		return confidenceScoreThreshold;
	}
	
	public void setConfidenceScoreThreshold(int confidenceScoreThreshold) {
		this.confidenceScoreThreshold = confidenceScoreThreshold;
	}

	public int getCustomRestConnectionTimeout() {
		return customRestConnectionTimeout;
	}

	public void setCustomRestConnectionTimeout(int customRestConnectionTimeout) {
		this.customRestConnectionTimeout = customRestConnectionTimeout;
	}

	public int getCustomRestRequestTimeout() {
		return customRestRequestTimeout;
	}

	public void setCustomRestRequestTimeout(int customRestRequestTimeout) {
		this.customRestRequestTimeout = customRestRequestTimeout;
	}

	public int getCustomRestReadTimeout() {
		return customRestReadTimeout;
	}

	public void setCustomRestReadTimeout(int customRestReadTimeout) {
		this.customRestReadTimeout = customRestReadTimeout;
	}

	public int getParallelism() {
		return parallelism;
	}

	public void setParallelism(int parallelism) {
		this.parallelism = parallelism;
	}

	
}
