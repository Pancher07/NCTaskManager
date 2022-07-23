package ua.edu.sumdu.j2se.panchenko.tasks.view;

import org.apache.log4j.Logger;
import ua.edu.sumdu.j2se.panchenko.tasks.Main;
import ua.edu.sumdu.j2se.panchenko.tasks.model.AbstractTaskList;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainView implements View {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public int printInfo(AbstractTaskList taskList) {
        System.out.println("Select a function and enter its number: ");
        System.out.println("1. Check your tasks from your task list.");
        System.out.println("2. Add new task to your task list.");
        System.out.println("3. Remove task from your task list.");
        System.out.println("4. Change the parameter of task from your task list");
        System.out.println("5. Check the calendar of scheduled events for a certain period of time.");
        System.out.println("6. Exit.");
        int number = 0;
        boolean isCorrectInput = false;
        while (!isCorrectInput) {
            try {
                number = Integer.parseInt(reader.readLine());
            } catch (Exception e) {
                System.out.println("The entered data is incorrect.");
                System.out.println("Select a function and enter its number: ");
                System.out.println("1. Check your tasks from your task list.");
                System.out.println("2. Add new task to your task list.");
                System.out.println("3. Remove task from your task list.");
                System.out.println("4. Change the parameter of task from your task list");
                System.out.println("5. Check the calendar of scheduled events for a certain period of time.");
                System.out.println("6. Exit.");
                continue;
            }
            if (number >= 1 && number <= 6) {
                isCorrectInput = true;
            } else {
                System.out.println("The entered data is incorrect.");
                System.out.println("Select a function and enter its number: ");
                System.out.println("1. Check your tasks from your task list.");
                System.out.println("2. Add new task to your task list.");
                System.out.println("3. Remove task from your task list.");
                System.out.println("4. Change the parameter of task from your task list");
                System.out.println("5. Check the calendar of scheduled events for a certain period of time.");
                System.out.println("6. Exit.");
            }
        }
        return number;
    }
}
