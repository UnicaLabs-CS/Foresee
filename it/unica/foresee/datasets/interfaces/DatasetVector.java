package it.unica.foresee.datasets.interfaces;

import java.util.List;

/**
 * Vector of data.
 */
public interface DatasetVector<T extends DatasetElement> extends Iterable<T>, Dataset<T>
{

}
