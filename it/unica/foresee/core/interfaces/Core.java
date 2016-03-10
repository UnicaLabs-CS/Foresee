package it.unica.foresee.core.interfaces;

import it.unica.foresee.Settings;
import it.unica.foresee.commandlists.interfaces.CommandList;
import it.unica.foresee.core.Env;
import it.unica.foresee.datasets.interfaces.DatasetLoader;
import it.unica.foresee.libraries.interfaces.Library;

import java.io.File;

/**
 * Defines the core of the framework, which manages the interaction
 * of all the other components.
 *
 * @// TODO: 10/03/16 add and complete commented methods to load modules
 */
public interface Core
{
    /**
     * Makes a snapshot of the current state.
     *
     * The {@link State} includes the Environment, the Settings, the instruction
     * file and the last executed line from the instruction file.
     *
     * @return the created state
     */
    State makeSnapshot();

    /**
     * Makes a snapshot from the given objects.
     *
     * @param e the environment
     * @param s the settings
     * @param instructionsFile the instruction file
     * @param lastExecutedLine the last line executed from the instructions file
     * @return the created state
     */
    State makeSnapshot(Env e, Settings s, File instructionsFile, int lastExecutedLine);

    /**
     * Restore a previously saved snapshot from a selected state.
     *
     */
    void restoreSnapshot(State s);

    /**
     * Saves a snapshot of the current state.
     */
    void saveSnapshot();

    /**
     * Saves a snapshot of the passed state.
     * @param s the state to save.
     */
    void saveSnapshot(State s);

    /**
     * Updates the environment.
     * @param e the new environment
     */
    void updateEnv(Env e);

    /**
     * Prints the output of the computation.
     *
     * @param output the output of the computation
     */
    void printOutput(Iterable output);

    /**
     * Saves the output of the computation.
     *
     * @param output the output of the computation
     */
    void saveOutput(Iterable output);

    //CommandList loadCommandListModule();

    //DatasetLoader loadDatasetLoaderModule();

    //Library loadLibraryModule();
}
