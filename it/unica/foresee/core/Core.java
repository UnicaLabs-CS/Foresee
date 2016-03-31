package it.unica.foresee.core;

import it.unica.foresee.commandlists.interfaces.CommandList;
import it.unica.foresee.commandlists.ARTCommandList;
import it.unica.foresee.commandlists.FSCommandList;
import it.unica.foresee.core.interfaces.Env;
import it.unica.foresee.core.interfaces.State;
import it.unica.foresee.Settings;

import static it.unica.foresee.utils.Tools.err;
import static it.unica.foresee.utils.Tools.warn;
import static it.unica.foresee.utils.Tools.log;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Core manager and interpreter of the framework.
 *
 * This class is a sort of manager, responsible for allowing all the other
 * classes to work together. At the same time it works as an interpreter,
 * parsing the commands from the given instructions file.
 */
public class Core implements it.unica.foresee.core.interfaces.Core
{

    /* Constants */

    /**
     * Directory where to store the snapshots.
     */
    private final String SNAP_DIR = "snapshots" + File.separator;

    /**
     * Name of the snapshot file.
     */
    private final String SNAP_FILE_NAME = "snapshot";

    /**
     * The command prompt to display.
     */
    private final String PROMPT = ">>>";

    /**
     * Pattern to check if there's any double quotes in the string.
     * <p>
     * Regex: {@code p{javaWhitespace}*".*}
     */
    public final Pattern CHECK_STRING_START = Pattern.compile("\\p{javaWhitespace}*\".*");

    /**
     * Pattern to check if there are matching double quotes, in the string.
     *
     * Skips escaped quotes (i.e. \") and allows to escape escape characters (i.e. \\).
     * <p>
     * Regex: {@code p{javaWhitespace}*".*(?<!(?<!\\)\\)"}
     */
    public final Pattern CHECK_STRING_MATCHING_DELIMITERS = Pattern.compile("\\p{javaWhitespace}*\".*(?<!(?<!\\\\)\\\\)\"");

    /**
     * Pattern to find pieces of strings surrounded by quotes.
     *
     * Skips escaped quotes (i.e. \") and allows to escape escape characters (i.e. \\).
     * <p>
     * Regex: {@code p{javaWhitespace}*"((?<!\\)(\\")|[^")])*(?<!(?<!\\)\\)"}
     * <p>
     * Meaning: take whitespaces, followed by quotes, followed by some characters (even 0) followed by
     * quotes. The characters can be any caracter except the " symbol except when escaped with a \.
     * The \ is an escape symbol, except if it escaped with another \ (i.e. to mean \ write \\).
     * The closing quotes cannot be preceded by a \, except if even that \ is escaped (i.e. "\\" is ok).
     */
    public final Pattern FIND_WHOLE_STRING = Pattern.compile("" +
            "\\p{javaWhitespace}*\"((?<!\\\\)(\\\\\")|[^\")])*(?<!(?<!\\\\)\\\\)\"");

    /* Properties */

    /**
     * The execution environment.
     */
    private Env env;

    /**
     * The application settings.
     */
    private Settings settings;

    /**
     * The source file containing the instructions to execute.
     */
    private File instructionsFile;

    /**
     * The number of the current line to execute.
     */
    private int lineNumber;

    /**
     * The list of the available commands
     */
    private CommandList commandList;

    /**
     * The next saved snapshot is going to have this number.
     *
     * It allows cycling snapshots files.
     */
    private int snapshotNumber = 0;

    /**
     * The highest number of saved snapshots.
     */
    private int maxSnapshotNumber = 5;

    /**
     * Constructor which handles the instructions file.
     * @param settings the settings
     * @param instructionsFile the source file containing the commands to execute
     */
    public Core(Settings settings, File instructionsFile)
    {
        this.settings = settings;
        this.instructionsFile = instructionsFile;

        // Check that the state is consistent
        if (settings.isInteractive() && instructionsFile != null)
        {
            throw new IllegalStateException("Invoking core in interactive mode " +
                    "requires a null instructions file.");
        }

        if (!settings.isInteractive() && instructionsFile == null)
        {
            throw new IllegalStateException("Invoking core in non interactive mode " +
                    "requires a not null instructions file.");
        }

        // Check which command list to use
        if (settings.isLegacy())
        {
            this.commandList = new FSCommandList();
        }
        else
        {
            this.commandList = new ARTCommandList();
        }

        this.env = new it.unica.foresee.core.Env();
    }

    /**
     * Constructor for interactive mode only.
     * @param settings the settings
     */
    public Core(Settings settings)
    {
        this(settings, null);
    }

    /**
     * Set the flag to forcefully exit from execution.
     * @param forceExit
     */
    private void setForceExit(boolean forceExit)
    {
        env.setForceExit(forceExit);
    }

    /**
     * Get the snapshot number for the file name.
     *
     * This function also updates the associated variable, to
     * always obtain the right number.
     * @return the snapshot number converted to string
     */
    private int getSnapshotNumber()
    {
        int lastSnapshotNumber = snapshotNumber;

        // Keep the snapshot number updated
        this.snapshotNumber++;
        if (snapshotNumber >= maxSnapshotNumber)
        {
            snapshotNumber = 0;
        }
        return lastSnapshotNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State makeSnapshot()
    {
        return new it.unica.foresee.core.State();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State makeSnapshot(Env e, Settings s, File instructionsFile, int lineNumber)
    {
        return new it.unica.foresee.core.State(e, s, instructionsFile, lineNumber);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveSnapshot()
    {
        saveSnapshot(makeSnapshot());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveSnapshot(State s)
    {
        try
        {
            // Check the existence of the path
            File dir = new File(env.getWorkDirectory() + this.SNAP_DIR);
            if (!dir.exists())
            {
                dir.mkdirs();
            }
            else if (dir.exists() && dir.isFile())
            {
                warn(dir.toString() + " is a file, a directory was required to save the snapshots");
                warn("snapshooting is now disabled, please change the work directory to make it work again");
            }

            // Set the full path and name of the file (this also updates the snapshot number)
            String fileName = env.getWorkDirectory() + this.SNAP_DIR +
                    this.SNAP_FILE_NAME + this.getSnapshotNumber() + ".snap";
            FileOutputStream snapshotFile = new FileOutputStream(fileName);
            ObjectOutputStream snapshot = new ObjectOutputStream(snapshotFile);

            // Save the object
            snapshot.writeObject(s);
            snapshot.close();
        }
        catch (IOException e)
        {
            warn(e.getMessage());
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restoreSnapshot(String fileName)
    {
        try
        {
            FileInputStream snapshotFile = new FileInputStream(fileName);
            ObjectInputStream snapshot = new ObjectInputStream(snapshotFile);

            State s = (State) snapshot.readObject();
            snapshot.close();

            // Set the properties with the loaded values
            this.env = s.getCurrentEnv();
            this.settings = s.getSettings();
            this.lineNumber = s.getLineNumber();
            this.instructionsFile = s.getInstructionsFile();

        }
        catch (ClassNotFoundException | IOException e)
        {
            err(e.getMessage());
            setForceExit(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEnv(Env e)
    {
        this.env = e;
    }

    /**
     * {@inheritDoc}
     * @// TODO: 30/03/16 implement this function
     */
    @Override
    public void printOutput(Iterable output) {
        warn("this function does nothing...");
    }

    /**
     * {@inheritDoc}
     * @// TODO: 30/03/16 implement this function
     */
    @Override
    public void saveOutput(Iterable output) {
        warn("this function does nothing...");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int run() throws FileNotFoundException
    {
        Scanner instructions;
        String statement;

        /* If an instruction file exists, use that as source */
        if (!settings.isInteractive())
        {
            instructions = new Scanner(instructionsFile);
            log("File " + instructionsFile + " loaded");
        }/* Else use the standard input */
        else
        {
            instructions = new Scanner(System.in);
            log("Interactive mode initialized");
        }

        if (settings.isInteractive())
        {
            System.out.print(PROMPT + " ");
        }

        /* Execution Loop */
        while (instructions.hasNextLine())
        {
            statement = instructions.nextLine();

            /* Skip comments */
            if (!statement.startsWith("#"))
            {
                this.execute(statement);

                // Save the current state
                if(settings.isActiveSnapshooting())
                {
                    saveSnapshot();
                }
            }

            /* Something triggered the exit command */
            if (env.isForceExit())
            {
                log("forcing exit...");
                break;
            }

            /* The current state is inconsistent, exit now */
            if (env.isAbnormalStatus())
            {
                err("the system will be closed due to inconsistent state: status " + env.getExitStatus());
            }

            if (settings.isInteractive())
            {
                System.out.print(PROMPT + " ");
            }
        }

        log("Leaving with exit status " + env.getExitStatus());
        return env.getExitStatus();
    }

    /**
     * Executes a single instruction.
     *
     * @param instruction the instruction to execute
     */
    private void execute(String instruction)
    {
        Scanner instructionReader = new Scanner(instruction);

        if (instructionReader.hasNext())
        {
            String command = instructionReader.next();

            /* Find the appropriate command to execute */
            if (this.commandList.containsKey(command))
            {
                env.setCurrentCommand(command);

                /* Parse arguments */
                ArrayList<String> args = new ArrayList<>();
                while (instructionReader.hasNext())
                {
                    /*
                     * Searching strings:
                     * 1 - Check if there's an open " symbol
                     * 2 - Check if the " symbols match (i.e. are paired)
                     * 3 - If those checks are passed take the resulting string as a whole
                     * 4 - Remove the " symbols from the string
                     */
                    /* If token starts with " */
                    if(instructionReader.hasNext(CHECK_STRING_START))
                    {
                        /* and ends with " */
                        instructionReader.useDelimiter("\n");
                        if(instructionReader.hasNext(CHECK_STRING_MATCHING_DELIMITERS))
                        {
                            /* Take the text within " as a whole argument */
                            String str = instructionReader.findInLine(FIND_WHOLE_STRING);
                            /* Normalize escaped characters and remove whitespaces*/
                            /* The order of replaces IS relevant:
                               if you replace \\ with \ first, a string like \\"
                               would become ", because after the first replace
                               it would become \" and then ".
                               Instead if you replace \" with " first
                               \\" becomes \" and the second replace has no match for it.
                             */
                            str = str.replace("\\\"", "\"").replace("\\\\", "\\").trim();
                            /* Remove first and last character to remove the surrounding quotes */
                            args.add(str.substring(1, str.length() - 1));
                        }/* else it's a malformed string, don't execute anything */
                        else
                        {
                            warn("missing closing \"");
                            return;
                        }
                        instructionReader.useDelimiter("\\p{javaWhitespace}+");
                    }/* if there's no " character take it normally */
                    else
                    {
                        args.add(instructionReader.next());
                    }
                }
                /* Convert the ArrayList to a String array before executing it*/
                this.env = this.commandList.get(command).exec(args.toArray(new String[0]), env);
            }
            else
            {
                warn("command not found: " + instruction);
            }
        }
    }
}
