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
        int modelVectorSize = clusterList.get(1).get(0).getPoint().length;

        // Loop through the clusters to make the models
        for(int clusterID = 0; clusterID < clusterList.size(); clusterID++)
        {
            // Initialise the model
            T model = null;
            try
            {
                model = (T) clusterList.get(1).get(0).getClass().newInstance();
                model.setVectorSize(modelVectorSize);
            }
            catch (Exception e)
            {
                throw new IllegalStateException(e);
            }

            double[] modelPoint = new double[model.getVectorSize()];
            Logger.debug("Determined size of the model: " + model.getVectorSize());

            List<T> cluster = clusterList.get(clusterID);

            // Add the values of each user to the model
            for (int userIndex = 0; userIndex < cluster.size(); userIndex++)
            {
                int userID = cluster.get(userIndex).getId();

                // This check ensures that we avoid putting the centroid in the map
                if (userID == 0)
                {
                    Logger.debug("Skipping user with ID assuming it's the centroid");
                    continue;
                }
                else
                {
                    // Get the points of the cluster
                    double[] point = cluster.get(userIndex).getPoint();

                    // Link the user to his or her model
                    userToModel.put(userID, clusterID);

                    // Check that every array has the same length
                    if (modelPoint.length != point.length)
                    {
                        throw new IllegalStateException(
                                "Every element of the dataset needs to have the same size."
                        );
                    }

                    // Add the element array to the model array
                    for (int i = 0; i < point.length; i++)
                    {
                        modelPoint[i] += point[i];
                    }
                }

            }

            // Then average the values and add them to the model
            for (int i = 0; i < modelPoint.length; i++)
            {
                model.put(i, new DoubleElement(modelPoint[i]/model.getVectorSize()));
                if(model.get(i).getDoubleValue() < 0 || model.get(i).getDoubleValue() > 5)
                {
                    throw new IllegalStateException("The rating is out of bound: " + model.get(i).getDoubleValue());
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
