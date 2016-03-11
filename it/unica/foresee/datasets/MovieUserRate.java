package it.unica.foresee.datasets;

import it.unica.foresee.utils.Triple;

/**
 * Defines a tuple of three elements.
 *
 * @author Fabio Colella
 */
public class MovieUserRate extends Triple<Integer, Integer, Double>
{
    /**
     * Constructs a triple (0, 0, 0.0)
     */
    public MovieUserRate()
    {
        super(0, 0, 0.0);
    }

    /**
     * Constructs a triple from three parameters.
     * @param fst first element
     * @param snd second element
     * @param trd third element
     */
    public MovieUserRate(Integer fst, Integer snd, Double trd)
    {
        setFst(fst);
        setSnd(snd);
        setTrd(trd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o)
    {
        if(o == this) return true;
        if(o == null) return false;

        MovieUserRate m = (MovieUserRate) o;

        return super.equals(m);
    }
    
    /**
     * Get a string of the type fst::snd::trd.
     * @return a string of the type fst::snd::trd
     */
    @Override
    public String toString()
    {
    	return this.fst + "::" + this.snd + "::" + this.trd;
    }

    /**
     * Compares the triples.
     *
     * The triples are ordered first comparing the first element and
     * then the second.
     *
     * @param t the triple to compare.
     */
    @Override
    public int compareTo(MovieUserRate t)
    {
        if (this.fst != t.getFst())
        {
            return this.fst - t.getFst();
        }
        else
        {
            return this.snd - t.getSnd();
        }
    }
}
