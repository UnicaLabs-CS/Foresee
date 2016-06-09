package it.unica.foresee.libraries;

import it.unica.foresee.datasets.DatasetSparseVector;
import it.unica.foresee.datasets.DoubleElement;
import it.unica.foresee.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Makes a group model from a list of clusters.
 */
public class GroupModel<T extends DatasetSparseVector<? extends DoubleElement>>
{

    /**
     * Maintains a connection between a user and the model where it's put.
     */
    private Map<Integer, Integer> userToModel = new HashMap<>();


    /**
     * Models a group with the Additive Utilitarian aka Average Strategy
     * modelling strategy.
     *
     * Each group is the average of the values of the composing elements.
     *
     * @param clusterList a list of clusters, each represented as lists of clusterable elements
     * @return a list of models, one for each cluster
     */
    public List<T> averageStrategy(List<List<T>> clusterList)
    {
        List<T> modelList = new ArrayList<>();

        // Set the size of the model to the value of the first element of the first cluster.
        // This operation assumes that every element has the same size.
        int vectorSize = clusterList.get(0).get(0).getVectorSize();

        // Loop through the clusters to make the models
        for(int clusterID = 0; clusterID < clusterList.size(); clusterID++)
        {
            // Initialise the model
            T model = null;
            try
            {
                model = (T) clusterList.get(0).get(0).getClass().newInstance();
                model.setVectorSize(vectorSize);
            }
            catch (Exception e)
            {
                throw new IllegalStateException(e);
            }

            double[] modelPoint = new double[model.getVectorSize()];
            Logger.debug("Determined size of the model: " + model.getVectorSize());

            List<T> currentCluster = clusterList.get(clusterID);

            // Add the values of each user to the model
            for (int userIndex = 0; userIndex < currentCluster.size(); userIndex++)
            {
                int userID = currentCluster.get(userIndex).getId();

                // This check ensures that we avoid putting the centroid in the map
                if (userID == 0)
                {
                    Logger.debug("Skipping user with ID assuming it's the centroid");
                    continue;
                }
                else
                {
                    // Get the points of the cluster
                    T user = currentCluster.get(userIndex);

                    // Link the user to his or her model
                    userToModel.put(userID, clusterID);

                    // Check that every array has the same length
                    if (modelPoint.length != user.size())
                    {
                        throw new IllegalStateException(
                                "Every element of the dataset needs to have the same size.\n" +
                                        "List of user: " + user
                        );
                    }

                    // Add the element array to the model array
                    for (int key : user.keySet())
                    {
                        if (user.get(key).getDoubleValue() < 1) throw new IllegalStateException(
                                "List of user: " + user + "\n" +
                                        "Element " + userID + ", " + key +
                                        " has value " + user.get(key).getDoubleValue()
                                );
                        modelPoint[key - 1] += user.get(key).getDoubleValue();
                    }
                }

            }

            // Then average the values and add them to the model
            // Warning: the values in the array are off by one to the left
            // i.e. they start from 0 and not from 1
            for (int i = 1; i < modelPoint.length + 1; i++)
            {
                model.put(i, new DoubleElement(modelPoint[i - 1]/currentCluster.size()));
                if(model.get(i).getDoubleValue() < 1 || model.get(i).getDoubleValue() > 5)
                {
                    Logger.warn("The rating is out of bound: " + model.get(i).getDoubleValue());
                }
            }

            // Finally add the model to the list of the models
            modelList.add(model);
        }

        return modelList;
    }

    /**
     * Gets the cluster where a user is.
     *
     * @param userID the ID of a user
     * @return the cluster/model ID
     */
    public int getUserModel(int userID) {
        return userToModel.get(userID);
    }

    public Map<Integer, Integer> getUserToModelMap() {
        return userToModel;
    }
}
