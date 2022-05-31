package ua.edu.sumdu.j2se.panchenko.tasks;

/**
 * The abstract class that describes operations that can be used with a task list.
 */

public abstract class AbstractTaskList {
    /**
     * Method that adds the specified task to the list.
     */
    public abstract void add(Task task);

    /**
     * Method that removes a task from the list and returns true if such a task was on the list.
     */
    public abstract boolean remove(Task task);

    /**
     * Method that returns the number of tasks in the list.
     */
    public abstract int size();

    /**
     * Method that returns the task that is in the specified place in the list (the first task has an index of 0).
     */
    public abstract Task getTask(int index);

    /**
     * Method that returns a subset of tasks that are scheduled to run at least once after the time "from" and no later than "to".
     */
    public final AbstractTaskList incoming(int from, int to) {
        AbstractTaskList taskList = null;
        if (this instanceof ArrayTaskList) {
            taskList = TaskListFactory.createTaskList(ListTypes.types.ARRAY);
        } else {
            taskList = TaskListFactory.createTaskList(ListTypes.types.LINKED);
        }
        for (int i = 0; i < size(); i++) {
            if ((getTask(i).nextTimeAfter(from) != -1) && (getTask(i).nextTimeAfter(from) <= to)) {
                taskList.add(getTask(i));
            }
        }
        return taskList;
    }
}
