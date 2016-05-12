package it.unica.foresee.libraries;

import static it.unica.foresee.utils.Logger.debug;
import static it.unica.foresee.utils.Logger.warn;

import it.unica.foresee.datasets.DatasetSparseVector;
import it.unica.foresee.datasets.interfaces.NumberElement;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.util.Pair;

import java.util.*;

/**
 * This class is an implementation of the nearest neighbour algorithm for user similarity.
 */
public class NearestNeighbour<T extends DatasetSparseVector<? extends NumberElement>>
{
    /**
     * The matrix of the users with the ratings.
     *
     * Elements can be retrieved as dataset[user, item].
     */
    private DatasetSparseVector<T> dataset;

    /**
     * Maps a userID to it's position in the similarity matrix.
     */
    private Integer[] keys;

    /**
     * Matrix of the similarity between users.
     */
    private double[][] similarityMatrix;

    /**
     * Initialise the object with a dataset.
     * @param dataset
     */
    public NearestNeighbour(DatasetSparseVector<T> dataset)
    {
        if(dataset.keySet().size() == 0)
        {
            throw new IllegalStateException("The dataset cannot be empty.");
        }
        this.dataset = dataset;
    }

    /**
     * Calculates the predictions for each non rated item
     * for each user in the dataset.
     * @return the updated dataset
     */
    public DatasetSparseVector<T> makePredictions(int neighboursAmount)
    {
        int originalSize = this.dataset.size();

        if(similarityMatrix == null)
        {
            initialiseSimilarityMatrix();
        }

        for (int userIndex = 0; userIndex < keys.length; userIndex++)
        {
            // Obtain the nearest neighbours to the current user
            List<Pair<Integer, Double>> nearestNeighbours = getNearestNeighbours(userIndex, neighboursAmount);

            // The current user
            T currentUser = dataset.get(keys[userIndex]);

            // Array of the keys of the user's items
            Integer[] itemsKeys= currentUser.keySet().toArray(new Integer[0]);

            for (int itemIndex = 0; itemIndex < itemsKeys.length; itemIndex++)
            {
                double rating = currentUser.getDatasetElement(itemsKeys[itemIndex]).getDoubleValue();

                // Make previsions only on missing entries
                if(rating == 0)
                {
                    // Useful variables to understand what's going on
                    double denominator = 0;
                    double userSimilarity = 0;
                    double neighbourAverage;
                    double neighbourRateOnItem;

                    // Set the rating to the average of the ratings of the user
                    rating = dataset.getDatasetElement(userIndex).getDoubleValue();

                    // Check that the average is not null
                    if (rating == 0.0)
                    {
                        throw new IllegalStateException("User " + userIndex + "has an average of 0");
                    }

                    for (Pair<Integer, Double> neighbour : nearestNeighbours)
                    {
                        userSimilarity = similarityMatrix[userIndex][neighbour.getFirst()];
                        neighbourAverage = dataset.getDatasetElement(neighbour.getFirst()).getDoubleValue();
                        neighbourRateOnItem = dataset.getDatasetElement(neighbour.getFirst()).getDatasetElement(itemIndex).getDoubleValue();

                        rating += userSimilarity * (neighbourRateOnItem - neighbourAverage);
                        denominator += userSimilarity;
                    }

                    rating /= denominator;

                    // Assign the new value
                    dataset.getDatasetElement(keys[userIndex]).getDatasetElement(itemsKeys[itemIndex]).setElement(rating);

                    // Check that the value is in the bounds
                    if (rating < 1 || rating > 5)
                    {
                        throw new IllegalStateException("The rating is out of bound: " + rating + "\n" +
                                "user similarity: " + userSimilarity + "\n" +
                                "denominator: " + denominator);
                    }
                }
            }
        }
        if(dataset.keySet().size() != originalSize)
        {
            throw new IllegalStateException("The dataset should not change size.");
        }

        if(dataset.keySet().size() == 0)
        {
            throw new IllegalStateException("The dataset cannot be empty.");
        }
        return dataset;
    }

    /**
     * Get the n nearest neighbours to the given user
     * @param userIndex user index in the similarity matrix
     * @param neighboursAmount amount of neighbours to check
     * @return a list of the n nearest neighbours
     */
    public List<Pair<Integer, Double>> getNearestNeighbours(int userIndex, int neighboursAmount)
    {
        ArrayList<Pair<Integer, Double>> neighbours = new ArrayList<>();

        // neighbour j
        for (int i = 0; i < similarityMatrix[userIndex].length; i++)
        {
            // Skip the user itself, just add its neighbours
            if (i != userIndex)
            {
                neighbours.add(new Pair<>(i, similarityMatrix[userIndex][i]));
            }
        }

        // Sort descending (note the minus sign before comparison)
        neighbours.sort((Pair<Integer, Double> a, Pair<Integer, Double> b)
                -> - Double.compare(a.getSecond(), b.getSecond()));

        if (neighbours.size() > neighboursAmount)
        {
            neighbours.subList(neighboursAmount - 1, neighbours.size()).clear();
        }

        return neighbours;
    }

    /**
     * Sets a similarity value for each user to each other user (neighbour).
     */
    public void initialiseSimilarityMatrix()
    {
        similarityMatrix = new double[dataset.size()][dataset.size()];

        // Array of the keys of the users
        this.keys = dataset.keySet().toArray(new Integer[0]);

        // user i
        for (int userIndex = 0; userIndex < dataset.size(); userIndex++)
        {
            // neighbour j
            for (int neighbourIndex = 0; neighbourIndex < dataset.size(); neighbourIndex++)
            {
                // The similarity is symmetric, the lower triangle of the matrix
                // is equal to the upper.
                if (similarityMatrix[neighbourIndex][userIndex] != 0)
                {
                    similarityMatrix[neighbourIndex][userIndex] = similarityMatrix[userIndex][neighbourIndex];
                }
                // When user and neighbour are different compute the similarity
                else if (keys[userIndex] != keys[neighbourIndex])
                {
                    // Set the value in the matrix
                    try
                    {
                        double sim = userSimilarity(dataset.get(keys[userIndex]), dataset.get(keys[neighbourIndex]));
                        // Assign a value of 0 if the similarity is negative
                        similarityMatrix[userIndex][neighbourIndex] = sim > 0 ? sim : 0;
                    }
                    catch (IllegalArgumentException e)
                    {
/*
                        debug(e + "\n" +
                        "User " + similarityMatrix[userIndex] + " and/or user "
                                + similarityMatrix[neighbourIndex] + " don't have enough data in common.\n");
*/
                        // Not having enough data seems enough to put similarity to 0
                        similarityMatrix[userIndex][neighbourIndex] = 0;
                    }

                }
                // No need to check the same user
                else if (similarityMatrix[userIndex] == similarityMatrix[neighbourIndex])
                {
                    similarityMatrix[userIndex][userIndex] = 1;
                }
            }
        }
    }

    /**
     * Calculates the Pearson similarity of the items rated by both
     * of the users.
     *
     * @param user a user array of values common between user and neighbour
     * @param neighbour a neighbour array of values common between user and neighbour
     * @return the Pearson similarity of the items rated by both
     * of the users
     */
    public double userSimilarity(double[] user, double[] neighbour)
    {
        if (user.length < 2 || neighbour.length < 2)
        {
            throw new IllegalArgumentException("The arrays are too short, min length is 2.\n" +
                    "user:\t" + Arrays.toString(user) + "\n" +
                    "neighbour:\t" + Arrays.toString(neighbour));
        }
        return new PearsonsCorrelation().correlation(user, neighbour);
    }

    /**
     * Calculates the Pearson similarity of the items rated by both
     * of the users.
     *
     * @param user a user
     * @param neighbour a neighbour of the user
     * @return the Pearson similarity of the items rated by both
     * of the users
     */
    public double userSimilarity(T user, T neighbour)
    {
        double[][] subArrays = getSubArrays(user, neighbour);
        return userSimilarity(subArrays[0], subArrays[1]);
    }

    /**
     * Get a subarray containing only the items rated by both the users.
     * @param user a user
     * @param neighbour a neighbour of the user
     * @return a matrix of user and neighbour common items
     */
    private double[][] getSubArrays(T user, T neighbour)
    {
        ArrayList<Double> userList, neighbourList;
        userList = new ArrayList<>();
        neighbourList = new ArrayList<>();

        // Check everything is working well
        if (user == null)
        {
            throw new IllegalStateException("The user pointer should not be null!");
        }
        if (neighbour == null)
        {
            throw new IllegalStateException("The neighbour pointer should not be null!");
        }

        // Add the common elements between the sets
        for(Integer item : user.navigableKeySet())
        {
            if(neighbour.containsKey(item))
            {
                userList.add(user.getDatasetElement(item).getDoubleValue());
                neighbourList.add(neighbour.getDatasetElement(item).getDoubleValue());
            }
        }

        double[] userArray = new double[userList.size()];
        double[] neighbourArray = new double[userList.size()];

        // Obtain arrays from the arraylists
        for (int i = 0; i < userArray.length; i++)
        {
            userArray[i] = userList.get(i);
            neighbourArray[i] = neighbourList.get(i);
        }
        return new double[][] {userArray, neighbourArray};
    }

}
