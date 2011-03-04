package com.wimove.content.protocol.http;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.closertag.smartmove.server.service.audio.TTSServiceManager;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:wimove-serviceContext.xml" })
public class TTSServiceManagerTest {

	@Autowired
	TTSServiceManager ttsServiceManager;

	// @Test
	public void testGenerateMp3() {

		ttsServiceManager
				.generateMp3FromServer(
						"ZET_0",
						"Nel luogo simbolo monumento-simbolo dell’Unità d’Italia, un"
								+ "riconoscimento postumo ai milioni di italiani che l’Italia dovettero abbandonarla per fuggire la fame e cercare una vita migliore. Ci sono "
								+ "voluti quasi tre anni per realizzare nei 400 mq della ex Gipsoteca dell’Altare della Patria il Museo dell’emigrazione italiana (Mei), nelcomplesso del Vittoriano.E se a livello locale, sono innumerevoli i"
								+ "musei che ricordano i migranti di una data area geografica, mancava"
								+ "ancora un “contenitore” unico che raccontasse nel suo insieme+"
								+ "un’esperienza tanto complessa. La data simbolica d’inizio è il 1861,"
								+ "anno dell’unificazione italiana, anche se l’emigrazione iniziò molto"
								+ "prima. Attraverso sei sezioni si arriva fino ai giorni nostri, con i"
								+ "casi di a", TTSServiceManager.Voice.Paola);
	}

	// @Test
	public void testItemMp3() throws IOException {

		ttsServiceManager.generateItemTTS("ZET_12749", Locale.ITALIAN);
	}

	@Test
	public void testcreateAll() throws IOException {

		//ttsServiceManager.generateAllMp3Files("libri, musica e video",
		//		Locale.ENGLISH);
		GenerateMp3Thread target = new GenerateMp3Thread("cattolici", Locale.ITALIAN);
		new Thread(
				target)
				.run();
		GenerateMp3Thread target2 = new GenerateMp3Thread("cattolici", Locale.ENGLISH);
		new Thread(
				target2)
		.run();
		
		
	}

	private class GenerateMp3Thread implements Runnable {

		private String category;
		private Locale locale;

		public GenerateMp3Thread(String category, Locale locale) {
			this.category = category;
			this.locale = locale;
			
		}

		public void run() {
			try {
				ttsServiceManager.generateAllMp3Files(category, locale);
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
}