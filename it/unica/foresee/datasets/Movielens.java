package it.unica.foresee.datasets;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.TreeSet;

/**
 * Provides methods to use the freely available movielens dataset.
 *
 * @author Fabio Colella
 */
public class Movielens extends Dataset
{

    /**
     * Empty constructor.
     */
    public Movielens()
    {
        super();
    }

    /**
     * Constructs a movielens from the specified file.
     *
     * {@inheritDoc}
     */
    public Movielens(File sourceFile)  throws FileNotFoundException
    {
        super(sourceFile);
    }

    /**
     * Loads data from a specified source.
     *
     * A movielens ratings file constains lines in the following form:
     * {@literal UserID::MovieID::Rating::Timestamp}
     * <ul>
     * <li>UserIDs range between 1 and 6040</li>
     * <li>MovieIDs range between 1 and 3952</li>
     * <li>Ratings are made on a 5-star scale (whole-star ratings only, 1 to 5)</li>
     * <li>Timestamp is represented in seconds since the epoch as returned by time(2)</li>
     * </ul>
     * Each user has at least 20 ratings.
     * The Timestamp is discarded, while userID, movieID and rating are stored
     * in the object itself.
     *
     * @param sourceFile the file from which to load the data
     */
    @Override
    public void loadDataset(File sourceFile) throws FileNotFoundException
    {
        // Initialize the objects if not already done
        if (this.dataset == null)
        {
            this.dataset = new ArrayList<>();
        }

        if (this.usersSet == null)
        {
            this.usersSet = new TreeSet<>();
        }

        if (this.moviesSet == null)
        {
            this.moviesSet = new TreeSet<>();
        }

        Scanner dataSource = new Scanner(sourceFile);

        int lineNumber = 1; //follows the line number in the file
        int userID;
        int movieID;
        int rating;
        double dRating; //rating converted to double

        // Read the file line by line
        while (dataSource.hasNextLine()) {

            Scanner line = new Scanner(dataSource.nextLine());
            line.useDelimiter("::");

            // Extract a single line of data
            if (line.hasNextInt()) {
                userID = line.nextInt();
                if (userID > this.maxUserID)
                {
                    this.maxUserID = userID;
                }
            } else {
                throw new InputMismatchException("expected userID at line " + lineNumber);
            }

            if (line.hasNextInt()) {
                movieID = line.nextInt();
                if (movieID > this.maxMovieID)
                {
                    this.maxMovieID = movieID;
                }
            } else {
                throw new InputMismatchException("expected movieID at line " + lineNumber);
            }

            if (line.hasNextInt()) {
                rating = line.nextInt();
            } else {
                throw new InputMismatchException("expected rating at line " + lineNumber);
            }

            // Check the correctness of the data
            if (userID < 1) {
                throw new InputMismatchException("userID < 1 at line " + lineNumber);
            }

            if (movieID < 1) {
                throw new InputMismatchException("movieID < 1 at line " + lineNumber);
            }

            if (rating < 1) {
                throw new InputMismatchException("rating < 1 at line " + lineNumber);
            }

            dRating = (double) rating;

            MovieUserRate t = new MovieUserRate(userID, movieID, dRating);

            this.dataset.add(t);
            this.usersSet.add(userID);
            this.moviesSet.add(movieID);

            lineNumber++;
        }

        this.setUsersAmount(this.usersSet.size());
        this.setMoviesAmount(this.moviesSet.size());

        if (this.getUsersAmount() > lineNumber)
        {
            throw new IllegalStateException("The amount of users is higher than entries.");
        }

        if (this.getMoviesAmount() > lineNumber)
        {
            throw new IllegalStateException("The amount of movies is higher than entries.");
        }

        if (this.getUsersAmount() < 20)
        {
            throw new IllegalStateException("The amount of users is lower than 20.");
        }
    }
}
