package it.unica.foresee.datasets;

import it.unica.foresee.datasets.interfaces.NumberElement;

/**
 * Wrapper for integers as datasets elements.
 */
public class DoubleElement extends DatasetElement<Double> implements NumberElement<Double>
{
    /**
     * Constructor from a Double.
     * @param d the element
     */
    public DoubleElement(Double d)
    {
        this.setElement(d);
    }

    /**
     * Constructor from a DatasetElement of type Double.
     * @param d the element
     */
    public DoubleElement(DatasetElement<Double> d)
    {
        super(d.getElement(), d.getDoubleValue());
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public double getDoubleValue()
    {
        return this.getElement();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        return Double.toString(this.getElement());
    }
}
