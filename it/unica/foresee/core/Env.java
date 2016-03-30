package it.unica.foresee.core;

import it.unica.foresee.datasets.interfaces.Dataset;

import java.io.File;

/**
 * Represents the environment.
 */
public class Env implements it.unica.foresee.core.interfaces.Env
{
    /**
     * flag for verbose mode
     */
    public boolean verb = false;

    /**
     * flag to force exit
     */
    public boolean forceExit = false;

    /**
     * exit status
     */
    public int exitStatus = 0;

    /**
     * the command in execution or just executed
     */
    public String currentCommand = null;

    /**
     * loaded dataset
     */
    public Dataset dataset = null;

    /**
     * Work directory where the program puts experiments and data file
     */
    private String workDirectory = "." + File.separator + "workdir" + File.separator;

    /**
     * Create a default environment.
     */
    public Env(){}

    public String getWorkDirectory() {
        return workDirectory;
    }

    public void setWorkDirectory(String workDirectory) {
        this.workDirectory = workDirectory;
    }
}