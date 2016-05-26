package it.unica.foresee.datasets.interfaces;

import it.unica.foresee.datasets.DatasetSparseVector;

import java.util.Map;
import java.util.TreeMap;

/**
 * Defines Sparse Vectors of Sparse Vectors.
 */
public interface DatasetNestedSparseVector<T extends DatasetSparseVector<?>> extends DatasetVector<T>, Map<Integer, T>
{
    /**
     * Returns the highest key among the keys of the nested elements.
     * @return
     */
    int getHighestNestedKey();

    /**
     * Returns the size of the vector including its missing entries.
     *
     * This is useful to convert the vector to an array.
     * @return the size of the vector
     */
    int getInternalVectorSize();

    /**
     * Sets the size of the vector including its missing entries.
     *
     * This is useful to convert the vector to an array.
     * @param vectorSize the size of the vector
     */
    void setInternalVectorSize(int vectorSize);
}
