package it.unica.foresee.datasets.interfaces;

/**
 * Element that can be deep cloned.
 */
public interface DeepClonable {
    /**
     * Create a deep clone of the object.
     * @return the cloned object
     */
    Object deepClone();
}
