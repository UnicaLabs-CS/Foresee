package it.unica.foresee.core;

import it.unica.foresee.core.interfaces.Env;
import it.unica.foresee.core.interfaces.State;
import it.unica.foresee.Settings;

import static it.unica.foresee.utils.Tools.err;
import static it.unica.foresee.utils.Tools.warn;
import static it.unica.foresee.utils.Tools.log;

import java.io.*;

/**
 * Core of the framework.
 *
 * This class is a sort of manager, responsible for allowing all the other
 * classes to work together.
 */
public class Core implements it.unica.foresee.core.interfaces.Core
{
    private final String SNAP_DIR = "snapshots" + File.separator;
    private final String SNAP_FILE_NAME = "snapshot";
    private boolean forceExit = false;
    private Env env;
    private Settings settings;
    private File instructionsFile;
    private int lineNumber;

    /**
     * The next saved snapshot is going to have this number.
     *
     * It allows cycling snapshots files.
     */
    private int snapshotNumber = 0;

    /**
     * The highest number of saved snapshots.
     */
    private int maxSnapshotNumber = 5;

    /**
     * Set the flag to forcefully exit from execution.
     * @param forceExit
     */
    private void setForceExit(boolean forceExit)
    {
        this.forceExit = forceExit;
    }

    /**
     * Get the snapshot number for the file name.
     *
     * This function also updates the associated variable, to
     * always obtain the right number.
     * @return the snapshot number converted to string
     */
    private int getSnapshotNumber()
    {
        int lastSnapshotNumber = snapshotNumber;

        // Keep the snapshot number updated
        snapshotNumber++;
        if (snapshotNumber >= maxSnapshotNumber)
        {
            snapshotNumber = 0;
        }
        return lastSnapshotNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State makeSnapshot()
    {
        return new it.unica.foresee.core.State();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State makeSnapshot(Env e, Settings s, File instructionsFile, int lineNumber)
    {
        return new it.unica.foresee.core.State(e, s, instructionsFile, lineNumber);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveSnapshot()
    {
        saveSnapshot(makeSnapshot());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveSnapshot(State s)
    {
        try
        {
            String fileName = env.getWorkDirectory() + this.SNAP_DIR +
                    this.SNAP_FILE_NAME + this.snapshotNumber + ".snap";
            FileOutputStream snapshotFile = new FileOutputStream(fileName);
            ObjectOutputStream snapshot = new ObjectOutputStream(snapshotFile);

            snapshot.writeObject(s);
            snapshot.close();
        }
        catch (FileNotFoundException e)
        {
            warn(e.getMessage());
        }
        catch (IOException e)
        {
            warn(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void restoreSnapshot(String fileName)
    {
        try
        {
            FileInputStream snapshotFile = new FileInputStream(fileName);
            ObjectInputStream snapshot = new ObjectInputStream(snapshotFile);

            State s = (State) snapshot.readObject();
            snapshot.close();

            this.env = s.getCurrentEnv();
            this.settings = s.getSettings();
            this.lineNumber = s.getLineNumber();
            this.instructionsFile = s.getInstructionsFile();

        }
        catch (FileNotFoundException e)
        {
            err(e.getMessage());
            setForceExit(true);
        }
        catch (IOException e)
        {
            err(e.getMessage());
            setForceExit(true);
        }
        catch (ClassNotFoundException e)
        {
            err(e.getMessage());
            setForceExit(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateEnv(Env e)
    {
        this.env = e;
    }

    /**
     * {@inheritDoc}
     * @// TODO: 30/03/16 implement this function
     */
    @Override
    public void printOutput(Iterable output) {
        
    }

    /**
     * {@inheritDoc}
     * @// TODO: 30/03/16 implement this function
     */
    @Override
    public void saveOutput(Iterable output) {

    }
}
