package model;

import Util.Command;

import java.util.Stack;

public class CommandManager {
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();

    /**
     * Execute a command and store it in the undo stack if reversible.
     */
    public void executeCommand(Command command) {
        command.execute();
        if (command.isReversible()) {
            undoStack.push(command);
            redoStack.clear();
        }
    }

    /**
     * Undo the last command and store it in the redo stack.
     */
    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.unexecute();
            redoStack.push(command);
        } else {
            System.out.println("Nothing to undo.");
        }
    }

    /**
     * Redo the last undone command and store it in the undo stack.
     */
    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        } else {
            System.out.println("Nothing to redo.");
        }
    }
}

