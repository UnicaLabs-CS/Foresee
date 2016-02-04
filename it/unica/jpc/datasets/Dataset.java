package it.unica.jpc.datasets;

/**
 * Represents a generic set of data.
 *
 * The classes implementing this interface can be used as a
 * dataset to retrieve useful information.
 *
 * @author Fabio Colella <fcole90@gmail.com>
 */
public abstract class Dataset
{

    /**
     * Loads data from a specified source.
     *
     * @param sourceFile the file from which to load the data
     * @return true if the loading completed without errors
     */
    public boolean loadDataset(String sourceFile);
}