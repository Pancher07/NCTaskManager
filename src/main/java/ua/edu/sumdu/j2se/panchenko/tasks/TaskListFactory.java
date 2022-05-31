package ua.edu.sumdu.j2se.panchenko.tasks;

/**
 * The class that has the static method that creates an ArrayTaskList and LinkedTaskList object depending on the parameter passed to it.
 */
public class TaskListFactory {
    /**
     * The method, according to the type parameter, returns an object of class ArrayTaskList or LinkedTaskList.
     */
    public static AbstractTaskList createTaskList(ListTypes.types type) {
        if (type == ListTypes.types.ARRAY) {
            return new ArrayTaskList();
        } else {
            return new LinkedTaskList();
        }
    }
}
