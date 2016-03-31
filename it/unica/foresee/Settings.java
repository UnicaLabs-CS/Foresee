package it.unica.foresee;

/**
 * Holds the settings of the application.
 */
public class Settings
{
    /**
     * Enable snapshots.
     */
    private boolean activeSnapshooting = true;

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

    /**
     * The name of the commandList to use.
     */
    private String commandList;

    /**
     * The path to the instruction file.
     */
    private String instructionPath;

    /**
     * Initializes all the flags to false and #selection to its default value.
     */
    public Settings(){}


    /*----------- Getter -----------*/

    public boolean isActiveSnapshooting() {
        return activeSnapshooting;
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


    /*----------- Setter -----------*/

    public void setActiveSnapshooting(boolean activeSnapshooting) {
        this.activeSnapshooting = activeSnapshooting;
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

}
