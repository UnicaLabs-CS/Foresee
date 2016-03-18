package it.unica.foresee.datasets;

import it.unica.foresee.utils.Pair;

import java.util.*;

/**
 * Provides methods to use the freely available movielens dataset.
 *
 * @author Fabio Colella
 */
public class Movielens extends DatasetVector<MovielensElement> {

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

    /**
     * Empty constructor.
     */
    public Movielens() {}

    /* Getter */

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

    /**
     * Gets k-fold partitioning based on the rates per movie.
     *
     * This method is an implementation of the stratified k-fold cross validation.
     *
     * @param k the number of partitons to obtain from the dataset
     *          {@literal (Precondition: k >= 1) }
     *          A k value equal to 1 means that no partitioning is being done.
     *          A good value for k is 10, as determined by various studies.
     *
     * @param layersAmount the number of layers for the stratified k-fold algorithm
     *                     {@literal (Precondition: layersAmount >= 1) }
     *                     A layerAmount equal to 1 means you're doing a non
     *                     stratified k-fold.
     * @return an array of partitions
     */
    public DatasetVector<DatasetElement<Pair<Integer,Integer>>>[] getKFoldMoviePartitions(int k, int layersAmount)
    {
        /* Get the movies with highest and lowest rate amount. */
        int maxMovieRatesAmount = 0;
        int minMovieRatesAmount = 0;

        TreeMap<Integer, Integer> ratesPerMovie = new TreeMap<>();

        /* Fill the array with the number of rates and update the max amount of rates per movie. */
        for (MovielensElement item : this)
        {
            /* Increment the value if already present */
            if (ratesPerMovie.containsKey(item.getElement().getSnd()))
            {
                ratesPerMovie.put(item.getElement().getSnd(), ratesPerMovie.get(item.getElement().getSnd()) + 1);
            }
            else /* Add the value otherwise */
            {
                ratesPerMovie.put(item.getElement().getSnd(), 1);
            }

            /* Keep the maximum amount of ratings for a single movie updated */
            if (ratesPerMovie.get(item.getElement().getSnd()) > maxMovieRatesAmount)
            {
                maxMovieRatesAmount = ratesPerMovie.get(item.getElement().getSnd());
            }
        }

        /* --- Stratification by movie rate amount. --- */

        /* Amplitude of the range of each layer. */
        double layerRange = (maxMovieRatesAmount - minMovieRatesAmount) / (layersAmount);

        /*
         * Place the movies in the respective layers.
         *
         * In each loop take a movie and try to put it in a layer from the first until the last is reached.
         * If the last layer is reached, add the element in it without further checks.
         */
        DatasetVector<DatasetElement>[] layers = new DatasetVector[layersAmount];

        /* Initialize the array elements */
        for (int i = 0; i < layers.length; i++)
        {
            layers[i] = new DatasetVector<>();
        }

        /* Stratification loop */
        for (Map.Entry<Integer, Integer> movie : ratesPerMovie.entrySet())
        {
            /* Create the element to add to the dataset */
            Pair<Integer, Integer> p = new Pair<>(movie.getKey(), movie.getValue());
            DatasetElement d = new DatasetElement(p, movie.getValue());

            /* Reset the high range. */
            double highRange = minMovieRatesAmount + layerRange;

            /* Movies with no rating should not appear. */
            if (movie.getValue() <= 0)
            {
                throw new IllegalStateException("Only rated movies should be considered.");
            }

            for (DatasetVector<DatasetElement> layer: layers)
            {
                /* We're on the last layer, add here the movie here. */
                if (layer.equals(layers[layersAmount - 1]))
                {
                    layer.add(d);
                    break; //Do not continue to check for a layer after it has been found
                }
                else if (movie.getValue() < highRange)
                {
                    layer.add(d);
                    break; //Do not continue to check for a layer after it has been found
                }
                /* Update the range. */
                highRange += layerRange;
            }
        }

        /* Fill the k partitions: k folding */
        DatasetVector<DatasetElement<Pair<Integer, Integer>>>[] partitions = new DatasetVector[k];
        Random randomizer = new Random();

        /* Initialize the partitions */
        for (int i = 0; i < partitions.length; i++)
        {
            partitions[i] = new DatasetVector<>();
        }

        /* For each layer add random elements to each partition */
        for (DatasetVector<DatasetElement> layer : layers)
        {
            /* Remove the elements added to the partitions */
            while (layer.size() > 0)
            {
                /* Select a random element and put it in a partition */
                for (DatasetVector<DatasetElement<Pair<Integer, Integer>>> partition : partitions)
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

        return partitions;
    }

}

