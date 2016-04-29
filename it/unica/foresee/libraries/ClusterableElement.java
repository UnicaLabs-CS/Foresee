package it.unica.foresee.libraries;

import it.unica.foresee.datasets.DatasetSparseVector;
import it.unica.foresee.datasets.interfaces.DatasetNestedSparseVector;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Element container whose elements can be clustered.
 */
public class ClusterableElement<T extends DatasetSparseVector<?>>
{

    /**
     * Hold a map to retrieve the elements by their getPoint array hashcode.
     */
    private HashMap<Integer, ArrayList<T>> hashToElements = new HashMap<>();

    /**
     * Empty constructor.
     */
    public ClusterableElement(){}

    /**
     * Put an element in the table without overwriting the preceding one.
     * @param key the hashcode of the array of getPoint of value
     * @param value the value to store
     */
    private void putHashElement(int key, T value)
    {
        ArrayList<T> arrayValue = hashToElements.get(key);

        // Initialize the element if it doesn't exists
        if (arrayValue == null)
        {
            arrayValue = new ArrayList<>();
            hashToElements.put(key, arrayValue);
        }
        hashToElements.get(key).add(value);
    }

    /**
     * Put an element in the table without overwriting the preceding one.
     * @param array the array of getPoint of value
     * @param value the value to store
     */
    private void putHashElement(double[] array, T value)
    {
        putHashElement(Arrays.hashCode(array), value);
    }

    /**
     * Put an element in the table without overwriting the preceding one.
     * @param value the value to store
     */
    private void putHashElement(T value)
    {
        putHashElement(Arrays.hashCode(value.getPoint()), value);
    }

    /**
     * Clusters the given dataset.
     *
     * @param dataset
     * @return
     */
    public DatasetSparseVector<T>[] cluster(DatasetNestedSparseVector<T> dataset, int centroidsAmount)
    {

        // Create a list of centroids
        List<CentroidCluster<DatasetSparseVector<?>>> clusterResults = null;

        // Needed because the position 0 is skipped
        int vectorSize = dataset.getHighestNestedKey() + 1;

        // Transform the partition in an ArrayList
        ArrayList<DatasetSparseVector<?>> partitionElementsArray = new ArrayList<>();

        /* Loop thorough every element of the dataset to:
         * - add it to the partition array
         * - set its vector size
         * - get its hashcode for the hashToElements
        */
        for (Integer key : dataset.keySet())
        {
            // This loop adds empty vectors in missing positions
            // and it's required to keep the indexes correct.
            while (partitionElementsArray.size() <= key){partitionElementsArray.add(new DatasetSparseVector<>(vectorSize));}

            // Set the vector size of the element
            dataset.get(key).setVectorSize(vectorSize);

            // Add the element to the array
            partitionElementsArray.add(key, dataset.get(key));
        }

        KMeansPlusPlusClusterer<DatasetSparseVector<?>> clusterer = new KMeansPlusPlusClusterer<>(centroidsAmount);
        clusterResults = clusterer.cluster(partitionElementsArray);

        /*
        System.out.println("\n\npartition " + i);
        for (int j = 0; j < clusterResults.size(); j++)
        {
            System.out.println("cluster " + j);
            System.out.println(clusterResults.get(j).getPoints());
        }
        */

        return null;
    }


}
