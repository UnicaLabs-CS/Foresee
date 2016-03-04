package it.unica.foresee;

/**
 * Holds the settings of the application.
 */
public class Settings
{
    private boolean commandListSet;

    private boolean instructionPathSet;

    private boolean interactive;

    private boolean legacy;

    private boolean verbose;

    /**
     * The name of the commandList to use.
     */
    private String commandList;


    /**
     * The path to the instruction file.
     */
    private String instructionPath;

    public Settings(){}

    public boolean isCommandListSet() {
        return commandListSet;
    }

    public boolean isInstructionPathSet() {
        return instructionPathSet;
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

    public void setCommandList(String commandList) {
        this.commandList = commandList;
    }

    public void setCommandListSet(boolean commandListSet) {
        this.commandListSet = commandListSet;
    }

    public void setInstructionPath(String instructionPath) {
        this.instructionPath = instructionPath;
    }

    public void setInstructionPathSet(boolean instructionPathSet) {
        this.instructionPathSet = instructionPathSet;
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
