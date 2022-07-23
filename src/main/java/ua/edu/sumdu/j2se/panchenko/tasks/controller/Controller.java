package ua.edu.sumdu.j2se.panchenko.tasks.controller;

import ua.edu.sumdu.j2se.panchenko.tasks.model.AbstractTaskList;
import ua.edu.sumdu.j2se.panchenko.tasks.view.View;

public abstract class Controller {
    protected View view;
    protected int actionToPerform;

    public Controller(View view, int actionToPerform) {
        this.view = view;
        this.actionToPerform = actionToPerform;
    }

    public boolean canProcess(int action) {
        return action == actionToPerform;
    }

    public int process(AbstractTaskList taskList) {
        return view.printInfo(taskList);
    }
}