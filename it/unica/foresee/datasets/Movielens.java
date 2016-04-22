package it.unica.foresee.datasets;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.TreeSet;

/**
 * Provides methods to use the freely available movielens dataset.
 */
public class Movielens extends DatasetSparseVector<MovielensElement>
{
    /**
     * Amount of movie rates.
     */
    private int moviesAmount;

    /**
     * Amount of user rates.
     */
    private int usersAmount;

    /**
     * Highest user ID
     */
    private int maxUserID;

    /**
     * Highest movie ID
     */
    private int maxMovieID;

    /**
     * Max rate.
     */
    public final int MAX_RATE = 5;

    /**
     * Min rate.
     */
    public final int MIN_RATE = 1;

    /**
     * List of users.
     */
    protected TreeSet<Integer> usersSet;

    /**
     * List of movies.
     */
    protected TreeSet<Integer> moviesSet;

    /* Getter */

    /**
     * Get a triple value in one single shot.
     * @param userID the user ID
     * @param movieID the movie ID
     * @return the rating given by the user on that movie
     */
    public IntegerElement get(Integer userID, Integer movieID)
    {
        return this.get(userID).get(movieID);
    }

    /**
     * Get a triple value in one single shot.
     * @param userID the user ID
     * @param movieID the movie ID
     * @return the rating given by the user on that movie
     */
    public Integer getElement(Integer userID, Integer movieID)
    {
        MovielensElement item = this.get(userID);
        if(item == null){return null;}
        IntegerElement rating = item.get(movieID);
        if(item == null){return null;}

        return rating.getElement();
    }

    /**
     * Similar to {@link DatasetSparseVector#getKFoldPartitions(int, int)} but
     * obtains partitions where there are representative elements for each user.
     * @param k
     * @return
     */
    public DatasetSparseVector[] getKFoldPartitions(int k)
    {
        /* Initialize the max and min with a reasonable value */
        double maxMeanValue = this.get(this.firstKey()).getValueForMean();
        double minMeanValue = this.get(this.firstKey()).getValueForMean();

        /* Fill the array with the number of occurrences. */
        for (MovielensElement item : this.values())
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


        /* --- Stratification by average of elements by each user. --- */

        /* Amplitude of the range of each layer. */
        int layersAmount = this.usersAmount;
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
        return maxMovieID;
    }

    public int getMaxUserID() {
        return maxUserID;
    }

    public int getMoviesAmount() {
        return moviesAmount;
    }

    public int getUsersAmount() {
        return usersAmount;
    }

    public TreeSet<Integer> getMoviesSet() {
        return moviesSet;
    }

    public TreeSet<Integer> getUsersSet() {
        return usersSet;
    }

    /* Setter */

    /**
     * Add a triple in one single shot.
     * @param userID the user ID
     * @param movieID the movie ID
     * @param rating the rating given by the user on that movie
     */
    public void put(Integer userID, Integer movieID, Integer rating)
    {
        MovielensElement el = this.get(userID);

        if (el == null)
        {
            el = new MovielensElement();
        }

        el.put(movieID, rating);
        this.put(userID, el);
    }

    /**
     * Special implementation to allow merging already present users.
     *
     * If you put an already present user, it's movies get merged.
     * {@inheritDoc}
     */
    @Override
    public MovielensElement put(Integer integer, MovielensElement t)
    {
        if (this.containsKey(integer) && t != null)
        {
            this.get(integer).putAll(t);
            return this.get(integer);
        }
        else
        {
            return super.put(integer, t);
        }
    }

    /**
     * Special implementation to allow merging already present users.
     * {@link java.util.TreeMap#putAll(Map)}
     */
    public void putAll(Movielens m)
    {
        // For each key of m, add it in position key merging its values
        for(Integer key : m.keySet())
        {
            this.put(key, m.get(key));
        }
    }

    public void setMaxMovieID(int maxMovieID) {
        this.maxMovieID = maxMovieID;
    }

    public void setMaxUserID(int maxUserID) {
        this.maxUserID = maxUserID;
    }

    public void setMoviesAmount(int moviesAmount) {
        this.moviesAmount = moviesAmount;
    }

    public void setMoviesSet(TreeSet<Integer> moviesSet) {
        this.moviesSet = moviesSet;
    }

    public void setUsersAmount(int usersAmount) {
        this.usersAmount = usersAmount;
    }

    public void setUsersSet(TreeSet<Integer> usersSet) {
        this.usersSet = usersSet;
    }

    /* Modifiers */
    public void incrementMoviesAmount()
    {
        this.moviesAmount++;
    }

    public void incrementUsersAmount()
    {
        this.usersAmount++;
    }
}
