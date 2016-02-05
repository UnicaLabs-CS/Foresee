package it.unica.jpc.datasets;

/**
 * Defines a tuple of three elements.
 *
 * @author Fabio Colella
 */
public class Triple implements Comparable <Triple>
{
    private int fst;
    private int snd;
    private double trd;

    /**
     * Constructs a triple (0, 0, 0.0)
     */
    public Triple()
    {
        this.fst = 0;
        this.snd = 0;
        this.trd = 0.0;
    }

    /**
     * Constructs a triple from three parameters.
     * @param fst first element
     * @param snd second element
     * @param trd third element
     */
    public Triple(int fst, int snd, double trd)
    {
        this.fst = fst;
        this.snd = snd;
        this.trd = trd;
    }

    public void setFst(int fst) {
        this.fst = fst;
    }

    public void setSnd(int snd) {
        this.snd = snd;
    }

    public void setTrd(double trd) {
        this.trd = trd;
    }

    public int getFst() {
        return fst;
    }

    public int getSnd() {
        return snd;
    }

    public double getTrd() {
        return trd;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o)
    {
        if(o == this) return true;
        if(o == null) return false;

        Triple obj = (Triple) o;

        if (this.fst != obj.getFst())
        {
            return false;
        }

        if (this.snd != obj.getSnd())
        {
            return false;
        }

        if (this.trd != obj.getTrd())
        {
            return false;
        }

        return true;
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
     * The triples are ordered first comparing the first element,
     * then the second.
     *
     * @param t the triple to compare.
     */
    @Override
    public int compareTo(Triple t)
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
