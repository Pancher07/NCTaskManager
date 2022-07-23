package ua.edu.sumdu.j2se.panchenko.tasks.view;

import org.apache.log4j.Logger;
import ua.edu.sumdu.j2se.panchenko.tasks.controller.SelectionOptions;
import ua.edu.sumdu.j2se.panchenko.tasks.model.AbstractTaskList;
import ua.edu.sumdu.j2se.panchenko.tasks.model.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class AddTaskView implements View {
    private final static Logger logger = Logger.getLogger(AddTaskView.class);
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public int printInfo(AbstractTaskList taskList) {
        Task task = createTask();
        if (task != null) {
            task.setActive(true);
            taskList.add(task);
        }
        return SelectionOptions.MAIN_MENU_ACTION.ordinal();
    }


    private Task createTask() {
        Task task = null;
        System.out.println("Will the task be repeated? " +
                "If the task is not repeated - enter \"0\", if it is repeated - enter \"1\".");
        System.out.println("To exit to the main menu, enter \"exit\"");
        int number = 0;
        boolean isCorrectInput = false;
        while (!isCorrectInput) {
            try {
                String str = reader.readLine();
                if (str.equalsIgnoreCase("exit")) {
                    break;
                } else {
                    number = Integer.parseInt(str);
                }
            } catch (Exception e) {
                System.out.println("Enter the correct variant: " +
                        "If the task is not repeated - enter \"0\", if it is repeated - enter \"1\".");
                continue;
            }
            if (number == 0) {
                task = createNonRepeatedTask();
                isCorrectInput = true;
            } else if (number == 1) {
                task = createRepeatedTask();
                isCorrectInput = true;
            } else {
                System.out.println("Enter the correct variant: " +
                        "If the task is not repeated - enter \"0\", if it is repeated - enter \"1\".");
            }
        }
        return task;
    }

    private Task createNonRepeatedTask() {
        String title = enterTitle();
        LocalDateTime time = enterTime("start");
        Task task = new Task(title, LocalDateTime.of(time.getYear(), time.getMonth(), time.getDayOfMonth(),
                time.getHour(), time.getMinute(), time.getSecond()));
        System.out.println("The new non-repeating task \"" + title + "\" was created.");
        return task;
    }

    private Task createRepeatedTask() {
        String title = enterTitle();
        LocalDateTime start = enterTime("start");
        LocalDateTime end = enterTime("end");
        int interval = enterInterval();
        Task task = new Task(title, LocalDateTime.of(start.getYear(), start.getMonth(), start.getDayOfMonth(),
                start.getHour(), start.getMinute(), start.getSecond(), start.getNano()),
                LocalDateTime.of(end.getYear(), end.getMonth(), end.getDayOfMonth(), end.getHour(),
                        end.getMinute(), end.getSecond(), end.getNano()), interval);
        System.out.println("The new repeating task \"" + title + "\" was created.");
        return task;
    }


    private String enterTitle() {
        String title = null;
        boolean isCorrectInput = false;
        System.out.println("Enter the title of task.");
        System.out.println("To exit to the main menu, enter \"exit\"");
        while (!isCorrectInput) {
            try {
                String str = reader.readLine();
                if (str.equalsIgnoreCase("exit")) {
                    break;
                } else {
                    title = str;
                }
                if (Objects.equals(title, "")) {
                    System.out.println("Title cannot be empty. Enter the title of task.");
                    System.out.println("To exit to the main menu, enter \"exit\"");
                } else {
                    isCorrectInput = true;
                }
            } catch (IOException e) {
                logger.error("Here exception: ", e);
                throw new RuntimeException(e);
            }
        }
        return title;
    }

    private LocalDateTime enterTime(String stateOfTime) {
        String timeString;
        LocalDateTime time = null;
        boolean isCorrectInput = false;
        System.out.println("Enter the " + stateOfTime + " time of the tasks in the format \"YYYY-MM-DD HH:MM:SS\"");
        while (!isCorrectInput) {
            try {
                timeString = reader.readLine();
            } catch (IOException e) {
                logger.error("Here exception: ", e);
                throw new RuntimeException(e);
            }
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            try {
                time = LocalDateTime.parse(timeString, formatter);
            } catch (DateTimeParseException dte) {
                System.out.println("The entered value is not correct. " +
                        "Enter the " + stateOfTime + " time of the tasks in the format \"YYYY-MM-DD HH:MM:SS\"");
                continue;
            }
            isCorrectInput = true;
        }
        return time;
    }

    private int enterInterval() {
        System.out.println("Enter the repetition interval in minutes");
        int interval = 0;
        boolean isCorrectInput = false;
        while (!isCorrectInput) {
            try {
                interval = Integer.parseInt(reader.readLine());
            } catch (Exception e) {
                System.out.println("Enter the correct value");
                continue;
            }
            if (interval > 0) {
                isCorrectInput = true;
            } else {
                System.out.println("The repetition interval of the task must be greater than zero. " +
                        "Enter the correct value");
            }
        }
        return interval * 60;
    }
}
