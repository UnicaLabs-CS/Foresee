package it.unica.foresee.tests;

import it.unica.foresee.datasets.*;
import it.unica.foresee.utils.Converter;

import org.apache.commons.math3.util.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Checks the correct execution of the conversions algorithms.
 */
public class ConverterTest
{
    Movielens dataset1;
    Movielens dataset2;
    double[][] usersMatrix1;

    /**
     * Fill a movielens dataset from a matrix
     * @param usersMatrix a matrix of values
     * @return a dataset
     */
    public Movielens fillDataset(double[][] usersMatrix)
    {
        Movielens dataset = new Movielens();
        DatasetSparseVector<DoubleElement> user = new MovielensElement();

        // Create a fake dataset
        for(int i = 0; i < usersMatrix.length; i++)
        {
            user = new MovielensElement();
            user.setId(i);

            for(int j = 0; j < usersMatrix[i].length; j++)
            {
                user.put(j, new DoubleElement(usersMatrix[i][j]));
            }

            dataset.put(i, (MovielensElement) user);
        }

        return dataset;
    }

    /**
     * Make a list of the elements in a dataset.
     * @param dataset
     * @return
     */
    public List<MovielensElement> datasetToList(Movielens dataset)
    {
        ArrayList<MovielensElement> list = new ArrayList<>();
        for(int index : dataset.keySet())
        {
            list.add(dataset.get(index));
        }
        return list;
    }

    /**
     * Flatten an array.
     * @param matrix a matrix of doubles
     * @return the linearized given matrix
     */
    public Double[] flatten(double[][] matrix)
    {
        int linearLength = 0;
        // Determine the length of the linearized array
        for (double[] array : matrix)
        {
            linearLength += array.length;
        }

        Double[] linear = new Double[linearLength];

        // array index
        int i = 0;
        for (double[] array : matrix)
        {
            for(double el : array)
            {
                // Assign the element and update the index
                linear[i] = el;
                i++;
            }
        }

        return linear;
    }

    @Before
    public void setUp()
    {
        // Initialize some values
        usersMatrix1 = new double[][]{
                new double[]{0, 0, 3},
                new double[]{0, 1, 0},
                new double[]{5, 0, 0}
        };


        // Initialize some values
        double[][] usersMatrix2 = new double[][]{
                new double[]{0, 0, 3},
                new double[]{0, 1, 0},
                new double[]{5, 0, 0}
        };

        dataset1 = (Movielens) fillDataset(usersMatrix1);

    }

    @Test
    public void testDatasetSparseVectorToArrayPair()
    {
        Converter<MovielensElement> converter = new Converter<>();
        Pair<Double[], Double[]> arrays = converter.getRMSEArrays(dataset1, dataset1);
        assertArrayEquals(arrays.getFirst(), arrays.getSecond());
        assertArrayEquals(arrays.getFirst(), flatten(usersMatrix1));
    }

    @Test
    public void testDatasetAndModelToArrayPair()
    {
        Converter<MovielensElement> converter = new Converter<>();
        List<MovielensElement> model = datasetToList(dataset1);
        Map<Integer, Integer> userToModel = new HashMap<>();
        for(int index : dataset1.keySet())
        {
            userToModel.put(index, index);
        }
        Pair<Double[], Double[]> arrays = converter.getRMSEArrays(dataset1, model, userToModel);
        assertArrayEquals(arrays.getFirst(), arrays.getSecond());
        assertArrayEquals(arrays.getFirst(), flatten(usersMatrix1));
    }
}
