package it.unica.foresee.tests;

import it.unica.foresee.datasets.*;
import it.unica.foresee.libraries.NearestNeighbour;
import it.unica.foresee.libraries.RMSE;
import it.unica.foresee.utils.Converter;
import it.unica.foresee.utils.Logger;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests the predictions algorithms.
 */
public class PredictionsTest
{

    public final double DEVIATION = 0.3;
    public final String BIG_DATASET = "test-data/big.dat";
    public final String MEDIUM_DATASET = "test-data/medium.dat";
    public final String SMALL_DATASET = "test-data/small.dat";

    private MovielensLoader mLoader;
    private File mFile;
    private Movielens m;
    private DatasetSparseVector<MovielensElement>[] parts;
    private int numPart;
    private int neighboursAmount;


    @Before
    public void setUp()  throws Exception
    {
        mLoader = new MovielensLoader();
        mFile = new File(MEDIUM_DATASET);
        m = mLoader.loadDataset(mFile);
        numPart = 5;
        parts = m.getKFoldPartitions(numPart);
        neighboursAmount = 50;
        Logger.setVerbosity(Logger.VERB_DEBUG);
    }

    @Test
    public void loadDataset()
    {
        assertNotEquals(m, null);
    }

    @Test
    public void loadDatasetUsers()
    {
        for(MovielensElement e : m)
        {
            assertNotEquals(e, null);
        }
    }

    @Test
    public void testRMSE()
    {
        DatasetSparseVector<MovielensElement> testSet = null;
        DatasetNestedSparseVector<MovielensElement> trainingSet;

        NearestNeighbour<MovielensElement> predictioner;

        // Run the tests k times
        for (int i = 0; i < numPart; i++)
        {
            Logger.log("Validation #" + (i + 1));
            // reset the training set
            trainingSet = new DatasetNestedSparseVector<>();

            // Initialize training and test set
            for (int j = 0; j < numPart; j++)
            {
                if (i == j)
                {
                    testSet = parts[i];
                }
                else
                {
                    assertNotEquals("The training set cannot be empty.", 0, parts[j].keySet().size());
                    for(int k : parts[j].keySet())
                    {
                        trainingSet.put(k, new MovielensElement(parts[j].get(k).deepClone()));
                    }
                    assertNotEquals("The training set cannot be empty.", 0, trainingSet.keySet().size());
                }
            }


            Logger.log("The sets are ready");

            assertEquals(testSet.keySet(), trainingSet.keySet());

            assertNotEquals("The training set cannot be empty.", 0, trainingSet.keySet().size());

            Logger.log("Starting personal predictions..");
            predictioner = new NearestNeighbour<>(trainingSet);
            trainingSet = (DatasetNestedSparseVector<MovielensElement>) predictioner.makeForecasts(neighboursAmount);
            Logger.log("Predictions complete.");

            // Check that every element has now the same size
            for (int key: trainingSet.keySet())
            {
                int vectorSize = trainingSet.getHighestNestedKey();
                assertTrue("Expected: " + trainingSet.get(key).lastKey() + " <= " +
                        vectorSize, trainingSet.get(key).lastKey() <= vectorSize);
            }
/*
            assertNotEquals("The training set cannot be empty.", 0, trainingSet.keySet().size());

            Logger.log("Starting clustering..");
            List<List<MovielensElement>> clusters = (new ClusterableElement<MovielensElement>()).cluster(trainingSet, 500);
            Logger.log("Clustering complete.");

            Logger.log("Starting modelling..");
            GroupModel<MovielensElement> model = new GroupModel<>();
            List<MovielensElement> modelsList = model.averageStrategy(clusters);
            Logger.log("Modelling complete.");

            assertEquals(trainingSet.keySet(), model.getUserToModelMap().keySet());

            assertNotEquals("The models list cannot be null.", null, modelsList);
*/
            RMSE rmseTester = new RMSE();
            Pair<Double[], Double[]> comparableArrays = (new Converter<MovielensElement>()).getRMSEArrays(testSet,
                    trainingSet);
/*
            Pair<Double[], Double[]> comparableArrays = rmseTester.getRMSEArrays(testSet,
                    modelsList,
                    model.getUserToModelMap());
*/


            double result = rmseTester.calculate(comparableArrays.getFirst(), comparableArrays.getSecond());

            Logger.log("RMSE result #" + (i + 1) + ": " + result);

            trainingSet = null;
            testSet = null;
        }
    }
}
