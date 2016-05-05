package it.unica.foresee.libraries;

import it.unica.foresee.datasets.DatasetSparseVector;
import it.unica.foresee.datasets.interfaces.DatasetElement;
import it.unica.foresee.datasets.interfaces.DatasetNestedSparseVector;
import it.unica.foresee.datasets.interfaces.DeepClonable;
import it.unica.foresee.utils.Logger;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Element container whose elements can be clustered.
 */
public class ClusterableElement<T extends DatasetSparseVector<? extends DatasetElement> & DeepClonable>
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
     * Get the element at the given key and/or with coordinates equal to the given point.
     *
     * Retrieves the element corresponding to the given hashcode or, if multiple points
     * have the same hashcode, the element corresponding to the given hashcode
     * and which is equal to the given point.
     *
     * @param key the hashcode of the point
     * @param point the point of the element
     * @throws IllegalStateException if no element corresponding to the given point is
     *         found at the given hashcode key location
     * @return the element corresponding to the given hashcode or, if multiple points
     * have the same hashcode, the element corresponding to the given hashcode
     * and which is equal to the given point
     */
    private T getHashElement(int key, double[] point)
    {
        // Null is returned if no element with the given key is found
        T value = null;
        ArrayList<T> valueArray = this.hashToElements.get(key);

        // If the hash id unique we have only an element per hashcode
        if (valueArray.size() == 1)
        {
            value = valueArray.get(0);
        }
        // If there has been an hash collision check for equality
        else
        {
            for (T element : valueArray)
            {
                // Using Arrays utility guarantees the equality of the
                // contained elements
                if (Arrays.equals(element.getPoint(), point))
                {
                    value = element;
                    break;
                }
            }

            // The key exists but it's not equal to the given point
            if (value == null)
            {
                throw new IllegalStateException("No element corresponding to the given point has been found " +
                        "at the given hashcode key location");
            }
        }

        return value;
    }

    /**
     * Clusters the given dataset.
     *
     * @param dataset
     * @return
     */
    public List<List<T>> cluster(DatasetNestedSparseVector<T, ?> dataset, int clustersAmount)
    {
        if (dataset == null)
        {
            throw new IllegalArgumentException("The dataset cannot be null.");
        }

        if (dataset.keySet().size() == 0)
        {
            throw new IllegalArgumentException("The dataset cannot have a size of 0");
        }
        // Create a variable to hold a list of resulting centroids
        List<CentroidCluster<T>> clusterResults = null;

        // Needed to retrieve the last element
        int pointDimensions = dataset.getHighestNestedKey();

        // Transform the dataset in an ArrayList
        ArrayList<T> datasetArray = new ArrayList<>();

        /* Loop throughout the dataset to:
         * - add each element to the dataset array
         * - set each element's vector size
         * - get each element's hashcode for the hashToElements
        */
        for (Integer key : dataset.keySet())
        {
            // This loop adds empty vectors in missing positions
            // and it's required to keep the indexes correct.
            while (datasetArray.size() <= key)
            {
                datasetArray.add((T) new DatasetSparseVector<>(pointDimensions));
            }

            // Add the element to the array and to the hashmap
            T element = dataset.get(key);
            // Set the point dimension (array size) of the element
            element.setVectorSize(pointDimensions);

            datasetArray.add(key, element);
        }

        //Logger.debug("Array size = " + datasetArray.size(), this.getClass());

        // Create a clusterer to perform the clustering
        KMeansPlusPlusClusterer<T> clusterer = new KMeansPlusPlusClusterer<>(clustersAmount);
        clusterResults = clusterer.cluster(datasetArray);

        // Obtain the results as a List of clusters of users
        List<List<T>> usersClustersList = new ArrayList<>();
        for (CentroidCluster<T> cluster : clusterResults)
        {
            usersClustersList.add(cluster.getPoints());
        }

        return usersClustersList;
    }
}
