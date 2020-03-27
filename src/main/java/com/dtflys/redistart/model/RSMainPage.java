package com.dtflys.redistart.model;

import javafx.scene.Parent;

public class RSMainPage {

    private final RSMainPageType type;

    private final Parent root;

    public RSMainPage(RSMainPageType type, Parent root) {
        this.type = type;
        this.root = root;
    }

    public RSMainPageType getType() {
        return type;
    }

    public Parent getRoot() {
        return root;
    }
}
