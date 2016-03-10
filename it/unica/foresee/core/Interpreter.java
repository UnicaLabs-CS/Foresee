package it.unica.foresee.core;

import it.unica.foresee.commandlists.ARTCommandList;
import it.unica.foresee.commandlists.interfaces.CommandList;
import it.unica.foresee.commandlists.FSCommandList;
import it.unica.foresee.commandlists.interfaces.Semantic;

import static it.unica.foresee.utils.Tools.log;
import static it.unica.foresee.utils.Tools.warn;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Class to parse the instructions of the program.
 *
 * This class represents a simple parser for the instructions of the framework. It can be invoked to execute
 * an instruction file or to take commands from an interactive shell.
 * <p>
 * The syntax of an accepted command is the following one:
 * <p>
 * {@code command [args]}
 * <p>
 * <ul>
 *     <li>{@code command} a command accepted by {@link CommandList}</li>
 *     <li>{@code [args]} a variable number of arguments</li>
 * </ul>
 * The arguments can be written literally or between quotes. The former is the standard for the
 * options of the commands, while the latter is useful for parameters which can contain whitespaces.
 * <p>
 * For a list of the standard accepted commands see {@link FSCommandList}.
 * <p>
 * For a list of the legacy, "ART", accepted commands see {@link ARTCommandList}.
 */
public class Interpreter
{
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


    /**
     * recognized instructions and their semantic
     */
    protected TreeMap<String, Semantic> command;

    /**
     * the environment of the parser
     * , see {@link Env}
     */
    protected Env env;

    /**
     * file containing the instructions
     */
    protected File instructionsFile;

    /**
     * flag to activate the interactive shell mode
     */
    protected boolean interactive;

    /**
     * Constructs a parser from an instruction file.
     *
     * @param instructionsFile the instructions file
     * @param verbose flag to set the level of verbosity
     */
    public Interpreter(File instructionsFile, boolean verbose, CommandList commandList)
    {
        if (instructionsFile != null)
        {
            this.instructionsFile = instructionsFile;
            this.interactive = false;
        }
        else
        {
            this.instructionsFile = null;
            this.interactive = true;
        }

        env = new Env();
        env.verb = verbose;
        this.command = commandList.loadCommandsSemantic();
    }

    /**
     * Constructs a parser for an interactive shell.
     *
     * @param verbose flag to set the level of verbosity
     */
    public Interpreter(boolean verbose, CommandList commandList)
    {
        this.instructionsFile = null;
        this.interactive = true;
        env = new Env();
        env.verb = verbose;
        this.command = commandList.loadCommandsSemantic();
    }

    /**
     * Helper function to use the current work directory.
     *
     * If you need a path to be in the work directory, just use {@code workdir(path)} to
     * retrieve the path relative to the work directory.
     *
     * @param path the path to a file or folder
     * @return a path relative to the current work directory
     */
    protected String workdir(String path)
    {
        return (env.work_directory == null) ? path : env.work_directory + File.separator + path;
    }

    /**
     * Parses the given instructions.
     *
     * @return the exit status
     */
    public int run() throws FileNotFoundException
    {
        Scanner instructions;
        String statement;
        boolean interactive = false;
        String prompt = ">>>";

        /* If an instruction file exists, use that as source */
        if (instructionsFile != null)
        {
            instructions = new Scanner(instructionsFile);
            if(env.verb){log("File " + instructionsFile + " loaded");}
        }/* Else use the standard input */
        else
        {
            instructions = new Scanner(System.in);
            if(env.verb){log("Interactive mode initialized");}
            interactive = true;
        }

        if (interactive)
        {
            System.out.print(prompt + " ");
        }

        /* Execution Loop */
        while (instructions.hasNextLine())
        {
            statement = instructions.nextLine();

            /* Skip comments */
            if (!statement.startsWith("#"))
            {
                this.execute(statement);
            }

            /* Something triggered the exit command
            or the current state is inconsistent */
            if (env.force_exit || env.exit_status != 0) {break;}

            if (interactive)
            {
                System.out.print(prompt + " ");
            }
        }

        if(env.verb){log("Leaving with exit status " + env.exit_status);}
        return env.exit_status;
    }

    /**
     * Executes a single instruction.
     *
     * @param instruction the instruction to execute
     */
    protected void execute(String instruction)
    {
        Scanner instructionReader = new Scanner(instruction);

        if (instructionReader.hasNext())
        {
            String command = instructionReader.next();

            /* Find the appropriate command to execute */
            if (this.command.containsKey(command))
            {
                env.current_command = command;

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
                /* Convert the ArrayList to a String array */
                this.env = this.command.get(command).exec(args.toArray(new String[0]), env);
            }
            else
            {
                warn("command not found: " + instruction);
            }
        }
    }
}