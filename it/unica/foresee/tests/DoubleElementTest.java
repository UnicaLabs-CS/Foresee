package it.unica.foresee.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import it.unica.foresee.datasets.DoubleElement;
import org.junit.Test;

/**
 * Test for the DoubleElement class.
 */
public class DoubleElementTest
{
    @Test
    public void testDeepClone()
    {
        Double dOriginal = 5.0;
        DoubleElement original = new DoubleElement(dOriginal);
        DoubleElement clone = original.deepClone();

        assertEquals(original, clone);
        assertNotSame(original, clone);
        assertNotSame(dOriginal, clone.getElement());
    }
}