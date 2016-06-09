package it.unica.foresee.datasets.interfaces;

/**
 * An element that can be converted to double.
 */
public interface DoubleConvertible<T> {

    /**
     * Obtain the double value of T
     * @param obj the object to convert to double
     * @return the double value of the object
     */
    double getDoubleValue(T obj);
}
