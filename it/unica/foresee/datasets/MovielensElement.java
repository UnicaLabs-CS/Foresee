package it.unica.foresee.datasets;

import it.unica.foresee.datasets.interfaces.DatasetElement;

/**
 * An element of the Movielens dataset.
 */
public class MovielensElement implements DatasetElement<MovieUserRate>
{
    /**
     * Internal stored element
     */
    private MovieUserRate element;
    private double movieAmount;

    /**
     * Initializes the element.
     * @param m
     */
    public MovielensElement(MovieUserRate m)
    {
        this.element = m;
    }

    public MovielensElement(MovieUserRate m, double movieAmount)
    {
        this.element = m;
        this.movieAmount = movieAmount;
    }

    public MovielensElement(MovieUserRate m, double movieAmount, double valueForMean)
    {
        this.element = m;
        this.movieAmount = movieAmount;
        this.setValueForMean(valueForMean);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MovieUserRate getElement() {
        return element;
    }

    public double getMovieAmount() {
        return movieAmount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getValueForMean()
    {
        return this.movieAmount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setElement(MovieUserRate element) {
        this.element = element;
    }

    public void setMovieAmount(double movieAmount) {
        this.movieAmount = movieAmount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValueForMean(double v)
    {
        this.movieAmount = v;
    }
}
