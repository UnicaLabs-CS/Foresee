package it.unica.foresee.datasets.interfaces;

/**
 * Represents an element from a dataset.
 */
public interface DatasetElement<T>
{
    /**
     * Set the dataset element
     * @param e the dataset element
     */
    public void setElement(T e);

    /**
     * Get the dataset element
     * @return
     */
    public T getElement();

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
     * Set the converter to an associated double value.
     */
    public void setDoubleValueConverter(DoubleConvertible<T> converter);

}
