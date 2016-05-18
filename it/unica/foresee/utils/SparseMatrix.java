package it.unica.foresee.utils;

import java.util.HashMap;

/**
 * Fast and efficient implementation of a sparse matrix.
 */
public class SparseMatrix
{
    /**
     * Internal Map to store and retrieve the elements.
     */
    private HashMap<Integer, HashMap<Integer, Double>> internalMap = new HashMap<>();

    /**
     * Double indexed get.
     * @param i
     * @param j
     * @return
     */
    public Double get(Integer i, Integer j)
    {
        HashMap<Integer, Double> map = internalMap.get(i);
        if(map == null)
        {
            return null;
        }
        else
        {
            return map.get(j);
        }
    }

    /**
     * Get the internal map.
     * @return
     */
    public HashMap<Integer, HashMap<Integer, Double>> getInternalMap() {
        return internalMap;
    }

    /**
     * Double indexed put.
     * @param i
     * @param j
     * @param value
     * @return
     */
    public Double put(Integer i, Integer j,  Double value) {
        HashMap<Integer, Double> map = internalMap.get(i);
        if(map == null)
        {
            map = new HashMap<>();
        }
        internalMap.put(i, map);
        return map.put(j, value);
    }

    /**
     * Double indexed symmetric get.
     *
     * This method assumes that the matrix is symmetric.
     *
     * It means that the element will be retrieved first
     * with the indexes straight, and then inverted if the
     * first attempt returned null.
     *
     * @param i
     * @param j
     * @return
     */
    public Double symmetricGet(Integer i, Integer j)
    {
        Double value = get(i, j);
        // If value is null, return the value of
        return value != null ? value : get(j, i);
    }

    /**
     * Double indexed symmetric put.
     *
     * It means that the element will be put both
     * with the indexes straight and inverted.
     * @param i
     * @param j
     * @param value
     * @return
     */
    public Double symmetricPut(Integer i, Integer j,  Double value)
    {
        put(i, j, value);
        return put(j, i, value);
    }


}
