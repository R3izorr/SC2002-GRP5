package main;

/**
 * A minimal "system" contract: initialize once, then run.
 */
public interface ISystem {
    /** Bootstrap repositories, controllers, etc. */
    void initialize();

    /** Kick off the login loop and dashboards. */
    void run();
}
