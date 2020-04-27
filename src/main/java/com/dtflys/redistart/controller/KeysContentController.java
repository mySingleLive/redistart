package com.dtflys.redistart.controller;

import com.dtflys.redistart.controls.RSToggleButton;
import com.dtflys.redistart.model.action.SelectKeyOnNewTabAction;
import com.dtflys.redistart.model.key.RSKey;
import com.dtflys.redistart.model.valuemode.StringValueMode;
import com.dtflys.redistart.service.RediStartService;
import com.dtflys.redistart.view.values.StringValueView;
import com.jfoenix.controls.JFXTabPane;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectPropertyBase;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class KeysContentController implements Initializable {

    @Resource
    private RediStartService rediStartService;

    @Autowired
    private StringValueView stringValueView;

    @FXML
    private SplitPane mainSplitPane;

    @FXML
    private VBox leftBox;

    @FXML
    private JFXTabPane valueTabPane;

    @FXML
    private HBox wrapTextBox;

    private Tab valueTab;

    @FXML
    private Label lbStatus;

    @FXML
    private HBox valueModeBox;

    @FXML
    private Label lbValueModeText;

    private ContextMenu valueModeMenu = new ContextMenu();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        leftBox.setPrefWidth(150);
        SplitPane.setResizableWithParent(leftBox, Boolean.FALSE);

        RSToggleButton wrapTextButton = new RSToggleButton();
        FontIcon icon = new FontIcon();
        icon.setIconLiteral("mdi-wrap");
        icon.setIconSize(17);
        wrapTextButton.setGraphic(icon);
        wrapTextBox.getChildren().add(wrapTextButton);
        wrapTextButton.setChecked(rediStartService.isWrapText());
        rediStartService.wrapTextProperty().bindBidirectional(wrapTextButton.checkedProperty());

        SelectKeyOnNewTabAction selectKeyOnNewTabAction = new SelectKeyOnNewTabAction(stringValueView, valueTabPane);
        rediStartService.registerAction(selectKeyOnNewTabAction);

        valueTab = new Tab();
        valueTab.setClosable(false);
        ObjectPropertyBase<RSKey> selectedKeyProperty = rediStartService.selectedKeyProperty();
        valueTab.textProperty().bind(Bindings.createStringBinding(() -> {
            RSKey key = selectedKeyProperty.get();
            if (key == null) {
                return "Key";
            }
            String title = key.getDatabase().getName() + " | " + key.getKey();
            return title;
        }, selectedKeyProperty));

        Parent stringValueRoot = stringValueView.loadAsParent(Map.of());
        valueTab.setContent(stringValueRoot);

        valueTabPane.getTabs().add(valueTab);

        createValueModeMenu(rediStartService.getStringValueModeList());

        rediStartService.stringValueModeListProperty().addListener((observableValue, oldValueModeList, newValueModeList) -> {
            createValueModeMenu(newValueModeList);
        });
        lbStatus.textProperty().unbind();
        lbStatus.textProperty().bind(rediStartService.valueStatusTextProperty());
    }

    private void createValueModeMenu(List<StringValueMode> valueModeList) {
        if (valueModeList == null) return;
        valueModeMenu.getItems().clear();
        for (StringValueMode mode : valueModeList) {
            MenuItem item = new MenuItem();
            item.setText(mode.getText());
            item.setOnAction(event -> {
                rediStartService.setStringValueMode(mode);
            });
            valueModeMenu.getItems().add(item);
        }
        rediStartService.setStringValueMode(valueModeList.get(0));
        lbValueModeText.textProperty().unbind();
        lbValueModeText.textProperty().bind(Bindings.createStringBinding(
                () -> rediStartService.getStringValueMode().getText(),
                rediStartService.stringValueModeProperty()));
    }

    public void onValueModeClick(MouseEvent mouseEvent) {
        valueModeMenu.show(valueModeBox, Side.TOP, 0, 0);
    }
}
