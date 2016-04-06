package it.unica.foresee.core.interfaces;

import it.unica.foresee.Settings;

import java.io.File;
import java.io.FileNotFoundException;

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
     * file and the last executed line (including line number) from the instruction file.
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
     * @param lineNumber the last line executed from the instructions file
     * @return the created state
     */
    State makeSnapshot(Env e, Settings s, File instructionsFile, int lineNumber);

    /**
     * Restore a previously saved snapshot from a selected file.
     * @param fileName the snapshot file name (including the path)
     */
    void restoreSnapshot(String fileName);

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

    /**
     * Parses the given instructions.
     *
     * @return the exit status
     */
    int run() throws FileNotFoundException;


    //CommandList loadCommandListModule();

    //DatasetLoader loadDatasetLoaderModule();

    //Library loadLibraryModule();
}
