package it.unica.foresee.datasets;

import it.unica.foresee.datasets.interfaces.*;

import java.util.Map;
import java.util.SortedMap;

/**
 * Implementation of {@link it.unica.foresee.datasets.interfaces.DatasetNestedSparseVector}
 * which keeps the internal highest key updated to the highest value.
 *
 * This is a simple implementations which keeps track of the highest key value
 * among the elements inserted using put and putAll methods, but it's not able
 * to update the its value if the value of a nested element is edited without
 * putting it in again.
 */
public class DatasetNestedSparseVector<T extends DatasetSparseVector<?>> extends DatasetSparseVector<T> implements it.unica.foresee.datasets.interfaces.DatasetNestedSparseVector<T>
{
    int internalHighestKey;

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHighestNestedKey() {

        return internalHighestKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getVectorSize() {

        return getHighestNestedKey();
    }



    /**
     * {@inheritDoc}
     * This implementation keeps track of the highest key value ever inserted.
     */
    @Override
    public T put(Integer key, T value) {
        if (value.lastKey() > this.internalHighestKey)
        {
            this.internalHighestKey = value.lastKey();
        }
        return super.put(key, value);
    }

    /**
     * {@inheritDoc}
     * This implementation keeps track of the highest key value ever inserted.
     */
    @Override
    public void putAll(Map<? extends Integer, ? extends T> map) {
        if(map.size() == 0)
        {
            throw new IllegalArgumentException("The dataset cannot have a size of 0");
        }

        for (T nestedVector : map.values())
        {
            if (nestedVector.lastKey() > this.internalHighestKey)
            {
                this.internalHighestKey = nestedVector.lastKey();
            }
        }
        super.putAll(map);
    }
}
