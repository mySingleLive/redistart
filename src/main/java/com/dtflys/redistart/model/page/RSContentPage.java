package com.dtflys.redistart.model.page;

import java.util.function.Consumer;

public abstract class RSContentPage<T> {

    public static final int CONNECTION_MANAGER_PAGE = 0;

    public static final int KEYS_CONTENT_PAGE = 1;

    public static final int DASHBOARD_PAGE = 2;

    public static final int ANALYSE_PAGE = 3;

    private final int pageType;

    private final boolean showSelectionItem;

    private Consumer<RSContentPage<T>> onInit;

    private Consumer<RSContentPage<T>> onSelect;

    public RSContentPage(int pageType, boolean showSelectionItem) {
        this.pageType = pageType;
        this.showSelectionItem = showSelectionItem;
    }

    public boolean isShowSelectionItem() {
        return showSelectionItem;
    }

    public int getPageType() {
        return pageType;
    }

    public Consumer<RSContentPage<T>> getOnInit() {
        return onInit;
    }

    public void setOnInit(Consumer<RSContentPage<T>> onInit) {
        this.onInit = onInit;
    }

    public Consumer<RSContentPage<T>> getOnSelect() {
        return onSelect;
    }

    public void setOnSelect(Consumer<RSContentPage<T>> onSelect) {
        this.onSelect = onSelect;
    }
}
