package it.unica.foresee.libraries;

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
