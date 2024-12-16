/**
 * An interface for commands that can be executed and undone.
 * A command can be either reversible or irreversible.
 * A reversible command can be undone and redone.
 * An irreversible command can only be executed once.
 */
package Util;

public interface Command {
    void execute();
    void unexecute();
    boolean isReversible();
}