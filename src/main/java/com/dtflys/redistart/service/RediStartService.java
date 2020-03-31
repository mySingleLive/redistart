package com.dtflys.redistart.service;

import com.dtflys.redistart.model.RedisConnection;
import com.dtflys.redistart.model.page.RSContentPage;
import com.dtflys.redistart.model.page.RSKeysContentPage;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.property.SimpleObjectProperty;
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
}
