package command;

import java.util.Stack;

/**
 * Manages commands for undo and redo functionality.
 */
public class CommandManager {
    private Stack<Command> undoStack = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();

    /**
     * Executes a command and adds it to the undo stack.
     * @param command the command to execute
     */
    public void doCommand(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear(); // Clear the redo stack after a new command
    }

    /**
     * Undoes the last executed command.
     */
    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.unexecute();
            redoStack.push(command);
        }
    }

    /**
     * Redoes the last undone command.
     */
    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        }
    }
}
