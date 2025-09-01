package model;

import Util.Command;

import java.util.Stack;

public class CommandManager {
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();


    public void executeCommand(Command command) {
        command.execute();
        if (command.isReversible()) {
            undoStack.push(command);
            redoStack.clear(); // Clear redo stack après une nouvelle action
            System.out.println("Command executed and added to undo stack. Stack size: " + undoStack.size());
        }
    }


    public void undo() {
        if (!undoStack.isEmpty()) {
            Command command = undoStack.pop();
            command.unexecute();
            redoStack.push(command);
            System.out.println("Command undone. Undo stack size: " + undoStack.size() +
                    ", Redo stack size: " + redoStack.size());
        } else {
            System.out.println("Nothing to undo.");
        }
    }


    public void redo() {
        if (!redoStack.isEmpty()) {
            Command command = redoStack.pop();
            command.execute();
            undoStack.push(command);
            System.out.println("Command redone. Undo stack size: " + undoStack.size() +
                    ", Redo stack size: " + redoStack.size());
        } else {
            System.out.println("Nothing to redo.");
        }
    }


    public boolean canUndo() {
        return !undoStack.isEmpty();
    }


    public boolean canRedo() {
        return !redoStack.isEmpty();
    }


    public void clear() {
        undoStack.clear();
        redoStack.clear();
        System.out.println("Command history cleared.");
    }
}