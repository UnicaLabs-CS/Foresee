package it.unica.foresee.datasets;

import it.unica.foresee.datasets.interfaces.*;
import it.unica.foresee.datasets.interfaces.DatasetElement;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Implementation of {@link it.unica.foresee.datasets.interfaces.DatasetNestedSparseVector}
 * which keeps the internal highest key updated to the highest value.
 *
 * This is a simple implementations which keeps track of the highest key value
 * among the elements inserted using put and putAll methods, but it's not able
 * to update the its value if the value of a nested element is edited without
 * putting it in again.
 */
/*
public class DatasetNestedSparseVector<T extends DatasetSparseVector<K>, K extends DatasetElement<?> & DeepClonable<?>> extends DatasetSparseVector<T> implements it.unica.foresee.datasets.interfaces.DatasetNestedSparseVector<T>, DeepClonable<DatasetSparseVector<T>>
*/
public class DatasetNestedSparseVector<T extends DatasetSparseVector<? extends DatasetElement<?>>> extends DatasetSparseVector<T> implements it.unica.foresee.datasets.interfaces.DatasetNestedSparseVector<T>, DeepClonable<DatasetSparseVector<T>>
{
    /**
     * Stores the size to use when converting the inner elements
     * to arrays.
     */
    private int internalVectorSize;

    /**
     * Returns the a set containing all the keys of the nested elements.
     *
     * @return a set containing all the keys of the nested elements.
     */
    public SortedSet<Integer> getInternalKeySet() {
        TreeSet<Integer> internalKeySet = new TreeSet<>();
        for (Integer key : this.keySet())
        {
            internalKeySet.addAll(this.get(key).keySet());
        }
        return internalKeySet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getInternalVectorSize()
    {
        return internalVectorSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHighestNestedKey()
    {
        return getInternalKeySet().last();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInternalVectorSize(int vectorSize) {
        super.setVectorSize(vectorSize);
    }

    /**
     * Insert an element, if it already exists, merge its elements.
     *
     * Special implementation to allow merging already present users.
     *
     * If you put an already present user, it's movies get merged.
     *
     * Note that if a movie is already present for a specific user, it will be overwritten.
     * {@inheritDoc}
     */
    @Override
    public T put(Integer key, T element)
    {
        if (element != null && this.containsKey(key))
        {
            // Merge the additional elements
            T oldElement = this.get(key);
            for (Integer subKey : element.keySet())
            {
                oldElement.put(subKey, element.get(subKey));
            }
            return oldElement;
        }
        else
        {
            // Call the non overridden method
            return super.put(key, element);
        }
    }

    /**
     * Insert all the given elements, if an element exists merges its elements.
     *
     * Special implementation to allow merging already present users instead of overwriting.
     * {@link java.util.TreeMap#putAll(Map)}
     */
    @Override
    public void putAll(Map<? extends Integer, ? extends T> m)
    {
        // For each key of m, add it in position key merging its values
        for(Integer key : m.keySet())
        {
            this.put(key, m.get(key));
        }
    }

    @Override
    public DatasetNestedSparseVector<T> deepClone() {
        DatasetNestedSparseVector<T> clone = new DatasetNestedSparseVector<>();
        clone.putAll(super.deepClone());
        return clone;
    }
}
