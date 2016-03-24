package it.unica.foresee.datasets;

import it.unica.foresee.datasets.interfaces.*;

import java.awt.*;
import java.util.*;

/**
 * An efficient data structure for sparse vectors.
 */
public class DatasetSparseVector<T extends DatasetElement> extends TreeMap<Integer, T> implements it.unica.foresee.datasets.interfaces.DatasetVector<T>, it.unica.foresee.datasets.interfaces.DatasetElement<DatasetSparseVector>
{
    /**
     * Mean of the elements means.
     */
    private double mean;

    /**
     * Flag to check if the mean value is calculated or user selected.
     */
    private boolean meanValueSetByUser = false;


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
    public DatasetSparseVector getElement() {
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DatasetSparseVector[] getKFoldPartitions(int k, int layersAmount)
    {
        /* Initialize the max and min with a reasonable value */
        double maxMeanValue = this.get(0).getValueForMean();
        double minMeanValue = this.get(0).getValueForMean();

        /* Fill the array with the number of occurrences. */
        for (T item : this.values())
        {
            /* Keep the max mean value for each element */
            if (item.getValueForMean() > maxMeanValue)
            {
                maxMeanValue = item.getValueForMean();
            }

            /* Keep the min mean value for each element */
            if (item.getValueForMean() < minMeanValue)
            {
                minMeanValue = item.getValueForMean();
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
        ArrayList[] layers = new ArrayList[layersAmount];

        /* Initialize the Array */
        for (int i = 0; i < layers.length; i++)
        {
            /* Each layer contains only the keys referring to the element */
            layers[i] = new ArrayList<Integer>();
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
                else if (this.get(key).getValueForMean() < highRange)
                {
                    layer.add(key);
                    break; //Do not continue to check for a layer after it has been found
                }

                /* Update the range. */
                highRange += layerRange;
            }
        }

        /* Fill the k partitions: k folding */
        DatasetSparseVector[] partitions = new DatasetSparseVector[k];
        Random randomizer = new Random();

        /* Initialize the partitions */
        for (int i = 0; i < partitions.length; i++)
        {
            partitions[i] = new DatasetSparseVector<T>();
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
     * Checks if the mean value has been set by the user.
     *
     * @return true if the mean value has been set by the user
     */
    public boolean isMeanValueSetByUser() {
        return meanValueSetByUser;
    }

    /**
     * {@inheritDoc}
     *
     * The mean is calculated internally as the mean of the elements means. If a different value is set
     * by the user, it will be used instead.
     */
    @Override
    public double getValueForMean()
    {
        if (this.meanValueSetByUser)
            return this.mean;
        else
        {
            double sumOfMeans = 0;
            for (T element : this.values())
            {
                sumOfMeans += element.getValueForMean();
            }
            return sumOfMeans / this.size();
        }
    }

    /* Setter */

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
    public void setElement(DatasetSparseVector e) {
        this.clear();
        this.putAll(e);
        this.meanValueSetByUser = e.isMeanValueSetByUser();
        if (this.meanValueSetByUser)
        {
            this.mean = e.getValueForMean();
        }
    }

    /**
     * {@inheritDoc}
     *
     * This value overrides the internally calculated mean.
     */
    @Override
    public void setValueForMean(double v) {
        this.meanValueSetByUser = true;
        this.mean = v;
    }

    /**
     * Unsets the mean set by the user. Successive calls of
     * {@link #getValueForMean()} will use the mean of the elements means.
     */
    public void unsetMeanValueSetByUser()
    {
        this.meanValueSetByUser = false;
        this.mean = 0;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<T> iterator() {
        return this.values().iterator();
    }
}
