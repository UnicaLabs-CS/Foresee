package it.unica.foresee.commandlists.interfaces;

import it.unica.foresee.core.Env;

import java.util.Map;

/**
 * Interface to implement a list of commands to be used by the {@link it.unica.foresee.core.Core} class.
 *
 * To make a new command list that checks for a different set of commands implement this interface
 * and override the {@link #loadCommandsSemantic} method, which is the one responsible for the
 * binding between commands and semantics and for adding them to
 * the {@link it.unica.foresee.core.Core#commandList} variable.
 * <p>
 * Each command is represented as a pair ({@link String}, {@link Semantic}).
 * The String is the command name, which will be looked for by the interpreter.
 * The Semantic is an interface containing the method {@link Semantic#exec}, which is the one
 * that will contain the command instructions.
 *
 * The exec function can use the variables declared in the {@link it.unica.foresee.core.Core#env} variable,
 * which is an instance of {@link Env}.
 * <p>
 * Follows an example code which uses the above mentioned technique.
 * <pre>
 * {@code
 *  command.put("mycommand", new Semantic(){
 *      public void exec(String args)
 *      {
 *          warn("You're now running the " + env.current_command + "command!";
 *      }
 *  });
 * }
 * </pre>
 * This example is very simple but shows everything needed to add a new command.
 */
public interface CommandList extends Map<String, Semantic>
{
    /**
     * Creates a binding between each command name
     * and its meaning.
     *
     * If you want to extend the recognized command list, override this function.
     */
    CommandList loadCommandsSemantic();
}