package com.dynamease.addressBooks;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;

import com.dynamease.entity.DynCategories;
import com.dynamease.entity.DynPerson;
import com.dynamease.entity.DynSubscriber;

public class DynCategorizerTest {

	private DynCategorizer underTest;
	private DynSubscriber subyni = new DynSubscriber("Yves", "Nicolas");
	private DynCatRatedPerson contact = new DynCatRatedPerson(new DynPerson("Pauline", "Joly"));

	private Method infercat;

	@Before
	public void setUp() throws Exception {

		// Initial set up of rating map.
		// Several categories purposedly not initiated
		contact.setRate(DynCategories.PROFESSIONAL, 50);

		// Under Test class setup with private field reflection set up
		underTest = new DynCategorizer();
		Field sub = DynCategorizer.class.getDeclaredField("sub");
		sub.setAccessible(true);
		sub.set(underTest, subyni);

		Field defaultcat = DynCategorizer.class.getDeclaredField("defaultcat");
		defaultcat.setAccessible(true);
		defaultcat.set(underTest, DynCategories.FRIEND);

		Field addrBook = DynCategorizer.class.getDeclaredField("addrBook");
		addrBook.setAccessible(true);
		addrBook.set(underTest, null);

		// Make private method accessible
		infercat = DynCategorizer.class.getDeclaredMethod("inferCat", DynCatRatedPerson.class, DynCatMode.class);
		infercat.setAccessible(true);

	}

	/*
	 * Tests private method Infercat
	 */

	@Test
	public void testInferCatForced() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		assertEquals(DynCategories.FRIEND, infercat.invoke(underTest, contact, DynCatMode.FORCED));
		assertEquals(DynCategories.FRIEND, infercat.invoke(underTest, contact, DynCatMode.RESET));

	}

	@Test
	public void testInferCatAuto() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		assertEquals(DynCategories.PROFESSIONAL, infercat.invoke(underTest, contact, DynCatMode.AUTO));

	}

	@Test
	public void testInferCatlwunk() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		// Test Lightweight mode should return unknown as category being under
		// level
		assertEquals(DynCategories.UNKNOWN, infercat.invoke(underTest, contact, DynCatMode.LIGHTWEIGHT));
		assertEquals(DynCategories.UNKNOWN, infercat.invoke(underTest, contact, DynCatMode.STRONGAUTO));

	}

	@Test
	public void testInferCatlwok() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		// Test Lightweight mode should return unknown as category being under
		// level
		contact.setRate(DynCategories.FAMILY, 70);
		assertEquals(DynCategories.FAMILY, infercat.invoke(underTest, contact, DynCatMode.LIGHTWEIGHT));
		assertEquals(DynCategories.FAMILY, infercat.invoke(underTest, contact, DynCatMode.STRONGAUTO));

	}

	@Test
	public void testupdateCat() {
	}
}

// @Test
// public void testCategorize() {
// fail("Not yet implemented");
// }

