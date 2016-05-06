package it.unica.foresee.datasets.interfaces;

import it.unica.foresee.datasets.DatasetSparseVector;

import java.util.Map;
import java.util.TreeMap;

/**
 * Defines Sparse Vectors of Sparse Vectors
 */
public interface DatasetNestedSparseVector<T extends DatasetSparseVector<?>> extends DatasetVector<T>, Map<Integer, T>
{
    /**
     * Returns the highest of the last keys of the nested elements.
     * @return
     */
    int getHighestNestedKey();
}
