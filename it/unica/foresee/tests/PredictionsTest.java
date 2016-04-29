package it.unica.foresee.tests;

import it.unica.foresee.datasets.DatasetSparseVector;
import it.unica.foresee.datasets.Movielens;
import it.unica.foresee.datasets.MovielensElement;
import it.unica.foresee.datasets.MovielensLoader;
import it.unica.foresee.libraries.NearestNeighbour;
import it.unica.foresee.utils.Tools;
import org.apache.commons.math3.ml.clustering.CentroidCluster;
import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        Tools.setVerbosity(Tools.VERB_NO_WARN);
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

    private List<CentroidCluster<MovielensElement>> clustering(DatasetSparseVector<MovielensElement> trainingSet)
    {
        List<CentroidCluster<MovielensElement>> clusterResults = null;
        int vectorSize = m.getMaxMovieID() + 1;
        System.out.println("Vector size: " + vectorSize);

        System.out.println("\n\nclustering:");

        // Transform the partition in an ArrayList
        ArrayList<MovielensElement> pElements = new ArrayList<>();
        for (Integer key : trainingSet.keySet())
        {
            //System.out.println("ArrayList size: " + pElements.size());
            while (pElements.size() <= key){pElements.add(new MovielensElement(vectorSize));}
            trainingSet.get(key).setVectorSize(vectorSize);
            pElements.add(key, trainingSet.get(key));
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

        return clusterResults;
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
        DatasetSparseVector<MovielensElement> testSet;
        DatasetSparseVector<MovielensElement> trainingSet;
        NearestNeighbour<MovielensElement> predictioner;

        // Run the tests k times
        for (int i = 0; i < numPart; i++)
        {
            // reset the training set
            trainingSet = new DatasetSparseVector<>();

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
            trainingSet = predictioner.makePredictions(neighboursAmount);
            List<CentroidCluster<MovielensElement>> clusters = clustering(trainingSet);




        }
    }
}