package it.unica.foresee.datasets.interfaces;

import java.io.File;

/**
 * Loads datasets from files.
 */
public interface FileDatasetLoader extends DatasetLoader
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
}
