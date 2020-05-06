package com.dtflys.redistart.model.action;

import com.dtflys.redistart.model.key.RSKey;
import com.dtflys.redistart.view.values.StringValueView;
import javafx.beans.binding.Bindings;
import javafx.scene.Parent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.util.Map;

public class SelectKeyOnNewTabAction extends RSAbstractAction implements RSAction {

    private final StringValueView stringValueView;

    private TabPane valueTabPane;

    public SelectKeyOnNewTabAction(StringValueView stringValueView, TabPane valueTabPane) {
        super(SELECT_KEY_ON_NEW_TAB);
        this.stringValueView = stringValueView;
        this.valueTabPane = valueTabPane;
    }


    @Override
    public String getActionDisplayName() {
        return "Open New Tab With Key";
    }

    @Override
    public void doAction(Object... args) {
        RSKey key = (RSKey) args[0];

        Tab valueTab = new Tab();
        valueTab.setClosable(true);

        valueTab.setText(key.getDatabase().getName() + " | " + key.getKey());

        Parent stringValueRoot = stringValueView.loadAsParent(Map.of());
        valueTab.setContent(stringValueRoot);
        valueTab.styleProperty().bind(Bindings.createStringBinding(() -> {
            int len = valueTabPane.getTabs().size();
            int size = 220 - len * 10;
            if (size < 150) {
                size = 150;
            }
            return "-fx-pref-width: " + size;
        }, valueTabPane.getTabs()));

        valueTabPane.getTabs().add(valueTab);
        valueTabPane.getSelectionModel().select(valueTab);
    }
}
