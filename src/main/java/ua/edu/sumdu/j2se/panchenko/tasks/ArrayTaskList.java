package ua.edu.sumdu.j2se.panchenko.tasks;

import java.util.Arrays;

/**
 * Class that contains the logic of creating a tasks list.
 */
public class ArrayTaskList {
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
            throw new IllegalArgumentException("Empty link cannot be added");
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
            throw new IllegalArgumentException("Empty link cannot be removed");
        }
        if (size == 0) {
            throw new IndexOutOfBoundsException("ArrayTaskList is empty");
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
     * Method that returns a subset of tasks that are scheduled to run at least once after the time "from" and no later than "to".
     */
    public ArrayTaskList incoming(int from, int to) {
        ArrayTaskList incomingList = new ArrayTaskList();
        for (Task task : taskArray) {
            if (task != null) {
                if ((task.nextTimeAfter(from) != -1) && (task.nextTimeAfter(from) <= to)) {
                    incomingList.add(task);
                }
            }
        }
        return incomingList;
    }

    /**
     * Method displays the ArrayTaskList
     */
    public void print() {
        for (Task t : taskArray) {
            if (t != null) {
                System.out.println(t);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        ArrayTaskList atl = (ArrayTaskList) obj;
        if (size != atl.size) {
            return false;
        }
        int resultCounter = 0;
        for (int i = 0; i < size; i++) {
            if (this.getTask(i).equals(atl.getTask(i))) {
                resultCounter++;
            }
        }
        return resultCounter == size;
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        for (int i = 0; i < size; i++) {
            hashCode = 31 * hashCode + taskArray[i].hashCode();
        }
        return hashCode;
    }
}
