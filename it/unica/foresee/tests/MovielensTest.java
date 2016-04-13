package it.unica.foresee.tests;

import it.unica.foresee.datasets.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.ml.clustering.KMeansPlusPlusClusterer;
import org.apache.commons.math3.ml.clustering.CentroidCluster;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Test Movielens loading
 */
public class MovielensTest
{
    public final double DEVIATION = 0.3;
    public final String BIG_DATASET = "test-data/ratings.dat";
    public final String SMALL_DATASET = "test-data/movielens-test-25-users.dat";

    private MovielensLoader mLoader;
    private File mFile;
    private Movielens m;
    private DatasetSparseVector<MovielensElement>[] parts;
    private int numPart;

    @Before public void setUp()  throws Exception
    {
        mLoader = new MovielensLoader();
        mFile = new File(BIG_DATASET);
        m = mLoader.loadDataset(mFile);
        numPart = 5;
        parts = m.getKFoldPartitions(numPart);
    }

    private void clustering()
    {
        int vectorSize = m.getMaxMovieID() + 1;
        System.out.println("Vector size: " + vectorSize);

        System.out.println("\n\nclustering:");
        for(int i = 0; i < parts.length; i++)
        {
            // Transform the partition in an ArrayList
            ArrayList<MovielensElement> pElements = new ArrayList<>();
            for (Integer key : parts[i].keySet())
            {
                System.out.println("ArrayList size: " + pElements.size());
                while (pElements.size() <= key){pElements.add(new MovielensElement(vectorSize));}
                parts[i].get(key).setVectorSize(vectorSize);
                pElements.add(key, parts[i].get(key));
            }

            KMeansPlusPlusClusterer<MovielensElement> clusterer = new KMeansPlusPlusClusterer<>(20);
            List<CentroidCluster<MovielensElement>> clusterResults = clusterer.cluster(pElements);

            System.out.println("\n\npartition " + i);
            for (int j = 0; j < clusterResults.size(); j++)
            {
                System.out.println("cluster " + j);
                System.out.println(clusterResults.get(j).getPoints());
            }
        }
    }

    private int checkSameUsers(DatasetSparseVector<MovielensElement> a, DatasetSparseVector<MovielensElement> b)
    {
        DatasetSparseVector<MovielensElement> cloneA, cloneB;
        double avgA, avgB;
        int successes = 0;
        Set<Integer> keysList = ((DatasetSparseVector<MovielensElement>) a.clone()).keySet();
        Set<Integer> keysA = ((DatasetSparseVector<MovielensElement>) a.clone()).keySet();
        Set<Integer> keysB = ((DatasetSparseVector<MovielensElement>) b.clone()).keySet();

        // Make checks key by key
        for (Integer userID : keysList)
        {
            // Check if the userID is contained in both the partitions
            if (keysA.contains(userID))
            {
                successes++;

                // Remove the successes
                keysA.remove(userID);
                keysB.remove(userID);
            }

            if(a.get(userID) == null)
            {
                throw new IllegalStateException(userID + " userID cannot be null");
            }

        }
        System.out.println("The following users where not present in both the partitions:");
        System.out.println(keysA);
        System.out.println(keysB);
        System.out.println("Amount of misses: " + (keysA.size() + keysB.size()));
        System.out.println("Amount of successes: " + successes);

        return keysA.size() + keysB.size();
    }


    /**
     * Check a value to be in a specified range.
     * @param min
     * @param max
     * @param value the value to check
     */
    public void assertRange(double min, double max, double value)
    {
        assertTrue("Expected value in range between " + min
        + " and " + max + ". Found: " + value,
                min <= value && max >= value);
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
    public void kFoldNoyNull()
    {
        assertNotEquals(parts, null);
    }

    @Test
    public void usersAmount()
    {
        assertEquals(m.values().size(), m.getUsersAmount());
    }

    @Test
    public void getElement()
    {
        assertEquals((int) m.getElement(25, 1676), 4);
    }

    @Test
    public void assertEachUserIsRepresented()
    {
        int misses = 0;

        System.out.println("\nUsers check...");
        for (int i = 1; i < parts.length; i++)
        {
            System.out.println("## Comparing partition 0 and  " + i + " on " + (this.numPart - 1));
            misses = checkSameUsers(parts[0], parts[i]);
            assertTrue(misses < (parts[0].size() / 2));
        }

    }

    @Test
    public void assertMovieRepresentation()
    {
        int misses;

        TreeSet<Integer>[] moviesSets = new TreeSet[this.numPart];
        TreeSet<Integer> fullMovieSet = new TreeSet<>();
        for (int i = 0; i < this.numPart; i++)
        {
            // Obtain a key set of movies
            moviesSets[i] = new TreeSet<>();

            // Take all the movies from the i-esim partition
            for (MovielensElement moviesByUser : parts[i].values())
            {
                moviesSets[i].addAll(moviesByUser.keySet());
            }
        }

        for (Set s : moviesSets)
        {
            fullMovieSet.addAll(s);
        }

        System.out.println("\nMovies check...");
        // Now check the common movies by partition
        for (int i = 0; i < parts.length; i++)
        {
            System.out.println("## Comparing partition 0 and  " + i + " on " + (this.numPart - 1));
            int totalMoviesAmount = fullMovieSet.size();
            TreeSet<Integer> diff = (TreeSet<Integer>) fullMovieSet.clone();
            diff.removeAll(moviesSets[i]);
            misses = diff.size();
            System.out.println("Movie misses: " + misses + " on " + totalMoviesAmount);
            //System.out.println("Movies of " + i + " not present: " + diff);
        }

        assertEquals(m.getMoviesSet(), fullMovieSet);
    }
/*
    @Test
    public void testClustering()
    {
        clustering();
    }
*/
}


