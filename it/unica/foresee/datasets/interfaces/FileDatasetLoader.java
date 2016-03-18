package it.unica.foresee.datasets.interfaces;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Loads datasets from files.
 */
public interface FileDatasetLoader<T extends Dataset> extends DatasetLoader
{
    /**
     * Sets a separator for data in the same line.
     * @param separator
     */
    void setSeparator(String separator);

    /**
     * Set the dataset file.
     * @param dataset the dataset
     */
    void setDatasetFile(File dataset);

    /**
     * Load a specified dataset.
     *
     * @return the loaded dataset
     */
    T loadDataset() throws FileNotFoundException;
}
