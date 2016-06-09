package it.unica.foresee.tests;

import it.unica.foresee.datasets.DatasetSparseVector;
import it.unica.foresee.datasets.Movielens;
import it.unica.foresee.datasets.MovielensElement;
import it.unica.foresee.libraries.NearestNeighbour;

import it.unica.foresee.utils.Logger;
import it.unica.foresee.utils.SparseMatrix;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Checks the correct execution of the Nearest Neighbours algorithm.
 *
 * Some spreadsheet file are provided to help preparing tests.
 */
public class NearestNeighboursTest
{
    public static double HIGH_ACCURACY = 0.0000001;
    public static double SMALL_ACCURACY = 0.15;

    @Before
    public void setUp()
    {
        // Set the verbosity to DEBUG level
        Logger.setVerbosity(Logger.VERB_DEBUG);
    }

    public void assertCorrectSimilarity(SparseMatrix similarityMatrix, double[][] simResults, double accuracy)
    {
        for (int i : similarityMatrix.getInternalMap().keySet())
        {
            // Check that the value for the value itself is maximum
            assertEquals(similarityMatrix.get(i, i), 1.0, HIGH_ACCURACY);
            for (int j : similarityMatrix.getInternalMap().get(i).keySet())
            {
                assertEquals(similarityMatrix.get(i, j), simResults[i][j], accuracy);
            }
        }
    }

    @Test
    public void similarityMatrixTest1()
    {
        // Initialize some values
        double[][] usersMatrix1 = new double[][]{
                new double[]{5, 5, 3},
                new double[]{5, 5, 3},
                new double[]{0, 0, 0}
        };

        double[][] simResults1 = new double[][]{
                new double[]{1.0, 1.0, 0.0},
                new double[]{1.0, 1.0, 0.0},
                new double[]{0.0, 0.0, 1.0}
        };

        Movielens dataset1 = TestUtils.fillDataset(usersMatrix1);

        NearestNeighbour<Movielens> nearestNeighbour = new NearestNeighbour<>(dataset1);
        nearestNeighbour.initialiseSimilarityMatrix();
        SparseMatrix similarityMatrix = nearestNeighbour.getSimilarityMatrix();
        Logger.log("Similarity Matrix 1:");
        TestUtils.printSparseMatrix(similarityMatrix);
        assertCorrectSimilarity(similarityMatrix, simResults1, HIGH_ACCURACY);
    }

    @Test
    public void similarityMatrixTest2()
    {
        double[][] usersMatrix2 = new double[][]{
                new double[]{5, 5, 3},
                new double[]{5, 5, 2},
                new double[]{5, 4, 4}
        };

        double[][] simResults2 = new double[][]{
                new double[]{1.0, 1.0, 0.5},
                new double[]{1.0, 1.0, 0.5},
                new double[]{0.5, 0.5, 1.0}
        };

        Movielens dataset2 = TestUtils.fillDataset(usersMatrix2);

        NearestNeighbour<Movielens> nearestNeighbour = new NearestNeighbour<>(dataset2);
        nearestNeighbour.initialiseSimilarityMatrix();
        SparseMatrix similarityMatrix = nearestNeighbour.getSimilarityMatrix();
        Logger.log("Similarity Matrix 2:");
        TestUtils.printSparseMatrix(similarityMatrix);
        assertCorrectSimilarity(similarityMatrix, simResults2, SMALL_ACCURACY);
    }

    @Test
    public void similarityMatrixTest3()
    {
        double[][] usersMatrix = new double[][]{
                new double[]{5, 5, 3, 0, 4, 5, 3},
                new double[]{5, 5, 2, 0, 4, 5, 3},
                new double[]{5, 4, 4, 0, 3, 5, 3}
        };

        double[][] simResults = new double[][]{
                new double[]{1.0, 0.980454, 0.924986},
                new double[]{0.980454, 1.0, 0.852223},
                new double[]{0.924986, 0.852223, 1.0}
        };

        Movielens dataset = TestUtils.fillDataset(usersMatrix);

        NearestNeighbour<Movielens> nearestNeighbour = new NearestNeighbour<>(dataset);
        nearestNeighbour.initialiseSimilarityMatrix();
        SparseMatrix similarityMatrix = nearestNeighbour.getSimilarityMatrix();
        Logger.log("Similarity Matrix 3:");
        TestUtils.printSparseMatrix(similarityMatrix);
        assertCorrectSimilarity(similarityMatrix, simResults, SMALL_ACCURACY);
    }

    @Test
    public void getNearestNeighboursTest()
    {
        double[][] usersMatrix = new double[][]{
                new double[]{5, 5, 3, 0, 4, 5, 3}, // Sample
                new double[]{5, 5, 2, 0, 4, 5, 3}, // Nearest neighbours
                new double[]{5, 5, 2, 0, 4, 5, 3},
                new double[]{5, 5, 2, 0, 4, 5, 3},
                new double[]{5, 5, 2, 0, 4, 5, 3},
                new double[]{5, 5, 2, 0, 4, 5, 3},
                new double[]{5, 5, 2, 0, 4, 5, 3},
                new double[]{5, 5, 2, 0, 4, 5, 3}, // last near neighbour
                new double[]{5, 4, 4, 0, 3, 5, 3}, // Far neighbours
                new double[]{5, 4, 4, 0, 3, 5, 3},
                new double[]{5, 4, 4, 0, 3, 5, 3},
                new double[]{5, 4, 4, 0, 3, 5, 3},
                new double[]{5, 4, 4, 0, 3, 5, 3},
                new double[]{5, 4, 4, 0, 3, 5, 3}
        };

        int elementToCheck = 0;
        int neighboursAmount = 7;
        int lastNearNeighbour = 7;

        Movielens dataset = TestUtils.fillDataset(usersMatrix);

        NearestNeighbour<Movielens> nearestNeighbour = new NearestNeighbour<>(dataset);
        nearestNeighbour.initialiseSimilarityMatrix();

        // Get the nearest neighbours to the element
        List<Pair<Integer, Double>> nearest = nearestNeighbour.getNearestNeighbours(elementToCheck, neighboursAmount);

        assertEquals(neighboursAmount, nearest.size());

        // Check that the neighbours are those we expect
        for (Pair<Integer, Double> el : nearest)
        {
            assertTrue("element " + el.getFirst() + "should be" +
                    " less than " + lastNearNeighbour, el.getFirst() <= lastNearNeighbour);
        }
    }


    @Test
    public void makePredictionsTest()
    {
        double[][] usersMatrix = new double[][]{
                new double[]{5, 5, 3, 1, 4, 5, 3}, // Sample 0
                new double[]{5, 5, 2, 1, 4, 5, 3}, // Nearest neighbours 1
                new double[]{5, 5, 2, 1, 4, 5, 3}, // 2
                new double[]{5, 5, 2, 1, 4, 5, 3}, // 3
                new double[]{5, 5, 2, 1, 4, 5, 3}, // 4
                new double[]{5, 5, 2, 1, 4, 5, 3}, // 5
                new double[]{5, 5, 2, 1, 4, 5, 3}, // 6
                new double[]{5, 5, 2, 1, 4, 5, 3}, // last near neighbour
                new double[]{5, 4, 4, 1, 3, 5, 3}, // Far neighbours
                new double[]{5, 4, 4, 1, 3, 5, 3}, // 9
                new double[]{5, 4, 4, 1, 3, 5, 3}, // 10
                new double[]{5, 4, 4, 1, 3, 5, 3}, // 11
                new double[]{5, 4, 4, 1, 3, 5, 3}, // 12
                new double[]{5, 4, 4, 1, 3, 5, 3}, // 13
                new double[]{5, 5, 3, 0, 4, 5, 3}  // user to predict 14
        };

        double[] predictionVector = new double[]{5, 5.0, 3.0, 0.88, 4.0, 5.0, 3.0};

        int elementToCheck = 14;
        int neighboursAmount = 100;

        Movielens dataset = TestUtils.fillDataset(usersMatrix);

        NearestNeighbour<Movielens> nearestNeighbour = new NearestNeighbour<>(dataset.deepClone());
        nearestNeighbour.initialiseSimilarityMatrix();

        DatasetSparseVector<MovielensElement> adulteratedDataset = nearestNeighbour.makeForecasts(neighboursAmount);

        TestUtils.printDataset(adulteratedDataset);

        assertNotEquals(adulteratedDataset.getDatasetElement(elementToCheck),
                dataset.getDatasetElement(elementToCheck)
        );

        assertEquals(dataset.getDatasetElement(elementToCheck).getVectorSize(),
                adulteratedDataset.getDatasetElement(elementToCheck).getVectorSize());

        assertArrayEquals(predictionVector, adulteratedDataset.getDatasetElement(elementToCheck).getPoint(), 0.2);
    }
}
