package it.unica.foresee.libraries;

import it.unica.foresee.datasets.*;
import it.unica.foresee.datasets.DatasetNestedSparseVector;
import it.unica.foresee.datasets.interfaces.*;
import it.unica.foresee.datasets.interfaces.DatasetElement;

import java.util.List;
import java.util.Map;

/**
 * Dataset to abstract the use of a model instead of a standard dataset.
 */
public class GroupModelDataset<T extends DatasetSparseVector<? extends DatasetElement<?>>> extends DatasetNestedSparseVector<T>
{
    /**
     * Creates a new GroupModel dataset.
     *
     * @param modelMap the map (userID to model) produced by the {@link GroupModel} class
     * @param modelsList a list of movielens elements that model the users
     * @param dataset the original movielens dataset
     */
    public GroupModelDataset(Map<Integer, Integer> modelMap, List<T> modelsList, DatasetNestedSparseVector<T> dataset)
    {
        this.setId(dataset.getId());
        this.setVectorSize(dataset.getVectorSize());

        // Initialise the map using the models and mapping them to the respective users
        for (Integer key : dataset.keySet())
        {
            this.put(key, modelsList.get(modelMap.get(key)));
        }
    }
}
