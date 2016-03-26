package com.tonilopezmr.dagger2rxjava.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.tonilopezmr.dagger2rxjava.PersonApplication;
import com.tonilopezmr.dagger2rxjava.R;
import com.tonilopezmr.dagger2rxjava.domain.Person;
import com.tonilopezmr.dagger2rxjava.presenter.PersonListPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Antonio López.
 */
public class MainActivity extends AppCompatActivity implements PersonListPresenter.View{

    RecyclerView recyclerView;
    View emptyCase;
    View loadingView;

    private PersonAdapter adapter;

    @Inject
    PersonListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initDependencyInjector();
        initToolbar();
        initFloatinActionButton();
        initRecyclerView();
        initEmptyCaseView();
        initLoadingView();


        presenter.setView(this);
        presenter.init();
    }

    private void initDependencyInjector() {
        PersonApplication app = (PersonApplication) getApplication();
        app.getMainComponent().inject(this);
    }

    private void initLoadingView() {
        loadingView = findViewById(R.id.progress_bar);
    }

    private void initEmptyCaseView() {
        emptyCase = findViewById(R.id.tv_empty_case);
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new PersonAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void initFloatinActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showEmptyView() {
        emptyCase.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEmptyView() {
        emptyCase.setVisibility(View.GONE);
    }

    @Override
    public void showPersons(List<Person> persons) {
        adapter.addAll(persons);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }
}
