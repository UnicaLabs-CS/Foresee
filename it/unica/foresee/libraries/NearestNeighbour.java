package it.unica.foresee.libraries;

import org.apache.commons.math3.ml.clustering.Clusterable;

import java.util.List;

/**
 * This class is an implementation of the nearest neighbour algorithm for user similarity.
 */
public class NearestNeighbour<T extends Clusterable>
{
    private List<T> users;

    private double prediction(T user, List<T> neighbours)
    {
        return 0;
    }

    private double userSimilarity(T user, T neighbour)
    {
        return 0;
    }

    public List<T> getResults()
    {
        return this.users;
    }

    


}
