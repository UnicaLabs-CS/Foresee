package it.unica.foresee.core;

import it.unica.foresee.Settings;

import java.io.File;

/**
 * State implementations.
 */
public class State implements it.unica.foresee.core.interfaces.State
{
    private it.unica.foresee.core.interfaces.Env currentEnv;
    private File instructionsFile;
    private int lineNumber;
    private Settings settings;

    /**
     * Empty constructor.
     */
    public State(){}

    /**
     * Initializing constructor.
     * @param e the current environment
     * @param s the current settings
     * @param lineNumber the last executed line number
     */
    public State(it.unica.foresee.core.interfaces.Env e, Settings s, File instructionsFile, int lineNumber)
    {
        this.currentEnv = e;
        this.instructionsFile = instructionsFile;
        this.lineNumber = lineNumber;
        this.settings = s;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public it.unica.foresee.core.interfaces.Env getCurrentEnv() {
        return currentEnv;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLineNumber() {
        return lineNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public File getInstructionsFile() {
        return instructionsFile;
    }

    /**
     * {@inheritDoc}
     */
    public Settings getSettings() {
        return settings;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentEnv(it.unica.foresee.core.interfaces.Env currentEnv) {
        this.currentEnv = currentEnv;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setInstructionsFile(File instructionsFile) {
        this.instructionsFile = instructionsFile;
    }

    /**
     * {@inheritDoc}
     */
    public void setSettings(Settings settings) {
        this.settings = settings;
    }
}
