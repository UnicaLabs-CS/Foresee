package it.unica.jpc.parsers;

import java.util.Scanner;

/**
 * Extend this class to implement the semantic of a command.
 */
public interface Semantic
{
    /**
     * Execute the instruction.
     *
     * @param args options of the command
     */
    public void exec(String[] args);
}