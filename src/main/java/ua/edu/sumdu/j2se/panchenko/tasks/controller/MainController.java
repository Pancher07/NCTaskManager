package ua.edu.sumdu.j2se.panchenko.tasks.controller;

import ua.edu.sumdu.j2se.panchenko.tasks.model.AbstractTaskList;
import ua.edu.sumdu.j2se.panchenko.tasks.model.TaskIO;
import ua.edu.sumdu.j2se.panchenko.tasks.view.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController extends Controller {
    private AbstractTaskList taskList;
    private List<Controller> controllers = new ArrayList<>();

    public MainController(AbstractTaskList taskList, View mainView) {
        super(mainView, SelectionOptions.MAIN_MENU_ACTION.ordinal());
        this.taskList = taskList;

        controllers.add(this);
        controllers.add(new CheckTasksController(new CheckTasksView(),
                SelectionOptions.EMPTY_LIST_ACTION.ordinal()));
        controllers.add(new AddTaskController(new AddTaskView(), SelectionOptions.ADD_TASK_ACTION.ordinal()));
        controllers.add(new RemoveTaskController(new RemoveTaskView(), SelectionOptions.REMOVE_TASK_ACTION.ordinal()));
        controllers.add(new ChangeTaskController(new ChangeTaskView(), SelectionOptions.CHANGE_TASK_ACTION.ordinal()));
        controllers.add(new CalendarController(new CalendarView(), SelectionOptions.CALENDAR_ACTION.ordinal()));
    }


    @Override
    public int process(AbstractTaskList taskList) {
        int action = view.printInfo(taskList);
        while (true) {
            for (Controller controller : controllers) {
                if (controller.canProcess(action)) {
                    action = controller.process(this.taskList);
                }
            }
            if (action == SelectionOptions.FINISH_ACTION.ordinal()) {
                break;
            }
        }
        return SelectionOptions.FINISH_ACTION.ordinal();
    }
}
