package it.unica.jpc.datasets;

import it.unica.jpc.datasets.Dataset;

/**
 * Provides methods to use the freely available movielens dataset.
 *
 * @author Fabio Colella <fcole90@gmail.com>
 */
public class Movielens extends Dataset
{
    /**
     * Empty constructor.
     */
    public Dataset()
    {
        //Empty
    }

    /**
     * Constructs a movielens from the specified file.
     *
     * @param sourceFile
     */
    public Dataset(String sourceFile)
    {
        //Use loadDataset
    }

    /**
     * Loads data from a specified source.
     *
     * @param sourceFile the file from which to load the data
     * @return true if the loading completed without errors
     */
    @Override
    public boolean loadDataset(String sourceFile)
    {

    }


}