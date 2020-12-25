package com.manish.categorization.util;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

public class LocaleBuilder {

	public static Locale getLocale(String strLocale) {

		Locale locale = Locale.US;
		if (StringUtils.isNotEmpty(strLocale)) {

			String[] localeArray = strLocale.split("_");
			String language = "";
			String country = "";
			if (localeArray != null) {

				if (localeArray.length == 1)
					language = localeArray[0];
				if (localeArray.length == 2) {
					language = localeArray[0];
					country = localeArray[1];
				}

				locale = new Locale(language, country);
			}
		}

		return locale;
	}
}
