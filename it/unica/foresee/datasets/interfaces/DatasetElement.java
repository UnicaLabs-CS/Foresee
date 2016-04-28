package it.unica.foresee.datasets.interfaces;

/**
 * Represents an element from a dataset.
 */
public interface DatasetElement<K>
{
    /**
     * Set the dataset element
     * @param e the dataset element
     */
    public void setElement(K e);

    /**
     * Get the dataset element
     * @return
     */
    public K getElement();

    /**
     * Get a real value of the element, to use for computing the mean.
     *
     * This value should represent a real value for the element, so that
     * a mean for elements of the same kind can be calculated.
     *
     * @return a real value of the element, to use for computing the mean.
     */
    public double getDoubleValue();

    /**
     * Set the value of the element to be used computing the mean.
     */
    public void setDoubleValue(double v);

}
