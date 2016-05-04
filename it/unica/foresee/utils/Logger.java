package it.unica.foresee.utils;

/**
 * Static logging class.
 */
public class Logger
{
    /**
     * Allow all messages and debug.
     */
    public static final int VERB_DEBUG = -1;

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
    private Logger(){}

    /**
     * Prints a debug message.
     * @param msg the debug message
     */
    public static void debug(String msg)
    {
        if (verbosity <= VERB_DEBUG)
        {
            System.out.println("Debug: " + msg);
        }
    }

    /**
     * Prints a debug message.
     * @param msg the debug message
     * @param className the name of the class invoking the method
     */
    public static void debug(String msg, String className)
    {
        if (verbosity <= VERB_DEBUG)
        {
            System.out.println("Debug in class " + className + ": " + msg);
        }
    }

    /**
     * Prints a debug message.
     * @param msg the debug message
     * @param logClass the class invoking the method
     */
    public static void debug(String msg, Class logClass)
    {
        debug(msg, logClass.getName());
    }

    /**
     * Prints a debug message.
     * @param msg the debug message
     * @param className the name of the class invoking the method
     */
    public static void debug(String msg, String className, String methodName)
    {
        if (verbosity <= VERB_DEBUG)
        {
            System.out.println("Debug in method " + className + "." + methodName + ": " + msg);
        }
    }

    /**
     * Prints a debug message.
     * @param msg the debug message
     * @param logClass the class invoking the method
     */
    public static void debug(String msg, Class logClass, String methodName)
    {
        debug(msg, logClass.getName(), methodName);
    }

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
     * Sets the verbosity level.
     *
     * - -1 show all and debug
     * - 0 show all logs
     * - 1 hide logs
     * - 2 hide wanings and preceding
     * - 3 hide errors and preceding
     *
     * @param verbosity
     */
    public static void setVerbosity(int verbosity)
    {
        Logger.verbosity = verbosity;
    }
}