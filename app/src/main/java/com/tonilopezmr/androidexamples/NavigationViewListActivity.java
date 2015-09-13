package com.tonilopezmr.androidexamples;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * @author Antonio López.
 */
public class NavigationViewListActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        String[] activityList = {"Normal NavigationView", "NavigationView with fixed footer"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, activityList);

        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

        switch (position){
            case 0:
                startActivity(new Intent(NavigationViewListActivity.this, NormalNavigationViewActivity.class));
                break;
            case 1:
                startActivity(new Intent(NavigationViewListActivity.this, WithBottomNavigationViewActivity.class));
                break;
        }
    }
}
