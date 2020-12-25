[
		{
			"name" : "insurance",
			"id" : 14,
			"displayName" : "Insurance",
			"label" : "insurance",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : true,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [ "Services and Supplies,Insurance" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "electronics",
			"id" : 43,
			"displayName" : "Electronics/General Merchandise",
			"label" : "electronics_general_merchandise",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [ "Businesses and Services,Technology,Mobile",
					"Electronics/General Merchandise" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "education",
			"id" : 6,
			"displayName" : "Education",
			"label" : "education",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [
					"Businesses and Services,Financial,Student Aid and Grants",
					"Community and Government,Day Care and Preschools",
					"Community and Government,Education",
					"Community and Government,Education,Adult Education",
					"Community and Government,Education,Art Lessons and Schools",
					"Community and Government,Education,Colleges and Universities",
					"Community and Government,Education,Computer Training",
					"Community and Government,Education,Culinary Lessons and Schools",
					"Community and Government,Education,Driving Schools",
					"Community and Government,Education,Primary and Secondary Schools",
					"Community and Government,Education,Tutoring and Educational Services",
					"Community and Government,Education,Vocational Schools",
					"Community and Government,Libraries",
					"Travel,Day Care and Preschools", "Travel,Education",
					"Travel,Education,Accounting and Bookkeeping",
					"Travel,Education,Adult",
					"Travel,Education,Art Lessons and Schools",
					"Travel,Education,Colleges and Universities",
					"Travel,Education,Computer Training",
					"Travel,Education,Culinary Lessons and Schools",
					"Travel,Education,Driving Schools",
					"Travel,Education,Fraternities and Sororities",
					"Travel,Education,Tutoring and al Services",
					"Travel,Education,Vocational Schools", "Travel,Libraries" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "retirement_income",
			"id" : 31,
			"displayName" : "Retirement Income",
			"categoryType" : "income",
			"global" : true,
			"parentCategoryName" : "investment_income",
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [],
			"rules" : [ {
				"container" : "stocks",
				"baseType" : "credit"
			}, {
				"container" : "bank",
				"baseType" : "credit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "credit_card_payments",
			"id" : 26,
			"displayName" : "Credit Card Payments",
			"label" : "credit_card_payments",
			"categoryType" : "transfer",
			"global" : true,
			"smallBusinessCategory" : true,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "credit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "taxes",
			"id" : 37,
			"displayName" : "Taxes",
			"label" : "taxes",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : true,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [ "Taxes" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : [ "fedTaxWithheld", "foreignTax",
					"stateTaxWithheld", "stockOptionWithholding" ]
		},
		{
			"name" : "advertising",
			"id" : 100,
			"displayName" : "Advertising",
			"categoryType" : "expense",
			"global" : true,
			"parentCategoryName" : "office_supplies",
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [
					"Businesses and Services,Advertising and Marketing",
					"Businesses and Services,Technology,Advertising" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "service_charges_fees",
			"id" : 24,
			"displayName" : "Service Charges/Fees",
			"label" : "service_charges_fees",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [
					"Businesses and Services,Business and Strategy Consulting",
					"Businesses and Services,Career Counseling",
					"Businesses and Services,Computers",
					"Businesses and Services,Employment Agencies",
					"Businesses and Services,Financial",
					"Businesses and Services,Financial,Accounting and Bookkeeping",
					"Businesses and Services,Financial,Banking and Finance",
					"Businesses and Services,Financial,Business Brokers and Franchises",
					"Businesses and Services,Financial,Check Cashing",
					"Businesses and Services,Financial,Collections",
					"Businesses and Services,Financial,Financial Planning and Investments",
					"Businesses and Services,Financial,Fund Raising",
					"Businesses and Services,Financial,Stock Brokers",
					"Businesses and Services,Home Improvement",
					"Businesses and Services,Household Expenses,Home Inspection Services",
					"Businesses and Services,Human Resources",
					"Businesses and Services,Legal,Notary",
					"Businesses and Services,Office Expenses and Marketing,Market Research and Consulting",
					"Businesses and Services,Office Expenses and Marketing,Public Relations",
					"Businesses and Services,Office Expenses and Marketing,Writing,Copywriting and Technical Writing",
					"Businesses and Services,Petroleum,Market Research and Consulting",
					"Businesses and Services,Petroleum,Public Relations",
					"Businesses and Services,Petroleum,Writing,Copywriting and Technical Writing",
					"Businesses and Services,Real Estate",
					"Businesses and Services,Real Estate,Building and Land Surveyors",
					"Businesses and Services,Real Estate,Property Management",
					"Businesses and Services,Real Estate,Real Estate Agents",
					"Businesses and Services,Real Estate,Real Estate Appraiser",
					"Businesses and Services,Real Estate,Real Estate Development and Title Companies",
					"Businesses and Services,Translation Services",
					"Community and Government,Disabled Persons Services",
					"Community and Government,Drug and Alcohol Services",
					"Community and Government,Senior Citizen services",
					"Community and Government,Senior Citizen services,Retirement",
					"Service Charges/Fees", "Travel,Disabled Persons Services",
					"Travel,Drug and Alcohol Services",
					"Travel,Senior Citizen services",
					"Travel,Senior Citizen services,Retirement" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : [ "accountFee", "accountMaintenanceFee",
					"administrativeFee", "charge", "fedexFee",
					"mortalityAndExpenseRiskCharge", "nsfFee",
					"otherAnnuityFee", "penalty", "reorganizationCharge",
					"returnedCheckFee", "rtqFee", "serviceCharge",
					"surrenderCharge" ]
		},
		{
			"name" : "rent",
			"id" : 21,
			"displayName" : "Rent",
			"label" : "rent",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [
					"Businesses and Services,Real Estate,Apartments,Condos and Houses",
					"Businesses and Services,Real Estate,Boarding Houses" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "utilities",
			"id" : 39,
			"displayName" : "Utilities",
			"label" : "utilities",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : true,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [
					"Businesses and Services,Water and Waste Management",
					"Community and Government,Utility Companies", "Skydiving",
					"Travel,Utility Companies" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "sales",
			"id" : 94,
			"displayName" : "Sales",
			"categoryType" : "income",
			"global" : true,
			"parentCategoryName" : "consulting",
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "credit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "cable_satellite_services",
			"id" : 15,
			"displayName" : "Cable/Satellite/Telecom",
			"label" : "cable_satellite_telecom",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [ "Cable/Satellite/Telecom" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "other_bills",
			"id" : 35,
			"displayName" : "Other Bills",
			"categoryType" : "expense",
			"global" : true,
			"parentCategoryName" : "other_expenses",
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [
					"Community and Government,Government Dept and Agencies",
					"Other Bills", "Travel,Law Enforcement and Public Safety",
					"Travel,Law Enforcement and Public Safety,Fire Stations" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "securities_trades",
			"id" : 36,
			"displayName" : "Securities Trades",
			"label" : "securities_trades",
			"categoryType" : "transfer",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "stocks",
				"baseType" : "credit"
			}, {
				"container" : "stocks",
				"baseType" : "debit"
			}, {
				"container" : "bank",
				"baseType" : "credit"
			} ],
			"transactionTypes" : [ "DVP", "RVP", "adjustedBuy", "adjustedSell",
					"adjustment", "assignOption", "automaticInvestment",
					"bondCall", "bondMatures", "buy", "buyAccruedInterest",
					"buyOption", "buyToClose", "buyToCover", "buyToOpen",
					"capitalCalls", "capitalGainsRecieved",
					"capitalGainsReinvested", "conversion",
					"corporateAcquisition", "csAdjustment",
					"dividendReinvestment", "esopAllocation", "exerciseOption",
					"expireOption", "fundExchange", "fundExpense",
					"interestReinvestment", "merger", "mmfIn", "mmfRein",
					"mmfTransaction", "nameChange", "orderOut",
					"recharacterization", "reinvestLongTermCapitalGains",
					"reinvestShortTermCapitalGains", "reminder", "reversal",
					"sell", "sellAccruedInterest", "sellOption", "sellToClose",
					"sellToOpen", "sharesIn", "sharesOut", "shortSell",
					"shortTermCapitalGainsDistribution", "spinoff",
					"stockOptionExercise", "stockSplit", "symbolChange",
					"tendered", "withdrawal" ]
		},
		{
			"name" : "automotive_expenses",
			"id" : 2,
			"displayName" : "Automotive/Fuel",
			"label" : "automotive_fuel",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [ "Automotive", "Automotive,Car Appraisers",
					"Automotive,Car Dealers and Leasing",
					"Automotive,Car Dealers and Leasing,Used Cars",
					"Automotive,Car Parts and Accessories",
					"Automotive,Car Wash and Detail",
					"Automotive,Classic and Antique Car",
					"Automotive,Maintenance and Repair",
					"Automotive,Maintenance and Repair,Oil and Lube",
					"Automotive,Maintenance and Repair,Smog Check",
					"Automotive,Maintenance and Repair,Tires",
					"Automotive,Maintenance and Repair,Transmissions",
					"Automotive,Motorcycles,Mopeds and Scooters",
					"Automotive,Motorcycles,Mopeds and Scooters,Repair",
					"Automotive,Motorcycles,Mopeds and Scooters,Sales",
					"Automotive,RVs and Motor Homes",
					"Automotive,Salvage Yards", "Automotive/Fuel",
					"Businesses and Services,Petroleum",
					"Businesses and Services,Renewable Energy" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "savings",
			"id" : 40,
			"displayName" : "Savings",
			"label" : "savings",
			"categoryType" : "transfer",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "stocks",
				"baseType" : "credit"
			}, {
				"container" : "stocks",
				"baseType" : "debit"
			}, {
				"container" : "bank",
				"baseType" : "credit"
			} ],
			"transactionTypes" : [ "529PlanContribution",
					"educationalPlanContribution", "excessContribution" ]
		},
		{
			"name" : "gifts",
			"id" : 9,
			"displayName" : "Gifts",
			"label" : "gifts",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : true,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "online_services",
			"id" : 16,
			"displayName" : "Services/Supplies",
			"label" : "services_supplies",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [ "Online Services", "Services/Supplies" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "other_income",
			"id" : 32,
			"displayName" : "Other Income",
			"label" : "other_income",
			"categoryType" : "income",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : false,
			"tdeLabels" : [],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "credit"
			}, {
				"container" : "card",
				"baseType" : "credit"
			} ],
			"transactionTypes" : [ "adjustedCredit", "annuityCredit",
					"atmWithdrawalFeeCredit", "deathBenefitPayout",
					"foreignTaxCredit", "iraDistribution",
					"iraNonQualifiedDistribution", "miscCredit",
					"miscJrlMarginToCash", "miscellaneousIncome",
					"moneyFundsJournalMarginToCash",
					"stockFundOptionJournalMarginToCash" ]
		},
		{
			"name" : "atm_cash_withdrawals",
			"id" : 25,
			"displayName" : "ATM/Cash Withdrawals",
			"label" : "atm_cash_withdrawals",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [
					"ATM/Cash Withdrawals",
					"Automotive,Towing",
					"Businesses and Services,Financial,Banking and Finance,ATMs",
					"Home Inspection Services" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "loans",
			"id" : 17,
			"displayName" : "Loans",
			"label" : "loans",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : true,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [
					"Businesses and Services,Financial,Loans/Mortgage and Mortgages",
					"Loans", "Retail,Pawn Shops" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			} ],
			"transactionTypes" : [ "loanDistribution", "loanPayment" ]
		},
		{
			"name" : "general_merchandise",
			"id" : 44,
			"displayName" : "General Merchandise",
			"categoryType" : "expense",
			"global" : true,
			"parentCategoryName" : "electronics",
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [ "Businesses and Services,WholeSale",
					"Retail,Convenience Stores", "Retail,Department Stores",
					"Retail,Discount Stores", "Retail,Furniture and Decor" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "restaurants_dining",
			"id" : 22,
			"displayName" : "Restaurants",
			"label" : "restaurants",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [
					"Businesses and Services,Food and Beverage",
					"Businesses and Services,Food and Beverage,Catering",
					"Businesses and Services,Food and Beverage,Distribution",
					"Restaurants",
					"Retail,Food and Beverage",
					"Services and Supplies,Bars",
					"Services and Supplies,Food and Dining",
					"Services and Supplies,Food and Dining,Bagels and Donuts",
					"Services and Supplies,Food and Dining,Bakeries",
					"Services and Supplies,Food and Dining,Cafes, Coffee and Tea Houses",
					"Services and Supplies,Food and Dining,Dessert",
					"Services and Supplies,Food and Dining,Food and Dining",
					"Services and Supplies,Food and Dining,Ice Cream Parlors",
					"Services and Supplies,Food and Dining,Internet Cafes",
					"Services and Supplies,Food and Dining,Juice Bars and Smoothies",
					"Services and Supplies,Food and Dining,Real Estate Appraiser",
					"Services and Supplies,Food and Dining,Restaurant",
					"Services and Supplies,Food and Dining,Restaurant,American",
					"Services and Supplies,Food and Dining,Restaurant,Asian",
					"Services and Supplies,Food and Dining,Restaurant,Barbecue",
					"Services and Supplies,Food and Dining,Restaurant,Buffets",
					"Services and Supplies,Food and Dining,Restaurant,Burgers",
					"Services and Supplies,Food and Dining,Restaurant,Chinese",
					"Services and Supplies,Food and Dining,Restaurant,Delis",
					"Services and Supplies,Food and Dining,Restaurant,Diners",
					"Services and Supplies,Food and Dining,Restaurant,Fast Food",
					"Services and Supplies,Food and Dining,Restaurant,Food Trucks",
					"Services and Supplies,Food and Dining,Restaurant,French",
					"Services and Supplies,Food and Dining,Restaurant,Indian",
					"Services and Supplies,Food and Dining,Restaurant,International",
					"Services and Supplies,Food and Dining,Restaurant,Italian",
					"Services and Supplies,Food and Dining,Restaurant,Japanese",
					"Services and Supplies,Food and Dining,Restaurant,Korean",
					"Services and Supplies,Food and Dining,Restaurant,Mexican",
					"Services and Supplies,Food and Dining,Restaurant,Middle Eastern",
					"Services and Supplies,Food and Dining,Restaurant,Pizza",
					"Services and Supplies,Food and Dining,Restaurant,Seafood",
					"Services and Supplies,Food and Dining,Restaurant,Steakhouses",
					"Services and Supplies,Food and Dining,Restaurant,Sushi",
					"Services and Supplies,Food and Dining,Restaurant,Vegan and Vegetarian",
					"Social,Bars", "Social,Bars,Jazz and Blues Cafes",
					"Social,Bars,Sports Bars", "Social,Bars,Wine Bars",
					"Social,Food and Dining",
					"Social,Food and Dining,Bagels and Donuts",
					"Social,Food and Dining,Bakeries",
					"Social,Food and Dining,Breweries",
					"Social,Food and Dining,Cafes, Coffee and Tea Houses",
					"Social,Food and Dining,Dessert",
					"Social,Food and Dining,Ice Cream Parlors",
					"Social,Food and Dining,Internet Cafes",
					"Social,Food and Dining,Juice Bars and Smoothies",
					"Social,Food and Dining,Restaurant",
					"Social,Food and Dining,Restaurants",
					"Social,Food and Dining,Restaurants,American",
					"Social,Food and Dining,Restaurants,Asian",
					"Social,Food and Dining,Restaurants,Barbecue",
					"Social,Food and Dining,Restaurants,Buffets",
					"Social,Food and Dining,Restaurants,Burgers",
					"Social,Food and Dining,Restaurants,Chinese",
					"Social,Food and Dining,Restaurants,Delis",
					"Social,Food and Dining,Restaurants,Diners",
					"Social,Food and Dining,Restaurants,Fast Food",
					"Social,Food and Dining,Restaurants,Food Trucks",
					"Social,Food and Dining,Restaurants,French",
					"Social,Food and Dining,Restaurants,Indian",
					"Social,Food and Dining,Restaurants,International",
					"Social,Food and Dining,Restaurants,Italian",
					"Social,Food and Dining,Restaurants,Japanese",
					"Social,Food and Dining,Restaurants,Korean",
					"Social,Food and Dining,Restaurants,Mexican",
					"Social,Food and Dining,Restaurants,Middle Eastern",
					"Social,Food and Dining,Restaurants,Pizza",
					"Social,Food and Dining,Restaurants,Seafood",
					"Social,Food and Dining,Restaurants,Steakhouses",
					"Social,Food and Dining,Restaurants,Sushi",
					"Social,Food and Dining,Restaurants,Thai",
					"Social,Food and Dining,Restaurants,Vegan and Vegetarian",
					"Social,Wineries and Vineyards" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "gasoline_fuel",
			"id" : 8,
			"displayName" : "Gasoline/Fuel",
			"categoryType" : "expense",
			"global" : true,
			"parentCategoryName" : "automotive_expenses",
			"smallBusinessCategory" : true,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [
					"Businesses and Services,Petroleum",
					"Businesses and Services,Petroleum,Creative Services",
					"Businesses and Services,Petroleum,Direct Mail and Email Marketing Services",
					"Businesses and Services,Petroleum,Online Advertising",
					"Businesses and Services,Petroleum,Petroleum",
					"Businesses and Services,Petroleum,Print,TV,Radio and Outdoor Advertising",
					"Businesses and Services,Petroleum,Promotional Items",
					"Businesses and Services,Petroleum,Search Engine Marketing and Optimization",
					"Businesses and Services,Renewable Energy",
					"Transportation,Gas Stations" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "personal_care",
			"id" : 20,
			"displayName" : "Personal/Family",
			"label" : "personal_family",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [
					"Businesses and Services,Personal/Family",
					"Businesses and Services,Personal/Family,Beauty Salons and Barbers",
					"Businesses and Services,Personal/Family,Dry Cleaning,Ironing and Laundry",
					"Businesses and Services,Personal/Family,Hair Removal",
					"Businesses and Services,Personal/Family,Manicures and Pedicures",
					"Businesses and Services,Personal/Family,Massage Clinincs and Therapists",
					"Businesses and Services,Personal/Family,Piercing",
					"Businesses and Services,Personal/Family,Skin Care",
					"Businesses and Services,Personal/Family,Spas",
					"Businesses and Services,Personal/Family,Tanning Salons",
					"Businesses and Services,Personal/Family,Tattooing",
					"Personal/Family",
					"Retail,Beauty Products",
					"Retail,Children",
					"Retail,Costumes",
					"Retail,Fashion",
					"Retail,Fashion,Jewelry and Watches",
					"Retail,Fashion,Shoes",
					"Retail,Fashion,Swimwear",
					"Retail,Outlet",
					"Retail,Sporting Goods",
					"Retail,Toys",
					"Retail,Wedding and Bridal",
					"Services and Supplies,Personal Care,Beauty Salons and Barbers",
					"Social,Personal Care",
					"Sportsand Recreation,Personal Trainers",
					"Sportsand Recreation,Yoga and Pilates" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "deposits",
			"id" : 27,
			"displayName" : "Deposits",
			"label" : "deposits",
			"categoryType" : "income",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [ "Deposits" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "credit"
			} ],
			"transactionTypes" : [ "deposit" ]
		},
		{
			"name" : "charitable_giving",
			"id" : 3,
			"displayName" : "Charitable Giving",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [
					"Community and Government,Animal Shelters and Humane Societies",
					"Community and Government,Housing Assistance and shelters",
					"Community and Government,Law Enforcement and Public Safety,Rescue Services",
					"Community and Government,Organizations and Associations,Charities and Non-Profits",
					"Community and Government,Organizations and Associations,Environmental",
					"Community and Government,Public and Services",
					"Community and Government,Public and Social Services",
					"Community and Government,Religious",
					"Community and Government,Religious,Buddhist Temples",
					"Community and Government,Religious,Churches",
					"Community and Government,Religious,Hindu Temples",
					"Community and Government,Religious,Mosques",
					"Community and Government,Religious,Synagogues",
					"Travel,Agents and Tour Operators",
					"Travel,Housing Assistance and shelters",
					"Travel,Law Enforcement and Public Safety,Rescue Services",
					"Travel,Organizations and Associations,Charities and Non-Profits",
					"Travel,Organizations and Associations,Environmental",
					"Travel,Religious", "Travel,Religious,Buddhist Temples",
					"Travel,Religious,Churches",
					"Travel,Religious,Hindu Temples",
					"Travel,Religious,Mosques", "Travel,Religious,Synagogues" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "checks",
			"id" : 33,
			"displayName" : "Check Payment",
			"label" : "check_payment",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : true,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [ "Check Payment", "Checks",
					"Services and Supplies,Financial,Banking and Finance" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			} ],
			"transactionTypes" : [ "check" ]
		},
		{
			"name" : "healthcare_medical",
			"id" : 11,
			"displayName" : "Healthcare/Medical",
			"label" : "healthcare_medical",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [
					"Health Care",
					"Health Care,AIDS Resources",
					"Health Care,Assisted Living services",
					"Health Care,Assisted Living services,Facilities and Nursing Homes",
					"Health Care,Blood Banks and Centers",
					"Health Care,Chiropractors",
					"Health Care,Dentists",
					"Health Care,Emergency Services",
					"Health Care,Emergency Services,Ambulance",
					"Health Care,Holistic,Alternative and Naturopathic Medicine",
					"Health Care,Holistic,Alternative and Naturopathic Medicine,Acupuncture",
					"Health Care,Home Health Care Services",
					"Health Care,Hospitals, Clinics and Medical Centers",
					"Health Care,Medical Supplies and Labs",
					"Health Care,Mental Health",
					"Health Care,Mental Health,Counseling and Therapy",
					"Health Care,Mental Health,Psychologists",
					"Health Care,Nurses",
					"Health Care,Optometrist",
					"Health Care,Pharmacies",
					"Health Care,Physical therapy and Rehabilitation",
					"Health Care,Physical therapy and Rehabilitation,Sports Medicine",
					"Health Care,Physicians",
					"Health Care,Physicians,Anesthesiologists",
					"Health Care,Physicians,Cardiologists",
					"Health Care,Physicians,Dermatologists",
					"Health Care,Physicians,Ear,Nose and Throat",
					"Health Care,Physicians,Family Medicine",
					"Health Care,Physicians,Gastroenterologists",
					"Health Care,Physicians,General Surgery",
					"Health Care,Physicians,Geriatrics",
					"Health Care,Physicians,Internal Medicine",
					"Health Care,Physicians,Medicine",
					"Health Care,Physicians,Neurologists",
					"Health Care,Physicians,Obstetricians and gynecologists",
					"Health Care,Physicians,Oncologists",
					"Health Care,Physicians,Ophthalmologists",
					"Health Care,Physicians,Orthopedic Surgeons",
					"Health Care,Physicians,Pathologists",
					"Health Care,Physicians,Pediatricians",
					"Health Care,Physicians,Plastic Surgeons",
					"Health Care,Physicians,Psychiatrists",
					"Health Care,Physicians,Radiologists",
					"Health Care,Physicians,Respiratory",
					"Health Care,Physicians,Urologists",
					"Health Care,Podiatrists",
					"Health Care,Pregnancy and Sexual Health",
					"Health Care,Weight Loss and Nutritionists",
					"Services and Supplies,Health Care,Hospitals, Clinics and Medical Centers",
					"Services and Supplies,Health Care,Pharmacies" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "groceries",
			"id" : 10,
			"displayName" : "Groceries",
			"label" : "groceries",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [ "Retail,Supermarkets and Groceries" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "rewards",
			"id" : 225,
			"displayName" : "Rewards",
			"label" : "rewards",
			"categoryType" : "income",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [ "Services and Supplies,Wineries and Vineyards" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "credit"
			}, {
				"container" : "card",
				"baseType" : "credit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "mortgages",
			"id" : 18,
			"displayName" : "Mortgage",
			"label" : "mortgage",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [ "Mortgage", "Services and Supplies,Real Estate" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "retirement_contributions",
			"id" : 41,
			"displayName" : "Retirement Contributions",
			"label" : "retirement_contributions",
			"categoryType" : "DeferredCompensation",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			} ],
			"transactionTypes" : [ "401kContribution",
					"401kEmployerContribution",
					"deferredCompensationContribution",
					"deferredCompensationDistribution", "employeeContribution",
					"employerContribution", "genericContribution",
					"iraContribution", "premiumDeduction",
					"rolloverContribution", "rolloverToQual",
					"rothContribution", "sepContribution",
					"simplePlanContribution" ]
		},
		{
			"name" : "child_dependent_expenses",
			"id" : 4,
			"displayName" : "Child/Dependent Expenses",
			"categoryType" : "expense",
			"global" : true,
			"parentCategoryName" : "personal_care",
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "expense_reimbursement",
			"id" : 114,
			"displayName" : "Expense Reimbursement",
			"label" : "expense_reimbursement",
			"categoryType" : "income",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "credit"
			} ],
			"transactionTypes" : [ "reimbursement" ]
		},
		{
			"name" : "entertainment",
			"id" : 7,
			"displayName" : "Entertainment/Recreation",
			"label" : "entertainment_recreation",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : true,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [
					"Businesses and Services,Entertainment/Recreation",
					"Businesses and Services,Entertainment/Recreation,Media",
					"Businesses and Services,Photography",
					"Community and Government,Education,Fraternities and Sororities",
					"Community and Government,Organizations and Associations,Youth Organizations",
					"Entertainment/Recreation",
					"Retail,Bookstores",
					"Retail,Computers and Household Expenses,Video Games",
					"Retail,Dance and Music",
					"Retail,Hobby and Collectibles",
					"Retail,Music,Video and DVD",
					"Services and Supplies",
					"Services and Supplies,Arts",
					"Services and Supplies,Arts,Museums",
					"Services and Supplies,Country Clubs",
					"Services and Supplies,Entertainment",
					"Services and Supplies,Entertainment,Adult Entertainment",
					"Services and Supplies,Entertainment,Amusement Parks",
					"Services and Supplies,Entertainment,Arcades",
					"Services and Supplies,Entertainment,Billiard and Pool",
					"Services and Supplies,Entertainment,Bingo",
					"Services and Supplies,Entertainment,Bowling",
					"Services and Supplies,Entertainment,Carnivals",
					"Services and Supplies,Entertainment,Casinos and Gaming",
					"Services and Supplies,Entertainment,Circuses",
					"Services and Supplies,Entertainment,Dance Halls and Saloons",
					"Services and Supplies,Entertainment,FairGrounds and Rodeos",
					"Services and Supplies,Entertainment,Go Carts",
					"Services and Supplies,Entertainment,Hookah Lounges",
					"Services and Supplies,Entertainment,Karaoke",
					"Services and Supplies,Entertainment,Miniature Golf",
					"Services and Supplies,Entertainment,Movie Theatres",
					"Services and Supplies,Entertainment,Music and Show Venues",
					"Services and Supplies,Entertainment,Night Clubs",
					"Services and Supplies,Entertainment,Party Centers",
					"Services and Supplies,Entertainment,Ticket Sales",
					"Services and Supplies,Zoos,Aquariums and Wildlife Sanctuaries",
					"Social",
					"Social,Arts",
					"Social,Arts,Museums",
					"Social,Country Clubs",
					"Social,Entertainment/Recreation",
					"Social,Entertainment/Recreation,Adult Entertainment/Recreation",
					"Social,Entertainment/Recreation,Amusement Parks",
					"Social,Entertainment/Recreation,Arcades",
					"Social,Entertainment/Recreation,Billiard and Pool",
					"Social,Entertainment/Recreation,Bingo",
					"Social,Entertainment/Recreation,Bowling",
					"Social,Entertainment/Recreation,Carnivals",
					"Social,Entertainment/Recreation,Casinos and Gaming",
					"Social,Entertainment/Recreation,Circuses",
					"Social,Entertainment/Recreation,Dance Halls and Saloons",
					"Social,Entertainment/Recreation,FairGrounds and Rodeos",
					"Social,Entertainment/Recreation,Go Carts",
					"Social,Entertainment/Recreation,Hookah Lounges",
					"Social,Entertainment/Recreation,Karaoke",
					"Social,Entertainment/Recreation,Miniature Golf",
					"Social,Entertainment/Recreation,Movie Theatres",
					"Social,Entertainment/Recreation,Music and Show Venues",
					"Social,Entertainment/Recreation,Night Clubs",
					"Social,Entertainment/Recreation,Party Centers",
					"Social,Entertainment/Recreation,Ticket Sales",
					"Social,Zoos,Aquariums and Wildlife Sanctuaries",
					"Sportsand Recreation,Outdoors",
					"Sportsand Recreation,Outdoors,Hiking",
					"Sportsand Recreation,Outdoors,Hot Air Balloons",
					"Sportsand Recreation,Outdoors,Hunting and Fishing",
					"Sportsand Recreation,Outdoors,Rock Climbing",
					"Sportsand Recreation,Outdoors,Skydiving",
					"Sportsand Recreation,Paintball",
					"Sportsand Recreation,Race Trackers",
					"Sportsand Recreation,Stadiums and Arenas" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "other_expenses",
			"id" : 19,
			"displayName" : "Other Expenses",
			"label" : "other_expenses",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : false,
			"tdeLabels" : [
					"Businesses and Services,Audiovisual",
					"Businesses and Services,Automation and Control Systems",
					"Businesses and Services,Chemical and gases",
					"Businesses and Services,Corporate HQ",
					"Businesses and Services,Engineering",
					"Businesses and Services,Funeral Services",
					"Businesses and Services,Geological",
					"Businesses and Services,Leather",
					"Businesses and Services,Refrigeration and Ice",
					"Businesses and Services,Repair Services",
					"Businesses and Services,Scientific",
					"Businesses and Services,Security and Safety",
					"Community and Government,Cemeteries",
					"Community and Government,Law Enforcement and Public Safety",
					"Community and Government,Law Enforcement and Public Safety,Fire Stations",
					"Community and Government,Law Enforcement and Public Safety,Police Stations",
					"Community and Government,Military",
					"Community and Government,Military,Bases",
					"Retail,Auctions",
					"Retail,Cards and stationery",
					"Retail,Florists",
					"Retail,Gift and Novelty",
					"Retail,Newsstands",
					"Retail,Party Supplies",
					"Retail,Photos and Frames",
					"Services and Supplies,Entertainment,Psychics and Astrologers",
					"Social,Entertainment/Recreation,Psychics and Astrologers",
					"Travel,Law Enforcement and Public Safety,Police Stations",
					"Travel,Military", "Travel,Military,Bases" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : [ "adjustedDebit", "marginInterestExpense",
					"miscJrlCashtoMargin", "moneyFundsJournalCashToMargin",
					"stockFundOptionJournalCashToMargin" ]
		},
		{
			"name" : "dues_subscriptions",
			"id" : 108,
			"displayName" : "Subscriptions/Renewals",
			"label" : "subscriptions_renewals",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : true,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [ "Subscriptions/Renewals",
					"Travel,Organizations and Associations" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "paychecks_salary",
			"id" : 29,
			"displayName" : "Salary/Regular Income",
			"label" : "salary_regular_income",
			"categoryType" : "income",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [ "Salary/Regular Income" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "credit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "office_supplies",
			"id" : 45,
			"displayName" : "Office Expenses",
			"label" : "office_expenses",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : true,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [
					"Businesses and Services,Office Expenses and Marketing",
					"Businesses and Services,Office Expenses and Marketing,Creative Services",
					"Businesses and Services,Office Expenses and Marketing,Direct Mail and Email Marketing Services",
					"Businesses and Services,Office Expenses and Marketing,Office Expenses, Agencies and Media Buyers",
					"Businesses and Services,Office Expenses and Marketing,Online Advertising",
					"Businesses and Services,Office Expenses and Marketing,Print,TV,Radio and Outdoor Advertising",
					"Businesses and Services,Office Expenses and Marketing,Promotional Items",
					"Businesses and Services,Office Expenses and Marketing,Search Engine Marketing and Optimization",
					"Businesses and Services,Office Expenses,Printing, Copying and Signage",
					"Businesses and Services,Paper",
					"Businesses and Services,Technology,Web Design and Development",
					"Community and Government,Government Dept and Agencies",
					"Community and Government,Government Lobbyists",
					"Office Expenses", "Retail", "Retail,Furniture and Decor",
					"Retail,Office Expenses", "Services and Supplies",
					"Services and Supplies,Financial,Banking and Finance",
					"Services and Supplies,Printing, Copying and Signage" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "office_maintenance",
			"id" : 110,
			"displayName" : "Office Maintenance",
			"categoryType" : "expense",
			"global" : true,
			"parentCategoryName" : "office_supplies",
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [
					"Businesses and Services,Technology,Infrastructure",
					"Retail,Computers and Electronics" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "home_maintenance",
			"id" : 12,
			"displayName" : "Home Maintenance",
			"categoryType" : "expense",
			"global" : true,
			"parentCategoryName" : "home_improvement",
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [
					"Businesses and Services,Art Restoration",
					"Businesses and Services,Construction",
					"Businesses and Services,Electrical Equipment",
					"Businesses and Services,Equipment Rental",
					"Businesses and Services,Home Improvement,Contractors,Sewer",
					"Businesses and Services,Home Improvement,Electricians",
					"Businesses and Services,Home Improvement,Hardware and Services",
					"Businesses and Services,Home Improvement,Heating,Ventilating and Air Conditioning",
					"Businesses and Services,Home Improvement,Pest Control",
					"Businesses and Services,Home Improvement,Plumbing",
					"Businesses and Services,Home Improvement,Pools and Spas",
					"Businesses and Services,Home Improvement,Roofers",
					"Businesses and Services,Home Improvement,Swimming Pool Maintenance and Services",
					"Businesses and Services,Home Improvement,Tree Service",
					"Businesses and Services,Home Improvement,Upholstery",
					"Businesses and Services,Household Expenses",
					"Businesses and Services,Household Expenses,Architects",
					"Businesses and Services,Household Expenses,Carpenters",
					"Businesses and Services,Household Expenses,Carpet and Flooring",
					"Businesses and Services,Household Expenses,Contractors",
					"Businesses and Services,Household Expenses,Contractors,Bathrooms",
					"Businesses and Services,Household Expenses,Contractors,Deck and Patio",
					"Businesses and Services,Household Expenses,Contractors,Sewer",
					"Businesses and Services,Household Expenses,Doors and Windows",
					"Businesses and Services,Household Expenses,Electricians",
					"Businesses and Services,Household Expenses,Fences,Fireplaces and garage Doors",
					"Businesses and Services,Household Expenses,Hardware and Services",
					"Businesses and Services,Household Expenses,Heating,Ventilating and Air Conditioning",
					"Businesses and Services,Household Expenses,Interior Design",
					"Businesses and Services,Household Expenses,Kitchens",
					"Businesses and Services,Household Expenses,Landscaping and Gardeners",
					"Businesses and Services,Household Expenses,Lighting Fixtures",
					"Businesses and Services,Household Expenses,Painting",
					"Businesses and Services,Household Expenses,Pest Control",
					"Businesses and Services,Household Expenses,Plumbing",
					"Businesses and Services,Household Expenses,Pools and Spas",
					"Businesses and Services,Household Expenses,Roofers",
					"Businesses and Services,Household Expenses,Swimming Pool Maintenance and Services",
					"Businesses and Services,Household Expenses,Tree Service",
					"Businesses and Services,Household Expenses,Upholstery",
					"Businesses and Services,Industrial Machinery and Vehicles",
					"Businesses and Services,Logging and Sawmills",
					"Businesses and Services,Metals",
					"Businesses and Services,Plastics",
					"Businesses and Services,Professional Cleaning",
					"Businesses and Services,Rubber",
					"Businesses and Services,Welding", "Retail,Adult",
					"Retail,Antiques", "Retail,Arts and Crafts",
					"Retail,Bicycles",
					"Retail,Computers and Household Expenses",
					"Retail,Computers and Household Expenses,Cameras",
					"Retail,Computers and Household Expenses,Mobile Phones",
					"Retail,Construction Supplies", "Retail,Flea Markets",
					"Retail,Food and Beverage,Beer,Wine and Spirits",
					"Retail,Food and Beverage,Candy Stores",
					"Retail,Food and Beverage,Cheese",
					"Retail,Food and Beverage,Chocolate",
					"Retail,Food and Beverage,Farmers Markets",
					"Retail,Food and Beverage,Health and Diet Food",
					"Retail,Food and Beverage,Kosher",
					"Retail,Food and Beverage,Meat and Seafood",
					"Retail,Glasses", "Retail,Home Appliances",
					"Retail,Housewares", "Retail,Nurseries and Garden Centers",
					"Retail,Pets", "Retail,Shopping Centers and Malls",
					"Retail,Supermarkets and Household Expenses",
					"Retail,Tobacco", "Retail,Vintage and Thrift",
					"Retail,Warehouses and Wholesale Stores",
					"Social,Arts,Art Dealers and Galleries" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "interest",
			"id" : 96,
			"displayName" : "Interest Income",
			"label" : "interest_income",
			"categoryType" : "income",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [ "Interest", "Interest Income" ],
			"rules" : [ {
				"container" : "stocks",
				"baseType" : "credit"
			}, {
				"container" : "bank",
				"baseType" : "credit"
			} ],
			"transactionTypes" : [ "interestCharge", "interestPayment" ]
		},
		{
			"name" : "transfers",
			"id" : 28,
			"displayName" : "Transfers",
			"label" : "transfers",
			"categoryType" : "transfer",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "stocks",
				"baseType" : "credit"
			}, {
				"container" : "stocks",
				"baseType" : "debit"
			}, {
				"container" : "bank",
				"baseType" : "credit"
			} ],
			"transactionTypes" : [ "1035Exchange", "achIn", "achOut",
					"directDeposit", "mmfSweep", "sweep", "transfer",
					"transferCashIn", "transferCashOut", "transferSharesIn",
					"transferSharesOut", "wireFundsIn", "wireFundsOut" ]
		},
		{
			"name" : "home_improvement",
			"id" : 13,
			"displayName" : "Home Improvement",
			"label" : "home_improvement",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [
					"Businesses and Services,Agriculture and Forestry",
					"Businesses and Services,Home Improvement",
					"Businesses and Services,Home Improvement,Architect",
					"Businesses and Services,Home Improvement,Carpenters",
					"Businesses and Services,Home Improvement,Carpet and Flooring",
					"Businesses and Services,Home Improvement,Contractors",
					"Businesses and Services,Home Improvement,Contractors,Bathrooms",
					"Businesses and Services,Home Improvement,Contractors,Deck and Patio",
					"Businesses and Services,Home Improvement,Doors and Windows",
					"Businesses and Services,Home Improvement,Fences,Fireplaces and garage Doors",
					"Businesses and Services,Home Improvement,Interior Design",
					"Businesses and Services,Home Improvement,Kitchens",
					"Businesses and Services,Home Improvement,Landscaping and Gardeners",
					"Businesses and Services,Home Improvement,Lighting Fixtures",
					"Businesses and Services,Home Improvement,Painting",
					"Construction,Construction",
					"Services and Supplies,Arts,Art Dealers and Galleries",
					"Services and Supplies,Home Improvement,Architect",
					"Services and Supplies,Home Improvement,Contractors" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "business_miscellaneous",
			"id" : 102,
			"displayName" : "Business Miscellaneous",
			"categoryType" : "expense",
			"global" : true,
			"parentCategoryName" : "office_supplies",
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [
					"Businesses and Services",
					"Businesses and Services,Import and Export",
					"Businesses and Services,Legal",
					"Businesses and Services,Legal,Credit Counseling and Bankruptcy Services",
					"Businesses and Services,Machine Shops",
					"Businesses and Services,Management",
					"Businesses and Services,Manufacturing",
					"Businesses and Services,Manufacturing,Dry Cleaning,Ironing and Laundry",
					"Businesses and Services,Manufacturing,Hair Removal",
					"Businesses and Services,Publishing",
					"Businesses and Services,Real Estate,Commercial Real Estate",
					"Businesses and Services,Real Estate,Corporate Housing",
					"Businesses and Services,Technology",
					"Community and Government",
					"Community and Government,Organizations and Associations",
					"Services and Supplies,Agriculture and Forestry",
					"Services and Supplies,Business and Strategy Consulting",
					"Services and Supplies,Computers",
					"Services and Supplies,Financial,Accounting and Bookkeeping",
					"Services and Supplies,Industrial Machinery and Vehicles",
					"Services and Supplies,Legal",
					"Services and Supplies,Manufacturing",
					"Services and Supplies,Metals",
					"Services and Supplies,Real Estate,Real Estate Agents",
					"Services and Supplies,WholeSale",
					"Travel,Government Dept and Agencies",
					"Travel,Government Lobbyists" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "telephone_services",
			"id" : 38,
			"displayName" : "Telephone Services",
			"categoryType" : "expense",
			"global" : true,
			"parentCategoryName" : "cable_satellite_services",
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [ "Businesses and Services,Telecommunication Services" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "consulting",
			"id" : 92,
			"displayName" : "Sales/Services income",
			"categoryType" : "income",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [ "Sales/Services Income" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "credit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "uncategorized",
			"id" : 1,
			"displayName" : "Uncategorized",
			"categoryType" : "uncategorize",
			"global" : true,
			"smallBusinessCategory" : true,
			"personnelCategory" : true,
			"specificCategory" : false,
			"tdeLabels" : [],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "stocks",
				"baseType" : "credit"
			}, {
				"container" : "stocks",
				"baseType" : "debit"
			}, {
				"container" : "bank",
				"baseType" : "credit"
			}, {
				"container" : "card",
				"baseType" : "credit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : [ "billPay", "miscellaneousExpense" ]
		},
		{
			"name" : "services",
			"id" : 98,
			"displayName" : "Services",
			"categoryType" : "income",
			"global" : true,
			"parentCategoryName" : "consulting",
			"smallBusinessCategory" : true,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [ "Services and Supplies,Business and Strategy Consulting" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "credit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "wages_paid",
			"id" : 112,
			"displayName" : "Wages Paid",
			"categoryType" : "expense",
			"global" : true,
			"parentCategoryName" : "other_expenses",
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "investment_income",
			"id" : 30,
			"displayName" : "Investment/Retirement Income",
			"label" : "investment_retirement_income",
			"categoryType" : "income",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [ "Investment/Retirement Income" ],
			"rules" : [ {
				"container" : "stocks",
				"baseType" : "credit"
			}, {
				"container" : "bank",
				"baseType" : "credit"
			} ],
			"transactionTypes" : [ "creditInLieuOfFractionalShare",
					"dividendPayment", "federalTaxFreeInterestIncome",
					"fractionalShareLiquidation", "interestIncome",
					"longTermCapitalGainsDistribution", "mmfDividend",
					"returnOfCapital", "stateTaxFreeDividend",
					"stateTaxFreeInterestIncome", "stockDividend",
					"taxFreeDividend" ]
		},
		{
			"name" : "printing",
			"id" : 106,
			"displayName" : "Printing",
			"categoryType" : "expense",
			"global" : true,
			"parentCategoryName" : "office_supplies",
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [
					"Businesses and Services,Printing, Copying and Signage",
					"Services and Supplies,Printing, Copying and Signage" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "clothing_shoes",
			"id" : 5,
			"displayName" : "Clothing/Shoes",
			"categoryType" : "expense",
			"global" : true,
			"parentCategoryName" : "personal_care",
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [ "Businesses and Services,Tailors",
					"Businesses and Services,Textiles",
					"Retail,Fashion,Clothing and Accessories" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "postage_shipping",
			"id" : 104,
			"displayName" : "Postage/Shipping",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [
					"Businesses and Services,Home Improvement,Movers",
					"Businesses and Services,Household Expenses,Movers",
					"Businesses and Services,Packaging",
					"Businesses and Services,Shipping, Freight and Material Transportation",
					"Community and Government,Post Offices",
					"Postage/Shipping",
					"Services and Supplies,Shipping, Freight and Material Transportation",
					"Travel,Post Offices" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "jewellery",
			"id" : 91,
			"displayName" : "Jewellery",
			"categoryType" : "expense",
			"global" : false,
			"regions" : [ 2 ],
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [],
			"rules" : [],
			"transactionTypes" : []
		},
		{
			"name" : "hobbies",
			"id" : 34,
			"displayName" : "Hobbies",
			"categoryType" : "expense",
			"global" : true,
			"parentCategoryName" : "entertainment",
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [
					"Sportsand Recreation",
					"Sportsand Recreation,Athletic Fields",
					"Sportsand Recreation,Baseball",
					"Sportsand Recreation,Baseball,Batting Ranges",
					"Sportsand Recreation,Basketball",
					"Sportsand Recreation,Combat Sports",
					"Sportsand Recreation,Cycling",
					"Sportsand Recreation,Dance",
					"Sportsand Recreation,Equestrian",
					"Sportsand Recreation,Football",
					"Sportsand Recreation,Golf",
					"Sportsand Recreation,Golf,Golf Courses",
					"Sportsand Recreation,Gun Ranges",
					"Sportsand Recreation,Gymnastics",
					"Sportsand Recreation,Gyms and Fitness Centers",
					"Sportsand Recreation,Hockey",
					"Sportsand Recreation,Outdoors",
					"Sportsand Recreation,Outdoors,Campgrounds and RV Parks",
					"Sportsand Recreation,Race Trackers",
					"Sportsand Recreation,Racquet Sports",
					"Sportsand Recreation,Racquet Sports,Racquetball",
					"Sportsand Recreation,Racquet Sports,Tennis",
					"Sportsand Recreation,Recreation Centers",
					"Sportsand Recreation,Running",
					"Sportsand Recreation,Skating",
					"Sportsand Recreation,Snow Sports",
					"Sportsand Recreation,Soccer",
					"Sportsand Recreation,Sports Clubs",
					"Sportsand Recreation,Sportsand Recreation,Outdoors,Outdoors,Hiking,Hunting and Fishing",
					"Sportsand Recreation,Sportsand Recreation,Outdoors,Outdoors,Hot Air Balloons,Rock Climbing",
					"Sportsand Recreation,Swimming Pools",
					"Sportsand Recreation,Water Sports",
					"Sportsand Recreation,Water Sports,Boating",
					"Sportsand Recreation,Water Sports,Canoes and Kayaks",
					"Sportsand Recreation,Water Sports,Rafting",
					"Sportsand Recreation,Water Sports,Scuba Diving",
					"Sportsand Recreation,Water Sports,Surfing",
					"Sportsand Recreation,Water Sports,Swimming",
					"Travel,Organizations and Associations,Youth Organizations" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		},
		{
			"name" : "travel",
			"id" : 23,
			"displayName" : "Travel",
			"label" : "travel",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : true,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [
					"Businesses and Services,Legal,Immigration",
					"Businesses and Services,Storage",
					"Community and Government,Government Dept and Agencies,Embassies",
					"Landmarks",
					"Landmarks,Buildings and Structures",
					"Landmarks,Gardens",
					"Landmarks,Historic and Protected Sites",
					"Landmarks,Monuments and Memorials",
					"Landmarks,Natural",
					"Landmarks,Natural,Beaches",
					"Landmarks,Natural,Forests",
					"Landmarks,Natural,Lakes",
					"Landmarks,Natural,Mountains",
					"Landmarks,Natural,Rivers",
					"Landmarks,Parks",
					"Landmarks,Parks,Dog Parks",
					"Landmarks,Parks,Natural Parks",
					"Landmarks,Parks,Picnic Areas",
					"Landmarks,Parks,Playgrounds",
					"Landmarks,Parks,Urban Parks",
					"Retail,Luggage",
					"Services and Supplies,Bars,Hotel Lounges",
					"Services and Supplies,Bars,Jazz and Blues Cafes",
					"Services and Supplies,Bars,Sports Bars",
					"Services and Supplies,Bars,Wine Bars",
					"Services and Supplies,Travel,Lodging,Hotels and Motels",
					"Social,Bars,Hotel Lounges",
					"Transportation",
					"Transportation,Airlines and Aviation Services",
					"Transportation,Airports",
					"Transportation,Parking",
					"Transportation,Public Transportation Services",
					"Transportation,Rest Areas",
					"Transportation,Taxi and Car Services",
					"Transportation,Taxi and Car Services,Car and Truck Rentals",
					"Transportation,Taxi and Car Services,Charter Buses",
					"Transportation,Taxi and Car Services,Limos and Chauffeurs",
					"Transportation,Transport Hubs",
					"Transportation,Transport Hubs,Airports",
					"Transportation,Transport Hubs,Airports,International Airports",
					"Transportation,Transport Hubs,Bus Stations",
					"Transportation,Transport Hubs,Heliports",
					"Transportation,Transport Hubs,Ports",
					"Transportation,Transport Hubs,Rail Stations", "Travel",
					"Travel,Agents and Tour Operators", "Travel,Cruises",
					"Travel,Government Dept and Agencies,Embassies",
					"Travel,Lodging", "Travel,Lodging,Bed and Breakfasts",
					"Travel,Lodging,Cottages and Cabins",
					"Travel,Lodging,Hostels",
					"Travel,Lodging,Hotels and Motels",
					"Travel,Lodging,Lodges and Vacation Rentals",
					"Travel,Lodging,Resorts",
					"Travel,Tourist Information and Services" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		}, {
			"name" : "refunds_adjustments",
			"id" : 227,
			"displayName" : "Refunds/Adjustments",
			"label" : "refunds_adjustments",
			"categoryType" : "income",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [ "Refund" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "credit"
			}, {
				"container" : "card",
				"baseType" : "credit"
			} ],
			"transactionTypes" : []
		}, {
			"name" : "pets_pet_care",
			"id" : 42,
			"displayName" : "Pets/Pet Care",
			"label" : "pets_pet_care",
			"categoryType" : "expense",
			"global" : true,
			"smallBusinessCategory" : false,
			"personnelCategory" : true,
			"specificCategory" : true,
			"tdeLabels" : [ "Businesses and Services,Veterinarians" ],
			"rules" : [ {
				"container" : "bank",
				"baseType" : "debit"
			}, {
				"container" : "card",
				"baseType" : "debit"
			} ],
			"transactionTypes" : []
		} ]