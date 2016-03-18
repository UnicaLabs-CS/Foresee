package it.unica.foresee.datasets;

import it.unica.foresee.datasets.interfaces.Dataset;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Implementaton of {@link it.unica.foresee.datasets.interfaces.FileDatasetLoader}.
 *
 * {@inheritDoc}
 */
public class FileDatasetLoader<T extends Dataset> implements it.unica.foresee.datasets.interfaces.FileDatasetLoader
{
    /**
     * Symbol separating the files.
     */
    private String separator;

    /**
     * File containing the dataset.
     */
    private File datasetFile;

    /**
     * Initializes the object with the given values.
     *
     * @param separator symbol separating the files
     * @param datasetFile file containing the dataset
     */
    public FileDatasetLoader(File datasetFile, String separator)
    {
        this.separator = separator;
        this.datasetFile = datasetFile;
    }

    public FileDatasetLoader(File datasetFile)
    {
        this.separator = ",";
        this.datasetFile = datasetFile;
    }

    public FileDatasetLoader()
    {
        this.separator = ",";
        this.datasetFile = null;
    }

    /**
     * {@inheritDoc}
     * @// TODO: 17/03/16 implement this method, but implement Dataset and DatasetElement before!
     */
    @Override
    public T loadDataset() throws FileNotFoundException
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSeparator(String separator)
    {
        this.separator = separator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDatasetFile(File datasetFile)
    {
        this.datasetFile = datasetFile;
    }

    /**
     * Get the dataset file.
     * @return the dataset file
     */
    public File getDatasetFile() {
        return datasetFile;
    }

    /**
     * Get the separator symbol.
     * @return the separator symbol
     */
    public String getSeparator() {
        return separator;
    }
}
