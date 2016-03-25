package it.unica.foresee.datasets;

import java.util.TreeSet;

/**
 * Provides methods to use the freely available movielens dataset.
 */
public class Movielens extends DatasetSparseVector<MovielensElement>
{
    /**
     * Amount of movie rates.
     */
    private int moviesAmount;

    /**
     * Amount of user rates.
     */
    private int usersAmount;

    /**
     * Highest user ID
     */
    private int maxUserID;

    /**
     * Highest movie ID
     */
    private int maxMovieID;

    /**
     * Max rate.
     */
    public final int MAX_RATE = 5;

    /**
     * Min rate.
     */
    public final int MIN_RATE = 1;

    /**
     * List of users.
     */
    protected TreeSet<Integer> usersSet;

    /**
     * List of movies.
     */
    protected TreeSet<Integer> moviesSet;

    /* Getter */

    /**
     * Get a triple value in one single shot.
     * @param userID the user ID
     * @param movieID the movie ID
     * @return the rating given by the user on that movie
     */
    public IntegerElement get(Integer userID, Integer movieID)
    {
        return this.get(userID).get(movieID);
    }

    /**
     * Get a triple value in one single shot.
     * @param userID the user ID
     * @param movieID the movie ID
     * @return the rating given by the user on that movie
     */
    public Integer getElement(Integer userID, Integer movieID)
    {
        return this.get(userID).get(movieID).getElement();
    }

    public int getMaxMovieID() {
        return maxMovieID;
    }

    public int getMaxUserID() {
        return maxUserID;
    }

    public int getMoviesAmount() {
        return moviesAmount;
    }

    public int getUsersAmount() {
        return usersAmount;
    }

    public TreeSet<Integer> getMoviesSet() {
        return moviesSet;
    }

    public TreeSet<Integer> getUsersSet() {
        return usersSet;
    }

    /* Setter */

    /**
     * Add a triple in one single shot.
     * @param userID the user ID
     * @param movieID the movie ID
     * @param rating the rating given by the user on that movie
     */
    public void put(Integer userID, Integer movieID, Integer rating)
    {
        MovielensElement el = this.get(userID);

        if (el == null)
        {
            el = new MovielensElement();
        }

        el.put(movieID, rating);
        this.put(userID, el);
    }

    public void setMaxMovieID(int maxMovieID) {
        this.maxMovieID = maxMovieID;
    }

    public void setMaxUserID(int maxUserID) {
        this.maxUserID = maxUserID;
    }

    public void setMoviesAmount(int moviesAmount) {
        this.moviesAmount = moviesAmount;
    }

    public void setMoviesSet(TreeSet<Integer> moviesSet) {
        this.moviesSet = moviesSet;
    }

    public void setUsersAmount(int usersAmount) {
        this.usersAmount = usersAmount;
    }

    public void setUsersSet(TreeSet<Integer> usersSet) {
        this.usersSet = usersSet;
    }

    /* Modifiers */
    public void incrementMoviesAmount()
    {
        this.moviesAmount++;
    }

    public void incrementUsersAmount()
    {
        this.usersAmount++;
    }
}
