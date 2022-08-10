package ua.edu.sumdu.j2se.panchenko.tasks.view;

import org.apache.log4j.Logger;
import ua.edu.sumdu.j2se.panchenko.tasks.controller.SelectionOptions;
import ua.edu.sumdu.j2se.panchenko.tasks.model.AbstractTaskList;

public class CheckTasksView implements View {
    private final static Logger logger = Logger.getLogger(CheckTasksView.class);
    @Override
    public int printInfo(AbstractTaskList taskList) {
        logger.info("The method has been called to view available tasks.");
        System.out.println(taskList.toString());
        return SelectionOptions.MAIN_MENU_ACTION.ordinal();
    }
}
