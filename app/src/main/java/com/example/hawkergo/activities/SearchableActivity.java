package com.example.hawkergo.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.Nullable;

import com.example.hawkergo.R;
import com.example.hawkergo.models.BaseDbFields;
import com.example.hawkergo.models.HawkerCentre;
import com.example.hawkergo.models.Searchable;
import com.example.hawkergo.services.UserService;
import com.example.hawkergo.utils.ui.Debouncer;

import java.util.ArrayList;
import java.util.List;

public abstract class SearchableActivity <T extends Searchable> extends ToolbarActivity {

    private SearchView searchView;
    private ListView searchResultsListView;
    private ArrayAdapter<String> searchResultAdapter;
    private ArrayList<String> searchResultsName = new ArrayList<>();
    private List<T> searchResults = new ArrayList<>();
    private Debouncer debouncer = new Debouncer();


    public void initSearchViews(){
        this.searchResultsListView = findViewById(R.id.search_results);
        this.searchResultAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, searchResultsName);
        this.searchResultsListView.setAdapter(searchResultAdapter);
    }

    protected abstract String onSearchResultItemClick(T x);

    private boolean onSearchSubmit(){
        searchResultsName.clear();
        searchResultsListView.setVisibility(View.GONE);
        if(searchView.getQuery() != null && searchView.getQuery().toString().trim().length() <= 0){
            resetDataOnEmptySearchSubmit();
        }
        return true;
    }

    protected abstract void resetDataOnEmptySearchSubmit();

    protected abstract void onSearchBoxTextChange(String s);

    private  boolean onCloseSearchTextBox(){
        searchResultsName = new ArrayList<>();
        searchResultsListView.setVisibility(View.GONE);
        if (searchView.getQuery() != null && searchView.getQuery().toString().trim().length() <= 0) {
            resetDataOnEmptySearchSubmit();
        }
        return false;
    }

    protected void onSearchBoxTextChange(List<T> o){
        searchResults = o;
        searchResultAdapter.clear();
        for(T x: o){
            searchResultAdapter.add(x.getName());
        }
        searchResultAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuItem searchButton = menu.findItem(R.id.search_button);
        searchButton.setVisible(true);
        searchView = (SearchView) searchButton.getActionView();

        searchResultsListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        T selecteditem = searchResults.get(position);
                        String toReplace = onSearchResultItemClick(selecteditem);
                        searchView.setQuery(toReplace, true);
                        searchView.clearFocus();
                        searchResultsListView.setVisibility(View.GONE);
                    }
                }
        );

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        return onSearchSubmit();
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        searchResultsListView.setVisibility(View.VISIBLE);
                        debouncer.debounce(
                                searchView,
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        onSearchBoxTextChange(s);
                                    }
                                }
                        );
                        return false;
                    }
                }
        );

        searchView.setOnCloseListener(
                new SearchView.OnCloseListener() {
                    @Override
                    public boolean onClose() {
                        return onCloseSearchTextBox();
                    }
                }
        );
        searchView.setQueryHint("Search for hawker centre nameeeee");
        return true;
    }
}

