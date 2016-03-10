package it.unica.foresee.datasets.interfaces;

/**
 * Represents a generic dataset.
 */
public interface Dataset extends Iterable<Dataset>
{
    /**
     * Get the vector at the specified index.
     * @param index the index of the vector
     * @return the requested vector
     */
    DataVector getDataVector(int index);
}
