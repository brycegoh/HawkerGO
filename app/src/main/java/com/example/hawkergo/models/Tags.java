package com.example.hawkergo.models;

import java.util.List;

public class Tags extends BaseDbFields {
    private List<String> categories;

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
