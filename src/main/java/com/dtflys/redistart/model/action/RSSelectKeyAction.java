package com.dtflys.redistart.model.action;

import com.dtflys.redistart.view.values.StringValueView;
import javafx.scene.control.TabPane;

public class RSSelectKeyAction extends RSAbstractAction implements RSAction {


    private final TabPane valueTabPane;


    public RSSelectKeyAction(TabPane valueTabPane) {
        super(SELECT_KEY);
        this.valueTabPane = valueTabPane;
    }

    @Override
    public String getActionDisplayName() {
        return "Select Key";
    }

    @Override
    public void doAction(Object... args) {

    }
}
