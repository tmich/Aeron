package it.aeg2000srl.aeron.entities;

/**
 * Created by tiziano.michelessi on 13/10/2015.
 */
public interface Sinchronizable {
    boolean isSynchronized();
    void setSynchronized(boolean sync);
}
