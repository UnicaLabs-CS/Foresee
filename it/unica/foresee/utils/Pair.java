package it.unica.foresee.utils;

/**
 * A pair is a tuple of two elements.
 */
public class Pair <T extends Comparable<T>, U extends Comparable<U>> implements Comparable<Pair<T,U>>
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
     * Empty constructor.
     */
    public Pair(){}

    /**
     * Initializer constructor.
     */
    public Pair(T fst, U snd)
    {
        this.fst = fst;
        this.snd = snd;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return fst + ", " + snd ;
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

        Pair p = (Pair) o;

        if (!fst.equals(p.fst))
        {
            return false;
        }

        if (!snd.equals(p.snd))
        {
            return false;
        }

        return true;
    }

    /**
     * {@inheritDoc}
     * Compares the the elements first by first, then second by second.
     */
    @Override
    public int compareTo(Pair<T, U> p)
    {
        if (fst.compareTo(p.fst) == 0)
        {
            return snd.compareTo(p.snd);
        }
        else
        {
            return fst.compareTo(p.fst);
        }
    }

    /* --- Setter --- */

    public void setFst(T fst) {
        this.fst = fst;
    }

    public void setSnd(U snd) {
        this.snd = snd;
    }


    /* --- Getter --- */

    public T getFst() {
        return fst;
    }

    public U getSnd() {
        return snd;
    }
}
