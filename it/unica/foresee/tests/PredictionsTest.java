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
    public final String BIG_DATASET = "test-data/ratings.dat";
    public final String SMALL_DATASET = "test-data/movielens-test-25-users.dat";

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
        mFile = new File(BIG_DATASET);
        m = mLoader.loadDataset(mFile);
        numPart = 5;
        parts = m.getKFoldPartitions(numPart);
        Logger.setVerbosity(Logger.VERB_NO_WARN);
        neighboursAmount = 50;
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
                    trainingSet.putAll(parts[j]);
                }
            }

            predictioner = new NearestNeighbour<>(trainingSet);
            trainingSet = (DatasetNestedSparseVector<MovielensElement>) predictioner.makePredictions(neighboursAmount);
            List<List<MovielensElement>> clusters = (new ClusterableElement<MovielensElement>()).cluster(trainingSet, 500);
            GroupModel<MovielensElement> model = new GroupModel<MovielensElement>();
            List<MovielensElement> modelsList = model.averageStrategy(clusters);

            RMSE<MovielensElement> rmseTester = new RMSE();

            Pair<Double[], Double[]> comparableArrays = rmseTester.getComparableArrays(testSet,
                    modelsList,
                    model.getUserToModelMap());

            double result = rmseTester.calculate(comparableArrays.getFirst(), comparableArrays.getSecond());

            System.out.println("RMSE result: " + result);

        }
    }
}
