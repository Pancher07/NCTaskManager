package ua.edu.sumdu.j2se.panchenko.tasks.model;

import org.apache.log4j.Logger;
import ua.edu.sumdu.j2se.panchenko.tasks.Main;

import java.time.LocalDateTime;

/**
 * Base class for main objects - tasks.
 */
public class Task implements Cloneable {
    private final static Logger logger = Logger.getLogger(Task.class);
    private String title;
    private LocalDateTime time;
    private LocalDateTime start;
    private LocalDateTime end;
    private int interval;
    private boolean active;
    private boolean repeated;

    /**
     * Create an inactive task that runs at a specified time without repeating the specified name.
     */
    public Task(String title, LocalDateTime time) {
        if (title == null || time == null) {
            logger.error("Title or time cannot be empty");
            throw new IllegalArgumentException("Title or time cannot be empty");
        }
        this.title = title;
        this.time = LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(),
                time.getHour(), time.getMinute(), time.getSecond(), time.getNano());
        repeated = false;
        active = false;
    }

    /**
     * Create an inactive task that runs at a specified time (both start and end inclusive)
     * at a specified interval and has a specified name.
     */
    public Task(String title, LocalDateTime start, LocalDateTime end, int interval) {
        if (title == null || start == null || end == null) {
            logger.error("Title or time cannot be empty");
            throw new IllegalArgumentException("Title or time cannot be empty");
        }
        if (end.isBefore(start)) {
            logger.error("The end time cannot be earlier than the start time");
            throw new IllegalArgumentException("The end time cannot be earlier than the start time");
        }
        if (interval <= 0) {
            logger.error("The repetition interval of the task must be greater than zero");
            throw new IllegalArgumentException("The repetition interval of the task must be greater than zero");
        }
        this.title = title;
        this.start = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(),
                start.getHour(), start.getMinute(), start.getSecond(), start.getNano());
        this.end = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(),
                end.getHour(), end.getMinute(),  end.getSecond(), end.getNano());
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
            logger.error("Title cannot be empty");
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
    public LocalDateTime getTime() {
        if (!this.repeated) {
            return time;
        } else {
            return start;
        }
    }

    /**
     * Method for changing execution time for non-repetitive task. If the task is repeated, it becomes non-repetitive.
     */
    public void setTime(LocalDateTime time) {
        if (time == null) {
            logger.error("Time cannot be empty");
            throw new NullPointerException("Time cannot be empty");
        }
        if (!this.repeated) {
            this.time = LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(),
                    time.getHour(), time.getMinute(), time.getSecond(), time.getNano());
        } else {
            this.repeated = false;
            this.time = LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(),
                    time.getHour(), time.getMinute(), time.getSecond(), time.getNano());
        }
    }

    /**
     * Method for reading start time for repetitive tasks.
     *
     * @return variable "start" for repetitive task. Return variable "time" if task is non-repetitive.
     */
    public LocalDateTime getStartTime() {
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
    public LocalDateTime getEndTime() {
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
    public void setTime(LocalDateTime start, LocalDateTime end, int interval) {
        if (start == null || end == null) {
            logger.error("Time (start or end) cannot be empty");
            throw new NullPointerException("Time (start or end) cannot be empty");
        }
        if (interval <= 0) {
            logger.error("The repetition interval of the task must be greater than zero");
            throw new IllegalArgumentException("The repetition interval of the task must be greater than zero");
        }
        if (this.repeated) {
            this.start = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(),
                    start.getHour(), start.getMinute(), start.getSecond(), start.getNano());
            this.end = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(),
                    end.getHour(), end.getMinute(), end.getSecond(), end.getNano());
            this.interval = interval;
        } else {
            repeated = true;
            this.start = LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(),
                    start.getHour(), start.getMinute(), start.getSecond(), start.getNano());
            this.end = LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(),
                    end.getHour(), end.getMinute(), end.getSecond(), end.getNano());
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
    public LocalDateTime nextTimeAfter(LocalDateTime current) {
        if (current == null) {
            logger.error("Current time cannot be empty");
            throw new NullPointerException("Current time cannot be empty");
        }
        LocalDateTime currentTime = LocalDateTime.of(current.getYear(), current.getMonth(), current.getDayOfMonth(),
                current.getHour(), current.getMinute(), current.getSecond(), current.getNano());
        if (this.getEndTime().isAfter(currentTime) && this.active) {
            if (this.repeated) {
                for (LocalDateTime i = this.start; !i.isAfter(this.end); i = i.plusSeconds(interval)) {
                    if (currentTime.isBefore(i)) {
                        return i;
                    }
                }
            } else {
                return time;
            }
        }
        return null;
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
        if (!this.isRepeated() && !task.isRepeated() && this.title.equals(task.title) && this.time.equals(task.time)) {
            return true;
        } else
            return this.isRepeated() && task.isRepeated() && this.title.equals(task.title) && this.start.equals(task.start)
                    && this.end.equals(task.end) && this.interval == task.interval;
    }

    @Override
    public int hashCode() {
        int hashCode = title == null ? 0 : title.hashCode();
        if (!this.isRepeated()) {
            hashCode = 31 * hashCode + time.hashCode();
        } else {
            hashCode = 31 * hashCode + start.hashCode() + end.hashCode() + interval;
        }
        return hashCode;
    }

    @Override
    public String toString() {
        if (isRepeated()) {
            return "Title: " + this.title + ", start: " + this.start + ", end: " + this.end + ", interval: " + this.interval
                    + ", active: " + this.isActive();
        } else {
            return "Title: " + this.title + ", time: " + this.time + ", active: " + this.isActive();
        }
    }
}
