package it.unica.foresee.datasets;

import it.unica.foresee.datasets.interfaces.*;

/**
 * Generic dataset element implementation.
 */
public class DatasetElement<T> implements it.unica.foresee.datasets.interfaces.DatasetElement<T>
{
    private double doubleValue;

    private T element;

    private DoubleConvertible<T> converter = null;

    /**
     * Empty constructor.
     */
    public DatasetElement(){}

    /**
     * Initialize the object with the element and and interface to get its double value.
     */
    public DatasetElement(T el, DoubleConvertible<T> converter)
    {
        this.element = el;
        this.converter = converter;
    }

    /**
     * Initialize the object with the element and the value for the mean
     * @param el
     * @param doubleValue
     */
    @Deprecated
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
        if (converter != null)
        {
            return converter.getDoubleValue(element);
        }
        else return this.doubleValue;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o)
    {
        if(o == this) return true;

        if (o == null) return false;

        if ( !(o instanceof DatasetElement) ) return false;

        DatasetElement d = (DatasetElement) o;

        if(!this.getElement().equals(d.getElement()) ||
                !(this.getDoubleValue() == d.getDoubleValue())) return false;

        return true;
    }
}
