package it.unica.jpc.parsers;

import it.unica.jpc.datasets.Dataset;
import it.unica.jpc.datasets.Movielens;
import static it.unica.jpc.utils.Tools.err;
import static it.unica.jpc.utils.Tools.log;
import static it.unica.jpc.utils.Tools.warn;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Class to parse the instructions of the program.
 *
 * This class represents a simple parser for the instructions of the framework. It can be invoked to execute
 * an instruction file or to take commands from an interactive shell.
 * <p>
 * To make a new parser that checks for a different set of commands it's suggested to extend this class
 * and override the {@link #loadCommandsSemantic()} method, which is the one responsible for the
 * binding between commands and semantics and for adding them to the {@link #command} variable.
 * <p>
 * Each command is represented as a pair ({@link String}, {@link Semantic}), contained in the command variable.
 * The String is the command name, which will be looked for by the parser.
 * The Semantic is an interface containing the method {@link Semantic#exec}, which is the one
 * that will contain the command instructions.
 *
 * The exec function can use the variables declared in the {@link #env} variable,
 * which is an instance of {@link Env}.
 * <p>
 * Follows an example code which uses the above mentioned technique.
 * <pre>
 * {@code
 *  this.command.put("mycommand", new Semantic(){
 *      public void exec(String args)
 *      {
 *          warn("You're now running the " + env.current_command + "command!";
 *      }
 *  });
 * }
 * </pre>
 * This example is quite simple but shows everything needed to add a new command.
 */
public class JPCParser
{
    /**
     * Pattern to check if there's any double quotes in the string.
     */
    public final Pattern CHECK_STRING_START = Pattern.compile("[ ]*\".*");

    /**
     * Pattern to check if there are matching double quotes, in the string.
     *
     * Skips escaped quotes (i.e. \").
     */
    public final Pattern CHECK_STRING_MATCHING_DELIMITERS = Pattern.compile("[ ]*\".*[^\\\\]\"");

    /**
     * Pattern to find pieces of strings surrounded by quotes.
     *
     * Skips escaped quotes (i.e. \").
     */
    public final Pattern FIND_WHOLE_STRING = Pattern.compile("[ ]*\"[^\"[\\\\\"]]*[^\\\\]\"");


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
     * Internal class to represent the environment.
     */
    protected class Env
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
        protected Env(){}
    }

    /**
     * Constructs a parser from an instruction file.
     *
     * @param instructionsFile the instructions file
     * @param verbose flag to set the level of verbosity
     */
    public JPCParser(File instructionsFile, boolean verbose)
    {
        this.instructionsFile = instructionsFile;
        env = new Env();
        env.verb = verbose;
        this.loadCommandsSemantic();
    }

    /**
     * Constructs a parser for an interactive shell.
     *
     * @param verbose flag to set the level of verbosity
     */
    public JPCParser(boolean verbose)
    {
        this.instructionsFile = null;
        env = new Env();
        env.verb = verbose;
        this.loadCommandsSemantic();
    }

    /**
     * Creates a binding between each command name
     * and its meaning.
     *
     * If you want to extend the recognized grammar, override this function or
     * edit the 'command' variable.
     */
    protected void loadCommandsSemantic()
    {
        this.command = new TreeMap<>();

        // addpersonaldata
        this.command.put("addpersonaldata", new Semantic(){
            public void exec(String[] args)
            {
                warn("command not yet implemented");
            }
        });

        // clustering
        this.command.put("clustering", new Semantic(){
            public void exec(String[] args)
            {
                warn("command not yet implemented");
            }
        });

        // exit
        this.command.put("exit", new Semantic(){
            /**
             * Command to exit.
             *
             * Syntax: exit [exit_status]
             *
             * Arguments:
             * exit_status: optional integer to force exit status
             *
             * @param args the command arguments
             */
            public void exec(String[] args)
            {
                switch (args.length)
                {
                    case 1:
                        try
                        {
                            env.exit_status = Integer.parseInt(args[0]);
                        }
                        catch (NumberFormatException e)
                        {
                            warn("argument is not an integer");
                        }
                        /* continue to case 0 */

                    case 0:
                        if(env.verb){log("Calling exit command...");}
                        env.force_exit = true;
                        log("Goodbye!");
                        break;

                    default:
                        warn("too many arguments");
                }

            }
        });

        // forcek
        this.command.put("forcek", new Semantic(){
            public void exec(String[] args)
            {
                warn("command not yet implemented");
            }
        });

        // initcommunities
        this.command.put("initcommunities", new Semantic(){
            public void exec(String[] args)
            {
                warn("command not yet implemented");
            }
        });

        // initmodeling
        this.command.put("initcommunities", new Semantic(){
            public void exec(String[] args)
            {
                warn("command not yet implemented");
            }
        });

        // initsets
        this.command.put("initsets", new Semantic(){
            public void exec(String[] args)
            {
                warn("command not yet implemented");
            }
        });

        // initnetwork
        this.command.put("initnetwork", new Semantic(){
            public void exec(String[] args)
            {
                warn("command not yet implemented");
            }
        });

        // loaddataset
        this.command.put("loaddataset", new Semantic(){
            /**
             * Command to load a dataset.
             *
             * Syntax: loaddateset \<dataset_file\>
             *
             * Arguments:
             * dataset_file: path to a dataset file
             *
             * @param args the command arguments
             */
            public void exec(String[] args)
            {
                switch (args.length)
                {
                    case 0:
                        warn("missing operand: <dataset_file>");
                        break;

                    case 1:
                        String filePath = args[0];
                        try
                        {
                            File datasetFile = new File(filePath);
                            env.dataset = new Movielens(datasetFile);
                            if(env.verb){log("dataset " + filePath + " loaded");}
                        }
                        catch (FileNotFoundException e)
                        {
                            err("dataset file not found: " + filePath);
                            env.exit_status = 1;
                        }
                        catch (InputMismatchException e)
                        {
                            err(e.getMessage() + " in " + filePath);
                        }
                        catch (IllegalStateException e)
                        {
                            err(e.getMessage() + " in " + filePath);
                        }
                        break;

                    default:
                        warn("too many arguments");
                        break;

                }
            }
        });

        // personalpredictions
        this.command.put("personalpredictions", new Semantic(){
            public void exec(String[] args)
            {
                warn("command not yet implemented");
            }
        });

        // workdir
        this.command.put("workdir", new Semantic(){
            /**
             * Command to change the current working directory.
             *
             * Syntax: workdir \<directory\>
             *
             * Arguments:
             * directory: path to the new working directory
             *
             * @param args the command arguments
             */
            public void exec(String[] args) {
                switch (args.length) {
                    case 0:
                        warn("missing operand: <directory>");
                        break;

                    case 1:
                        String path = args[0];

                        File folder = new File(workdir(path));
                        if (!folder.exists()) {
                            warn("folder does not exist: " + path);
                        } else {
                            env.work_directory = path;
                            if (env.verb) {
                                log("Changed workdir: " + path);
                            }
                        }
                        break;

                    default:
                        warn("too many arguments: " + args[0] + ", " + args[1]);
                        break;
                }
            }
        });

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
    public int parse() throws FileNotFoundException
    {
        Scanner instructions;
        String statement = null;
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
                            args.add(instructionReader.findInLine(FIND_WHOLE_STRING));
                        }/* else it's a malformed string */
                        else
                        {
                            warn("missing closing \"");
                            break;
                        }
                        instructionReader.useDelimiter("\\p{javaWhitespace}+");
                    }/* if there's no " character take it as literal */
                    else
                    {
                        args.add(instructionReader.next());
                    }
                }
                /* Convert the ArrayList to a String array */
                this.command.get(command).exec(args.toArray(new String[0]));
            }
            else
            {
                warn("command not found: " + instruction);
            }
        }
    }
}