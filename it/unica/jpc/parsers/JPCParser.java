package it.unica.jpc.parsers;

import it.unica.jpc.datasets.Dataset;
import it.unica.jpc.datasets.Movielens;
import static it.unica.jpc.utils.Tools.err;
import static it.unica.jpc.utils.Tools.log;
import static it.unica.jpc.utils.Tools.warn;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * Class to parse the instructions of the program.
 */
public class JPCParser
{
    /**
     * recognized instructions and their semantic
     */
    protected TreeMap<String, Semantic> command;

    /**
     * the environment
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
     * Function that creates a binding between each command name
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
            public void exec(Scanner args)
            {
                warn("command not yet implemented");
            }
        });

        // clustering
        this.command.put("clustering", new Semantic(){
            public void exec(Scanner args)
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
            public void exec(Scanner args)
            {
                if (args.hasNextInt())
                {
                    env.exit_status = args.nextInt();
                }
                if(env.verb){log("Calling exit command...");}
                env.force_exit = true;
                log("Goodbye!");
            }
        });

        // forcek
        this.command.put("forcek", new Semantic(){
            public void exec(Scanner args)
            {
                warn("command not yet implemented");
            }
        });

        // initcommunities
        this.command.put("initcommunities", new Semantic(){
            public void exec(Scanner args)
            {
                warn("command not yet implemented");
            }
        });

        // initmodeling
        this.command.put("initcommunities", new Semantic(){
            public void exec(Scanner args)
            {
                warn("command not yet implemented");
            }
        });

        // initsets
        this.command.put("initsets", new Semantic(){
            public void exec(Scanner args)
            {
                warn("command not yet implemented");
            }
        });

        // initnetwork
        this.command.put("initnetwork", new Semantic(){
            public void exec(Scanner args)
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
            public void exec(Scanner args)
            {
                if (args.hasNext())
                {
                    String filePath = args.next();
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
                }
                else
                {
                    warn("missing operand: <dataset_file>");
                }
            }
        });

        // personalpredictions
        this.command.put("personalpredictions", new Semantic(){
            public void exec(Scanner args)
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
            public void exec(Scanner args)
            {
                if (args.hasNext())
                {
                    String path = args.next();

                    File folder = new File(workdir(path));
                    if (!folder.exists())
                    {
                        warn("folder does not exist: " + path);
                    }
                    else
                    {
                        env.work_directory = path;
                        if(env.verb){log("Changed workdir: " + path);}
                    }
                }
                else
                {
                    warn("missing operand: <directory>");
                }
            }
        });

    }

    /**
     * Helper class to use the current work directory.
     *
     * @param path the path to a file or folder
     * @return a path relative to the current work directory
     */
    protected String workdir(String path)
    {
        return (env.work_directory == null) ? path : env.work_directory + File.separator + path;
    }

    /**
     * Parse the given instructions.
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
     * Execute a single instruction.
     *
     * @param statement the instruction to execute
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
                this.command.get(command).exec(instructionReader);
            }
            else
            {
                warn("command not found: " + instruction);
            }
        }
    }
}