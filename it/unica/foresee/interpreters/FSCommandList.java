package it.unica.foresee.interpreters;

import it.unica.foresee.datasets.Movielens;
import static it.unica.foresee.utils.Tools.err;
import static it.unica.foresee.utils.Tools.log;
import static it.unica.foresee.utils.Tools.warn;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.TreeMap;

/**
 * List of commands for the interpreter and their semantic.
 * <p>
 * Follows a list of the available commands in alphabetical order:
 * <ul>
 *     <li>
 *       <p>
 *       {@code addpersonaldata} - add personal demographic data to the according to the inserted values
 *       <p>
 *       Syntax: {@code addpersonaldata <gender> [age job zipcode]}
 *       <p>
 *       Arguments:
 *       <ul>
 *           <li>
 *               gender:
 *           </li>
 *           <li>
 *               age: optional
 *           </li>
 *           <li>
 *               job: optional
 *           </li>
 *           <li>
 *               zipcode: optional
 *           </li>
 *       </ul>
 *     </li>
 *
 *     <li>
 *       <p>
 *       {@code clustering} - allows to work on the clustering
 *       <p>
 *       Syntax: {@code clustering load|calculate
 *       <cd|kmlocal|kmlocalpersonalprediction|kmeanspersonalprediction|kmeans> [numclusters]}
 *       <p>
 *       Arguments:
 *       <ul>
 *           <li>
 *               load: mark a cluster for loading. This process needs to be finalized with the command
 *               {@code initcommunities}.
 *           </li>
 *           <li>
 *               calculate: calculates the clustering using the specified algorithm among
 *               {@literal <cd|kmlocal|kmlocalpersonalprediction|kmeanspersonalprediction|kmeans>}
 *           </li>
 *           <li>
 *               numclusters: optional integer to specify the cluster number
 *           </li>
 *       </ul>
 *     </li>
 *
 *     <li>
 *       <p>
 *       {@code exit} - command to quit the program
 *       <p>
 *       Syntax: {@code exit [exit_status]}
 *       <p>
 *       Arguments:
 *       <ul>
 *           <li>
 *               exit_status: optional integer to force the exit status
 *           </li>
 *       </ul>
 *     </li>
 *
 *     <li>
 *       <p>
 *       {@code forcek} - force the experiment to use a single fold
 *       <p>
 *       Syntax: {@code forcek <value>}
 *       <p>
 *       Arguments:
 *       <ul>
 *           <li>
 *               value: the k-esim fold on which execute the experiment
 *           </li>
 *       </ul>
 *     </li>
 *
 *     <li>
 *       <p>
 *       {@code initcommunities} - loads a cluster inside the data stucture of the framework loaded with the
 *       {@code clustering} command
 *       <p>
 *       Syntax: {@code initcommunities <clustering_type> [mingroupsize]}
 *       <p>
 *       Arguments:
 *       <ul>
 *           <li>
 *               clustering_type: type of clustering
 *           </li>
 *           <li>
 *               mingroupsize: minimum size of the group
 *           </li>
 *       </ul>
 *     </li>
 *
 *     <li>
 *       <p>
 *       {@code initmodeling} - start the modeling of the groups, according to the specified parameters
 *       <p>
 *       Syntax: {@code initmodeling <modeling|type|prediction_type>
 *           [coratings|coratings top|coratings top trust|coratings top trust personalprediction]}
 *       <p>
 *       Arguments:
 *       <ul>
 *           <li>
 *               options undocumented
 *           </li>
 *       </ul>
 *     </li>
 *
 *     <li>
 *       <p>
 *       {@code initsets} - subdivides the sets in k subsets in preparation for the use of the k-fold algorithm
 *       <p>
 *       Syntax: {@code initsets load|calculate [k-value]}
 *       <p>
 *       Arguments:
 *       <ul>
 *           <li>
 *               calculate: use the dataset to create the k subsets
 *           </li>
 *           <li>
 *               load: load a group of sets already subdivided
 *           </li>
 *           <li>
 *               k-value: optional integer to specify the amount of sets to obtain
 *           </li>
 *       </ul>
 *     </li>
 * </ul>
 *
 */
public class FSCommandList implements CommandList
{

    /**
     * Initialize the commands.
     */
    public TreeMap<String, Semantic> loadCommandsSemantic()
    {

        /* command list */
        TreeMap<String, Semantic> command = new TreeMap<>();

        // addpersonaldata
        command.put("addpersonaldata", (args, env) -> {
            warn("command not yet implemented");
            return env;
        });

        // clustering
        command.put("clustering", (args, env) -> {
            warn("command not yet implemented");
            return env;
        });

        // exit
        command.put("exit", new Semantic(){
            public Env exec(String[] args, Env env)
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
                return env;
            }
        });

        // forcek
        command.put("forcek", (args, env) -> {
            warn("command not yet implemented");
            return env;
        });

        // initcommunities
        command.put("initcommunities", (args, env) -> {
            warn("command not yet implemented");
            return env;
        });

        // initmodeling
        command.put("initmodeling", (args, env) -> {
            warn("command not yet implemented");
            return env;
        });

        // initsets
        command.put("initsets", (args, env) -> {
            warn("command not yet implemented");
            return env;
        });

        // initnetwork
        command.put("initnetwork", (args, env) -> {
            warn("command not yet implemented");
            return env;
        });

        // loaddataset
        command.put("loaddataset", new Semantic(){
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
            public Env exec(String[] args, Env env)
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
                return env;
            }
        });

        // personalpredictions
        command.put("personalpredictions", (args, env) -> {
            warn("command not yet implemented");
            return env;
        });

        // workdir
        command.put("workdir", new Semantic(){
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
            public Env exec(String[] args, Env env) {
                switch (args.length)
                {
                    case 0:
                        warn("missing operand: <directory>");
                        break;

                    case 1:
                        String path = args[0];

                        File folder = new File(path);
                        if (!folder.exists()) {
                            warn("folder does not exist: " + path);
                        } else {
                            env.work_directory = path;
                            if (env.verb) {
                                log("Changed workdir: " + folder.getAbsolutePath());
                            }
                        }
                        break;

                    default:
                        warn("too many arguments: " + args[0] + ", " + args[1]);
                        break;
                }
                return env;
            }
        });
        return command;
    }
}