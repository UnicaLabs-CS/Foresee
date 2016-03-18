package it.unica.foresee.utils;

/**
 * A triple is a tuple of three elements.
 */
public class Triple<T extends Comparable<T>, U extends Comparable<U>, V extends Comparable<V>> implements Comparable<Triple<T,U,V>>
{
    /**
     * First element.
     */
    private T fst;

    /**
     * Second element.
     */
    private U snd;

    /**
     * Third element.
     */
    private V trd;

    /**
     * Empty constructor.
     */
    public Triple(){}

    /**
     * Initializer constructor.
     */
    public Triple(T fst, U snd, V trd)
    {
        this.fst = fst;
        this.snd = snd;
        this.trd = trd;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return fst + ", " + snd + ", " + trd;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o)
    {
        if(o == this) return true;

        if (o == null)
        {
            return false;
        }

        Triple t = (Triple) o;

        if (!fst.equals(t.fst))
        {
            return false;
        }

        if (!snd.equals(t.snd))
        {
            return false;
        }

        if (!trd.equals(t.trd))
        {
            return false;
        }

        return true;
    }

    /**
     * {@inheritDoc}
     * Compares the elements from the first to the third.
     */
    @Override
    public int compareTo(Triple<T, U, V> t)
    {
        if (fst.compareTo(t.fst) == 0)
        {
            if (snd.compareTo(t.snd) == 0)
            {
                return trd.compareTo(t.trd);
            }
            else
            {
                return snd.compareTo(t.snd);
            }
        }
        else
        {
            return fst.compareTo(t.fst);
        }
    }

    /* --- Setter --- */

    public void setFst(T fst) {
        this.fst = fst;
    }

    public void setSnd(U snd) {
        this.snd = snd;
    }

    public void setTrd(V trd) {
        this.trd = trd;
    }


    /* --- Getter --- */

    public T getFst() {
        return fst;
    }

    public U getSnd() {
        return snd;
    }

    public V getTrd() {
        return trd;
    }
}
