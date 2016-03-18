package it.unica.foresee.datasets;

/**
 * Generic dataset element implementation.
 */
public class DatasetElement<T> implements it.unica.foresee.datasets.interfaces.DatasetElement<T>
{
    private double valueForMean;

    private T element;

    public DatasetElement(){}

    public DatasetElement(T el, double valueForMean)
    {
        this.element = el;
        this.valueForMean = valueForMean;
    }

    /* Getter */

    /**
     * {@inheritDoc}
     */
    public double getValueForMean()
    {
        return this.valueForMean;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T getElement() {
        return element;
    }

    /* Setter */

    /**
     * {@inheritDoc}
     */
    @Override
    public void setElement(T element) {
        this.element = element;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValueForMean(double valueForMean) {
        this.valueForMean = valueForMean;
    }
}
