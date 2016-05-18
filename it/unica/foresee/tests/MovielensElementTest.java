package it.unica.foresee.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import it.unica.foresee.datasets.MovielensElement;
import org.junit.Test;

/**
 * Test for the MovielensElement class.
 */
public class MovielensElementTest
{
    @Test
    public void testEquals()
    {
        double[] array = {4.0, 5.0, 3.2, 1.2};
        MovielensElement one = (MovielensElement) TestUtils.fillDatasetEntry(array);
        MovielensElement two = (MovielensElement) TestUtils.fillDatasetEntry(array);

        assertEquals(one, two);
        assertNotSame(one, two);
    }


    @Test
    public void testDeepClone()
    {
        double[] array = {4.0, 5.0, 3.2, 1.2};
        MovielensElement original = (MovielensElement) TestUtils.fillDatasetEntry(array);
        MovielensElement clone = original.deepClone();

        assertEquals(original, clone);
        assertNotSame(original, clone);
    }
}