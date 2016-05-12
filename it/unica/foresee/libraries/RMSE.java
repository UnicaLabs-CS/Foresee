package it.unica.foresee.libraries;

import it.unica.foresee.utils.Logger;

/**
 * Root mean squared error calculator.
 */
public class RMSE
{
    /**
     * Performs the RMSE calculation for each item.
     * @param estimator the value that makes an estimate of a parameter
     * @param parameter
     * @return the RMSE
     */
    public double calculate (Double[] estimator, Double[] parameter)
    {
        double[] rawEstimator = new double[estimator.length];
        double[] rawParameter = new double[estimator.length];

        if (estimator.length != parameter.length)
        {
            throw new IllegalArgumentException("Estimator and parameter arrays need to have the same length");
        }

        // Covert to raw type
        for (int i = 0; i < estimator.length; i++)
        {
            rawEstimator[i] = estimator[i];
            rawParameter[i] = parameter[i];
        }

        return calculate(rawEstimator, rawParameter);
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

        if (estimator.length == 0)
        {
            throw new IllegalStateException("The arrays cannot be empty");
        }

        double numerator = 0;
        double diff;

        for (int i = 0; i < estimator.length; i++)
        {
            Logger.debug("RMSE: est[" + estimator[i] + "]" + " param[" + parameter[i] + "]");
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
        double[] rawEstimator = new double[estimator.length];
        double[] rawParameter = new double[estimator.length];

        if (estimator.length != parameter.length)
        {
            throw new IllegalArgumentException("Estimator and parameter arrays need to have the same length");
        }

        // Covert to raw type
        for (int i = 0; i < estimator.length; i++)
        {
            rawEstimator[i] = (double) estimator[i];
            rawParameter[i] = (double) parameter[i];
        }

        return calculate(rawEstimator, rawParameter);
    }
}
