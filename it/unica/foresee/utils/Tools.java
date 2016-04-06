package it.unica.foresee.utils;

/**
 * Static utility class.
 */
public class Tools
{
    /**
     * Allow all messages.
     */
    public static final int VERB_ALL = 0;

    /**
     * Avoid log level messages. (default)
     */
    public static final int VERB_NO_LOG = 1;

    /**
     * Avoid warning level messages.
     */
    public static final int VERB_NO_WARN = 2;

    /**
     * Avoid error level messages.
     */
    public static final int VERB_NO_ERR = 3;

    /**
     * The verbosity level.
     */
    private static int verbosity = VERB_NO_LOG;

    /**
     * Avoid instantiating this class.
     */
    private Tools(){}

    /**
     * Prints an error message.
     * @param msg the error message
     */
    public static void err(String msg)
    {
        if (verbosity < VERB_NO_ERR)
        {
            System.out.println("Error: " + msg);
        }
    }

    /**
     * Prints a warning message.
     * @param msg the warning message
     */
    public static void warn(String msg)
    {
        if (verbosity < VERB_NO_WARN)
        {
            System.out.println("Warning: " + msg);
        }
    }

    /**
     * Prints a log message.
     * @param msg the log message
     */
    public static void log(String msg)
    {
        if (verbosity < VERB_NO_LOG)
        {
            System.out.println("Log: " + msg);
        }
    }

    /**
     * Sets the verbosity level.
     *
     * - 0 show all
     * - 1 hide logs
     * - 2 hide wanings and preceding
     * - 3 hide errors and preceding
     *
     * @param verbosity
     */
    public static void setVerbosity(int verbosity)
    {
        Tools.verbosity = verbosity;
    }
}