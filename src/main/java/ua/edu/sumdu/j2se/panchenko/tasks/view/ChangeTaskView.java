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
import java.util.Iterator;
import java.util.Objects;

public class ChangeTaskView implements View {
    private final static Logger logger = Logger.getLogger(ChangeTaskView.class);
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public int printInfo(AbstractTaskList taskList) {
        String enterTitle = enterTitle();
        Task task = null;
        if (!enterTitle.equalsIgnoreCase("exit")) {
            task = checkTask(enterTitle, taskList);
        }
        if (task != null) {
            changing(task);
        }

        return SelectionOptions.MAIN_MENU_ACTION.ordinal();
    }

    private String enterTitle() {
        String title = null;
        boolean isCorrectInput = false;
        System.out.println("Enter the title of the task to be changed");
        System.out.println("(To exit to the main menu, enter \"exit\")");
        while (!isCorrectInput) {
            try {
                title = reader.readLine();
                if (title.equalsIgnoreCase("exit")) {
                    break;
                }
                if (Objects.equals(title, "")) {
                    System.out.println("Title cannot be empty. Enter the title of task to be changed.");
                } else {
                    isCorrectInput = true;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return title;
    }

    private Task checkTask(String title, AbstractTaskList taskList) {
        Task result = null;
        Iterator<Task> it = taskList.iterator();
        int availabilityСheckOfTitle = 0;
        boolean isCorrectInput = false;
        while (it.hasNext()) {
            Task task = it.next();
            if (task.getTitle().equals(title)) {
                System.out.println("You change the parameters of task \"" + title + "\".");
                ++availabilityСheckOfTitle;
                result = task;
                isCorrectInput = true;
            }
        }
        if (availabilityСheckOfTitle == 0) {
            System.out.println("The task with title \"" + title + "\" does not exist in the task list.");
            isCorrectInput = true;
        }
        return result;
    }

    private void changing(Task task) {
        System.out.println("Select the option you want to change.");
        System.out.println("1. Change the \"title\" of the task.");
        System.out.println("2. Change the \"time\" of the task.");
        System.out.println("3. Change the \"activity status\" of the task.");
        System.out.println("(To exit to the main menu, enter \"exit\")");
        boolean isCorrectInput = false;
        int number = 0;
        while (!isCorrectInput) {
            try {
                String str = reader.readLine();
                if (str.equalsIgnoreCase("exit")) {
                    break;
                } else {
                    number = Integer.parseInt(str);
                }
            } catch (Exception e) {
                System.out.println("The entered data is incorrect.");
                System.out.println("Select the option you want to change.");
                System.out.println("1. Change the \"title\" of the task.");
                System.out.println("2. Change the \"time\" of the task.");
                System.out.println("3. Change the \"activity status\" of the task.");
                System.out.println("(To exit to the main menu, enter \"exit\")");
                continue;
            }
            if (number == 1) {
                changeTitle(task);
                isCorrectInput = true;
            } else if (number == 2) {
                if (!task.isRepeated()) {
                    changeTimeForNonRepetitiveTask(task);
                } else {
                    changeTimeForRepetitiveTask(task);
                }
                isCorrectInput = true;
            } else if (number == 3) {
                changeActivityStatus(task);
                isCorrectInput = true;
            } else {
                System.out.println("The entered number is incorrect.");
                System.out.println("Select the option you want to change.");
                System.out.println("1. Change the \"title\" of the task.");
                System.out.println("2. Change the \"time\" of the task.");
                System.out.println("3. Change the \"activity status\" of the task.");
                System.out.println("(To exit to the main menu, enter \"exit\")");
            }
        }
    }

    private void changeTitle(Task task) {
        System.out.println("Enter the new title");
        try {
            String newTitle = reader.readLine();
            task.setTitle(newTitle);
        } catch (IOException e) {
            logger.error("Here exception: ", e);
            throw new RuntimeException(e);
        }
    }

    private void changeTimeForNonRepetitiveTask(Task task) {
        String timeString;
        LocalDateTime time = null;
        boolean isCorrectInput = false;
        System.out.println("Enter the new time of the tasks in the format \"YYYY-MM-DD HH:MM:SS\"");
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
                        "Enter the new time of the tasks in the format \"YYYY-MM-DD HH:MM:SS\"");
                continue;
            }
            task.setTime(time);
            isCorrectInput = true;
        }
    }

    private void changeTimeForRepetitiveTask(Task task) {
        LocalDateTime start = enterTime("start");
        LocalDateTime end = enterTime("end");
        int interval = enterInterval();
        task.setTime(start, end, interval);
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

    private void changeActivityStatus(Task task) {
        task.setActive(!task.isActive());
    }
}

