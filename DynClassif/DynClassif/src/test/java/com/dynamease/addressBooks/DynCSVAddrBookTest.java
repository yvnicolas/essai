package com.dynamease.addressBooks;

import static org.junit.Assert.*;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "file:src/main/applicationContext.xml" })
public class DynCSVAddrBookTest {

	private DynCSVAddrBook underTest;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Test
	public void testDynCSVAddrBook() throws IOException {
		
		Reader  is =new InputStreamReader(new ClassPathResource("SimpleMockCSVCiel.csv").getInputStream());
		underTest = new DynCSVAddrBook(is);
		assertNotNull(underTest);
	}

	@Test
	public void testNext() throws IOException {
		
		Reader  is =new InputStreamReader(new ClassPathResource("SimpleMockCSVCiel.csv").getInputStream());
		underTest = new DynCSVAddrBook(is);
		assertTrue(underTest.hasNext());
	}
}
