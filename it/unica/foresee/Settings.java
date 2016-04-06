package it.unica.foresee;

import java.io.File;

/**
 * Holds the settings of the application.
 */
public class Settings
{
    /* Constants */

    /**
     * Directory where to store the snapshots.
     */
    public final String SNAP_DIR = "snapshots" + File.separator;

    /**
     * Name of the snapshot file.
     */
    public final String SNAP_FILE_NAME = "snapshot";

    /**
     * Extension for the snapshots file
     */
    public final String SNAP_FILE_EXT = ".snap";

    /* Flags */

    /**
     * Enable snapshots.
     */
    private boolean snapshootingEnabled = true;

    /**
     * Shows an help message.
     */
    private boolean helpMode = false;

    /**
     * Enables the interactive shell mode.
     */
    private boolean interactive = false;

    /**
     * Enables the compatibility mode with the legacy ART interpreter.
     */
    private boolean legacy = false;

    /**
     * Enables the verbose mode.
     */
    private boolean verbose = false;

    /* Other settings */

    /**
     * The name of the commandList to use.
     */
    private String commandList;

    /**
     * The path to the instruction file.
     */
    private String instructionPath;

    /**
     * The work directory is where temporary files, buffers and snapshots are stored.
     */
    private String workDirectory = "workdir" + File.separator;

    /**
     * The snapshot
     */

    /**
     * Initializes all the flags to false and #selection to its default value.
     */
    public Settings(){}


    /*----------- Getter -----------*/

    public boolean isSnapshootingEnabled() {
        return snapshootingEnabled;
    }

    public boolean isCommandListSet() {
        return commandList != null;
    }

    public boolean isHelpMode() {
        return helpMode;
    }

    public boolean isInstructionPathSet() {
        return instructionPath != null;
    }

    public boolean isInteractive() {
        return interactive;
    }

    public boolean isLegacy() {
        return legacy;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public String getCommandList() {
        return commandList;
    }

    public String getInstructionPath() {
        return instructionPath;
    }

    /**
     * Get the current work directory path.
     *
     * The work directory is where temporary files
     * buffers and snapshots are stored.
     * @return the path to the work directory
     */
    public String getWorkDirectory(){
        return this.workDirectory;
    }


    /*----------- Setter -----------*/

    public void setSnapshootingEnabled(boolean snapshootingEnabled) {
        this.snapshootingEnabled = snapshootingEnabled;
    }

    public void setCommandList(String commandList) {
        this.commandList = commandList;
    }

    public void setHelpMode(boolean helpMode) {
        this.helpMode = helpMode;
    }

    public void setInstructionPath(String instructionPath) {
        this.instructionPath = instructionPath;
    }

    public void setInteractive(boolean interactive) {
        this.interactive = interactive;
    }

    public void setLegacy(boolean legacy) {
        this.legacy = legacy;
    }

    public void setVerbose(boolean verbose) {
        this.verbose = verbose;
    }

    /**
     * Sets the current work directory path.
     *
     * The work directory is where temporary files
     * buffers and snapshots are stored.
     *
     * @param workDirectory the path to the work directory
     */
    public void setWorkDirectory(String workDirectory)
    {
        // Add a separator at the end of the string if not already present
        if (!workDirectory.endsWith(File.separator))
        {
            workDirectory += File.separator;
        }

        this.workDirectory = workDirectory;
    }

}
