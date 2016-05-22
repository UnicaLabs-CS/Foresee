package it.unica.foresee.libraries;

import static it.unica.foresee.utils.Logger.debug;
import static it.unica.foresee.utils.Logger.warn;

import it.unica.foresee.datasets.DatasetSparseVector;
import it.unica.foresee.datasets.Movielens;
import it.unica.foresee.datasets.MovielensElement;
import it.unica.foresee.datasets.interfaces.NumberElement;
import it.unica.foresee.utils.Logger;
import it.unica.foresee.utils.SparseMatrix;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.util.Pair;

import java.util.*;

/**
 * This class is an implementation of the nearest neighbour algorithm for user similarity.
 */
public class NearestNeighbour<T extends MovielensElement>
{

    /**
     * The matrix of the users with the ratings.
     */
    private Movielens dataset;

    /**
     * Matrix of the similarity between users.
     */
    private SparseMatrix similarityMatrix;

    /**
     * Initialise the object with a dataset.
     * @param dataset
     */
    public NearestNeighbour(Movielens dataset)
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
    public Movielens makeForecasts(int neighboursAmount)
    {
        int originalSize = this.dataset.size();

        if(similarityMatrix == null)
        {
            initialiseSimilarityMatrix();
        }

        for (int userIndex : dataset.keySet())
        {
            // The current user target of the forecasting
            MovielensElement currentUser = dataset.get(userIndex);

            // Obtain the nearest neighbours (ID, userSim) to the current user
            List<Pair<Integer, Double>> nearestNeighbours = getNearestNeighbours(userIndex, neighboursAmount);

            // Obtain the sum of the userSim of the neighbours
            double denominator = 0;
            Logger.debug("User similarities for user: " + userIndex);
            for(Pair<Integer, Double> neighbour : nearestNeighbours)
            {
                Logger.debug(neighbour.getFirst() + ": " + neighbour.getSecond());
                denominator += neighbour.getSecond();
            }

            // Search the items of the current user for a non rated item
            for (int itemIndex : currentUser.keySet())
            {
                double rating = currentUser.getDatasetElement(itemIndex).getDoubleValue();

                // Make forecasts only on missing entries (equal to 0)
                if (rating == 0)
                {
                    // Useful variables to understand what's going on
                    double userSimilarity = 0;
                    double neighbourAverage = 0;
                    double neighbourRateOnItem = 0;
                    double numerator = 0;

                    // Set the rating to the average of the ratings of the user
                    rating = dataset.getDatasetElement(userIndex).getDoubleValue();

                    // Check that the average is not null
                    if (rating == 0.0)
                    {
                        throw new IllegalStateException("User " + userIndex + "has an average of 0");
                    }

                    for (Pair<Integer, Double> neighbour : nearestNeighbours)
                    {
                        userSimilarity = neighbour.getSecond();
                        neighbourAverage = dataset.getDatasetElement(neighbour.getFirst()).getDoubleValue();
                        Logger.debug("Neighbour average: user " + neighbour.getFirst() + " - " + neighbourAverage);
                        neighbourRateOnItem = dataset.getDatasetElement(neighbour.getFirst()).getDatasetElement(itemIndex).getDoubleValue();
                        Logger.debug("Neighbour rate: user " + neighbour.getFirst() +
                                " - item " + itemIndex +
                                " - rating " + neighbourAverage);

                        // Skip unrated items
                        if(neighbourRateOnItem == 0) continue;

                        numerator += userSimilarity * (neighbourRateOnItem - neighbourAverage);
                        Logger.debug("Numerator: " + numerator);
                    }

                    // Skip impossible results
                    if (denominator == 0) continue;

                    rating += numerator/denominator;

                    // Do not consider negative values
                    if (rating < 0) continue;

                    // Assign the new value
                    dataset.getDatasetElement(userIndex).getDatasetElement(itemIndex).setElement(rating);

                    // Check that the value is in the bounds
                    if (rating > 5)
                    {
                        throw new IllegalStateException("The rating is out of bound: " + rating + "\n" +
                                "user similarity: " + userSimilarity + "\n" +
                                "neighbour rate: " + neighbourRateOnItem + "\n" +
                                "neighbour average: " + neighbourAverage + "\n" +
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
     * Get the n nearest neighbours to the given user (ID, userSim).
     * @param userIndex user index in the similarity matrix
     * @param neighboursAmount amount of neighbours to check
     * @return a list of the n nearest neighbours (ID, userSim)
     */
    public List<Pair<Integer, Double>> getNearestNeighbours(int userIndex, int neighboursAmount)
    {
        ArrayList<Pair<Integer, Double>> neighbours = new ArrayList<>();

        // neighbour j
        for (int j : dataset.keySet())
        {
            // Skip the user itself, just add its neighbours
            if (j != userIndex)
            {
                neighbours.add(new Pair<>(j, similarityMatrix.symmetricGet(userIndex, j)));
            }
        }

        // Sort descending (note the minus sign before comparison)
        neighbours.sort((Pair<Integer, Double> a, Pair<Integer, Double> b)
                -> - Double.compare(a.getSecond(), b.getSecond()));

        // Cut the list up to the limit set by the neighbours amount argument
        if (neighbours.size() > neighboursAmount)
        {
            neighbours.subList(neighboursAmount, neighbours.size()).clear();
        }

        return neighbours;
    }

    /**
     * Sets a similarity value for each user to each other user (neighbour).
     */
    public void initialiseSimilarityMatrix()
    {
        // Create a matrix to store the similarity
        similarityMatrix = new SparseMatrix();

        // Set of the keys (user IDs) of the dataset
        Set<Integer> keys = dataset.keySet();

        //Compute the similarity for each user and for each of its neighbours.
        // User i
        for (int userIndex : keys)
        {
            // Neighbour j
            for (int neighbourIndex : keys)
            {
                // If the user and the neighbour are the same, they have complete similarity.
                if (userIndex == neighbourIndex)
                {
                    similarityMatrix.symmetricPut(userIndex, neighbourIndex, 1.0);
                }
                else if(similarityMatrix.symmetricGet(userIndex, neighbourIndex) != null)
                {
                    // Skip computing the similarities already known
                    continue;
                }
                // When user and neighbour are different compute the similarity
                else if (userIndex != neighbourIndex)
                {
                    // Set the value in the matrix
                    try
                    {
                        double sim = userSimilarity(dataset.get(userIndex), dataset.get(neighbourIndex));
                        // Assign a value of 0 if the similarity not greater than zero
                        similarityMatrix.symmetricPut(userIndex, neighbourIndex, sim > 0 ? sim : 0);
                    }
                    catch (IllegalArgumentException e)
                    {

                        debug(e + "\n" +
                        "User " + userIndex + " and/or user "
                                + neighbourIndex + " don't have enough data in common.\n" +
                                "Similarity set to 0.");

                        // Not having enough data seems enough to put similarity to 0
                        similarityMatrix.symmetricPut(userIndex, neighbourIndex, 0.0);
                    }

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
    public double userSimilarity(MovielensElement user, MovielensElement neighbour)
    {
        double[][] subArrays = getSubArrays(user, neighbour);
        return userSimilarity(subArrays[0], subArrays[1]);
    }

    /**
     * Get a matrix of sub-arrays containing only the items rated by both the users.
     * @param user a user
     * @param neighbour a neighbour of the user
     * @return a matrix of user and neighbour common items
     */
    private double[][] getSubArrays(MovielensElement user, MovielensElement neighbour)
    {
        ArrayList<Double> userList, neighbourList;
        userList = new ArrayList<>();
        neighbourList = new ArrayList<>();

        // Check everything is working well
        if (user == null)
        {
            throw new IllegalArgumentException("The user pointer should not be null!");
        }

        if (neighbour == null)
        {
            throw new IllegalArgumentException("The neighbour pointer should not be null!");
        }

        // Add the elements in common between user and neighbour
        for(int item : user.keySet())
        {
            if(neighbour.containsKey(item))
            {
                userList.add(user.getDatasetElement(item).getDoubleValue());
                neighbourList.add(neighbour.getDatasetElement(item).getDoubleValue());
            }
        }

        // Fail early
        if (userList.size() < 2)
        {
            throw new IllegalArgumentException("The arrays are too short, min length is 2.\n" +
                    "user:\t" + userList + "\n" +
                    "neighbour:\t" + neighbourList);
        }

        double[] userArray = new double[userList.size()];
        double[] neighbourArray = new double[userList.size()];

        // Obtain arrays from the ArrayLists
        for (int i = 0; i < userArray.length; i++)
        {
            userArray[i] = userList.get(i);
            neighbourArray[i] = neighbourList.get(i);
        }

        return new double[][] {userArray, neighbourArray};
    }

    public SparseMatrix getSimilarityMatrix()
    {
        return this.similarityMatrix;
    }

}
