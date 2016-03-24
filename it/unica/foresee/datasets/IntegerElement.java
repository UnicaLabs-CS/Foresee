package it.unica.foresee.datasets;

/**
 * Wrapper for integers as datasets elements.
 */
public class IntegerElement extends DatasetElement<Integer>
{

    /**
     * Constructor from an Integer.
     * @param i the element
     */
    public IntegerElement(Integer i)
    {
        this.setElement(i);
    }

    /**
     * Constructor from a DatasetElement of type Integer.
     * @param d the element
     */
    public IntegerElement(DatasetElement<Integer> d)
    {
        super(d.getElement(), d.getValueForMean());
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public double getValueForMean()
    {
        return (double) this.getElement();
    }
}
