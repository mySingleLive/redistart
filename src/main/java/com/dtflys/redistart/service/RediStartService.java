package com.dtflys.redistart.service;

import com.dtflys.redistart.model.action.RSAction;
import com.dtflys.redistart.model.connection.RedisConnection;
import com.dtflys.redistart.model.key.RSKey;
import com.dtflys.redistart.model.page.RSContentPage;
import com.dtflys.redistart.model.page.RSDashboardPage;
import com.dtflys.redistart.model.page.RSKeysContentPage;
import com.dtflys.redistart.model.value.RSStringValueMode;
import com.dtflys.redistart.model.valuemode.StringValueMode;
import com.google.common.collect.Lists;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Component("rediStartService")
public class RediStartService {
    @Resource
    private ActionService actionService;
    private final List<RSContentPage> pages = FXCollections.observableArrayList();
    private final ObjectPropertyBase<RSContentPage> selectedPage = new SimpleObjectProperty<>();
    private final Map<RedisConnection, RSKeysContentPage> keysContentPageMap = new HashMap<>();
    private final Map<RedisConnection, RSDashboardPage> dashboardPageMap = new HashMap<>();
    private final ObjectPropertyBase<RSKey> selectedKey = new SimpleObjectProperty<>();
    private final StringProperty valueStatusText = new SimpleStringProperty("");
    private final ObjectPropertyBase<StringValueMode> stringValueMode = new SimpleObjectProperty<>(null);
    private final ObjectPropertyBase<List<StringValueMode>> stringValueModeList = new SimpleObjectProperty<>(Lists.newArrayList());

    private final BooleanProperty wrapText = new SimpleBooleanProperty(false);


    public RediStartService() {
        selectedPage.addListener((observableValue, oldPage, newPage) -> {
            if (newPage != null) {
                if (!pages.contains(newPage)) {
                    Consumer<RSContentPage> onInit = newPage.getOnInit();
                    if (onInit != null) {
                        onInit.accept(newPage);
                    }
                    pages.add(newPage);
                    if (newPage instanceof RSKeysContentPage) {
                        keysContentPageMap.put(((RSKeysContentPage) newPage).getConnection(), (RSKeysContentPage) newPage);
                    } else if (newPage instanceof RSDashboardPage) {
                        dashboardPageMap.put(((RSDashboardPage) newPage).getConnection(), (RSDashboardPage) newPage);
                    }
                }
                Consumer<RSContentPage> onSelect = newPage.getOnSelect();
                if (onSelect != null) {
                    onSelect.accept(newPage);
                }
            }
        });
    }

    public Map<RedisConnection, RSKeysContentPage> getKeysContentPageMap() {
        return keysContentPageMap;
    }

    public Map<RedisConnection, RSDashboardPage> getDashboardPageMap() {
        return dashboardPageMap;
    }

    public RSContentPage getSelectedPage() {
        return selectedPage.get();
    }

    public ObjectPropertyBase<RSContentPage> selectedPageProperty() {
        return selectedPage;
    }

    public void setSelectedPage(RSContentPage selectedPage) {
        this.selectedPage.set(selectedPage);
    }

    public void setSelectedKeysContentPage(RedisConnection contentPage) {
        RSKeysContentPage keysContentPage = keysContentPageMap.get(contentPage);
        if (keysContentPage != null) {
            setSelectedPage(keysContentPage);
        }
    }

    public RSKey getSelectedKey() {
        return selectedKey.get();
    }

    public ObjectPropertyBase<RSKey> selectedKeyProperty() {
        return selectedKey;
    }

    public void setSelectedKey(RSKey selectedKey) {
        this.selectedKey.set(selectedKey);
    }

    public String getValueStatusText() {
        return valueStatusText.get();
    }

    public StringProperty valueStatusTextProperty() {
        return valueStatusText;
    }

    public void setValueStatusText(String valueStatusText) {
        this.valueStatusText.set(valueStatusText);
    }

    public StringValueMode getStringValueMode() {
        return stringValueMode.get();
    }

    public ObjectPropertyBase<StringValueMode> stringValueModeProperty() {
        return stringValueMode;
    }

    public void setStringValueMode(StringValueMode stringValueMode) {
        this.stringValueMode.set(stringValueMode);
    }

    public List<StringValueMode> getStringValueModeList() {
        return stringValueModeList.get();
    }

    public ObjectPropertyBase<List<StringValueMode>> stringValueModeListProperty() {
        return stringValueModeList;
    }

    public void setStringValueModeList(List<StringValueMode> stringValueModeList) {
        this.stringValueModeList.set(stringValueModeList);
    }

    public boolean isWrapText() {
        return wrapText.get();
    }

    public BooleanProperty wrapTextProperty() {
        return wrapText;
    }

    public void setWrapText(boolean wrapText) {
        this.wrapText.set(wrapText);
    }

    public void registerAction(RSAction action) {
        actionService.registerAction(action);
    }

    public void doAction(String actionName, Object... args) {
        actionService.doAction(actionName, args);
    }
}


