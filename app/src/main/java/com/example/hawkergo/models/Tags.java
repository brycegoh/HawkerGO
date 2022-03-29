package com.example.hawkergo.models;

import java.util.List;

public class Tags extends BaseDbFields {
    private List<String> categories;

    public List<String> getCategories() {
        return categories;
    }

    public String[] getCategoriesArray() {
        String[] categoriesArray = new String[categories.size()];
        for (int i = 0; i < categoriesArray.length; i++) {
            categoriesArray[i] = categories.get(i);

        }

        return categoriesArray;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}