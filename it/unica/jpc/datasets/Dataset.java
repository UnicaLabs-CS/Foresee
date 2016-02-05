package it.unica.jpc.datasets;

import java.util.ArrayList;
import java.io.File;

/**
 * Represents a generic collection of data.
 *
 * This class implements an algorithm of stratified k-fold cross-validation.
 *
 * @author Fabio Colella
 */
public abstract class Dataset
{
    /**
     * Stores the dataset.
     *
     * The use of Vector instead of ArrayList is
     * motivated by it's tread-safeness.
     */
    ArrayList<Triple> dataset;

    /**
     * Amount of movie rates.
     */
    int movieRatesAmount;

    /**
     * Amount of user rates.
     */
    int userRatesAmount;

    /**
     * Creates a dataset with default values.
     */
    public Dataset()
    {
        this.movieRatesAmount = 0;
        this.userRatesAmount = 0;
    }

    /**
     * Creates a dataset from the given source file.
     *
     * @param sourceFile the file from which to load the data
     */
    public Dataset(File sourceFile)
    {
        this.loadDataset(sourceFile);
    }

    /**
     * Loads data from a specified source.
     *
     * @param sourceFile the file from which to load the data
     * @return true if the loading completed without errors
     */
    public abstract boolean loadDataset(File sourceFile);

    /**
     * Gets the test set of a k-fold cross-validation.
     *
     * @param layersAmount the number of layers for the stratified k-fold algorithm
     *                     {@literal (Precondition: layersAmount >= 1) }
     *                     A layerAmount equal to 1 means you're doing a non
     *                     stratified k-fold.
     * @param testPecentage percentage of the dataset that belong to the test set
     *                      {@literal (Precondition: testPercentage >= 0) }
     * @return the elements contained in the test set
     */
    public abstract ArrayList<String> getTestSet(int layersAmount, double testPecentage);

    /**
     * Gets the partition of the dataset.
     *
     * This method is an implementation of the stratified k-fold cross validation.
     *
     * @param k the number of partitons to obtain from the dataset
     *          {@literal (Precondition: k >= 1) }
     *          A k value equal to 1 means that no partitioning is being done.
     *          A good value for k is 10, as determined by various studies.
     *
     * @param layersAmount the number of layers for the stratified k-fold algorithm
     *                     {@literal (Precondition: layersAmount >= 1) }
     *                     A layerAmount equal to 1 means you're doing a non
     *                     stratified k-fold.
     * @return an array of partitions
     */
    public abstract ArrayList<Triple>[] getDatasetPartition(int k, int layersAmount);

    /**
     * Saves all the partitions in the given directory.
     *
     * @param testSetIndex the index of the partition containing the test set
     *                     {@literal (Precondition: testSetIndex >= 0
     *                     && testSetIndex <= partitionsAmount - 1 }
     * @param savePath the path where the partitions are going to be saved
     *                 A null path is intended as the current working directory.
     */
    public abstract void savePartitions(int testSetIndex, String savePath);
}