package it.unica.foresee.datasets;

import it.unica.foresee.datasets.interfaces.Dataset;

import java.io.File;

/**
 * Implementaton of {@link it.unica.foresee.datasets.interfaces.FileDatasetLoader}.
 *
 * {@inheritDoc}
 */
public class FileDatasetLoader implements it.unica.foresee.datasets.interfaces.FileDatasetLoader
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
    public FileDatasetLoader(String separator, File datasetFile)
    {
        this.separator = separator;
        this.datasetFile = datasetFile;
    }

    public FileDatasetLoader()
    {
        String defaultSeparator = ",";
        this.datasetFile = null;
    }

    /**
     * {@inheritDoc}
     * @// TODO: 17/03/16 implement this method, but implement Dataset before!
     */
    @Override
    public Dataset loadDataset()
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
