package ua.edu.sumdu.j2se.panchenko.tasks;

import java.util.*;

/**
 * The abstract class that describes operations that can be used with a task list.
 */

public abstract class AbstractTaskList implements Iterable<Task> {
    public Iterator<Task> iterator() {
        return new Itr();
    }

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
        AbstractTaskList taskList;
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

    private class Itr implements Iterator<Task> {
        int modCount = 0;
        int cursor = 0;
        int lastRet = -1;
        int expectedModCount = modCount;

        public boolean hasNext() {
            return cursor != size();
        }

        public Task next() {
            checkForComodification();
            try {
                int i = cursor;
                Task next = getTask(i);
                lastRet = i;
                cursor = i + 1;
                return next;
            } catch (IndexOutOfBoundsException e) {
                checkForComodification();
                throw new NoSuchElementException(e);
            }
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                AbstractTaskList.this.remove(getTask(lastRet));
                if (lastRet < cursor)
                    cursor--;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

    @Override
    public String toString() {
        Iterator<Task> it = iterator();
        if (!it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (; ; ) {
            Task t = it.next();
            sb.append('{').append(t).append('}');
            if (!it.hasNext())
                return sb.append(']').toString();
            sb.append(',').append(' ');
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AbstractTaskList))
            return false;

        Iterator<Task> e1 = iterator();
        Iterator<?> e2 = ((AbstractTaskList) o).iterator();
        while (e1.hasNext() && e2.hasNext()) {
            Object o1 = e1.next();
            Object o2 = e2.next();
            if (!(Objects.equals(o1, o2)))
                return false;
        }
        return !(e1.hasNext() || e2.hasNext());
    }

    @Override
    public int hashCode() {
        int hashCode = 1;
        for (int i = 0; i < size(); i++) {
            hashCode = 31 * hashCode + this.getTask(i).hashCode();
        }
        return hashCode;
    }
}
