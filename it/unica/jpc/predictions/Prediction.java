package it.unica.jpc.predictions;

/**
 * Makes predictions on the empty values of a vector.
 *
 * @author Fabio Colella
 */
public abstract class Prediction {
	private int neighborsAmount;
	private String trainingSet;
	private String networkPath;
	private String predictionsPath;

	/**
     * Initializes the object with defaults.
	 *
	 * The neighborsAmount is set to 0, the other
	 * values are set to null.
     */
    public Prediction()
	{
		this.neighborsAmount = 0;
	}

    /**
     * Initializes the object with the received parameters.
	 *
	 * @param neighborsAmount the number of neighbors
	 * @param trainingSet the training set to use
	 * @param networkPath the path of the network
	 * @param predictionsPath the path of the predictions
     */
    public Prediction(int neighborsAmount,
					  String trainingSet,
					  String networkPath,
					  String predictionsPath) {
		this.neighborsAmount = neighborsAmount;
		this.trainingSet = trainingSet;
		this.networkPath = networkPath;
		this.predictionsPath = predictionsPath;
    }
}
