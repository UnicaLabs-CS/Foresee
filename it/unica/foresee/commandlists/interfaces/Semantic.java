package it.unica.foresee.commandlists.interfaces;

import it.unica.foresee.core.interfaces.Env;

/**
 * Implement this interface to implement the semantic of a command.
 */
public interface Semantic
{
    /**
     * Execute the instruction.
     *
     * @param args options of the command
     * @param env the interpreter environment
     * @return the modified environment
     */
    public Env exec(String[] args, Env env);
}