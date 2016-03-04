package it.unica.foresee;

import it.unica.foresee.interpreters.ARTCommandList;
import it.unica.foresee.interpreters.FSCommandList;
import it.unica.foresee.interpreters.Interpreter;
import it.unica.foresee.Settings;

import static it.unica.foresee.utils.Tools.err;
import static it.unica.foresee.utils.Tools.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Main class to run the Foresee framework.
 * <p>
 * Possible arguments are:
 *             <ul>
 *              <li>-h --help: Shows help message</li>
 *              <li>-p --path: Path to the instruction file folder</li>
 *              <li>-i --interactive: Interactive Mode (default)</li>
 *             </ul>
 *             Options:
 *             <ul>
 *              <li>-v --verbose: Enable verbose output</li>
 *              <li>-l --legacy: Enable the legacy ART parser</li>
 *             </ul>
 * <p>
 * Accepted extensions for the instructions file are the following:
 *  <ul>
 *              <li>.fs</li>
 *              <li>.foresee</li>
 *              <li>.ins</li>
 *              <li>.instruction</li>
 *  </ul>
 */
public class Foresee
{

    /**
     * Selection special value for unknown arguments
     */
    public final static char INCONSISTENT_STATE = '!';

    /**
     * Selection default value
     */
    public final static char DEFAULT_ARG = 0;

    /**
     * Main function to load the program.
     *
     * @param args command line parameters.
     */
    public static void main (String[] args)
    {
        int exitStatus = mainHelper(args);
        System.exit(exitStatus);
    }

    /**
     * Actual main method which runs the program.
     *
     * @param args command line arguments
     * @return exit status
     */
    public static int mainHelper(String[] args)
    {
         /*
         * Special selections:
         *  -   0: no selection
         *  - "!": invalid argument
         */
        boolean verbose;
        boolean legacy;

        /* Instructions file */
        File instructionsFile;

        /* Flag to enable verbose output */
        verbose = false;

        /* Flag to enable legacy ART parser */
        legacy = false;

        /* Argument selected */
        char selection = DEFAULT_ARG;

        /* Options */
        String instructionsPath = null;

        /* Exit status */
        int exitStatus = 0;

        /* Loop to check arguments */
        for (int i = 0; i < args.length; i++)
        {
            /* Help */
            if (args[i].equals("-h")  || args[i].equals("--help"))
            {
                if (selection == DEFAULT_ARG)
                {
                    selection = 'h';
                }
                else
                {
                    selection = INCONSISTENT_STATE;
                }
            }/* Interactive */
            else if (args[i].equals("-i")  || args[i].equals("--interactive"))
            {
                if (selection == DEFAULT_ARG)
                {
                    selection = 'i';
                }
                else
                {
                    selection = INCONSISTENT_STATE;
                }
            }/* Path */
            else if (args[i].equals("-p")  || args[i].equals("--path"))
            {
                if (selection == DEFAULT_ARG)
                {
                    selection = 'p';

                    try
                    {
                        i++;
                        instructionsPath = args[i];
                    }
                    catch (ArrayIndexOutOfBoundsException e)
                    {
                        err("option -p, --path requires <path>");
                        help();
                        exitStatus = 1;
                        selection = INCONSISTENT_STATE;
                    }
                }
                else
                {
                    selection = INCONSISTENT_STATE;
                }
            }/* Verbose */
            else if (args[i].equals("-v")  || args[i].equals("--verbose"))
            {
                if (!verbose)
                {
                    verbose = true;
                    log("Enabling verbose mode: brace yourself");
                }
                else
                {
                    selection = INCONSISTENT_STATE;
                }
            }/* Legacy mode*/
            else if (args[i].equals("-l")  || args[i].equals("--legacy"))
            {
                if (!legacy)
                {
                    legacy = true;
                }
                else
                {
                    selection = INCONSISTENT_STATE;
                }
            }/* The argument is not among those recognized */
            else
            {
                selection = '!';
                err("Argument '" + args[i] + "' not recognized");
            }
        }

        /* Execution */
        switch (selection)
        {
            /* Show help */
            case 'h':
                help();
                break;

            /* No option specified */
            case DEFAULT_ARG:
                log("no argument specified, defaulting on interactive mode");

            /* Interactive shell mode */
            case 'i':
                if(verbose){log("Loading interactive mode...");}
                exitStatus = loadInterpreter(legacy, verbose, null);
                break;

            /* Path: load instruction files at <path> */
            case 'p':
                if (instructionsPath != null)
                {
                    instructionsFile = new File(instructionsPath);;
                        /* If the path is a folder make the user
                        select one file in the folder, otherwise just load the file */
                    if (instructionsFile.isDirectory())
                    {
                        /* Returns null if no file is available */
                        instructionsFile = askFileToLoad(verbose, instructionsFile);
                    }

                    /* Check if a file has actually been selected */
                    if (instructionsFile != null)
                    {
                        /* Call the interpreter */
                        exitStatus = loadInterpreter(legacy, verbose, instructionsFile);
                    }
                    else
                    {
                        err("Sorry no file found or invalid selection");
                        exitStatus = 1;
                    }
                    break;
                }
                else
                {
                    throw new IllegalStateException("Instructions path has not been initialized");
                }

            /* Wrong option specified */
            case INCONSISTENT_STATE:
                err("incorrect argument specified");
                exitStatus = 1;
                break;

        }

        return exitStatus;
    }

    /**
     * Loads the instruction file and runs the interpreter.
     *
     * @param legacy flag for legacy interpreter
     * @param verbose flag for verbose mode
     * @param instructionsFile file containing the instructions to run
     * @return the exit status
     */
    public static int loadInterpreter(boolean legacy, boolean verbose, File instructionsFile)
    {
        int exitStatus;
        /* Load the interpreter and run the program*/
        try
        {
            if (legacy)
            {
                if(verbose){log("Enabling legacy ART compatible mode");}
                Interpreter i = new Interpreter(instructionsFile, verbose, new ARTCommandList());
                exitStatus = i.run();
            }
            else
            {
                Interpreter i = new Interpreter(instructionsFile, verbose, new FSCommandList());
                exitStatus = i.run();
            }
        }
        catch (FileNotFoundException e)
        {
            err(e.getMessage());
            exitStatus = 1;
        }
        return exitStatus;
    }

    /**
     * Asks the user which file to load among those in the current path.
     *
     * @param verbose flag for verbose mode
     * @param instructionsDir folder containing instructions files
     * @return selected file containing instructions to run, if no file was available, returns null
     */
    public static File askFileToLoad(boolean verbose, File instructionsDir)
    {
        File instructionsFile = null;

        if(verbose){log("Path is a directory, loading files...");}

        /* Anonymous function used to match only the
           files with a particular pattern */
        File[] ls = instructionsDir.listFiles(
                (file, fileName) ->
                        (fileName.toLowerCase().endsWith("fs") ||
                                fileName.toLowerCase().endsWith("foresee") ||
                                fileName.toLowerCase().endsWith("ins") ||
                                fileName.toLowerCase().endsWith("instruction")
                        ));

        /* Display the list of files */
        for(int i = 0; i < ls.length; i++)
        {
            /* Printed numbers are 1 forward the index */
            System.out.println((i + 1) + ": " + ls[i]);
        }

        if (ls.length > 0)
        {
            boolean fileSelected = false;

            /* Prompt to ask the file to load */
            while (!fileSelected)
            {
                System.out.print("Select a file: \n>>> ");
                Scanner in = new Scanner(System.in);
                if (in.hasNextInt())
                {
                    /* The input is one forward the index */
                    int index = in.nextInt() - 1;

                    /* Check that the input is in the bound */
                    if (index >= 0 && index < ls.length)
                    {
                        fileSelected = true;
                        instructionsFile = ls[index];
                    }
                    else
                    {
                        err("Sorry, file number out of range, " +
                                "choose from 1 to " + ls.length);
                    }
                }/* The user input is not even a number */
                else
                {
                    err("Please, choose a number from 1 to " + ls.length);
                }
            }
        }

        return instructionsFile;
    }

    /**
     * Prints a useful help message.
     */
    public static void help()
    {
        System.out.println("" +
                "Foresee" +
                "\nA software framework for automatic recommendation by the University of Cagliari.");

        /* Display usage*/
        System.out.println("" +
                "Usage: foresee -iv|-h|-pv <path>" +
                "\n" +
                "\n-i, --interactive \tRun in interactive mode (default)." +
                "\n-h, --help        \tShow this help message." +
                "\n-p, --path <path> \tUse <path> as the path to an instruction file or a folder" +
                "\n\nOptions:" +
                "\n-v, --verbose     \tEnable verbose output." +
                "\n-l, --legacy      \tEnable the legacy mode, backwards compatible with ART framework.");
    }
}