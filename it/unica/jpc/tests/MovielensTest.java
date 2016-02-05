package it.unica.jpc.tests;

import it.unica.jpc.datasets.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;


/**
 * Tests the Movielens class.
 *
 * @author: Fabio Colella
 */
public class MovielensTest
{
    /**
     * Tests the loading of data from example data and throwing error because of too few users.
     */
    @Test
    public void toofewUsersTest() throws FileNotFoundException
    {
        File testData = new File("test-data/movielens-test-20-users.dat");
        try
        {
            Movielens ml = new Movielens(testData);
        }
        catch (IllegalStateException e)
        {
            assertEquals("The amount of users is lower than 20.", e.getMessage());
        }
    }

    /**
     * Tests the loading of data from example data and throwing error because of rating lower than one.
     */
    @Test
    public void tooLowerRating() throws FileNotFoundException
    {
        File testData = new File("test-data/movielens-test-mismatch-less.dat");
        try
        {
            Movielens ml = new Movielens(testData);
        }
        catch (InputMismatchException e)
        {
            assertEquals("rating < 1 at line 5", e.getMessage());
        }
    }

    /**
     * Tests the loading of data from example data and throwing error because of malformed line.
     */
    @Test
    public void malformedLine() throws FileNotFoundException
    {
        File testData = new File("test-data/movielens-test-mismatch-malformed-line.dat");
        try
        {
            Movielens ml = new Movielens(testData);
        }
        catch (InputMismatchException e)
        {
            assertEquals("expected rating at line 5", e.getMessage());
        }
    }
}