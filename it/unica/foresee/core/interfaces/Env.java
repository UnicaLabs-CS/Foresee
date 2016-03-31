package it.unica.foresee.core.interfaces;

import it.unica.foresee.datasets.interfaces.Dataset;

/**
 * Represents the environment of execution.
 */
public interface Env
{
    /* Getter */

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
     * Get the current work directory path.
     *
     * The work directory is where temporary files
     * buffers and snapshots are stored.
     * @return the path to the work directory
     */
     String getWorkDirectory();

    /**
     * Check if the application is set to exit forcefully.
     * @return true if the application should exit
     */
     boolean isForceExit();

    /**
     * Check if the application is in abnormal state.
     *
     * This should is the same as to check if the exit status is still 0.
     */
     boolean isAbnormalStatus();

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

    /**
     * Sets the current work directory path.
     *
     * The work directory is where temporary files
     * buffers and snapshots are stored.
     *
     * @param workDirectory the path to the work directory
     */
     void setWorkDirectory(String workDirectory);








}
