package it.unica.foresee.datasets.interfaces;

/**
 * Generic interface to load a Dataset.
 */
public interface DatasetLoader
{
    /**
     * Get the specified dataset.
     * @return the specified dataset.
     */
    Dataset loadDataset();
}
