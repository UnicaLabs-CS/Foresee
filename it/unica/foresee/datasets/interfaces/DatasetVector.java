package it.unica.foresee.datasets.interfaces;

import java.util.List;

/**
 * Vector of data.
 */
public interface DatasetVector<T extends DatasetElement> extends Iterable<T>, Dataset<T>
{
    /**
     * Get the vector size to create an array
     * @return the vector size
     */
    int getVectorSize();
}
