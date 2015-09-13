package com.tonilopezmr.androidexamples;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * @author Antonio López.
 */
public class WithBottomNavigationViewActivity extends AppCompatActivity{

    DrawerLayout drawer;
    NavigationView bottomNavigationView;
    NavigationView containerNavigationView;
    NavigationView bodyNavigationView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_footer);

        textView = (TextView) findViewById(R.id.text_view);
        drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        containerNavigationView = (NavigationView)findViewById(R.id.navigation_drawer_container);
        bodyNavigationView = (NavigationView)findViewById(R.id.navigation_drawer_body);
        bottomNavigationView = (NavigationView)findViewById(R.id.navigation_drawer_bottom);

        setUpBodyNavigationView(bodyNavigationView);
        setupBottomNavigationView(bottomNavigationView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void setupBottomNavigationView(NavigationView bottomNavigationView) {
        bottomNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawer.closeDrawers();
                        Snackbar.make(drawer, menuItem.getTitle(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        return true;
                    }
                });
    }

    private void setUpBodyNavigationView(NavigationView bodyNavigationView) {
        bodyNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawer.closeDrawers();
                        textView.setText(menuItem.getTitle());
                        return true;
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
