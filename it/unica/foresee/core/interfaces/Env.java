package it.unica.foresee.core.interfaces;

/**
 * Represents the environment of execution.
 */
public interface Env
{
    public String getWorkDirectory();

    public void setWorkDirectory(String workDirectory);
}
