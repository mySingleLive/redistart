package com.dtflys.redistart.controller;

import com.dtflys.redistart.view.StringValueView;
import com.jfoenix.controls.JFXTabPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class KeysContentController implements Initializable {

    @FXML
    private SplitPane mainSplitPane;

    @FXML
    private VBox leftBox;

    @FXML
    private JFXTabPane valueTabPane;

    private Tab valueTab;

    @Autowired
    private StringValueView stringValueView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        leftBox.setPrefWidth(150);
        SplitPane.setResizableWithParent(leftBox, Boolean.FALSE);

        valueTab = new Tab();
        valueTab.setText("Key");
        valueTab.setClosable(false);

        Parent stringValueRoot = stringValueView.loadAsParent(Map.of());
        valueTab.setContent(stringValueRoot);

        valueTabPane.getTabs().add(valueTab);
    }

}
