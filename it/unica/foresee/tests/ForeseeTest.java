package it.unica.foresee.tests;

import it.unica.foresee.Foresee;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests the main class {@link Foresee#main(String[])}
 */
public class ForeseeTest
{
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
     * Call the main function using the testing output.
     * @param args main args
     * @return obtained output
     */
    public String callMain(String[] args)
    {
        setTestingOut();
        Foresee.mainHelper(args);
        setStandardOut();
        return getOutput();
    }

    /**
     * Tests the parameter -p
     */
    @Test
    public void testArgP()
    {
        String[] args = {"-p"};
        //System.out.println(callMain(args));

        assertEquals("Option " + args[0] + "is not working as expected",
                "Error: option -p, --path requires <path>",
                callMain(args).split("\n")[0]);

    }
}
