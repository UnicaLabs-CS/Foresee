package it.unica.foresee.libraries;

import it.unica.foresee.datasets.DatasetSparseVector;
import it.unica.foresee.datasets.IntegerElement;
import org.apache.commons.math3.ml.clustering.Clusterable;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is an implementation of the nearest neighbour algorithm for user similarity.
 */
public class NearestNeighbour<T extends DatasetSparseVector<it.unica.foresee.datasets.interfaces.DatasetElement<IntegerElement>> & Clusterable>
{
    /**
     * The matrix of the users with the ratings.
     *
     * Elements can be retrieved as dataset[user, item].
     */
    private DatasetSparseVector<T> dataset;

    /**
     * Matrix of the similarity between users.
     */
    private double[][] similarìtyMatrix;

    /**
     * Initialise the object with a dataset.
     * @param dataset
     */
    public NearestNeighbour(DatasetSparseVector<T> dataset)
    {
        this.dataset = dataset;
    }

    /**
     * Calculates the predictions for each non rated item
     * for each user in the dataset.
     * @return the updated dataset
     */
    public DatasetSparseVector<T> makePredictions(int neighboursAmount)
    {
        if(similarìtyMatrix == null)
        {
            initialiseSimilarityMatrix();
        }

        for (int userID = 0; userID < dataset.size(); userID++)
        {
            List<Pair<Integer, Double>> nearestNeighbours = getNearestNeighbours(userID, neighboursAmount);

            for (int itemID = 0; itemID < dataset.size(); itemID++)
            {
                double rating = dataset.getDatasetElement(userID).getDatasetElement(itemID).getElement().getElement();
                if(rating == 0)
                {
                    // Useful variables to understand what's going on
                    double denominator = 0;
                    double userSimilarity;
                    double neighbourAverage;
                    double neighbourRateOnItem;

                    // Set the rating to the average of the ratings of the user
                    rating = dataset.getDatasetElement(userID).getValueForMean();

                    for (Pair<Integer, Double> neighbour : nearestNeighbours)
                    {
                        userSimilarity = similarìtyMatrix[userID][neighbour.getFirst()];
                        neighbourAverage = dataset.getDatasetElement(neighbour.getFirst()).getValueForMean();
                        neighbourRateOnItem = dataset.getDatasetElement(neighbour.getFirst()).getDatasetElement(itemID).getElement().getElement();

                        rating += userSimilarity * (neighbourRateOnItem - neighbourAverage);
                        denominator += userSimilarity;
                    }

                    rating /= denominator;

                    // Assign the new value casted to int
                    dataset.getDatasetElement(userID).getDatasetElement(itemID).getElement().setElement((int) rating);
                }
            }
        }
        return this.dataset;
    }

    public List<Pair<Integer, Double>> getNearestNeighbours(int userID, int neighboursAmount)
    {
        ArrayList<Pair<Integer, Double>> neighbours = new ArrayList<>();

        // neighbour j
        for (int i = 0; i < similarìtyMatrix[userID].length; i++)
        {
            // Skip the user itself, just add its neighbours
            if (i != userID)
            {
                neighbours.add(new Pair<>(i, similarìtyMatrix[userID][i]));
            }
        }

        // Sort descending (note the minus sign before comparison)
        neighbours.sort((Pair<Integer, Double> a, Pair<Integer, Double> b)
                -> - Double.compare(a.getSecond(), b.getSecond()));

        return neighbours.subList(0, neighboursAmount);
    }

    /**
     * Sets a similarity value for each user to each other user (neighbour).
     */
    public void initialiseSimilarityMatrix()
    {
        similarìtyMatrix = new double[dataset.size()][dataset.size()];

        // user i
        for (int i = 0; i < dataset.size(); i++)
        {
            // neighbour j
            for (int j = 0; j < dataset.size(); j++)
            {
                // The similarity is symmetric, the lower triangle of the matrix
                // is equal to the upper.
                if (similarìtyMatrix[j][i] != 0)
                {
                    similarìtyMatrix[j][i] = similarìtyMatrix[i][j];
                }
                // When user and neighbour are different compute the similarity
                else if (i != j)
                {
                    // Set the value in the matrix
                    similarìtyMatrix[i][j] = userSimilarity(dataset.getDatasetElement(i), dataset.getDatasetElement(j));
                }
                // No need to check the same user
                else if (i == j)
                {
                    similarìtyMatrix[i][i] = 1;
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

        // Add the common elements between the sets
        for(Integer item : user.keySet())
        {
            if(neighbour.containsKey(item))
            {
                userList.add(user.getDatasetElement(item).getValueForMean());
                neighbourList.add(neighbour.getDatasetElement(item).getValueForMean());
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
