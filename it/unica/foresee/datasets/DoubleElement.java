package it.unica.foresee.datasets;

/**
 * Wrapper for integers as datasets elements.
 */
public class DoubleElement extends DatasetElement<Double>
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
        super(d.getElement(), d.getValueForMean());
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public double getValueForMean()
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
