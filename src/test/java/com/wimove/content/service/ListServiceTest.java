/* Wimove project, 2009.
 * Java restful server to erogate GPS contents to multiple devices. All rights reserved
 * 24 Nov 2009
 * mccalv
 * ZetemaMuseiXmlParsing
 * 
 */
package com.wimove.content.service;

import java.util.List;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.wimove.content.domain.ItemList;
import com.wimove.content.geometry.GeoLocHelper;
import com.wimove.content.persistence.filter.SearchFilter;

/**
 * It parses data coming from a different source. At this stage, data are not
 * provided by a relative XSD schema
 * 
 * @author mccalv
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:wimove-serviceContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class ListServiceTest {


	@Autowired
	ListService listService;

	/**
	 * Returns all the available list for a given geometry
	 * @throws Exception
	 */
	@Test
	public void testSearchListByGeometry() throws Exception {
	 /**
	  *    select
        this_.id as id12_1_,
        this_.creation_date as creation2_12_1_,
        this_.name as name12_1_,
        this_.update_date as update4_12_1_,
        gidzones3_.id_list as id1_,
        gidzones1_.id as id2_,
        gidzones1_.id as id14_0_,
        gidzones1_.identifier as identifier14_0_,
        gidzones1_.polygon as polygon14_0_ 
    from
        public.list this_ 
    inner join
        list_gidzone gidzones3_ 
            on this_.id=gidzones3_.id_list 
    inner join
        public.gidzone gidzones1_ 
            on gidzones3_.id_gidzone=gidzones1_.id 
    where
        st_contains(gidzones1_.polygon, PointFromText('POINT(12.4713136553873 41.9019988853709)',4326)) 
        
        
	  */
        //st_contains(gidzones1_.polygon, PointFromText('POINT(12.4713134765625 41.902000427246094)',4326)) 
		
		List<ItemList> listsByGeometry = listService.getListsByGeometry(
				
				GeoLocHelper.createPoint(12.505016f, 41.894962f)
				//http://maps.google.com/maps?f=q&source=s_q&hl=en&q=Piazza+Vittorio+Emanuele+II,+00185+Roma,+Lazio,+Italy&sll=41.902036,12.471292&sspn=0.008976,0.021372&ie=UTF8&cd=1&geocode=FflEfwIduM6-AA&split=0&hq=&hnear=Piazza+Vittorio+Emanuele+II,+00185+Roma,+Lazio,+Italy&ll=&spn=0.008977,0.021372&z=16&iwloc=A
				
				
				, Locale.ENGLISH);	
		System.out.println(listsByGeometry.size());
		for (ItemList i:listsByGeometry){
			
			System.out.println(i.getListName());
		}
	}

}
