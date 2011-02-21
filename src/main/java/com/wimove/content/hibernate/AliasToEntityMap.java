/**
 * 
 */
package com.wimove.content.hibernate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.transform.ResultTransformer;

/**
 * @author mccalv
 *
 */
public class AliasToEntityMap implements ResultTransformer {
	public Object transformTuple(Object[] tuple, String[] aliases) {
		Map result = new HashMap(tuple.length);
		for ( int i=0; i<tuple.length; i++ ) {
			String alias = aliases[i];
			if ( alias!=null ) {
				result.put( alias, tuple[i] );
			}
		}
		return result;
	}

	public List transformList(List collection) {
		return collection;
	}

}
