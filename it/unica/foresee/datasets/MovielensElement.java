package it.unica.foresee.datasets;

import it.unica.foresee.datasets.interfaces.DatasetElement;
import it.unica.foresee.utils.Pair;
import org.apache.commons.math3.ml.clustering.Clusterable;

import java.util.Collection;

/**
 * An element of the Movielens dataset.
 *
 * A movielens element is a vector of ratings, and it can equally be a vector of ratings given
 * by the same user on a set of movies or a vector of ratings on the same movies given by a set of
 * users who rated it.
 */
public class MovielensElement extends DatasetSparseVector<IntegerElement> implements Clusterable
{
    /**
     * Empty constructor.
     */
    public MovielensElement(){}

    /**
     * Vector size.
     * @param vectorSize size of the element vector
     */
    public MovielensElement(int vectorSize)
    {
        super.setVectorSize(vectorSize);
    }



    /**
     * Initializes the element from an Integer.
     * @param key the index of the element
     * @param value the value of the element at the index position
     */
    public MovielensElement(Integer key, Integer value)
    {
        this.put(key, new IntegerElement(value));
    }

    /**
     * Initializes the element from an IntegerElement.
     * @param key the index of the element
     * @param value the value of the element at the index position
     */
    public MovielensElement(Integer key, IntegerElement value) {this.put(key,value);}

    /**
     * Initializes the element from a DatasetElement of type Integer.
     * @param key the index of the element
     * @param value the value of the element at the index position
     */
    public MovielensElement(Integer key, it.unica.foresee.datasets.DatasetElement<Integer> value)
    {
        this.put(key, new IntegerElement(value));
    }

    /**
     * Initializes the element.
     * @param p a Pair of q key and a value
     */
    public MovielensElement(Pair<Integer, Integer> p) {this.put(p.getFst(), new IntegerElement(p.getSnd()));}

    /**
     * Additional put method to support Integers.
     * @param key the index
     * @param value the value at the index
     * @return the value as an IntegerElement
     */
    public IntegerElement put(Integer key, Integer value)
    {
        IntegerElement e = new IntegerElement(value);
        this.put(key, e);
        return e;
    }

    /**
     * {@inheritDoc}
     */
    public double[] getPoint()
    {
        // The size of the array is set to the highest key value, so that it can store all the items
        double[] points = new double[super.getVectorSize()];

        if (this.isEmpty())
        {
            return points;
        }

        // Associate the indexes with the corresponding values
        for (int k : this.keySet())
        {
            points[k] = this.getDatasetElement(k).getElement().doubleValue();
        }

        return points;
    }

}
