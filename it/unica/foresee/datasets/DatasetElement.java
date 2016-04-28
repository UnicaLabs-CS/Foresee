package it.unica.foresee.datasets;

/**
 * Generic dataset element implementation.
 */
public class DatasetElement<T> implements it.unica.foresee.datasets.interfaces.DatasetElement<T>
{
    private double doubleValue;

    private T element;

    /**
     * Empty constructor.
     */
    public DatasetElement(){}

    /**
     * Initialize the object with the element and the value for the mean
     * @param el
     * @param doubleValue
     */
    public DatasetElement(T el, double doubleValue)
    {
        this.element = el;
        this.doubleValue = doubleValue;
    }

    /* Getter */

    /**
     * {@inheritDoc}
     */
    public double getDoubleValue()
    {
        return this.doubleValue;
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
    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return Double.toString(this.getDoubleValue());
    }
}
