package it.unica.foresee.datasets;

import it.unica.foresee.utils.Pair;


/**
 * An element of the Movielens dataset.
 *
 * A movielens element is a vector of ratings, and it can equally be a vector of ratings given
 * by the same user on a set of movies or a vector of ratings on the same movies given by a set of
 * users who rated it.
 */
public class MovielensElement extends DatasetSparseVector<DoubleElement>
{
    /**
     * Empty constructor.
     */
    public MovielensElement(){}

    /**
     * {@inheritDoc}
     */
    public MovielensElement(int vectorSize)
    {
        super(vectorSize);
    }

    /**
     * Constructs an element from a dataset sparse vector.
     */
    public MovielensElement(DatasetSparseVector<DoubleElement> dsv)
    {
        this.setVectorSize(dsv.getVectorSize());
        this.setId(dsv.getId());
        this.putAll(dsv);
    }


    /**
     * Initializes the element from an Integer.
     * @param key the index of the element
     * @param value the value of the element at the index position
     */
    public MovielensElement(Integer key, Integer value)
    {
        this.put(key, new DoubleElement(value.doubleValue()));
    }

    /**
     * Initializes the element from a Double.
     * @param key the index of the element
     * @param value the value of the element at the index position
     */
    public MovielensElement(Integer key, Double value)
    {
        this.put(key, new DoubleElement(value));
    }

    /**
     * Initializes the element from an IntegerElement.
     * @param key the index of the element
     * @param value the value of the element at the index position
     */
    public MovielensElement(Integer key, IntegerElement value) {this.put(key, value.getElement().doubleValue());}

    /**
     * Initializes the element from a DoubleElement.
     * @param key the index of the element
     * @param value the value of the element at the index position
     */
    public MovielensElement(Integer key, DoubleElement value) {this.put(key,value);}

    /**
     * Initializes the element from a DatasetElement of type Double.
     * @param key the index of the element
     * @param value the value of the element at the index position
     */
    public MovielensElement(Integer key, it.unica.foresee.datasets.DatasetElement<Double> value)
    {
        this.put(key, value.getElement());
    }

    /**
     * Initializes the element.
     * @param p a Pair of q key and a value
     */
    public MovielensElement(Pair<Integer, Double> p)
    {
        this.put(p.getFst(), new DoubleElement(p.getSnd()));
    }

    /**
     * Additional put method to support direct Double insertion.
     * @param key the index
     * @param value the value at the index
     * @return the value as a DoubleElement
     */
    public DoubleElement put(Integer key, Double value)
    {
        return this.put(key, new DoubleElement(value));
    }

    /**
     * {@inheritDoc}
     */
    public MovielensElement deepClone()
    {
        MovielensElement clone = new MovielensElement();

        for(int key : this.keySet())
        {
            clone.put(key, (DoubleElement) this.get(key).deepClone());
        }
        clone.setId(this.getId());
        clone.setVectorSize(this.getVectorSize());
        return clone;
    }

}
