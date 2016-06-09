package it.unica.foresee.datasets;

import it.unica.foresee.datasets.interfaces.DeepClonable;

import java.util.*;


/**
 * Provides methods to use the freely available movielens dataset.
 */
public class Movielens extends DatasetNestedSparseVector<MovielensElement>
{
    /**
     * Max rate.
     */
    public final int MAX_RATE = 5;

    /**
     * Min rate.
     */
    public final int MIN_RATE = 1;

    /* Getter */

    /**
     * Get a rating value by a user on a movie.
     * @param userID the user ID
     * @param movieID the movie ID
     * @return the rating given by the user on that movie
     */
    public DoubleElement get(Integer userID, Integer movieID)
    {
        return this.get(userID).get(movieID);
    }

    /**
     * Get a rating value by a user on a movie.
     * @param userID the user ID
     * @param movieID the movie ID
     * @return the rating given by the user on that movie
     */
    public Double getElement(Integer userID, Integer movieID)
    {
        MovielensElement item = this.get(userID);
        if(item == null){return null;}
        DoubleElement rating = item.get(movieID);
        if(rating == null){return null;}

        return rating.getElement();
    }

    /**
     * Similar to {@link DatasetSparseVector#getKFoldPartitions(int, int)} but
     * obtains partitions where there are elements which are representative of each user.
     * @param k the amount of partitions, the k value of the k-fold cross validation
     * @return an array of partitions
     */
    public Movielens[] getKFoldPartitions(int k)
    {
        /* Initialize the max and min with a reasonable value */
        double maxMeanValue = this.get(this.firstKey()).getDoubleValue();
        double minMeanValue = this.get(this.firstKey()).getDoubleValue();

        /* Fill the array with the number of occurrences. */
        for (MovielensElement item : this.values())
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


        /* --- Stratification by average of elements per each user. --- */

        /* Amplitude of the range of each layer. */
        int layersAmount = this.size();
        double layerRange = (maxMeanValue - minMeanValue) / (layersAmount);

        /* Fill the k partitions: k folding */
        Movielens[] partitions = new Movielens[k];
        Random randomizer = new Random();

        /* Initialize the partitions */
        for (int i = 0; i < partitions.length; i++)
        {
            partitions[i] = new Movielens();
        }

        int randIndex;
        Integer randomKey;
        ArrayList<Integer> userMoviesKeys;

        /* For each user add random elements to each partition */
        for (Integer userID : this.keySet())
        {
            userMoviesKeys = new ArrayList<>(this.get(userID).keySet());

            /* Remove the elements added to the partitions */
            while (userMoviesKeys.size() > 0)
            {
                /* Select a random element and put it in a partition */
                for (Movielens partition : partitions)
                {
                    if (userMoviesKeys.size() <= 0)
                    {
                        /* Stop looping when the layer is empty */
                        break;
                    }
                    else
                    {
                        randIndex = randomizer.nextInt(userMoviesKeys.size());
                        randomKey = userMoviesKeys.remove(randIndex);
                        partition.put(userID, randomKey, this.get(userID, randomKey).getElement());
                    }
                }
            }
        }

        return partitions;
    }

    public int getMaxMovieID() {
        return this.getMoviesSet().last();
    }

    public int getMaxUserID() {
        return this.lastKey();
    }

    public int getMoviesAmount()
    {
        return this.getMoviesSet().size();
    }

    public SortedSet<Integer> getMoviesSet() {
        return this.getInternalKeySet();
    }

    public int getUsersAmount() {
        return this.size();
    }

    public SortedSet<Integer> getUsersSet() {
        TreeSet<Integer> usersSet = new TreeSet<>();
        usersSet.addAll(this.keySet());
        return usersSet;
    }

    /* Setter */

    /**
     * Add a triple in one single shot.
     * @param userID the user ID
     * @param movieID the movie ID
     * @param rating the rating given by the user on that movie
     */
    public void put(Integer userID, Integer movieID, Double rating)
    {
        MovielensElement el = this.get(userID);

        if (el == null)
        {
            el = new MovielensElement();
            el.setId(userID);
        }

        el.put(movieID, rating);
        this.put(userID, el);
    }

    @Override
    public Movielens deepClone() {
        Movielens clone = new Movielens();
        clone.putAll(super.deepClone());
        return clone;
    }

}
