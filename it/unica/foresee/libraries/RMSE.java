package it.unica.foresee.libraries;

import it.unica.foresee.datasets.DatasetSparseVector;
import it.unica.foresee.datasets.DoubleElement;
import org.apache.commons.math3.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Root mean squared error calculator.
 */
public class RMSE<T extends DatasetSparseVector<? extends DoubleElement>>
{

    /**
     * Creates two arrays that can be compared with the RMSE test.
     *
     * @param test the test set
     * @param model the training set
     * @param userToModel a map to obtain the corresponding user from the model
     * @return a pair of arrays that can be compared through the RMSE test
     */
    public Pair<Double[], Double[]> getComparableArrays(DatasetSparseVector<T> test,
                                                        List<T> model,
                                                        Map<Integer, Integer> userToModel)
    {
        ArrayList<Double> testArray = new ArrayList<Double>();
        ArrayList<Double> trainArray = new ArrayList<>();

        // Visit each user of the test set, then the corresponding
        // from the training set
        for(int userID : test.keySet())
        {
            T user = test.getDatasetElement(userID);
            for (int movieID : user.keySet())
            {
                DoubleElement movie = user.getDatasetElement(movieID);

                // Obtain a movie from the testset
                testArray.add(movie.getDoubleValue());
                // Obtain the corresponding rating provided by training set
                trainArray.add(model.get(userToModel.get(userID)).get(movieID).getDoubleValue());
            }
        }

        return new Pair<>(trainArray.toArray(new Double[0]),
                testArray.toArray(new Double[0]));
    }

    /**
     * Performs the RMSE calculation for each item.
     * @param estimator the value that makes an estimate of a parameter
     * @param parameter
     * @return the RMSE
     */
    public double calculate (Double[] estimator, Double[] parameter)
    {
        if (estimator.length != parameter.length)
        {
            throw new IllegalArgumentException("Estimator and parameter arrays need to have the same length");
        }

        double numerator = 0;
        double diff;

        for (int i = 0; i < estimator.length; i++)
        {
            diff = (estimator[i] - parameter[i]);
            numerator += diff * diff ;
        }

        return Math.sqrt(numerator / estimator.length);
    }

    /**
     * Performs the RMSE calculation for each item.
     * @param estimator the value that makes an estimate of a parameter
     * @param parameter
     * @return the RMSE
     */
    public double calculate (double[] estimator, double[] parameter)
    {
        if (estimator.length != parameter.length)
        {
            throw new IllegalArgumentException("Estimator and parameter arrays need to have the same length");
        }

        double numerator = 0;
        double diff;

        for (int i = 0; i < estimator.length; i++)
        {
            diff = (estimator[i] - parameter[i]);
            numerator += diff * diff ;
        }

        return Math.sqrt(numerator / estimator.length);
    }

    /**
     * Performs the RMSE calculation for each item.
     * @param estimator the value that makes an estimate of a parameter
     * @param parameter
     * @return the RMSE
     */
    public double calculate (int[] estimator, int[] parameter)
    {
        if (estimator.length != parameter.length)
        {
            throw new IllegalArgumentException("Estimator and parameter arrays need to have the same length");
        }

        int numerator = 0;
        int diff;

        for (int i = 0; i < estimator.length; i++)
        {
            diff = (estimator[i] - parameter[i]);
            numerator += diff * diff ;
        }

        return Math.sqrt(numerator / estimator.length);
    }
}
