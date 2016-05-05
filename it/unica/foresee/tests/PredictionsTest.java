package it.unica.foresee.tests;

import it.unica.foresee.datasets.*;
import it.unica.foresee.datasets.interfaces.NumberElement;
import it.unica.foresee.libraries.ClusterableElement;
import it.unica.foresee.libraries.GroupModel;
import it.unica.foresee.libraries.NearestNeighbour;
import it.unica.foresee.libraries.RMSE;
import it.unica.foresee.utils.Logger;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotEquals;

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
        Logger.setVerbosity(Logger.VERB_ALL);
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

    private List<CentroidCluster<MovielensElement>> clusteringAll()
    {
        List<CentroidCluster<MovielensElement>> clusterResults = null;
        int vectorSize = m.getMaxMovieID() + 1;
        System.out.println("Vector size: " + vectorSize);

        System.out.println("\n\nclustering:");
        for(int i = 0; i < parts.length; i++)
        {
            // Transform the partition in an ArrayList
            ArrayList<MovielensElement> pElements = new ArrayList<>();
            for (Integer key : parts[i].keySet())
            {
                //System.out.println("ArrayList size: " + pElements.size());
                while (pElements.size() <= key){pElements.add(new MovielensElement(vectorSize));}
                parts[i].get(key).setVectorSize(vectorSize);
                pElements.add(key, parts[i].get(key));
            }

            KMeansPlusPlusClusterer<MovielensElement> clusterer = new KMeansPlusPlusClusterer<>(20);
            clusterResults = clusterer.cluster(pElements);

            /*
            System.out.println("\n\npartition " + i);
            for (int j = 0; j < clusterResults.size(); j++)
            {
                System.out.println("cluster " + j);
                System.out.println(clusterResults.get(j).getPoints());
            }
            */
        }
        return clusterResults;
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
                    if(parts[j].keySet().size() == 0)
                    {
                        throw new IllegalStateException("The parts cannot be empty.");
                    }
                    trainingSet.putAll(parts[j]);
                    if(trainingSet.keySet().size() == 0)
                    {
                        throw new IllegalStateException("The training set cannot be empty.");
                    }
                }
            }

            if(trainingSet.keySet().size() == 0)
            {
                throw new IllegalStateException("The training set cannot be empty. Iteration");
            }
            predictioner = new NearestNeighbour<>(trainingSet);
            trainingSet = (DatasetNestedSparseVector<MovielensElement>) predictioner.makePredictions(neighboursAmount);

            if(trainingSet.keySet().size() == 0)
            {
                throw new IllegalStateException("The training set cannot be empty.");
            }

            List<List<MovielensElement>> clusters = (new ClusterableElement<MovielensElement>()).cluster(trainingSet, 500);
            GroupModel<MovielensElement> model = new GroupModel<>();
            List<MovielensElement> modelsList = model.averageStrategy(clusters);

            if(modelsList == null)
            {
                throw new IllegalStateException("The models list cannot be null.");
            }

            RMSE<MovielensElement> rmseTester = new RMSE();

            Pair<Double[], Double[]> comparableArrays = rmseTester.getComparableArrays(testSet,
                    modelsList,
                    model.getUserToModelMap());

            double result = rmseTester.calculate(comparableArrays.getFirst(), comparableArrays.getSecond());

            System.out.println("RMSE result: " + result);
        }
    }
}
