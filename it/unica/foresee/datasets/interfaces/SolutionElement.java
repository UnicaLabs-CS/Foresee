package it.unica.foresee.datasets.interfaces;

/**
 * Represents the solution or output of a certain function.
 */
public interface SolutionElement<T>
{
    /**
     * Obtain the solution.
     * @return solution
     */
    T getSolution();

    /**
     * Set the solution
     * @param t the solution
     */
    void setSolution(T t);
}
