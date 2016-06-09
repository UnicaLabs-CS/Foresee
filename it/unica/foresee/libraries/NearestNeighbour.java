package it.unica.foresee.libraries;

import static it.unica.foresee.utils.Logger.debug;
import static it.unica.foresee.utils.Logger.warn;

import it.unica.foresee.datasets.DatasetNestedSparseVector;
import it.unica.foresee.datasets.DatasetSparseVector;
import it.unica.foresee.datasets.DoubleElement;
import it.unica.foresee.utils.Logger;
import it.unica.foresee.utils.SparseMatrix;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.util.Pair;

import java.util.*;

/**
 * This class is an implementation of the nearest neighbour algorithm for user similarity.
 */
public class NearestNeighbour<T extends DatasetNestedSparseVector<? extends DatasetSparseVector<? extends DoubleElement>>>
{

    /**
     * The matrix of the users with the ratings.
     */
    private T dataset;

    /**
     * Lowest acceptable value.
     */
    private double minValue = 1;

    /**
     * Highest acceptable value.
     */
    private double maxValue = 5;

    /**
     * Matrix of the similarity between users.
     */
    private SparseMatrix similarityMatrix;

    /**
     * Initialise the object with a dataset.
     * @param dataset
     */
    public NearestNeighbour(T dataset)
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
    public T makeForecasts(int neighboursAmount)
    {
        List<Pair<Integer, Double>> nearestNeighbours = null;
        DatasetSparseVector<?> currentUser = null;
        double denominator = 0;
        double userSimilarity = 0;
        double neighbourAverage = 0;
        double neighbourRateOnItem = 0;
        double numerator = 0;

        int lastItem = dataset.getHighestNestedKey();

        int originalSize = this.dataset.size();

        if(similarityMatrix == null)
        {
            initialiseSimilarityMatrix();
        }

        for (int userIndex : dataset.keySet())
        {
            // The current user target of the forecasting
            currentUser = dataset.get(userIndex);
            denominator = 0;

            if (currentUser == null)
            {
                throw new IllegalStateException("User " + userIndex + " is null.");
            }

            if (currentUser.isEmpty())
            {
                throw new IllegalStateException("User " + userIndex + " is empty");
            }

            // Obtain the nearest neighbours (ID, userSim) to the current user
            nearestNeighbours = getNearestNeighbours(userIndex, neighboursAmount);

            debug("NearestNeighbours amount: " + nearestNeighbours.size());

            // Initialise to null for each user
            Map<Integer, Double> neighboursMeans = null;

            // Search the items of the current user for a non rated item
            for (int itemIndex = 1; itemIndex <= lastItem; itemIndex++)
            {
                // Make this computation only once per user and only if required
                if (neighboursMeans == null)
                {
                    neighboursMeans = new HashMap<>();
                    for (Pair<Integer, Double> neighSim : nearestNeighbours)
                    {
                        // The denominator is the sum of the neighbours similarities
                        denominator += neighSim.getSecond();
                        debug("denominator inremented by: " + neighSim.getSecond() + " = " + denominator);
                        // Pre-calculate the means of the neighbours
                        dataset.get(neighSim.getFirst()).setVectorSize(lastItem);
                        neighboursMeans.put(neighSim.getFirst(),
                                dataset.get(neighSim.getFirst()).getMean());
                    }
                }

                // Create missing entries
                if(currentUser.get(itemIndex) == null) currentUser.put(itemIndex, new DoubleElement(0.0));

                // Make forecasts only on missing entries (equal to 0)
                if (currentUser.get(itemIndex).getDoubleValue() == 0.0)
                {
                    // Set the rating to the average of the ratings of the user
                    currentUser.setVectorSize(lastItem);

                    // Check that the average is not null
                    if (currentUser.getMean() == 0.0)
                    {
                        throw new IllegalStateException("User " + userIndex + " has an average of 0 \n" +
                                currentUser);
                    }

                    // Useful variables to understand what's going on
                    numerator = 0;
                    for (Pair<Integer, Double> neighbourSimilarity : nearestNeighbours)
                    {
                        DatasetSparseVector<? extends DoubleElement> neighbour = dataset.get(neighbourSimilarity.getFirst());
                        // Skip unrated items
                        if (neighbour.get(itemIndex) == null) continue;

                        neighbourRateOnItem = neighbour.get(itemIndex).getDoubleValue();

                        // Skip unrated items
                        if(neighbourRateOnItem == 0) continue;

                        userSimilarity = neighbourSimilarity.getSecond();

                        neighbour.setVectorSize(lastItem);
                        neighbourAverage = neighboursMeans.get(neighbourSimilarity.getFirst());

                        // Formula for the numerator
                        numerator += userSimilarity * (neighbourRateOnItem - neighbourAverage);

                        debug("\t\t\t" + userSimilarity + " * (" + neighbourRateOnItem + " -  " + neighbourAverage + ")");
                        debug(currentUser.getMean() + " +  -----------------------------------");
                        debug("\t\t\t" + userSimilarity + "\n");

                        debug("r_ni – mean r_n * uSim - " + neighbourSimilarity.getFirst() + ": " +
                                userSimilarity * (neighbourRateOnItem - neighbourAverage));
                    }

                    debug("numerator: " + numerator);
                    debug("denominator: " + denominator);

                    // Skip impossible results
                    if (denominator == 0) continue;

                    double rating = currentUser.getMean() + (numerator/denominator);

                    // Check that the value is in the bounds
                    if (rating > getMaxValue())
                    {
                        warn("Converting too high rating: " + rating + " -> " + getMaxValue());
                        rating = getMaxValue();
                    }

                    if (rating < getMinValue())
                    {
                        warn("Converting too low rating: " + rating + " -> " + getMinValue());
                        rating = getMinValue();
                    }

                    // Assign the new value
                    dataset.get(userIndex).get(itemIndex).setElement(rating);
                    Logger.log("User " + userIndex + " of " + dataset.size() +
                    ": " + rating);
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
                    debug("Skipping already known similarity: (" + userIndex + ", " +
                    neighbourIndex + ")");
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
    public double userSimilarity(DatasetSparseVector<?> user, DatasetSparseVector<?> neighbour)
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
    private double[][] getSubArrays(DatasetSparseVector<?> user, DatasetSparseVector<?> neighbour)
    {
        // Check everything is working well
        if (user == null)
        {
            throw new IllegalArgumentException("The user pointer should not be null!");
        }

        if (neighbour == null)
        {
            throw new IllegalArgumentException("The neighbour pointer should not be null!");
        }

        // Retrieve the common elements between user and neighbour
        Set<Integer> commonElementsSet = new HashSet<>();
        commonElementsSet.addAll(user.keySet());
        commonElementsSet.retainAll(neighbour.keySet());

        // Fail early
        if (commonElementsSet.size() < 2)
        {
            throw new IllegalArgumentException("The arrays are too short, min length is 2.\n" +
                    "Common elements: " + commonElementsSet);
        }

        double[] userArray = new double[commonElementsSet.size()];
        double[] neighbourArray = new double[commonElementsSet.size()];

        int i = 0;
        for(int item : commonElementsSet)
        {
            userArray[i] = user.get(item).getDoubleValue();
            neighbourArray[i] = neighbour.getDatasetElement(item).getDoubleValue();
            i++;
        }

        return new double[][] {userArray, neighbourArray};
    }

    public SparseMatrix getSimilarityMatrix()
    {
        return this.similarityMatrix;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }
}
