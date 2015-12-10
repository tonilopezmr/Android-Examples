package com.tonilopezmr.animations;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.transition.Transition;
import android.view.View;

/**
 * @author Antonio López.
 */
public class DetailActivity extends AppCompatActivity {

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        final Transition fade = getWindow().getEnterTransition();
        fade.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {

            }

            @Override
            public void onTransitionEnd(Transition transition) {
                fab.setVisibility(View.VISIBLE);
                fade.removeListener(this);
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        fab.setVisibility(View.INVISIBLE);
        finishAfterTransition();
    }
}
