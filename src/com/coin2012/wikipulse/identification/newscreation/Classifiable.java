package com.coin2012.wikipulse.identification.newscreation;


/**
 * Defines a clustering contract of classifiable objects.
 * 
 */

public interface Classifiable {

	/**
	 * Classifies a list objects based on domain rules
	 * 
	 * @param classifiable
	 *          object that can be classified
	 * @return true if the object is positively classified other returns false
	 */
	public  boolean  classify(Object classifiable);

}
