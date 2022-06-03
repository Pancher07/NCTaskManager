package ua.edu.sumdu.j2se.panchenko.tasks;

/**
 * Base class for main objects - tasks.
 */
public class Task implements Cloneable {
    private String title;
    private int time;
    private int start;
    private int end;
    private int interval;
    private boolean active;
    private boolean repeated;

    /**
     * Create an inactive task that runs at a specified time without repeating the specified name.
     */
    public Task(String title, int time) {
        if (title == null) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (time < 0) {
            throw new IllegalArgumentException("Time cannot be a negative number");
        }
        this.title = title;
        this.time = time;
        repeated = false;
        active = false;
    }

    /**
     * Create an inactive task that runs at a specified time (both start and end inclusive)
     * at a specified interval and has a specified name.
     */
    public Task(String title, int start, int end, int interval) {
        if (title == null) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        if (start < 0 || end < 0) {
            throw new IllegalArgumentException("Time (start or end) cannot be a negative number");
        }
        if (interval <= 0) {
            throw new IllegalArgumentException("The repetition interval of the task must be greater than zero");
        }
        this.title = title;
        this.start = start;
        this.end = end;
        this.interval = interval;
        repeated = true;
        active = false;
    }

    /**
     * Method for reading the name of the task.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Method for setting the name of the task.
     */
    public void setTitle(String title) {
        if (title == null) {
            throw new NullPointerException("Title cannot be empty");
        }
        this.title = title;
    }

    /**
     * Methods for reading of the task status.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Methods for setting of the task status.
     *
     * @param active can set 2 task statuses: active or inactive.
     */
    public void setActive(boolean active) {
        this.active = active;
    }


    /**
     * Method for reading execution time for non-repetitive task.
     *
     * @return variable "time" for a task that does not repeat. Return variable "start" for a task that does repeat.
     */
    public int getTime() {
        if (!this.repeated) {
            return time;
        } else {
            return start;
        }
    }

    /**
     * Method for changing execution time for non-repetitive task. If the task is repeated, it becomes non-repetitive.
     */
    public void setTime(int time) {
        if (time < 0) {
            throw new IllegalArgumentException("Time cannot be a negative number");
        }
        if (!this.repeated) {
            this.time = time;
        } else {
            this.repeated = false;
            this.time = time;
        }
    }

    /**
     * Method for reading start time for repetitive tasks.
     *
     * @return variable "start" for repetitive task. Return variable "time" if task is non-repetitive.
     */
    public int getStartTime() {
        if (this.repeated) {
            return start;
        } else {
            return time;
        }
    }

    /**
     * Method for reading the end time for repetitive tasks.
     *
     * @return variable "end" for repetitive task. Return variable "time" if task is non-repetitive.
     */
    public int getEndTime() {
        if (this.repeated) {
            return end;
        } else {
            return time;
        }
    }

    /**
     * Method for reading the interval for repetitive tasks.
     *
     * @return variable "interval" for repetitive tasks. Return 0 for non-repetitive tasks.
     */
    public int getRepeatInterval() {
        if (this.repeated) {
            return interval;
        } else {
            return 0;
        }
    }

    /**
     * Method for changing execution time for repetitive task. If the task is not repeated, it becomes repeatable.
     */
    public void setTime(int start, int end, int interval) {
        if (start < 0 || end < 0) {
            throw new IllegalArgumentException("Time (start or end) cannot be a negative number");
        }
        if (interval <= 0) {
            throw new IllegalArgumentException("The repetition interval of the task must be greater than zero");
        }
        if (this.repeated) {
            this.start = start;
            this.end = end;
            this.interval = interval;
        } else {
            repeated = true;
            this.start = start;
            this.end = end;
            this.interval = interval;
        }
    }

    /**
     * Method for checking the repeatability of the task.
     */
    public boolean isRepeated() {
        return repeated;
    }

    /**
     * Method of checking the next task.
     *
     * @param current - specified current time.
     * @return the time of the next task after the specified current time.
     * If after the specified time the task is not executed, or is inactive, the method returns -1.
     */
    public int nextTimeAfter(int current) {
        if (current < 0) {
            throw new IllegalArgumentException("Current time cannot be a negative number");
        }
        if (this.getEndTime() > current && this.active) {
            if (this.repeated) {
                for (int i = this.start; i < this.end; i += interval) {
                    if (current < i) {
                        return i;
                    }
                }
            } else {
                return time;
            }
        }
        return -1;
    }

    public Task clone() {
        try {
            return (Task) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
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
        Task task = (Task) obj;
        if (this.title == null) {
            return task.title == null;
        }
        if (!this.isRepeated() && !task.isRepeated() && this.title.equals(task.title) && this.time == task.time) {
            return true;
        } else
            return this.isRepeated() && task.isRepeated() && this.title.equals(task.title) && this.start == task.start
                    && this.end == task.end && this.interval == task.interval;
    }

    @Override
    public int hashCode() {
        int hashCode = title == null ? 0 : title.hashCode();
        if (!this.isRepeated()) {
            hashCode = 31 * hashCode + time;
        } else {
            hashCode = 31 * hashCode + start + end + interval;
        }
        return hashCode;
    }

    @Override
    public String toString() {
        if (isRepeated()) {
            return "Title: " + this.title + ", start: " + this.start + ", end: " + this.end + ", interval: " + this.interval;
        } else {
            return "Title: " + this.title + ", time: " + this.time;
        }
    }
}
