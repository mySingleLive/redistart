package com.dtflys.redistart.service;

import com.dtflys.redistart.model.RSMainPage;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

@Component("rediStartService")
public class RediStartService {

    private ObservableList<RSMainPage> mainPageList = FXCollections.observableArrayList();

    private ObservableObjectValue<RSMainPage> currentMainPage;

    public void addMainPage(RSMainPage mainPage) {
        mainPageList.add(mainPage);
    }

    public ObservableList<RSMainPage> getMainPageList() {
        return mainPageList;
    }
}
