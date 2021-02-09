package sample.controllers;

import sample.Main;

public abstract class AbstractController {

    protected Main main;

    public void setMainApp(Main main) {
        this.main = main;
    }
}
