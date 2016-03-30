package it.unica.foresee.core.interfaces;

import it.unica.foresee.Settings;

import java.io.File;
import java.io.Serializable;

/**
 * The state of the application, useful to save snapshots.
 */
public interface State extends Serializable
{
    /**
     * Sets the line number of the instruction file.
     * @param lineNumber the line number of the instruction file
     */
    void setLineNumber(int lineNumber);

    /**
     * Gets the line number of the instruction file.
     * @return the line number of the instruction file
     */
    int getLineNumber();

    /**
     * Sets the current execution environment.
     * @param currentEnv the current execution environment
     */
    void setCurrentEnv(Env currentEnv);

    /**
     * Gets the current execution environment.
     * @return the current execution environment
     */
    Env getCurrentEnv();

    /**
     * Sets the current instructions file.
     * @param instructionsFile the current instructions file
     */
    void setInstructionsFile(File instructionsFile);

    /**
     * Gets the current instructions file.
     * @return the current instructions file
     */
    File getInstructionsFile();

    /**
     * Sets the current settings.
     * @param settings the current settings
     */
    void setSettings(Settings settings);

    /**
     * Gets the current settings.
     * @return the current settings
     */
    Settings getSettings();

}
