package it.unica.jpc;

import it.unica.jpc.parsers.ARTParser;
import it.unica.jpc.parsers.JPCParser;
import static it.unica.jpc.utils.Tools.err;
import static it.unica.jpc.utils.Tools.log;
import static it.unica.jpc.utils.Tools.warn;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Main class to run the JPC framework.
 */
public class JPC
{

    /**
     * Main function to load the program.
     * @param args command line parameters.
     *             Possible arguments are:
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
     */
    public static void main (String[] args)
    {
        /*
         * Special selections:
         *  -   0: no selection
         *  - "!": invalid argument
         */
        boolean verbose;
        boolean legacy;

        /* Flag to enable verbose output */
        verbose = false;

        /* Flag to enable legacy ART parser */
        legacy = false;

        /* Argument selected */
        char selection = 0;

        /* Options */
        String instructionsPath = new String();

        /* Error message */
        String errorMessage;

        /* Exit status */
        int exitStatus = 0;

        /* Parsing loop */
        for (int i = 0; i < args.length; i++)
        {
            /* Help */
            if (args[i].equals("-h")  || args[i].equals("--help"))
            {
                if (selection == 0)
                {
                    selection = 'h';
                }
                else
                {
                    selection = '!';
                }
            }/* Interactive */
            else if (args[i].equals("-i")  || args[i].equals("--interactive"))
            {
                if (selection == 0)
                {
                    selection = 'i';
                }
                else
                {
                    selection = '!';
                }
            }/* Path */
            else if (args[i].equals("-p")  || args[i].equals("--path"))
            {
                if (selection == 0)
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
                        System.exit(1);
                    }
                }
                else
                {
                    selection = '!';
                }
            }/* Verbose */
            else if (args[i].equals("-v")  || args[i].equals("--verbose"))
            {
                if (verbose == false)
                {
                    verbose = true;
                    log("Enabling verbose mode: brace yourself");
                }
                else
                {
                    selection = '!';
                }
            }/* Legacy mode*/
            else if (args[i].equals("-l")  || args[i].equals("--legacy"))
            {
                if (legacy == false)
                {
                    legacy = true;
                }
                else
                {
                    selection = '!';
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
            /* Help */
            case 'h':
                help();
                break;

            /* No option specified */
            case 0:
                log("no argument specified, defaulting on interactive mode");

            /* Interactive mode */
            case 'i':
                if(verbose){log("Loading interactive mode...");}
                try
                {
                    if (legacy)
                    {
                        if(verbose){log("Enabling legacy ART compatible mode");}
                        ARTParser p = new ARTParser(verbose);
                        exitStatus = p.parse();
                    }
                    else
                    {
                        JPCParser p = new JPCParser(verbose);
                        exitStatus = p.parse();
                    }
                }
                catch (FileNotFoundException e)
                {
                    err(e.getMessage());
                    exitStatus = 1;
                }
                break;

            /* Path: load instruction files at <path> */
            case 'p':
                File instructionsFile = new File(instructionsPath);;
                /* If the path is a folder make the user
                select one file in the folder, otherwise just load the file */
                if (instructionsFile.isDirectory())
                {
                    if(verbose){log("Path is a directory, loading files...");}

                    /* Anonymous function used to match only the
                    files with a particular pattern */
                    File[] ls = instructionsFile.listFiles(
                            (file, fileName) ->
                            (fileName.toLowerCase().endsWith("jpc") ||
                             fileName.toLowerCase().endsWith("ins") ||
                             fileName.toLowerCase().endsWith("instruction")
                            ));

                    for(int i = 0; i < ls.length; i++)
                    {
                        /* Printed numbers are 1 forward the index */
                        System.out.println((i + 1) + ": " + ls[i]);
                    }

                    boolean fileSelected = false;
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

                /* Load and execute the file */
                if(verbose){log("Loading file " + instructionsPath + "...");}
                try
                {
                    if (legacy)
                    {
                        if(verbose){log("Enabling legacy ART compatible mode");}
                        ARTParser p = new ARTParser(instructionsFile, verbose);
                        exitStatus = p.parse();
                    }
                    else
                    {
                        JPCParser p = new JPCParser(instructionsFile, verbose);
                        exitStatus = p.parse();
                    }

                }
                catch (FileNotFoundException e)
                {
                    err("file not found: " + instructionsPath);
                    exitStatus = 1;
                }
                break;


            /* Wrong option specified */
            case '!':
                err("incorrect argument specified");
                exitStatus = 1;
                break;

        }
        System.exit(exitStatus);
    }

    /**
     * Prints a useful help message.
     */
    public static void help()
    {
        System.out.println("" +
                "JPC - Java Predict and Cluster" +
                "\nA software framework for data mining by the University of Cagliari.");

        /* Display usage*/
        System.out.println("" +
                "Usage: jpc -iv|-h|-pv <path>" +
                "\n" +
                "\n-i, --interactive \tRun in interactive mode (default)." +
                "\n-h, --help        \tShow this help message." +
                "\n-p, --path <path> \tUse <path> as the path where to find instruction files." +
                "\n\nOptions:" +
                "\n-v, --verbose     \tEnable verbose output." +
                "\n-l, --legacy      \tEnable the legacy mode, backwards compatible with ART framework.");
    }
}