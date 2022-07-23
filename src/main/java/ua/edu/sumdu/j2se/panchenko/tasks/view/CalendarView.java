package ua.edu.sumdu.j2se.panchenko.tasks.view;

import org.apache.log4j.Logger;
import ua.edu.sumdu.j2se.panchenko.tasks.controller.SelectionOptions;
import ua.edu.sumdu.j2se.panchenko.tasks.model.AbstractTaskList;
import ua.edu.sumdu.j2se.panchenko.tasks.model.Task;
import ua.edu.sumdu.j2se.panchenko.tasks.model.Tasks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Set;
import java.util.SortedMap;

public class CalendarView implements View {
    private final static Logger logger = Logger.getLogger(CalendarView.class);
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public int printInfo(AbstractTaskList taskList) {
        LocalDateTime start = enterTime("start");
        LocalDateTime end = null;
        if (start != null) {
            end = enterTime("end");
            if (end != null) {
                SortedMap<LocalDateTime, Set<Task>> resultList = Tasks.calendar(taskList, start, end);
                System.out.println(resultList.toString());
            }
        }
        return SelectionOptions.MAIN_MENU_ACTION.ordinal();
    }

    private LocalDateTime enterTime(String stateOfTime) {
        String timeString = null;
        LocalDateTime time = null;
        boolean isCorrectInput = false;
        System.out.println("Please enter the " + stateOfTime + " of the period in the format \"YYYY-MM-DD HH:MM:SS\"");
        System.out.println("(To exit to the main menu, enter \"exit\")");
        while (!isCorrectInput) {
            try {
                timeString = reader.readLine();
                if (timeString.equalsIgnoreCase("exit")) {
                    break;
                }
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
                        "Enter the " + stateOfTime + " of the period in the format \"YYYY-MM-DD HH:MM:SS\"");
                continue;
            }
            isCorrectInput = true;
        }
        return time;
    }
}
