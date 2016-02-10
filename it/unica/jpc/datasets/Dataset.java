package it.unica.jpc.datasets;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;


/**
 * Represents a generic collection of data.
 *
 * This class implements an algorithm of stratified k-fold cross-validation.
 *
 * @author Fabio Colella
 */
public abstract class Dataset
{
    /**
     * Stores the dataset.
     *
     * int user, int movie, int rating
     */
    protected ArrayList<Triple> dataset;

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
    protected int maxUserID;

    /**
     * Highest movie ID
     */
    protected int maxMovieID;

    /**
     * Max rate.
     */
    protected final double MAX_RATE = 5.0;

    /**
     * Min rate.
     */
    protected final double MIN_RATE = 1.0;

    /**
     * List of users.
     */
    protected TreeSet<Integer> usersSet;

    /**
     * List of movies.
     */
    protected TreeSet<Integer> moviesSet;

    /**
     * Inner class to handle pairs (movie * ratesAmount)
     */
    public class MovieRatesAmountPair
    {
        private int movie;
        private int ratesAmount;

        public MovieRatesAmountPair()
        {
            this.movie = 0;
            this.ratesAmount = 0;
        }

        public MovieRatesAmountPair(int movie, int ratesAmount)
        {
            this.movie = movie;
            this.ratesAmount = ratesAmount;
        }

        public int getMovie() {
            return this.movie;
        }

        public int getRatesAmount() {
            return this.ratesAmount;
        }

        public void setMovie(int movie) {
            this.movie = movie;
        }

        public void setRatesAmount(int ratesAmount) {
            this.ratesAmount = ratesAmount;
        }

        public String toString()
        {
            return this.movie + "::" + this.ratesAmount;
        }
    }

    /* Methods */

    public ArrayList<Triple> getDataset() {
        return dataset;
    }

    public int getMoviesAmount() {
        return moviesAmount;
    }

    public int getUsersAmount() {
        return usersAmount;
    }

    public void setMoviesAmount(int moviesAmount) {
        this.moviesAmount = moviesAmount;
    }

    public void setUsersAmount(int usersAmount) {
        this.usersAmount = usersAmount;
    }

    /**
     * Creates a dataset with default values.
     */
    public Dataset()
    {
        this.moviesAmount = 0;
        this.usersAmount = 0;
    }

    /**
     * Creates a dataset from the given source file.
     *
     * @param sourceFile the file from which to load the data
     */
    public Dataset(File sourceFile) throws FileNotFoundException
    {
        this.loadDataset(sourceFile);
    }

    /**
     * Loads data from a specified source.
     *
     * @param sourceFile the file from which to load the data
     */
    public abstract void loadDataset(File sourceFile) throws FileNotFoundException;

    /**
     * Gets the test set of a k-fold cross-validation.
     *
     * @param layersAmount the number of layers for the stratified k-fold algorithm
     *                     {@literal (Precondition: layersAmount >= 1) }
     *                     A layerAmount equal to 1 means you're doing a non
     *                     stratified k-fold.
     * @param testPecentage percentage of the dataset that belong to the test set
     *                      {@literal (Precondition: testPercentage >= 0) }
     * @return the elements contained in the test set
     */
    public ArrayList<Triple> getTestSet(int layersAmount, double testPecentage)
    {
        //Fill
        return new ArrayList<>();
    }

    /**
     * Gets the partition of the dataset.
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
    public ArrayList<MovieRatesAmountPair>[] getPartitions(int k, int layersAmount)
    {
        /* Get the movies with highest and lowest rate amount. */
        int maxMovieRatesAmount = 0;
        int minMovieRatesAmount = 0;

        TreeMap <Integer, Integer> ratesPerMovie = new TreeMap<>();

        /* Fill the array with the number of rates and update the max amount of rates per movie. */
        for (Triple item : dataset)
        {
            /* Increment the value if already present */
            if (ratesPerMovie.containsKey(item.getSnd()))
            {
                ratesPerMovie.put(item.getSnd(), ratesPerMovie.get(item.getSnd()) + 1);
            }
            else /* Add the value otherwise */
            {
                ratesPerMovie.put(item.getSnd(), 1);
            }

            /* Keep the maximum amount of ratings for a single movie updated */
            if (ratesPerMovie.get(item.getSnd()) > maxMovieRatesAmount)
            {
                maxMovieRatesAmount = ratesPerMovie.get(item.getSnd());
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
        ArrayList<MovieRatesAmountPair>[] layers = (ArrayList<MovieRatesAmountPair>[]) new ArrayList[layersAmount];

        /* Initialize the ArrayList */
        for (int i = 0; i < layers.length; i++)
        {
            layers[i] = new ArrayList<>();
        }

        for (Map.Entry<Integer, Integer> movie : ratesPerMovie.entrySet())
        {
            /* Reset the high range. */
            double highRange = minMovieRatesAmount + layerRange;

            /* Movies with no rating should not appear. */
            if (movie.getValue() <= 0)
            {
                throw new IllegalStateException("Only rated movies should be considered.");
            }

            for (ArrayList<MovieRatesAmountPair> layer: layers)
            {
                /* We're on the last layer, add here the movie here. */
                if (layer.equals(layers[layersAmount - 1]))
                {
                    layer.add(new MovieRatesAmountPair(movie.getKey(), movie.getValue()));
                    break; //Do not continue to check for a layer after it has been found
                }
                else if (movie.getValue() < highRange)
                {
                    layer.add(new MovieRatesAmountPair(movie.getKey(), movie.getValue()));
                    break; //Do not continue to check for a layer after it has been found
                }
                /* Update the range. */
                highRange += layerRange;
            }
        }

        /* Fill the k partitions: k folding */
        ArrayList<MovieRatesAmountPair>[] partitions = (ArrayList<MovieRatesAmountPair>[]) new ArrayList[k];
        Random randomizer = new Random();

        /* Initialize the partitions */
        for (int i = 0; i < partitions.length; i++)
        {
            partitions[i] = new ArrayList<>();
        }

        /* For each layer add random elements to each partition */
        for (ArrayList<MovieRatesAmountPair> layer : layers)
        {
            /* Remove the elements added to the partitions */
            while (layer.size() > 0)
            {
                /* Select a random element and put it in a partition */
                for (ArrayList<MovieRatesAmountPair> partition : partitions)
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

    /**
     * Saves all the partitions in the given directory.
     *
     * @param testSetIndex the index of the partition containing the test set
     *                     {@literal (Precondition: testSetIndex >= 0
     *                     && testSetIndex <= partitionsAmount - 1 }
     * @param savePath the path where the partitions are going to be saved
     *                 A null path is intended as the current working directory.
     */
    public void savePartitions(int testSetIndex, String savePath)
    {
        // Fill
    }
}