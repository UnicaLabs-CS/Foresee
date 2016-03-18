package it.unica.foresee.datasets.interfaces;

/**
 * Represents a generic dataset.
 */
public interface Dataset extends Iterable<DatasetElement>
{
    /**
     * Get the element at the specified index.
     * @param index the index of the vector
     * @return the requested element
     */
    DatasetElement getDatasetElement(int index);

    /**
     * Set an element at a specified index.
     * @param index the index where to put it
     * @param e the element to set
     */
    void setDatasetElement(int index, DatasetElement e);

    /**
     * Get the partitions obtained using the stratified k-fold algorithm.
     *
     * @param k the number of partitions
     * @param layers the number of layers for the stratification
     * @return the obtained partitions
     */
    Dataset[] getKFoldPartitions(int k, int layers);


}
