package com.dtflys.redistart.service;

import com.dtflys.redistart.model.connection.RedisConnection;
import com.dtflys.redistart.model.key.RSKey;
import com.dtflys.redistart.model.page.RSContentPage;
import com.dtflys.redistart.model.page.RSKeysContentPage;
import com.dtflys.redistart.model.value.RSStringValueMode;
import com.dtflys.redistart.model.valuemode.StringValueMode;
import com.google.common.collect.Lists;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Component("rediStartService")
public class RediStartService {

    private final List<RSContentPage> pages = FXCollections.observableArrayList();
    private final ObjectPropertyBase<RSContentPage> selectedPage = new SimpleObjectProperty<>();
    private final Map<RedisConnection, RSKeysContentPage> keysContentPageMap = new HashMap<>();
    private final ObjectPropertyBase<RSKey> selectedKey = new SimpleObjectProperty<>();
    private final StringProperty valueStatusText = new SimpleStringProperty("");
    private final ObjectPropertyBase<StringValueMode> stringValueMode = new SimpleObjectProperty<>(null);
    private final ObjectPropertyBase<List<StringValueMode>> stringValueModeList = new SimpleObjectProperty<>(Lists.newArrayList());


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
}


