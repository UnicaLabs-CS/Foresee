package it.unica.foresee.core.interfaces;

import it.unica.foresee.datasets.interfaces.Dataset;
import it.unica.foresee.utils.Pair;

/**
 * Represents the environment of execution.
 */
public interface Env
{
    /* Getter */

    /**
     * Gets a buffer that allows a small communication between Env and {@link it.unica.foresee.core.Core}.
     *
     * Once the buffer is read, it's deleted, to avoid storing personal information.
     *
     * @return a key value pair
     */
    public Pair<String, String> getBuffer();

    /**
     * Gets the current command being executed.
     *
     * @return the current command being executed
     */
     String getCurrentCommand();

    /**
     * Gets the current dataset.
     *
     * @return the current dataset
     */
     Dataset getDataset();

    /**
     * Obtain the current value for the exit status.
     *
     * It should be always 0.
     *
     * @return the value of the exit status
     */
     int getExitStatus();

    /**
     * Check if the application is in abnormal state.
     *
     * This should is the same as to check if the exit status is still 0.
     */
    boolean isAbnormalStatus();

    /**
     * Check if the buffer is set.
     * @return true if the buffer is set
     */
    boolean isBufferSet();

    /**
     * Check if the application is set to exit forcefully.
     * @return true if the application should exit
     */
     boolean isForceExit();

    /* Setter */

    /**
     * Sets an abnormal exit status.
     *
     * This functions doesn't allow to set back the exit status
     * to 0 (normal exit status).
     *
     * @param status the abnormal exit status, it cannot be 0
     */
     void setAbnormalExitStatus(int status);

    /**
     * Sets a buffer that allows a small communication between Env and {@link it.unica.foresee.core.Core}.
     *
     * The buffer can only be set once in each command execution.
     *
     * @param buffer a key value pair
     */
    public void setBuffer(Pair<String, String> buffer);

    /**
     * Sets a buffer that allows a small communication between Env and {@link it.unica.foresee.core.Core}.
     *
     * The buffer can only be set once in each command execution.
     *
     * @param key the key of the pair
     * @param value the value of the pair
     */
    public void setBuffer(String key, String value);

    /**
     * Sets the current command being executed.
     *
     * @param command the current command being executed
     */
     void setCurrentCommand(String command);

    /**
     * Sets the current dataset.
     *
     * @param dataset the current dataset
     */
     void setDataset(Dataset dataset);

    /**
     * Sets if the application should exit forcefully
     * @param forceExit is true if the application should exit forcefully
     */
     void setForceExit(boolean forceExit);

}
