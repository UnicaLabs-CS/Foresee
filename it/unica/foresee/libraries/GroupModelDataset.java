package it.unica.foresee.libraries;

import it.unica.foresee.datasets.DatasetSparseVector;
import it.unica.foresee.datasets.DoubleElement;
import it.unica.foresee.datasets.Movielens;
import it.unica.foresee.datasets.MovielensElement;

import java.util.List;
import java.util.Map;

/**
 * Dataset to abstract the use of a model instead of a standard dataset.
 */
public class GroupModelDataset extends Movielens
{
    Map<Integer, Integer> modelMap = null;
    List<MovielensElement> modelsList = null;
    int highestNestedKey;

    /**
     * Creates a new GroupModel dataset.
     *
     * @param modelMap the map produced by the {@link GroupModel} class
     * @param modelsList a list of movielens elements that model the users
     * @param dataset the original movielens dataset
     */
    public GroupModelDataset(Map<Integer, Integer> modelMap, List<MovielensElement> modelsList, Movielens dataset)
    {
        this.modelMap = modelMap;
        this.modelsList = modelsList;
        this.setId(dataset.getId());
        this.setVectorSize(dataset.getVectorSize());
        this.setDoubleValue(dataset.getDoubleValue());
        this.setMaxMovieID(this.getMaxUserID());
        this.setMaxUserID(this.getMaxUserID());
        this.setMoviesAmount(dataset.getMoviesAmount());
        this.setMoviesSet(dataset.getMoviesSet());
        this.highestNestedKey = dataset.getHighestNestedKey();

        for (DatasetSparseVector el : this.modelsList)
        {
            el.setVectorSize(highestNestedKey);
        }
    }

    @Override
    public int getHighestNestedKey() {
        return highestNestedKey;
    }

    @Override
    public MovielensElement get(Object key) {
        return modelsList.get(modelMap.get(key));
    }

    @Override
    public DoubleElement get(Integer userID, Integer movieID) {
        MovielensElement user = this.get(userID);
        if (user != null)
        {
            return user.get(movieID);
        }
        else return null;
    }

    public List<MovielensElement> getModelsList() {
        return modelsList;
    }

    public Map<Integer, Integer> getModelMap() {
        return modelMap;
    }

    public void setModelMap(Map<Integer, Integer> modelMap) {
        this.modelMap = modelMap;
    }

    public void setModelsList(List<MovielensElement> modelsList) {
        this.modelsList = modelsList;
    }
}
