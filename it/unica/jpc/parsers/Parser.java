package it.unica.jpc.parsers;

import it.unica.jpc.datasets.Movielens;
import static it.unica.jpc.utils.Tools.err;
import static it.unica.jpc.utils.Tools.log;
import static it.unica.jpc.utils.Tools.warn;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Class to parse the instructions of the program.
 */
public class Parser
{
    /**
     * flag for verbose mode
     */
    private boolean verbose;

    /**
     * flag to force exit
     */
    private boolean exit;

    /**
     * file containing the instructions
     */
    private File instructionsFile;

    /**
     * loaded dataset
     */
    private Movielens dataset;

    /**
     * Constructs a parser from an instruction file.
     *
     * @param instructionsFile the instructions file
     * @param verbose flag to set the level of verbosity
     */
    public Parser(File instructionsFile, boolean verbose)
    {
        this.instructionsFile = instructionsFile;
        this.verbose = verbose;
    }

    /**
     * Constructs an interactive parser.
     *
     * @param verbose flag to set the level of verbosity
     */
    public Parser(boolean verbose)
    {
        this.instructionsFile = null;
        this.verbose = verbose;
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
        int exitStatus = 0;
        boolean interactive = false;
        this.exit = false;
        String prompt = ">>>";

        /* If an instruction file exists, use that as source */
        if (instructionsFile != null)
        {
            instructions = new Scanner(instructionsFile);
            if(verbose){log("File " + instructionsFile + "loaded");}
        }/* Else use the standard input */
        else
        {
            instructions = new Scanner(System.in);
            if(verbose){log("Interactive mode initialized");}
            interactive = true;
        }

        if (interactive)
        {
            System.out.print(prompt + " ");
        }

        while (instructions.hasNextLine())
        {
            statement = instructions.nextLine();

            exitStatus = this.execute(statement);

            /* Something triggered the exit command
            or the current state is inconsistent */
            if (exit || exitStatus != 0) {break;}

            if (interactive)
            {
                System.out.print(prompt + " ");
            }
        }

        if(verbose){log("Leaving with exit status " + exitStatus);}
        return exitStatus;
    }

    /**
     * Execute a single instruction.
     *
     * @param statement the instruction to execute
     * @return exit status of the instruction
     */
    private int execute(String statement)
    {
        int exitStatus = 0;
        Scanner statementReader = new Scanner(statement);

        if (statementReader.hasNext())
        {
            String command = statementReader.next();

            /* Find the appropriate command to execute */

            /* Exit command */
            if (command.equals("exit"))
            {
                exitStatus = cExit(statementReader);
            }/*Load dataset*/
            else if(command.equals("load"))
            {
                exitStatus = cLoad(statementReader);
            }/* No command recognized */
            else
            {
                warn("instruction not recognized: " + statement);
            }
        }



        return exitStatus;
    }

    /**
     * Command to exit.
     *
     * Syntax: exit [exit_status]
     *
     * exit_status: optional integer to force exit status
     *
     * @param statementReader the statement
     * @return exit status
     */
    private int cExit(Scanner statementReader)
    {
        int exitStatus = 0;
        if (statementReader.hasNextInt())
        {
            exitStatus = statementReader.nextInt();
        }
        if(verbose){log("Calling exit command...");}
        exit = true;
        log("Goodbye!");

        return exitStatus;
    }

    /**
     * Command to load a dataset.
     *
     * Syntax: load \<dataset_file\>
     *
     * dataset_file: path to a dataset file
     *
     * @param statementReader the statement
     * @return exit status
     */
    private int cLoad(Scanner statementReader)
    {
        int exitStatus = 0;
        if (statementReader.hasNext())
        {
            String filePath = statementReader.next();
            try
            {
                File datasetFile = new File(filePath);
                this.dataset = new Movielens(datasetFile);
                if(verbose){log("dataset " + filePath + " loaded");}
            }
            catch (FileNotFoundException e)
            {
                err("dataset file not found: " + filePath);
                exitStatus = 1;
            }
        }
        else
        {
            warn("missing operand: <dataset_file>");
        }
        return exitStatus;
    }
}