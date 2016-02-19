package it.unica.foresee.utils;

/**
 * Static utility class.
 */
public class Tools
{
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
        System.out.println("Error: " + msg);
    }

    /**
     * Prints a warning message.
     * @param msg the warning message
     */
    public static void warn(String msg)
    {
        System.out.println("Warning: " + msg);
    }

    /**
     * Prints a log message.
     * @param msg the log message
     */
    public static void log(String msg)
    {
        System.out.println("Log: " + msg);
    }
}