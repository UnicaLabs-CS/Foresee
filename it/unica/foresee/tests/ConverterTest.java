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

        dataset1 = TestUtils.fillDataset(usersMatrix1);

    }

    @Test
    public void testDatasetSparseVectorToArrayPair()
    {
        Converter<MovielensElement> converter = new Converter<>();
        Pair<Double[], Double[]> arrays = converter.getRMSEArrays(dataset1, dataset1);
        assertArrayEquals(arrays.getFirst(), arrays.getSecond());
        assertArrayEquals(arrays.getFirst(), TestUtils.flatten(usersMatrix1));
    }

    @Test
    public void testDatasetAndModelToArrayPair()
    {
        Converter<MovielensElement> converter = new Converter<>();
        List<MovielensElement> model = TestUtils.datasetToList(dataset1);
        Map<Integer, Integer> userToModel = new HashMap<>();
        for(int index : dataset1.keySet())
        {
            userToModel.put(index, index);
        }
        Pair<Double[], Double[]> arrays = converter.getRMSEArrays(dataset1, model, userToModel);
        assertArrayEquals(arrays.getFirst(), arrays.getSecond());
        assertArrayEquals(arrays.getFirst(), TestUtils.flatten(usersMatrix1));
    }
}
