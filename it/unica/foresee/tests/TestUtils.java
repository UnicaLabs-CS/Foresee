package it.unica.foresee.tests;

import it.unica.foresee.datasets.DatasetSparseVector;
import it.unica.foresee.datasets.DoubleElement;
import it.unica.foresee.datasets.Movielens;
import it.unica.foresee.datasets.MovielensElement;
import it.unica.foresee.utils.SparseMatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Utility class for tests.
 */
public class TestUtils
{
    /**
     * Make a list of the elements in a dataset.
     * @param dataset
     * @return
     */
    public static List<MovielensElement> datasetToList(Movielens dataset)
    {
        ArrayList<MovielensElement> list = new ArrayList<>();
        for(int index : dataset.keySet())
        {
            list.add(dataset.get(index));
        }
        return list;
    }

    /**
     * Fill a movielens element from an array
     * @param usersVector an array of values
     * @return a dataset entry
     */
    public static MovielensElement fillDatasetEntry(double[] usersVector)
    {
        MovielensElement user = new MovielensElement();
        user.setVectorSize(usersVector.length);
        for(int j = 0; j < usersVector.length; j++)
        {
            user.put(j, usersVector[j]);
        }
        return user;
    }

    /**
     * Fill a movielens dataset from a matrix
     * @param usersMatrix a matrix of values
     * @return a dataset
     */
    public static Movielens fillDataset(double[][] usersMatrix)
    {
        Movielens dataset = new Movielens();
        MovielensElement user;

        // Create a fake dataset
        for(int i = 0; i < usersMatrix.length; i++)
        {
            user = new MovielensElement();
            user.setId(i);
            user.setVectorSize(usersMatrix[i].length);

            for(int j = 0; j < usersMatrix[i].length; j++)
            {
                user.put(j, usersMatrix[i][j]);
            }

            dataset.put(i, user);
        }

        return dataset;
    }

    /**
     * Flatten an array.
     * @param matrix a matrix of doubles
     * @return the linearized given matrix
     */
    public static Double[] flatten(double[][] matrix)
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

    /**
     * Print a double matrix.
     * @param matrix
     */
    public static void printDoubleMatrix(double[][] matrix)
    {
        for(double[] line : matrix)
        {
            System.out.print("[");
            for (double el: line)
            {
                System.out.print(el + " ");
            }
            System.out.println("]");
        }
    }

    /**
     * Print a double matrix.
     * @param matrix
     */
    public static void printSparseMatrix(SparseMatrix matrix)
    {
        for(HashMap<Integer, Double> line : matrix.getInternalMap().values())
        {
            System.out.print("[");
            for (double el: line.values())
            {
                System.out.print(el + " ");
            }
            System.out.println("]");
        }
    }

    /**
     * Print a movielens.
     * @param matrix
     */
    public static void printDataset(DatasetSparseVector<MovielensElement> matrix)
    {
        for(MovielensElement line : matrix.values())
        {
            System.out.print("[");
            for (DoubleElement el: line.values())
            {
                System.out.print(el.getDoubleValue() + " ");
            }
            System.out.println("]");
        }
    }
}
