package it.unica.foresee.datasets.interfaces;

/**
 * Represents a generic dataset.
 */
public interface Dataset<T extends DatasetElement> extends Iterable<T>
{
    /**
     * Get the element at the specified index.
     * @param index the index of the vector
     * @return the requested element
     */
    T getDatasetElement(int index);

    /**
     * Set an element at a specified index.
     * @param index the index where to put it
     * @param e the element to set
     */
    void setDatasetElement(int index, T e);

    /**
     * Get the partitions obtained using the stratified k-fold algorithm.
     *
     * @param k the number of partitions
     * @param layers the number of layers for the stratification
     * @return the obtained partitions
     */
    Dataset[] getKFoldPartitions(int k, int layers);


}
