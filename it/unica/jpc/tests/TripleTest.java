package it.unica.jpc.tests;

import it.unica.jpc.datasets.Triple;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Tests the Triple class.
 *
 * @author: Fabio Colella <fcole90@gmail.com>
 */
public class TripleTest 
{

    protected int a;
    protected int b;
    protected double c;

	
	@Test
    public void testNotNull()
    {
        assertNotNull("Should not be null.", new Triple(9, 3, 5.1));
    }
    
    @Test
    public void testOrder()
    {
    	Triple t1 = new Triple(9, 3, 5.1);
    	Triple t2 = new Triple(9, 4, 5.1);
    	Triple t3 = new Triple(10, 3, 5.1);
    	
    	assertTrue("Triples with greater first number come first.", t3.compareTo(t2) > 0);
    	assertTrue("Triples with even first number but greater second number come first.", t2.compareTo(t1) > 0);
    }
    
    @Test
    public void testEquality()
    {
    	Triple t1 = new Triple(9, 3, 5.1);
    	Triple t2 = new Triple(9, 3, 5.1);
    	assertEquals("Two triples with the same elements should be equal.", t1, t2);
    }
    
    @Test
    public void testString()
    {
    	Triple t1 = new Triple(9, 3, 5.1);
    	assertEquals("The method toString() should produce an output formatted "
    	+ "in this way: fst::snd::trd.", t1.toString(), "9::3::5.1");	
    }
}
