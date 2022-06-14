package ua.edu.sumdu.j2se.panchenko.tasks;

import java.util.*;
import java.util.stream.Stream;

/**
 * The abstract class that describes operations that can be used with a task list.
 */
public abstract class AbstractTaskList implements Iterable<Task> {
    public Iterator<Task> iterator() {
        return new Itr();
    }

    /**
     * The method that adds the specified task to the list.
     */
    public abstract void add(Task task);

    /**
     * The method that removes a task from the list and returns true if such a task was on the list.
     */
    public abstract boolean remove(Task task);

    /**
     * The method that returns the number of tasks in the list.
     */
    public abstract int size();

    /**
     * The method that returns the task that is in the specified place in the list (the first task has an index of 0).
     */
    public abstract Task getTask(int index);

    /**
     * The method that creates a stream from a collection.
     */
    public abstract Stream<Task> getStream();

    private class Itr implements Iterator<Task> {
        /**
         * Index of element to be returned by subsequent call to next.
         */
        int cursor = 0;
        /**
         * Index of element returned by most recent call to next or previous. Reset to -1 if this element
         * is deleted by a call to remove.
         */
        int lastReturned = -1;

        public boolean hasNext() {
            return cursor != size();
        }

        public Task next() {
            int i = cursor;
            if (i >= size()) {
                throw new NoSuchElementException("No next task");
            }
            Task next = getTask(i);
            lastReturned = i;
            cursor = i + 1;
            return next;
        }

        public void remove() {
            if (lastReturned < 0)
                throw new IllegalStateException("Task was deleted by a call to remove");
            try {
                AbstractTaskList.this.remove(getTask(lastReturned));
                cursor--;
                lastReturned = -1;
            } catch (IndexOutOfBoundsException e) {
                throw new ConcurrentModificationException();
            }
        }
    }

    @Override
    public String toString() {
        Iterator<Task> it = iterator();
        if (!it.hasNext()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (; ; ) {
            Task t = it.next();
            sb.append('{').append(t).append('}');
            if (!it.hasNext()) {
                return sb.append(']').toString();
            }
            sb.append(',').append(' ');
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AbstractTaskList)) {
            return false;
        }
        Iterator<Task> e1 = iterator();
        Iterator<Task> e2 = ((AbstractTaskList) o).iterator();
        while (e1.hasNext() && e2.hasNext()) {
            Task t1 = e1.next();
            Task t2 = e2.next();
            if (!(Objects.equals(t1, t2))) {
                return false;
            }
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
