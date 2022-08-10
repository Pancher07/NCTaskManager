package ua.edu.sumdu.j2se.panchenko.tasks;

import org.apache.log4j.Logger;
import ua.edu.sumdu.j2se.panchenko.tasks.controller.Controller;
import ua.edu.sumdu.j2se.panchenko.tasks.controller.MainController;
import ua.edu.sumdu.j2se.panchenko.tasks.model.*;
import ua.edu.sumdu.j2se.panchenko.tasks.view.MainView;
import ua.edu.sumdu.j2se.panchenko.tasks.view.View;

import java.io.*;

public class Main {
    private final static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) throws RuntimeException {
        logger.info("Method main started.");
        AbstractTaskList taskList = new ArrayTaskList();
        File file = new File("savedTasks.bin");
        if (!file.exists()) {
            try {
                file.createNewFile();
                logger.info("File \"savedTasks.bin\" was created.");
            } catch (IOException e) {
                logger.error("Here exception: ", e);
                throw new RuntimeException(e);
            }
        }
        if (file.length() > 0) {
            try (FileInputStream fis = new FileInputStream(file)) {
                try {
                    TaskIO.read(taskList, fis);
                } catch (IOException e) {
                    logger.error("Here exception: ", e);
                    throw new RuntimeException(e);
                }
            } catch (IOException e) {
                logger.error("Here exception: ", e);
                throw new RuntimeException(e);
            }
        }
        System.out.println("Task Manager was started.");
        View mainView = new MainView();
        Controller mainController = new MainController(taskList, mainView);
        mainController.process(null);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            try {
                TaskIO.write(taskList, fos);
            } catch (IOException e) {
                logger.error("Here exception: ", e);
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            logger.error("Here exception: ", e);
            throw new RuntimeException(e);
        }
        System.out.println("Task Manager was closed.");
        logger.info("Method main finished.");
    }
}
