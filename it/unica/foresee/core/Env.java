package it.unica.foresee.core;

import it.unica.foresee.datasets.interfaces.Dataset;

/**
 * Represents the environment.
 */
public class Env
{
    /**
     * flag for verbose mode
     */
    public boolean verb = false;

    /**
     * flag to force exit
     */
    public boolean force_exit = false;

    /**
     * exit status
     */
    public int exit_status = 0;

    /**
     * the command in execution or just executed
     */
    public String current_command = null;

    /**
     * loaded dataset
     */
    public Dataset dataset = null;

    /**
     * work directory where the program puts experiments and data file
     */
    public String work_directory = null;

    /**
     * Create a default environment.
     */
    public Env(){}
}