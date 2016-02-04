package it.unica.jpc.predictions;

/**
 * Makes predictions on the empty values of a vector.
 *
 * @author Fabio Colella <fcole90@gmail.com>
 */
public abstract class Prediction {
	private int neighborsAmount;
	private String trainingSet;
	private String networkPath;
	private String predictionsPath;

	/**
     * Empty constructor.
     */
    public Prediction() {
	}

    /**
     * Constructor.
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
