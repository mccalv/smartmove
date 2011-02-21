/**
 * 
 */
package com.wimove.content.bean;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.junit.Test;

import com.wimove.content.domain.Item;

/**
 * @author mccalv
 * 
 */
public class BeanComparatorTest {
	@Test
	public void compareItem() {
		
		Item item = new Item();
		//item.setId(1L);

		Item item2 = new Item();
		//item2.setId(1L);
		System.out.println(item.equals(item2));
	}

}
