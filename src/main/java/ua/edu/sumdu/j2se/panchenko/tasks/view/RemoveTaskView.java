package ua.edu.sumdu.j2se.panchenko.tasks.view;

import org.apache.log4j.Logger;
import ua.edu.sumdu.j2se.panchenko.tasks.controller.SelectionOptions;
import ua.edu.sumdu.j2se.panchenko.tasks.model.AbstractTaskList;
import ua.edu.sumdu.j2se.panchenko.tasks.model.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Objects;

public class RemoveTaskView implements View {
    private final static Logger logger = Logger.getLogger(RemoveTaskView.class);
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public int printInfo(AbstractTaskList taskList) {
        String enterTitle = enterTitle();
        if (!enterTitle.equalsIgnoreCase("exit")) {
            int startSize = taskList.size();
            Iterator<Task> it = taskList.iterator();
            while (it.hasNext()) {
                Task task = it.next();
                if (task.getTitle().equals(enterTitle))
                    it.remove();
            }
            if (startSize == taskList.size()) {
                System.out.println("A task with this title does not exist in the task list.");
            } else {
                System.out.println("Task \"" + enterTitle + "\" was removed.");
            }
        }
        return SelectionOptions.MAIN_MENU_ACTION.ordinal();
    }

    private String enterTitle() {
        String title = null;
        boolean isCorrectInput = false;
        System.out.println("Enter the title of the task to be deleted");
        System.out.println("(To exit to the main menu, enter \"exit\")");
        while (!isCorrectInput) {
            try {
                title = reader.readLine();
                if (title.equalsIgnoreCase("exit")) {
                    break;
                }
                if (Objects.equals(title, "")) {
                    System.out.println("Title cannot be empty. Enter the title of task to be deleted.");
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
}
