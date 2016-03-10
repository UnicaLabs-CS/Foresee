package it.unica.foresee.datasets;

/**
 * Generic interface to load a Dataset.
 */
public interface DatasetLoaderInterface
{
    /**
     * Sets a separator for data in the same line.
     * @param separator
     */
    void setSeparator(String separator);

    /**
     * Get the specified dataset.
     * @return the specified dataset.
     */
    DatasetInterface loadDataset();
}
