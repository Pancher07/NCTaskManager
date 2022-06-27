package ua.edu.sumdu.j2se.panchenko.tasks;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Tasks implements Serializable {
    /**
     * The static method that returns a subset of tasks that are scheduled to run at least once
     * after the time "from" and no later than "to".
     */
    public static Iterable<Task> incoming(Iterable<Task> tasks, LocalDateTime start, LocalDateTime end) {
        return streamOf(tasks).filter((task -> task != null && (task.nextTimeAfter(start) != null) &&
                        (!task.nextTimeAfter(start).isAfter(end))))
                .collect(Collectors.toList());
    }

    /**
     * The method converts Iterable to Stream.
     */
    private static Stream<Task> streamOf(Iterable<Task> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    /**
     * The static method that builds a calendar of tasks for a given period is a table where each date corresponds
     * to a set of tasks to be performed at that time, and one task can occur according to several dates if it
     * has to be performed several times during the specified period.
     */
    public static SortedMap<LocalDateTime, Set<Task>>
    calendar(Iterable<Task> tasks, LocalDateTime start, LocalDateTime end) {
        SortedMap<LocalDateTime, Set<Task>> map = new TreeMap<>();
        List<Task> list = (List<Task>) incoming(tasks, start, end);
        for (Task task : list) {
            if (task.isRepeated()) {
                for (LocalDateTime i = task.nextTimeAfter(start); !i.isAfter(end);
                     i = i.plusSeconds(task.getRepeatInterval())) {
                    map.computeIfAbsent(i, (unused) -> new HashSet<>()).add(task);
                }
            } else {
                map.computeIfAbsent(task.getTime(), (unused) -> new HashSet<>()).add(task);
            }
        }
        return map;
    }
}
