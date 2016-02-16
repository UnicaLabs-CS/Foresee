package it.unica.jpc.parsers;

import it.unica.jpc.datasets.Movielens;
import static it.unica.jpc.utils.Tools.err;
import static it.unica.jpc.utils.Tools.log;
import static it.unica.jpc.utils.Tools.warn;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Class to parse the instructions of the program.
 *
 * Legacy parser intended to be backwards compatible with
 * the new JPC parser.
 */
public class ARTParser extends JPCParser
{


    /**
     * {@inheritDoc}
     */
    public ARTParser(File instructionsFile, boolean verbose)
    {
        super(instructionsFile, verbose);
        loadLegacyGrammar();
    }

    /**
     * {@inheritDoc}
     */
    public ARTParser(boolean verbose)
    {
        super(verbose);
        loadLegacyGrammar();
    }

    /**
     * Edit the JPC parser grammar to fit the ART grammar.
     */
    protected void loadLegacyGrammar()
    {

    }
}