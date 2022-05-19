package ua.edu.sumdu.j2se.panchenko.tasks;

import java.util.Arrays;

/**
 * Class that contains the logic of creating a tasks list.
 */
public class ArrayTaskList {
    private Task[] taskArray;
    private int counter = 0;

    private static final int DEFAULT_CAPACITY = 10;

    /**
     * Constructor creates a list of tasks.
     */
    public ArrayTaskList() {
        taskArray = new Task[DEFAULT_CAPACITY];
    }

    /**
     * Constructor creates a list of tasks which is based of incoming tasks array.
     *
     * @param taskArray is the array on which the list is created.
     * @param counter   is the number of incoming tasks.
     */
    private ArrayTaskList(Task[] taskArray, int counter) {
        this.taskArray = taskArray;
        this.counter = counter;
    }

    /**
     * Method that adds the specified task to the list.
     */
    public void add(Task task) {
        if (counter == taskArray.length) {
            double newLength = taskArray.length * 1.5 + 1;
            taskArray = Arrays.copyOf(taskArray, (int) newLength);
        }
        taskArray[counter++] = task;
    }

    /**
     * Method that removes a task from the list and returns true if such a task was on the list.
     */
    public boolean remove(Task task) {
        boolean result = false;
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
                result = true;
                counter--;
                break;
            }
        }
        return result;
    }

    /**
     * Method that returns the number of tasks in the list.
     */
    public int size() {
        return counter;
    }

    /**
     * Method that returns the task that is in the specified place in the list (the first task has an index of 0).
     */
    public Task getTask(int index) {
        return taskArray[index];
    }

    /**
     * Method that returns a subset of tasks that are scheduled to run at least once after the time "from" and no later than "to".
     */
    public ArrayTaskList incoming(int from, int to) {
        Task[] incomingArray = new Task[0];
        int incomingCounter = 0;
        for (Task task : taskArray) {
            if (task != null) {
                if ((task.nextTimeAfter(from) != -1) && (task.nextTimeAfter(from) <= to)) {
                    incomingArray = Arrays.copyOf(incomingArray, incomingCounter + 1);
                    incomingArray[incomingCounter] = task;
                    incomingCounter++;
                }
            }
        }
        return new ArrayTaskList(incomingArray, incomingCounter);
    }

    @Override
    public int hashCode() {
        int result = size() == 0 ? 0 : taskArray[0].getTitle().hashCode();
        for (int i = 0; i < size(); i++) {
            result = 31 * result + taskArray[i].hashCode();
        }
        return result;
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
        if (this.size() != atl.size()) {
            return false;
        }
        int resultCounter = 0;
        for (int i = 0; i < this.size(); i++) {
            if (this.getTask(i).equals(atl.getTask(i))) {
                resultCounter++;
            }
        }
        return resultCounter == this.size();
    }
}
