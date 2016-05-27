package it.unica.foresee.datasets;

import it.unica.foresee.datasets.interfaces.DatasetElement;
import it.unica.foresee.datasets.interfaces.DeepClonable;
import it.unica.foresee.datasets.interfaces.DoubleConvertible;
import it.unica.foresee.datasets.interfaces.Identifiable;
import org.apache.commons.math3.ml.clustering.Clusterable;

import java.util.*;

/**
 * An efficient data structure for sparse vectors.
 */
public class DatasetSparseVector<T extends DatasetElement<?> & DeepClonable> extends TreeMap<Integer, T> implements it.unica.foresee.datasets.interfaces.DatasetVector<T>, it.unica.foresee.datasets.interfaces.ClonableElement<DatasetSparseVector<T>>, Clusterable, Identifiable
{
    /**
     * The id of the element.
     */
    private int id;

    /**
     * Max size of the vector.
     */
    private int vectorSize;

    /**
     * Empty constructor.
     */
    public DatasetSparseVector(){}

    /**
     * Vector size.
     * @param vectorSize size of the element vector
     */
    public DatasetSparseVector(int vectorSize)
    {
        this.setVectorSize(vectorSize);
    }

    /* Getter */

    /**
     * {@inheritDoc}
     */
    @Override
    public T getDatasetElement(int key)
    {
        return this.get(key);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getDoubleValue() {
        return getMean();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DatasetSparseVector getElement() {
        return this;
    }

    @Override
    public int getId()
    {
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DatasetSparseVector[] getKFoldPartitions(int k, int layersAmount)
    {
        /* Initialize the max and min with a reasonable value */
        double maxMeanValue = this.get(this.firstKey()).getDoubleValue();
        double minMeanValue = this.get(this.firstKey()).getDoubleValue();

        /* Fill the array with the number of occurrences. */
        for (T item : this.values())
        {
            /* Keep the max mean value for each element */
            if (item.getDoubleValue() > maxMeanValue)
            {
                maxMeanValue = item.getDoubleValue();
            }

            /* Keep the min mean value for each element */
            if (item.getDoubleValue() < minMeanValue)
            {
                minMeanValue = item.getDoubleValue();
            }
        }


        /* --- Stratification by mean value. --- */

        /* Amplitude of the range of each layer. */
        double layerRange = (maxMeanValue - minMeanValue) / (layersAmount);

        /*
         * Place the elements keys in the respective layers.
         *
         * In each loop take an element key and try to put it in a layer from the first until
         * the last is reached.
         * If the last layer is reached, add the element in it without further checks.
         */
        ArrayList<Integer>[] layers = new ArrayList[layersAmount];

        /* Initialize the Array */
        for (int i = 0; i < layers.length; i++)
        {
            /* Each layer contains only the keys referring to the element */
            layers[i] = new ArrayList<>();
        }

        /* Stratification loop */
        for (Integer key: this.keySet())
        {
            /* Reset the high range. */
            double highRange = minMeanValue + layerRange;

            for (ArrayList<Integer> layer: layers)
            {
                /* We're on the last layer, add here the element here. */
                if (layer.equals(layers[layersAmount - 1]))
                {
                    layer.add(key);
                    break; //Do not continue to check for a layer after it has been found
                }
                else if (this.get(key).getDoubleValue() < highRange)
                {
                    layer.add(key);
                    break; //Do not continue to check for a layer after it has been found
                }

                /* Update the range. */
                highRange += layerRange;
            }
        }

        /* Fill the k partitions: k folding */
        DatasetSparseVector<T>[] partitions = new DatasetSparseVector[k];
        Random randomizer = new Random();

        /* Initialize the partitions */
        for (int i = 0; i < partitions.length; i++)
        {
            partitions[i] = new DatasetSparseVector<>();
        }

        /* For each layer add random elements to each partition */
        for (ArrayList<Integer> layer : layers)
        {
            /* Remove the elements added to the partitions */
            while (layer.size() > 0)
            {
                /* Select a random element and put it in a partition */
                for (DatasetSparseVector<T> partition : partitions)
                {
                    if (layer.size() <= 0)
                    {
                        /* Stop looping when the layer is empty */
                        break;
                    }
                    else
                    {
                        int randIndex = randomizer.nextInt(layer.size());
                        Integer randomKey = layer.remove(randIndex);
                        partition.put(randomKey, this.get(randomKey));
                    }
                }
            }
        }

        return partitions;
    }

    /**
     * The mean is calculated as the mean of the elements values.
     */
    public double getMean()
    {
        // Sum all the elements values
        double sumOfValues = 0;
        for (T element : this.values())
        {
            sumOfValues += element.getDoubleValue();
        }

        // Sanity checks
        if (this.getVectorSize() == 0 && sumOfValues != 0)
        {
            throw new IllegalStateException("Cannot determine the mean of a vector of size " +
                    "equal to zero but not empty. Id: " + this.getId());
        }
        else if (sumOfValues == 0)
        {
            return 0;
        }
        else
        {
            return sumOfValues / this.getVectorSize();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] getPoint()
    {
        if (!this.isEmpty() && (getVectorSize() < this.lastKey()))
        {
            throw new IllegalStateException("The vector size set is incorrect: " +
                    "vectorSize = " + getVectorSize() + " lastKey = " + lastKey());
        }

        // The size of the array is set to the highest key value, so that it can store all the items
        double[] points = new double[getVectorSize()];

        // Returns an empty array if no value is set
        if (this.isEmpty())
        {
            return points;
        }

        // Associate the indexes with the corresponding values
        for (int k : this.keySet())
        {
            points[k-1] = this.getDatasetElement(k).getDoubleValue();
        }

        return points;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getVectorSize() {
        return vectorSize;
    }

    /**
     * Checks if the mean value has been set by the user.
     *
     * @return true if the mean value has been set by the user
     */
    @Deprecated
    public boolean isMeanValueSetByUser() {
        return false;
    }

    /**
     * Checks if the element can be casted to the required type
     * to be put in the element.
     *
     * @param o the object to check
     * @return if the element can be safely put
     */
    public boolean isPuttable(Object o)
    {
        if (o instanceof DeepClonable && o instanceof DatasetElement)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /* Setter */


    /**
     * Special put method with automatic cast of the value.
     *
     * {@link #put(Object, Object)}
     */
    public T put(Integer key, Object value)
    {
        Class<?> cls = null;

        if (!isPuttable(value))
        {
            throw new IllegalStateException("The element " + value + " is not of the required type.");
        }

        try
        {
            cls = this.getClass().getMethod("get", Object.class).getReturnType();
        }
        catch (NoSuchMethodException e)
        {
            throw new IllegalStateException(e);
        }

        return super.put(key, (T) cls.cast(value));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDatasetElement(int index, T e) {
        this.put(index, e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setElement(DatasetSparseVector<T> e) {
        this.clear();
        this.putAll(e);
    }

    @Override
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * This method is implemented for API compatibility but does actually nothing
     * because an automatic conversion is in place.
     * @param converter
     */
    @Override
    public void setDoubleValueConverter(DoubleConvertible<DatasetSparseVector<T>> converter) {}

    /**
     * Set the vector size for array creation
     * @param vectorSize the size of the max vector
     */
    public void setVectorSize(int vectorSize) {
        this.vectorSize = vectorSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<T> iterator() {
        return this.values().iterator();
    }

    /**
     * {@inheritDoc}
     */
    public DatasetSparseVector<T> deepClone()
    {
        DatasetSparseVector<T> clone = new DatasetSparseVector<>();

        for(int key : this.keySet())
        {
            clone.put(key, (T) this.get(key).deepClone());
        }
        clone.setId(this.getId());
        clone.setVectorSize(this.vectorSize);
        return clone;
    }
}
