package it.unica.foresee.tests;

import it.unica.foresee.interpreters.CommandList;
import it.unica.foresee.interpreters.Env;
import it.unica.foresee.interpreters.Interpreter;
import it.unica.foresee.interpreters.Semantic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests the {@link Interpreter} class.
 *
 * Note: this test tests only the Interpreter, to test a specific {@link it.unica.foresee.interpreters.CommandList}
 * please refer to the associated CommandList test.
 */
public class InterpreterTest
{
    /**
     * flag to disable verbose
     */
    public static final boolean VERBOSE_DISABLE = false;

    /**
     * flag to enable verbose
     */
    public static final boolean VERBOSE_ENABLE = true;

    /**
     * output for testing
     */
    private ByteArrayOutputStream out;

    /**
     * standard output
     */
    PrintStream stdout;

    /**
     * exit notification
     */
    private static final String exitNotification = "Log: Leaving with exit status 0";

    /**
     * empty command list
     */
    private static final CommandList EMPTY_CL = () -> null;

    /**
     * Redirects output to testing output.
     */
    public void setTestingOut()
    {
        this.stdout = System.out;
        this.out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(this.out));
    }

    /**
     * Resets the output stream.
     */
    public void setStandardOut()
    {
        System.setOut(this.stdout);
    }

    /**
     * Obtain the string from the output.
     * @return a string representing the printed output
     */
    public String getOutput()
    {
        return this.out.toString();
    }

    /**
     * Creates a test file in the default temporary files directory.
     * @return an empty temporary file
     */
    public File getTestFile() throws IOException
    {
        String name = "fs-interpreter-test";
        return File.createTempFile(name, ".fs");
    }

    /**
     * Tests the response to an empty command and the loading of a file.
     */
    @Test
    public void emptyCommand() throws IOException
    {
        File temp = getTestFile();
        Interpreter i = new Interpreter(temp, VERBOSE_DISABLE, EMPTY_CL);
        setTestingOut();
        i.run();
        setStandardOut();
        assertEquals("An empty instructions file should not produce output.",
                "",
                getOutput());
    }

    /**
     * Tests the matching of strings.
     *
     * To add another test for matches,
     * add another line to testMatch containing the argument to check and
     * add another line to correctMatches containing the expected result.
     *
     */
    @Test
    public void testStringsInQuotes() throws IOException
    {
        File temp = getTestFile();
        Env fakeEnv = new Env();

        /* Strings to check */
        String testMatch = "a\n" +
                "a b\n" +
                "\"a b\"\n" +
                "\"a \\\" b\"\n" +
                "\"a \\\\ b\"\n" +
                "\"a \\\"\"\n" +
                "\"a \\\\\"\n" +
                "\"a \\\"";

        /* Expected results */
        String[] correctMatches = ("#a#\n" +
                "#a#b#\n" +
                "#a b#\n" +
                "#a \" b#\n" +
                "#a \\ b#\n" +
                "#a \"#\n" +
                "#a \\#\n" +
                "Warning: missing closing \""
        ).split("\n");

        /* create a command to print the arguments according to the matches */
        Semantic s = (args, env) -> {
            String s1 = "#";
            for (String arg : args)
            {
                s1 += arg + "#";
            }
            System.out.println(s1);
            return env;
        };

        /* bind the command to the command name */
        CommandList cl = () -> {
            TreeMap<String, Semantic> t = new TreeMap<>();
            t.put("match",s);
            return t;
        };

        /* Create an instruction file from the testMatch */
        PrintStream temp_p = new PrintStream(temp);
        for (String c : testMatch.split("\n"))
        {
            temp_p.println("match " + c);
        }

        /* Execute the file */
        Interpreter p = new Interpreter(temp, VERBOSE_DISABLE, cl);
        setTestingOut();
        p.run();
        setStandardOut();

        /* Assert everything works */
        String[] outputLines = getOutput().split("\n");

        for(int i = 0; i < outputLines.length; i++)
        {
            assertEquals("Mismatching on line " + (i+1),
                    correctMatches[i],
                    outputLines[i]);
        }
    }
}
