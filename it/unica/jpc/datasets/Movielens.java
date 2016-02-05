package it.unica.jpc.datasets;

import java.util.ArrayList;
import java.io.File;

/**
 * Provides methods to use the freely available movielens dataset.
 *
 * @author Fabio Colella
 */
public class Movielens extends Dataset
{

    /**
     * Empty constructor.
     */
    public Movielens()
    {
        super();
    }

    /**
     * Constructs a movielens from the specified file.
     *
     * {@inheritDoc}
     */
    public Movielens(String sourceFile)
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
