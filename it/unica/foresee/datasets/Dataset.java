package it.unica.foresee.datasets;

import it.unica.foresee.datasets.interfaces.DatasetElement;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.TreeSet;


/**
 * Represents a generic collection of data.
 *
 * This class implements an algorithm of stratified k-fold cross-validation.
 *
 * @author Fabio Colella
 */
public abstract class Dataset<T extends DatasetElement> implements it.unica.foresee.datasets.interfaces.Dataset<T>
{




    /**
     * Saves all the partitions in the given directory.
     *
     * @param testSetIndex the index of the partition containing the test set
     *                     {@literal (Precondition: testSetIndex >= 0
     *                     && testSetIndex <= partitionsAmount - 1 }
     * @param savePath the path where the partitions are going to be saved
     *                 A null path is intended as the current working directory.
     */
    public void savePartitions(int testSetIndex, String savePath)
    {
        // Fill
    }
}