package it.unica.foresee.tests;

import it.unica.foresee.datasets.DatasetSparseVector;
import it.unica.foresee.datasets.Movielens;
import it.unica.foresee.datasets.MovielensElement;
import it.unica.foresee.libraries.RMSE;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Checks the correct execution of the RMSE algorithm
 */
public class RMSETest
{
    public static double ACCURACY = 0.0000000000000001;
    private RMSE tester = new RMSE();

    @Test
    public void sameArrays()
    {
        double[] testArray = new double[]{1, 1, 1, 1};

        double result = tester.calculate(testArray, testArray);

        assertEquals(0, result, ACCURACY);
    }

    @Test
    public void oppositeArrays()
    {
        double[] testArray1 = new double[]{1, 1, 1, 1};
        double[] testArray2 = new double[]{4, 4, 4, 4};

        double result = tester.calculate(testArray1, testArray2);

        assertEquals(3, result, ACCURACY);
    }
}
