package ua.edu.sumdu.j2se.panchenko.tasks;

import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Class that contains the logic of creating a tasks list.
 */
public class ArrayTaskList extends AbstractTaskList implements Cloneable {
    private Task[] taskArray;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Constructor creates a list of tasks.
     */
    public ArrayTaskList() {
        taskArray = new Task[DEFAULT_CAPACITY];
    }

    /**
     * Method that adds the specified task to the list.
     */
    public void add(Task task) {
        if (task == null) {
            throw new NullPointerException("Empty link cannot be added");
        }
        if (size == taskArray.length) {
            double newLength = taskArray.length * 1.5 + 1;
            taskArray = Arrays.copyOf(taskArray, (int) newLength);
        }
        taskArray[size++] = task;
    }

    /**
     * Method that removes a task from the list and returns true if such a task was on the list.
     */
    public boolean remove(Task task) {
        if (task == null) {
            throw new NullPointerException("Empty link cannot be removed");
        }
        Task[] temp = new Task[Math.max(DEFAULT_CAPACITY, taskArray.length - 1)];
        for (int i = 0; i < taskArray.length; i++) {
            if (taskArray[i] == task) {
                for (int index = 0; index < i; index++) {
                    temp[index] = taskArray[index];
                }
                for (int j = i; j < taskArray.length - 1; j++) {
                    temp[j] = taskArray[j + 1];
                }
                taskArray = Arrays.copyOf(temp, temp.length);
                size--;
                return true;
            }
        }
        throw new IllegalArgumentException("This task does not belong to this list");
    }

    /**
     * Method that returns the number of tasks in the list.
     */
    public int size() {
        return size;
    }

    /**
     * Method that returns the task that is in the specified place in the list (the first task has an index of 0).
     */
    public Task getTask(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException("The index is out of range for the list");
        }
        return taskArray[index];
    }

    /**
     * The method that creates a stream from a ArrayTaskList.
     */
    public Stream<Task> getStream() {
        return Arrays.stream(taskArray);
    }

    public ArrayTaskList clone() {
        try {
            ArrayTaskList result = (ArrayTaskList) super.clone();
            result.taskArray = taskArray.clone();
            return result;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

