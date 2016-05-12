package it.unica.foresee.tests;

import it.unica.foresee.datasets.Movielens;
import it.unica.foresee.datasets.MovielensElement;
import org.junit.Before;
import org.junit.Test;

/**
 * Checks the correct execution of the Nearest Neighbours algorithm
 */
public class NearestNeighboursTest
{
    private Movielens dataset;
    private double[][] usersMatrix;

    @Before
    public void setUp()
    {
        // Initialize some values
        double[][] usersMatrix = new double[][]{
                new double[]{0, 0, 3},
                new double[]{0, 1, 0},
                new double[]{5, 0, 0}
        };

        MovielensElement user;

        // Create a fake dataset
        for(int i = 0; i < usersMatrix.length; i++)
        {
            user = new MovielensElement();
            user.setId(i);

            for(int j = 0; j < usersMatrix[i].length; j++)
            {
                user.put(j, usersMatrix[i][j]);
            }

            dataset.put(i, user);
        }
    }


}
