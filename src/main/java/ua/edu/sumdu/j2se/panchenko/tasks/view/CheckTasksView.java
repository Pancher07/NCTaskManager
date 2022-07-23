package ua.edu.sumdu.j2se.panchenko.tasks.view;

import ua.edu.sumdu.j2se.panchenko.tasks.controller.SelectionOptions;
import ua.edu.sumdu.j2se.panchenko.tasks.model.AbstractTaskList;

public class CheckTasksView implements View {
    @Override
    public int printInfo(AbstractTaskList taskList) {
        System.out.println(taskList.toString());
        return SelectionOptions.MAIN_MENU_ACTION.ordinal();
    }
}
