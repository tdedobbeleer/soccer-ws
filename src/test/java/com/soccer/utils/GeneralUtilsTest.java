package com.soccer.utils;

import com.soccer.ws.utils.GeneralUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by u0090265 on 24/11/16.
 */
public class GeneralUtilsTest {

    @Test
    public void testAbbreviateName() throws Exception {
        assertEquals("DD", GeneralUtils.abbreviateLastName("Doe Doe"));
        assertEquals("G", GeneralUtils.abbreviateLastName("Genius"));
        assertEquals("GEG", GeneralUtils.abbreviateLastName("Great Evil Genius"));
        assertEquals("", GeneralUtils.abbreviateLastName(""));
        assertEquals("", GeneralUtils.abbreviateLastName(null));
    }
}