package com.dynamease.addressBooks;

import static org.junit.Assert.*;

import org.junit.Test;

import com.dynamease.entity.DynCategories;

public class DynDefaultCategoryMapperTest {

    @Test
    public void testvalid() {
        DynDefaultCategoryMapper underTest = new DynDefaultCategoryMapper();
        assertEquals(DynCategories.CLIENT, underTest.map("client"));
        assertEquals(DynCategories.CLIENT, underTest.map("cliEnt"));
        assertEquals(DynCategories.SUPPLIER, underTest.map("suPPlier"));
    }

    @Test
    public void testInvalid() {
        DynDefaultCategoryMapper underTest = new DynDefaultCategoryMapper();
        assertNull(underTest.map("bidon"));
    }
}
