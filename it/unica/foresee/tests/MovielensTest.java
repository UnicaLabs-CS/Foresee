package it.unica.foresee.tests;

import it.unica.foresee.datasets.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;


/**
 * Tests the Movielens_deprecated class.
 *
 * @author: Fabio Colella
 */
public class MovielensTest
{
    private final boolean VERBOSE = false;
    /**
     * Simulates the partitioning of a dataset.
     * @param k number of partitions
     * @param layers number of layers
     */
    public void partitioning(int k, int layers) throws FileNotFoundException
    {
        File testData = new File("test-data/movielens-test-25-users.dat");
        Movielens_deprecated ml = new Movielens_deprecated(testData);

        ArrayList<Dataset.MovieRatesAmountPair>[] partitions = ml.getPartitions(k, layers);

        int sizeFirstPartition = partitions[0].size();
        double means[] = new double[k];
        int sumPartitions = 0;

        for (int i = 0; i < partitions.length; i++)
        {
            System.out.println("#" + i + ": -----------------------");
            System.out.println("Partition size: " + partitions[i].size());
            for (Dataset.MovieRatesAmountPair element : partitions[i])
            {
                //Decomment here to have verbose output
                if (VERBOSE) {System.out.println(element);}
                means[i] += element.getRatesAmount();
                assertTrue("Partitions should not have common elements.",
                        !partitions[(i + 1) % (k - 1)].contains(element)
                                && !partitions[(i + 2) % (k - 1)].contains(element));
            }

            means[i] /= partitions[i].size();
            sumPartitions += partitions[i].size();
            System.out.println("Mean " + i + ": " + means[i]);
            System.out.println("---------------------------\n\n");

            assertTrue("The partitions should have a similar size: first(" +
                    sizeFirstPartition + ") - compared(" + partitions[i].size() + ")",
                    partitions[i].size() <= sizeFirstPartition + layers
                            && partitions[i].size() >= sizeFirstPartition - layers);
        }

        /* Some statistics */

        /* Check the mean of the means of the partitions */
        double meansMean = 0;
        for (double mean : means)
        {
            meansMean += mean;
        }
        meansMean /= k;

        /* Check the variance */
        double[] variances = new double[k];
        for (int i = 0; i < k; i++)
        {
            variances[i] = Math.pow((meansMean - means[i]), 2.0);
        }

        double variancesMean = 0;
        for (double variance : variances)
        {
            variancesMean += variance;
        }
        variancesMean /= k;

        /* Output the statistics */
        System.out.println("Statistical data:");
        for (int i = 0; i < k; i++)
        {
            System.out.print("Partition:" + i);
            System.out.print("\t mean:" + means[i]);
            System.out.print("  \t variance:" + variances[i] + "\n\n");
        }
        System.out.println("Average mean:           \t" + meansMean);
        System.out.println("Average variance:       \t" + variancesMean);
        System.out.println("Partitions:             \t" + k);
        System.out.println("Layers (stratification):\t" + layers + "\n\n\n");
    }

    /**
     * Tests the loading of data from example data and throwing error because of too few users.
     */
    @Test
    public void toofewUsersTest() throws FileNotFoundException
    {
        File testData = new File("test-data/movielens-test-20-users.dat");
        try
        {
            Movielens_deprecated ml = new Movielens_deprecated(testData);
        }
        catch (IllegalStateException e)
        {
            assertEquals("The amount of users is lower than 20.", e.getMessage());
        }
    }

    /**
     * Tests the loading of data from example data and throwing error because of rating lower than one.
     */
    @Test
    public void tooLowerRating() throws FileNotFoundException
    {
        File testData = new File("test-data/movielens-test-mismatch-less.dat");
        try
        {
            Movielens_deprecated ml = new Movielens_deprecated(testData);
        }
        catch (InputMismatchException e)
        {
            assertEquals("rating < 1 at line 5", e.getMessage());
        }
    }

    /**
     * Tests the loading of data from example data and throwing error because of malformed line.
     */
    @Test
    public void malformedLine() throws FileNotFoundException
    {
        File testData = new File("test-data/movielens-test-mismatch-malformed-line.dat");
        try
        {
            Movielens_deprecated ml = new Movielens_deprecated(testData);
        }
        catch (InputMismatchException e)
        {
            assertEquals("expected rating at line 5", e.getMessage());
        }
    }

    /**
     * Tests the partitioning without layering
     */
    @Test
    public void correctPartitioning() throws FileNotFoundException
    {
        int k = 5;
        int layers = 1;
        System.out.println("Test #1");
        partitioning(k, layers);
    }

    @Test
    public void correctPartitioningLayered() throws FileNotFoundException
    {
        int k = 5;
        int layers = 3;
        System.out.println("Test #2");
        partitioning(k, layers);
    }

    @Test
    public void bestPartitioningLayered() throws FileNotFoundException
    {
        int k = 10;
        int layers = 3;
        System.out.println("Test #3");
        partitioning(k, layers);
    }

    @Test
    public void checkItemsAmountNoLayering() throws FileNotFoundException
    {
        File testData = new File("test-data/movielens-test-25-users.dat");
        Movielens_deprecated ml = new Movielens_deprecated(testData);
        int k = 5;
        int layers = 1;

        ArrayList<Dataset.MovieRatesAmountPair>[] partitions = ml.getPartitions(k, layers);
        int totalOfThePartitions = 0;

        for (ArrayList<Dataset.MovieRatesAmountPair> partition : partitions)
        {
            totalOfThePartitions += partition.size();
        }

        assertEquals("The sum of the partitions should contain all the movies.",
                ml.getMoviesAmount(),
                totalOfThePartitions);
    }

    @Test
    public void checkItemsAmountWithLayering() throws FileNotFoundException
    {
        File testData = new File("test-data/movielens-test-25-users.dat");
        Movielens_deprecated ml = new Movielens_deprecated(testData);
        int k = 5;
        int layers = 3;

        ArrayList<Dataset.MovieRatesAmountPair>[] partitions = ml.getPartitions(k, layers);
        int totalOfThePartitions = 0;

        for (ArrayList<Dataset.MovieRatesAmountPair> partition : partitions)
        {
            totalOfThePartitions += partition.size();
        }

        assertEquals("The sum of the partitions should contain all the movies.",
                ml.getMoviesAmount(),
                totalOfThePartitions);
    }
}