package it.unica.foresee.datasets.interfaces;

import java.io.FileNotFoundException;

/**
 * Generic interface to load a Dataset.
 */
public interface DatasetLoader<T extends Dataset>
{
    /**
     * Get the specified dataset.
     * @return the specified dataset.
     */
    T loadDataset() throws FileNotFoundException;
}
