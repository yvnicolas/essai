package com.dynamease.addressBooks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dynamease.entity.DynCategories;
import com.dynamease.entity.DynPerson;
import com.dynamease.entity.DynSubscriber;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "file:src/main/applicationContext.xml" })
public class DynCSVAddrBookTest {

	private Logger logger = LoggerFactory.getLogger(DynCSVAddrBook.class);

	private DynCSVAddrBook underTest;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testDynCSVAddrBook() throws IOException {

		File is = new File("src/main/resource/SimpleMockCSVCiel.csv");
		underTest = new DynCSVAddrBook(is);
		assertNotNull(underTest);
	}

	@Test
	public void testNext() throws IOException {

		File is = new File("src/main/resource/SimpleMockCSVCiel.csv");
		underTest = new DynCSVAddrBook(is);
		assertTrue(underTest.hasNext());
	}

	@Test
	public void testNext2() throws IOException {

		File is = new File("src/main/resource/NoContact.csv");
		underTest = new DynCSVAddrBook(is);
		assertFalse(underTest.hasNext());
	}

	@Test
	public void testIteration() {
		File is = new File("src/main/resource/SimpleMockCSVCiel.csv");
		underTest = new DynCSVAddrBook(is);
		int compteur = 0;
		DynPerson current;
		while (underTest.hasNext()) {
			current = underTest.getNextEntry();
			if (logger.isDebugEnabled()) {
				logger.info(String.format("Contact %s : %s %s", compteur,
						current.getFirstName(), current.getLastName()));
			}
			compteur++;
		}
		assertEquals(17, compteur);
	}

	@Test
	public void testListCat() {
		File is = new File("src/main/resource/googlecontactsboulot.csv");
		underTest = new DynCSVAddrBook(is);
		Set<String> categories = underTest.retreiveCategories();
		for (String cat : categories) {
			logger.info(String.format("Categorie:%s:", cat));
		}
		assertEquals(8, categories.size());

	}

	@Test
	public void testRating() {
		DynSubscriber subYni = new DynSubscriber("Yves", "Nicolas");
		File is = new File("src/main/resource/forCheckingRates.csv");
		underTest = new DynCSVAddrBook(is);

		// First contact should not match any categories
		for (DynCategories cat : DynCategories.values()) {
			assertEquals(0, underTest.rateCurrent(cat, subYni));
		}

		// second contact should be in professionnal
		underTest.getNextEntry();
		for (DynCategories cat : DynCategories.values()) {
			if (cat.equals(DynCategories.PROFESSIONAL)) {
				assertEquals(100, underTest.rateCurrent(cat, subYni));
			} else {
				assertEquals(0, underTest.rateCurrent(cat, subYni));

			}
		}

		// third contact should be in family
		underTest.getNextEntry();
		for (DynCategories cat : DynCategories.values()) {
			if (cat.equals(DynCategories.FAMILY)) {
				assertEquals(100, underTest.rateCurrent(cat, subYni));
			} else {
				assertEquals(0, underTest.rateCurrent(cat, subYni));

			}
		}
	}
}
