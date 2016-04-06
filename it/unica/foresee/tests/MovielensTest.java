package it.unica.foresee.tests;

import java.io.*;

import it.unica.foresee.datasets.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

/**
 * Test Movielens loading
 */
public class MovielensTest
{
    public final double DEVIATION = 0.3;
    public final String BIG_DATASET = "test-data/ratings.dat";
    public final String SMALL_DATASET = "test-data/movielens-test-25-users.dat";

    private MovielensLoader mLoader;
    private File mFile;
    private Movielens m;
    private DatasetSparseVector[] dVec;


    @Before public void setUp()  throws Exception
    {
        mLoader = new MovielensLoader();
        mFile = new File(SMALL_DATASET);
        m = mLoader.loadDataset(mFile);
        dVec = m.getKFoldPartitions(5, 3);
    }

    /**
     * Check a value to be in a specified range.
     * @param min
     * @param max
     * @param value the value to check
     */
    public void assertRange(double min, double max, double value)
    {
        assertTrue("Expected value in range between " + min
        + " and " + max + ". Found: " + value,
                min <= value && max >= value);
    }

    @Test
    public void loadDataset()
    {
        assertNotEquals(m, null);
    }

    @Test
    public void kFold()
    {
        assertNotEquals(dVec, null);
    }

    @Test
    public void usersAmount()
    {
        assertEquals(m.values().size(), m.getUsersAmount());
    }

    @Test
    public void getElement()
    {
        assertEquals((int) m.getElement(25, 1676), 4);
    }

    @Test
    public void assureSimilarMeansAmongPartitions()
    {
        double mean = dVec[0].getValueForMean();
        double min = mean - DEVIATION;
        double max = mean + DEVIATION;
        for (DatasetSparseVector d : dVec)
        {
            assertRange(min, max, d.getValueForMean());
        }
    }
}


