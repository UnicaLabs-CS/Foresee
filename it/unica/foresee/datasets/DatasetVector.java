package it.unica.foresee.datasets;

import it.unica.foresee.datasets.interfaces.DatasetElement;

import java.util.ArrayList;
import java.util.Random;

/**
 * Implementation of {@link it.unica.foresee.datasets.interfaces.DatasetVector}
 */
@Deprecated
public class DatasetVector<T extends DatasetElement> extends ArrayList<T> implements it.unica.foresee.datasets.interfaces.DatasetVector<T>
{
    private DatasetVector[] kFoldPartitions;

    /**
     * Empty constructor.
     */
    public DatasetVector()
    {
        super();
    }

    @Override
    public int getVectorSize()
    {
        return this.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T getDatasetElement(int index)
    {
        return super.get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDatasetElement(int index, T e)
    {
        super.set(index, e);
    }


    /**
     * {@inheritDoc}
     *
     * If the dataset has been changed after calling this function,
     * call {@link DatasetVector#updateKFoldPartitions(int, int)} )} before
     * getting the partitions.
     */
    @Override
    public DatasetVector[] getKFoldPartitions(int k, int layersAmount)
    {
        if (this.kFoldPartitions == null)
        {
            updateKFoldPartitions(k, layersAmount);
        }

        return this.kFoldPartitions;
    }

    /**
     * Updates the k-fold partitions calculating them again.
     */
    public void updateKFoldPartitions(int k, int layersAmount)
    {
        /* Initialize the max and min with a reasonable value */
        double maxMeanValue = this.get(0).getDoubleValue();
        double minMeanValue = this.get(0).getDoubleValue();

        /* Fill the array with the number of occurrences. */
        for (DatasetElement item : this)
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
         * Place the elements in the respective layers.
         *
         * In each loop take an element and try to put it in a layer from the first until the last is reached.
         * If the last layer is reached, add the element in it without further checks.
         */
        DatasetVector[] layers = new DatasetVector[layersAmount];

        /* Initialize the ArrayList */
        for (int i = 0; i < layers.length; i++)
        {
            layers[i] = new DatasetVector();
        }

        /* Stratification loop */
        for (DatasetElement element: this)
        {
            /* Reset the high range. */
            double highRange = minMeanValue + layerRange;

            for (DatasetVector layer: layers)
            {
                /* We're on the last layer, add here the element here. */
                if (layer.equals(layers[layersAmount - 1]))
                {
                    layer.add(element);
                    break; //Do not continue to check for a layer after it has been found
                }
                else if (element.getDoubleValue() < highRange)
                {
                    layer.add(element);
                    break; //Do not continue to check for a layer after it has been found
                }

                /* Update the range. */
                highRange += layerRange;
            }
        }

        /* Fill the k partitions: k folding */
        DatasetVector[] partitions = new DatasetVector[k];
        Random randomizer = new Random();

        /* Initialize the partitions */
        for (int i = 0; i < partitions.length; i++)
        {
            partitions[i] = new DatasetVector();
        }

        /* For each layer add random elements to each partition */
        for (DatasetVector layer : layers)
        {
            /* Remove the elements added to the partitions */
            while (layer.size() > 0)
            {
                /* Select a random element and put it in a partition */
                for (DatasetVector partition : partitions)
                {
                    if (layer.size() <= 0)
                    {
                        /* Stop looping when the layer is empty */
                        break;
                    }
                    else
                    {
                        int randIndex = randomizer.nextInt(layer.size());
                        partition.add(layer.remove(randIndex));
                    }
                }
            }
        }

        this.kFoldPartitions = partitions;
    }
}
