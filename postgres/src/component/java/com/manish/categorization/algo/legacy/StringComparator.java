package com.manish.categorization.algo.legacy;

import java.util.Comparator;

/**
 * This class is used for sorting Strings
 * 
 */
public class StringComparator implements Comparator {
	public StringComparator() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		String s1 = (String) o1, s2 = (String) o2;
		if (s1 == null && s2 == null)
			return 0;
		if (s1 == null)
			return 1;
		if (s2 == null)
			return -1;
		int length1 = s1.length();
		int length2 = s2.length();
		if (length1 == length2)
			return s2.compareToIgnoreCase(s1);
		else
			return length2 - length1;
	}
}
