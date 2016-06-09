package it.unica.foresee.utils;

import it.unica.foresee.datasets.DatasetSparseVector;
import it.unica.foresee.datasets.DoubleElement;
import org.apache.commons.math3.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Converts different classes from one to another
 */
public class Converter<T extends DatasetSparseVector<? extends DoubleElement>>
{
    /**
     * If a lower limit to the admissible values.
     */
    boolean lowerLimitSet;

    /**
     * A lower limit to the admissible values.
     */
    double lowerLimit;

    /**
     * If an upper limit to the admissible values.
     */
    boolean upperLimitSet;

    /**
     * An upper limit to the admissible values.
     */
    double upperLimit;

    public double getLowerLimit() {
        return lowerLimit;
    }

    public double getUpperLimit() {
        return upperLimit;
    }

    public boolean isLowerLimitSet() {
        return lowerLimitSet;
    }

    public boolean isUpperLimitSet() {
        return upperLimitSet;
    }

    public void setLowerLimit(double lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public void setUpperLimit(double upperLimit) {
        this.upperLimit = upperLimit;
    }

    public void setLowerLimitSet(boolean lowerLimitSet) {
        this.lowerLimitSet = lowerLimitSet;
    }

    public void setUpperLimitSet(boolean upperLimitSet) {
        this.upperLimitSet = upperLimitSet;
    }

    /**
     * Creates two arrays that can be compared with the RMSE test.
     *
     * Each array is composed of the sequence of ratings of each user one after the other,
     * so you can expect to start with the ratings given by the first user, followed
     * by the ratings of the following user and so on.
     *
     * The test set is used as a reference, and only entries in both the
     * test and training sets are considered.
     *
     * A warning is issued if an entry present in the test set is not
     * present in the training set, as this should be avoided.
     *
     * @param test the test set
     * @param model the model of the training set
     * @param userToModel a map to obtain the corresponding user from the model
     * @return a pair of arrays that can be compared through the RMSE test
     */
    public org.apache.commons.math3.util.Pair<Double[], Double[]> getRMSEArrays(DatasetSparseVector<T> test,
                                                                                List<T> model,
                                                                                Map<Integer, Integer> userToModel)
    {
        // Sanity checks
        if (test.size() == 0)
        {
            throw new IllegalArgumentException("The first argument must not be empty");
        }

        if (model.size() == 0)
        {
            throw new IllegalArgumentException("The second argument must not be empty");
        }

        if (userToModel.size() == 0)
        {
            throw new IllegalArgumentException("The third argument must not be empty");
        }


        ArrayList<Double> testArray = new ArrayList<>();
        ArrayList<Double> trainArray = new ArrayList<>();

        // Visit each user of the test set, then the corresponding
        // from the training set
        for(int userID : test.keySet())
        {
            T user = test.getDatasetElement(userID);

            // Skip null users and users not present in the model
            if (userToModel.get(userID) == null || model.get(userToModel.get(userID)) == null)
            {
                Logger.warn("User" + userID + " is present in test set but not in train");
                continue;
            }

            T trainUser = model.get(userToModel.get(userID));

            for (int movieID : user.keySet())
            {
                DoubleElement movie = user.getDatasetElement(movieID);
                DoubleElement trainMovie = trainUser.getDatasetElement(movieID);

                if (trainMovie == null)
                {
                    Logger.warn("Movie" + movieID + " of User"
                            + userID + " is present in test set but not in train");
                    continue;
                }
                else if (isLowerLimitSet() && trainMovie.getDoubleValue() < getLowerLimit()
                        || isUpperLimitSet() && trainMovie.getDoubleValue() > getUpperLimit())
                {
                    throw new IllegalStateException("Value not permitted: " +
                            trainMovie.getDoubleValue() + " for entry[" + userID + "][" + movieID + "]");
                }

                // Obtain a movie from the testset
                testArray.add(movie.getDoubleValue());
                // Obtain the corresponding rating provided by training set
                trainArray.add(trainMovie.getDoubleValue());
            }
        }

        if (trainArray.size() == 0 || testArray.size() == 0)
        {
            throw new IllegalStateException("The arrays should not be empty");
        }

        // Fist the estimator, than the parameter
        return new org.apache.commons.math3.util.Pair<>(trainArray.toArray(new Double[0]),
                testArray.toArray(new Double[0]));
    }

    /**
     * Creates two arrays that can be compared with the RMSE test.
     *
     * {@link #getRMSEArrays(DatasetSparseVector, List, Map)}
     *
     * @param test the test set
     * @param training the training set
     * @return a pair of arrays that can be compared through the RMSE test
     */
    public org.apache.commons.math3.util.Pair<Double[], Double[]> getRMSEArrays(DatasetSparseVector<T> test,
                                                                                DatasetSparseVector<T> training)
    {
        // Sanity checks
        if (test.size() == 0)
        {
            throw new IllegalArgumentException("The first argument must not be empty");
        }

        if (training.size() == 0)
        {
            throw new IllegalArgumentException("The second argument must not be empty");
        }

        ArrayList<Double> testArray = new ArrayList<>();
        ArrayList<Double> trainArray = new ArrayList<>();

        // Visit each user of the test set, then the corresponding
        // from the training set
        for(int userID : test.keySet())
        {
            T user = test.getDatasetElement(userID);

            // Skip null users and users not present in the model
            if (training.get(userID) == null)
            {
                Logger.warn("User" + userID + " is present in test set but not in train");
                continue;
            }

            T trainUser = training.get(userID);

            for (int movieID : user.keySet())
            {
                DoubleElement movie = user.getDatasetElement(movieID);
                DoubleElement trainMovie = trainUser.getDatasetElement(movieID);

                if (trainMovie == null)
                {
                    Logger.warn("Movie" + movieID + " of User"
                            + userID + " is present in test set but not in train");
                    continue;
                }
                else if (isLowerLimitSet() && trainMovie.getDoubleValue() < getLowerLimit()
                        || isUpperLimitSet() && trainMovie.getDoubleValue() > getUpperLimit())
                {
                    throw new IllegalStateException("Value not permitted: " +
                            trainMovie.getDoubleValue() + " for entry[" + userID + "][" + movieID + "]");
                }

                // Obtain a movie from the testset
                testArray.add(movie.getDoubleValue());
                // Obtain the corresponding rating provided by training set
                trainArray.add(trainMovie.getDoubleValue());
            }
        }

        if (trainArray.size() == 0 || testArray.size() == 0)
        {
            throw new IllegalStateException("The arrays should not be empty");
        }

        // Fist the estimator, than the parameter
        return new org.apache.commons.math3.util.Pair<>(trainArray.toArray(new Double[0]),
                testArray.toArray(new Double[0]));
    }
}
