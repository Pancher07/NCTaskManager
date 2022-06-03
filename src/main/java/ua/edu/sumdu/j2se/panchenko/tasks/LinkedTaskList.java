package ua.edu.sumdu.j2se.panchenko.tasks;

/**
 * Class that contains the logic of creating a tasks list that will store tasks in a linked list.
 */
public class LinkedTaskList extends AbstractTaskList implements Cloneable {
    private Node first;
    private Node last;
    private int size;

    /**
     * Method that adds the specified task to the list.
     */
    public void add(Task task) {
        if (task == null) {
            throw new NullPointerException("Empty link cannot be added");
        }
        final Node l = last;
        final Node newNode = new Node(l, task, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        size++;
    }

    /**
     * Method that removes a task from the list and returns true if such a task was on the list.
     */
    public boolean remove(Task task) {
        if (task == null) {
            throw new NullPointerException("Empty link cannot be removed");
        }
        for (Node x = first; x != null; x = x.next) {
            if (task.equals(x.value)) {
                unlink(x);
                return true;
            }
        }
        throw new IllegalArgumentException("This task does not belong to this list");
    }

    private void unlink(Node x) {
        final Node next = x.next;
        final Node prev = x.prev;
        if (prev == null) {
            first = next;
        } else {
            prev.next = next;
            x.prev = null;
        }
        if (next == null) {
            last = prev;
        } else {
            next.prev = prev;
            x.next = null;
        }
        x.value = null;
        size--;
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
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("The index is out of range for the list!");
        }
        if (index < (size / 2)) {
            Node x = first;
            for (int i = 0; i < index; i++) {
                x = x.next;
            }
            return x.value;
        } else {
            Node x = last;
            for (int i = size - 1; i > index; i--) {
                x = x.prev;
            }
            return x.value;
        }
    }

    private static class Node {
        private Task value;
        private Node prev;
        private Node next;

        Node(Node prev, Task element, Node next) {
            this.prev = prev;
            this.value = element;
            this.next = next;
        }
    }

    public LinkedTaskList clone() {
        try {
            LinkedTaskList result = (LinkedTaskList) super.clone();
            result.first = result.last = null;
            result.size = 0;
            for (Node x = first; x != null; x = x.next) {
                result.add(x.value);
            }
            return result;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
