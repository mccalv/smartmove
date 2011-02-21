package com.wimove.content.protocol.http;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.wimove.content.service.BatchContentsService;
import com.wimove.util.HtmlUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:wimove-serviceContext.xml" })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
@Transactional
public class CopyOfBatchServiceManagerTest {

	@Autowired
	BatchContentsService batchContentsService;
	/**
	 * Test for {@link BatchContentsService#createMp3(int)}
	 */
	@Test
	public void testCreateMp3() {
		
		String o  = StringEscapeUtils.unescapeHtml(HtmlUtils.unescapeHtmlText("<div>Ha quasi 40 anni&nbsp;il tradizionale allestimento presepiale ideato e realizzato da Giuseppe Ianni in collaborazione con&nbsp;gli operatori ecologici dell&#39;azienda AMA di Roma. <br /><br />Realizzato completamente in muratura con calce e composto da oltre 2000 pietre, di cui 350 provenienti da tutto il mondo, che intende ricostruire fin nei minimi dettagli le tipiche costruzioni della Palestina di 2000 anni fa. Si contano ben 100 case in pietra di tufo dotate di porte e finestre, 52 metri di strade &ldquo;miniaturizzate&rdquo; in sampietrini, 4 fiumi lunghi complessivamente 12 metri e 4 acquedotti. E ancora 870 gradini, 24 grotte scavate nella roccia, 2 pareti umide che formano stalattiti e ben 270 personaggi. <br /><br />Il lavoro di perfezionamento e arricchimento del Presepe da parte del suo autore &egrave; proseguito in tutti questi anni, con l&rsquo;impegno volontario fuori orario di lavoro di alcune decine di operatori ecologici e dipendenti dell&rsquo;AMA, e continua ancora oggi con piccole ma continue modifiche. Si stima che abbiano ammirato il Presepe oltre 2 milioni di persone, tra cui nel gennaio del 1974 Papa Paolo VI, dall&rsquo;inizio del Suo Pontificato fino al 2002 Papa Giovanni Paolo II, che non &egrave; mai mancato al tradizionale appuntamento natalizio con i netturbini romani, e nel 2006 Papa Benedetto XVI. Da ricordare anche le visite di Madre Teresa di Calcutta nel 1996, del Presidente della Repubblica Giorgio Napolitano nel 2007, e di tutti i primi cittadini che si sono alternati alla guida della Capitale, fino all&rsquo;attuale Sindaco Gianni Alemanno.<br /></div>"));
		System.out.println(o);
	}
	
	
	@Test
	public void testImportAll() {
		batchContentsService.importAll();
	}


}