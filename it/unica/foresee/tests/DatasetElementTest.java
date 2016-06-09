package it.unica.foresee.tests;

import it.unica.foresee.datasets.DatasetElement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import it.unica.foresee.datasets.interfaces.DoubleConvertible;
import org.junit.Test;

/**
 * Test for the DatasetElement class.
 */
public class DatasetElementTest
{
    @Test
    public void testEquals()
    {
        DoubleConvertible<Double> dConverter = (d) -> d.doubleValue();
        assertEquals(new DatasetElement<>(5.0, dConverter), new DatasetElement<>(5.0, dConverter));
        assertEquals(new DatasetElement(5.0, dConverter), new DatasetElement(5.0, dConverter));
        assertNotEquals(new DatasetElement(5.0, dConverter), new DatasetElement(7.0, dConverter));

        DoubleConvertible<Integer> iConverter = (Integer d) -> d.doubleValue();
        assertEquals(new DatasetElement<>(5, iConverter), new DatasetElement<>(5, iConverter));
        assertEquals(new DatasetElement(5, iConverter), new DatasetElement(5, iConverter));
        assertNotEquals(new DatasetElement(5, iConverter), new DatasetElement(7, iConverter));
    }
}
