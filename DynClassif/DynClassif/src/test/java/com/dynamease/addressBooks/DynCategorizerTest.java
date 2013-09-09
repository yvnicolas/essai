package com.dynamease.addressBooks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.dynamease.entity.DynCategories;
import com.dynamease.entity.DynContact;
import com.dynamease.entity.DynPerson;
import com.dynamease.entity.DynSubscriber;
import com.dynamease.services.ldap.DynContactDaoInterface;
import com.dynamease.services.ldap.DynDirectoryAccessException;

public class DynCategorizerTest {

	private DynCategorizer underTest;
	private DynSubscriber subyni = new DynSubscriber("Yves", "Nicolas");
	private DynCatRatedPerson contact;
	private DynContact tobeAdded;

	private Method infercat, updatecat;

	private DynContactDaoInterface contactDaoMock = mock(DynContactDaoInterface.class);
	private DynExternalAddressBook addrBookMock = mock(DynExternalAddressBook.class);

	@Before
	public void setUp() throws Exception {

		// Initial set up of rating map.
		// Several categories purposedly not initiated
		contact = new DynCatRatedPerson(new DynPerson("Pauline", "Joly"));
		contact.setRate(DynCategories.PROFESSIONAL, 50);
		tobeAdded = new DynContact(contact.getPerson().getFirstName(), contact.getPerson().getLastName(), subyni);

		// Under Test class setup with private field reflection set up
		underTest = new DynCategorizer();

		ReflectionTestUtils.setField(underTest, "defaultcat", DynCategories.FRIEND, DynCategories.class);
		ReflectionTestUtils.setField(underTest, "dynContactDao", contactDaoMock, DynContactDaoInterface.class);
		ReflectionTestUtils.setField(underTest, "addrBook", addrBookMock, DynExternalAddressBook.class);

		// Make private method accessible
		infercat = DynCategorizer.class.getDeclaredMethod("inferCat", DynCatRatedPerson.class, DynCatMode.class);
		infercat.setAccessible(true);
		updatecat = DynCategorizer.class.getDeclaredMethod("updateCat", DynContact.class, DynCatRatedPerson.class, DynCatMode.class);
		updatecat.setAccessible(true);

		// Set up address book mock for 1 elements in list
		when(addrBookMock.hasNext())
			.thenReturn(true)
			.thenReturn(false);
		when(addrBookMock.next(any(DynSubscriber.class))).thenReturn(contact);

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

	// Test Private Methode updateCat

	@Test
	public void testupdateCatunknown() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		// Lightweight mode should generate an unknown category, no change to
		// the contact category
		updatecat.invoke(underTest, tobeAdded, contact, DynCatMode.LIGHTWEIGHT);
		assertTrue(tobeAdded.getCategories().isEmpty());

	}

	@Test
	public void testupdateCatForced() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// Mode Forced : after update Friend and Professionnal should be present
		tobeAdded.addCategory(DynCategories.PROFESSIONAL);
		updatecat.invoke(underTest, tobeAdded, contact, DynCatMode.FORCED);
		assertTrue(tobeAdded.categoryIsPresent(DynCategories.FRIEND));
		assertTrue(tobeAdded.categoryIsPresent(DynCategories.PROFESSIONAL));

	}

	@Test
	public void testupdateCatAuto() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// Mode Auto : after update Friend and Professionnal should be present
		tobeAdded.addCategory(DynCategories.FRIEND);
		updatecat.invoke(underTest, tobeAdded, contact, DynCatMode.AUTO);
		assertTrue(tobeAdded.categoryIsPresent(DynCategories.FRIEND));
		assertTrue(tobeAdded.categoryIsPresent(DynCategories.PROFESSIONAL));

	}

	@Test
	public void testupdateCatLW() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// Mode Lightweight : after update Friend and Family should be present,
		// not Professionnal
		tobeAdded.addCategory(DynCategories.FRIEND);
		contact.setRate(DynCategories.FAMILY, 70);
		updatecat.invoke(underTest, tobeAdded, contact, DynCatMode.AUTO);
		assertTrue(tobeAdded.categoryIsPresent(DynCategories.FRIEND));
		assertFalse(tobeAdded.categoryIsPresent(DynCategories.PROFESSIONAL));
		assertTrue(tobeAdded.categoryIsPresent(DynCategories.FAMILY));

	}

	@Test
	public void testupdateCatReset() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// Mode Reset : after update Professionnal has been reased by Friedn
		tobeAdded.addCategory(DynCategories.PROFESSIONAL);
		updatecat.invoke(underTest, tobeAdded, contact, DynCatMode.RESET);
		assertTrue(tobeAdded.categoryIsPresent(DynCategories.FRIEND));
		assertFalse(tobeAdded.categoryIsPresent(DynCategories.PROFESSIONAL));

	}

	@Test
	public void testupdateCatStrongAuto() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		// Mode Reset : after update Professionnal has been erased by Family
		tobeAdded.addCategory(DynCategories.PROFESSIONAL);
		contact.setRate(DynCategories.FAMILY, 70);
		updatecat.invoke(underTest, tobeAdded, contact, DynCatMode.STRONGAUTO);
		assertTrue(tobeAdded.categoryIsPresent(DynCategories.FAMILY));
		assertFalse(tobeAdded.categoryIsPresent(DynCategories.PROFESSIONAL));

	}




// Tests Categorize

 @Test
 public void testCategorizeContactNotFound() throws DynDirectoryAccessException {
	 
	 // setup DynContactDao Mock to return null
	 when (contactDaoMock.findContact(any(DynPerson.class), eq(subyni))).thenReturn(null);
	 
	 underTest.categorize(subyni, addrBookMock, DynCatMode.AUTO, DynCategories.CLIENT);
	 verify(contactDaoMock).addContact((DynContact) isNotNull());
 
 }
 
 @Test
 public void testCategorizeContactFound() throws DynDirectoryAccessException {
	 
	 // setup DynContactDao Mock to return null
	 when (contactDaoMock.findContact(any(DynPerson.class), eq(subyni))).thenReturn(tobeAdded);
	 
	 underTest.categorize(subyni, addrBookMock, DynCatMode.AUTO, DynCategories.CLIENT);
	 verify(contactDaoMock).addContact(eq(tobeAdded));
 
 }
}


