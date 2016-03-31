package it.unica.foresee;

import it.unica.foresee.core.Core;
import it.unica.foresee.utils.Tools;

import static it.unica.foresee.utils.Tools.err;
import static it.unica.foresee.utils.Tools.warn;
import static it.unica.foresee.utils.Tools.log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
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
     * Expands agglomerated arguments into separate arguments.
     * (e.g. -ivl becomes -i -v -l)
     *
     * @param args the application arguments
     * @return the expanded arguments
     */
    public static String[] expandArguments(String[] args)
    {
        ArrayList<String> argsList = new ArrayList<>();
        for (int i = 0; i < args.length; i++)
        {
            /* Check if it's a string argument or a char argument with only
             * two characters (the dash and the char).
             */
            if (args[i].startsWith("--") || args[i].length() == 2)
            {
                argsList.add(args[i]);
            }
            else /* multiple chars aggregated (eg. -ivl) */
            {
                /* add the first two characters */
                argsList.add(args[i].substring(0, 2));

                /* start just after the aforementioned chars */
                for (int j = 2; j < args[i].length(); j++)
                {
                    /* add each character as if it was prefixed by a dash */
                    argsList.add("-" + args[i].substring(j, j + 1));
                }
            }
        }
        return argsList.toArray(args);
    }

    /**
     * Parses the application arguments to obtain the
     * settings for the current run.
     *
     * @param args the application arguments
     * @return the settings for the current run
     */
    public static Settings parseArguments(String[] args)
    {
        Settings s = new Settings();
        boolean modeHasBeenSelected = false;

        /* Loop to parse the arguments */
        for (int i = 0; i < args.length; i++)
        {
            /* Help */
            if (args[i].equals("-h")  || args[i].equals("--help"))
            {
                if (!modeHasBeenSelected)
                {
                    modeHasBeenSelected = true;
                    s.setHelpMode(true);
                }
                else
                {
                    throw new IllegalStateException(args[i]);
                }
            }/* Interactive shell mode */
            else if (args[i].equals("-i")  || args[i].equals("--interactive"))
            {
                if (!modeHasBeenSelected)
                {
                    modeHasBeenSelected = true;
                    s.setInteractive(true);
                }
                else
                {
                    throw new IllegalStateException(args[i]);
                }
            }/* Path */
            else if (args[i].equals("-p")  || args[i].equals("--path"))
            {
                if (!modeHasBeenSelected)
                {
                    modeHasBeenSelected = true;

                    /* Look at the following argument */
                    if (i+1 < args.length)
                    {
                        i++;
                        s.setInstructionPath(args[i]);
                    }
                    else
                    {
                        err("option -p, --path requires <path>");
                        throw new IllegalStateException(args[i]);
                    }
                }
                else
                {
                    throw new InputMismatchException("argument " + args[i] + "not recognized.");
                }
            }/* Verbose */
            else if (args[i].equals("-v")  || args[i].equals("--verbose"))
            {
                if (!s.isVerbose())
                {
                    s.setVerbose(true);
                    Tools.setVerbosity(Tools.VERB_ALL);
                    log("Enabling verbose mode: brace yourself");
                }
                else
                {
                    throw new IllegalStateException(args[i]);
                }
            }/* Legacy mode*/
            else if (args[i].equals("-l")  || args[i].equals("--legacy"))
            {
                if (!s.isLegacy())
                {
                    s.setLegacy(true);
                }
                else
                {
                    throw new IllegalStateException(args[i]);
                }
            }/* The argument is not among those recognized */
            else
            {
                throw new InputMismatchException("argument " + args[i] + " not recognized.");
            }
        }

        return s;
    }

    /**
     * Actual main method which runs the program.
     *
     * While having this method and the #main() may seem redundant,
     * this choice is done to make testing the methods of this class easier.
     *
     * @param args command line arguments
     * @return exit status
     */
    public static int mainHelper(String[] args)
    {
        int exitStatus = 0;
        /* Obtain the settings for the application */
        Settings settings;
        /* args like -ivs become -i -v -s */
        args = expandArguments(args);
        try
        {
            settings = parseArguments(args);
        }
        // This happens when the user selects multiple modes that cannot work together
        catch (IllegalStateException e)
        {
            err("Multiple modes selected or missing parameter while parsing argument " + e.getMessage());
            return 1;
        }
        // This happens if the input arguments are not recognised
        catch (InputMismatchException e)
        {
            err(e.getMessage());
            return 1;
        }

        if (settings.isHelpMode())
        {
            help();
            return exitStatus;
        }

        try
        {
            // Interactive mode
            if (settings.isInteractive())
            {
                log("Loading interactive mode...");
                return (new Core(settings)).run();
            }
            // Instructions file mode
            else if (settings.isInstructionPathSet())
            {
                File instructionsFile = new File(settings.getInstructionPath());

                /* If the path is a folder make the user
                select one file in the folder, otherwise just load the file */
                if (instructionsFile.isDirectory())
                {
                    /* Returns null if no file is available */
                    instructionsFile = askFileToLoad(settings.isVerbose(), instructionsFile);
                }

                /* Check if a file has actually been selected */
                if (instructionsFile != null)
                {
                    /* Call the interpreter */
                    exitStatus = (new Core(settings, instructionsFile)).run();
                }
                else
                {
                    err("Sorry no file found or invalid selection");
                    exitStatus = 1;
                }
            }
            else /* No valid selection has been done, use intercative as fallback */
            {
                log("no argument specified, defaulting on interactive mode");
                settings.setInteractive(true);

                if (settings.isVerbose())
                {
                    log("Loading interactive mode...");
                }
                return (new Core(settings)).run();
            }
        }
        // This should have been checked already, but care is needed anyway
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

        log("Path is a directory, loading files...");

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

    /**
     * Loads user settings from a file.
     *
     * @param s current settings to update with those in the file
     * @param f file containing the settings
     * @return updated settings
     */
    public static Settings loadSettingsFile(Settings s, File f)
    {
        warn("loadSettingsFile is not yet implemented");
        return s;
    }
}