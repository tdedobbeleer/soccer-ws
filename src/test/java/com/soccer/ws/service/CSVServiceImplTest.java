package com.soccer.ws.service;

import org.assertj.core.util.Lists;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class CSVServiceImplTest {
    private final CSVService csvService = new CSVServiceImpl();

    @BeforeMethod
    public void setUp() {
    }

    @Test
    public void testWrite() {
        assertEquals("test,test\ntest1,test2", csvService.write(Lists.newArrayList(Lists.newArrayList("test", "test"), Lists.newArrayList("test1", "test2"))));
    }
}
