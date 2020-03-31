package com.dtflys.redistart.model.page;

import java.util.function.Consumer;

public class RSContentPage<T> {

    public static final int CONNECTION_MANAGER_PAGE = 0;

    public static final int KEYS_CONTENT_PAGE = 1;

    public static final int COLLECTION_PAGE = 2;

    private final int pageType;

    private Consumer<RSContentPage<T>> onInit;

    private Consumer<RSContentPage<T>> onSelect;

    public RSContentPage(int pageType) {
        this.pageType = pageType;
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
