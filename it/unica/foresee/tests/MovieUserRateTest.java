package it.unica.foresee.tests;

import it.unica.foresee.datasets.MovieUserRate;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests the MovieUserRate class.
 *
 * @author: Fabio Colella
 */
public class MovieUserRateTest
{

    protected int a;
    protected int b;
    protected double c;

    /**
     * Tests the existence of the object.
     */
	@Test
    public void testNotNull()
    {
        assertNotNull("Should not be null.", new MovieUserRate(9, 3, 5.1));
    }

    /**
     * Tests the correct ordering of the object.
     */
    @Test
    public void testOrder()
    {
    	MovieUserRate t1 = new MovieUserRate(9, 3, 5.1);
    	MovieUserRate t2 = new MovieUserRate(9, 4, 5.1);
    	MovieUserRate t3 = new MovieUserRate(10, 3, 5.1);
    	
    	assertTrue("Triples with greater first number come first.", t3.compareTo(t2) > 0);
    	assertTrue("Triples with even first number but greater second number come first.", t2.compareTo(t1) > 0);
    }

    /**
     * Tests the equality of the object.
     */
    @Test
    public void testEquality()
    {
    	MovieUserRate t1 = new MovieUserRate(9, 3, 5.1);
    	MovieUserRate t2 = new MovieUserRate(9, 3, 5.1);
    	assertEquals("Two triples with the same elements should be equal.", t1, t2);
    }

    /**
     * Tests the toString() method.
     */
    @Test
    public void testString()
    {
    	MovieUserRate t1 = new MovieUserRate(9, 3, 5.1);
    	assertEquals("The method toString() should produce an output formatted "
    	+ "in this way: fst::snd::trd.", t1.toString(), "9::3::5.1");	
    }
}
