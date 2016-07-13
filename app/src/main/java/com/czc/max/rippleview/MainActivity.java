package com.czc.max.rippleview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.czc.max.library.RippleView;

public class MainActivity extends AppCompatActivity {

    private RippleView rippleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rippleView = ((RippleView) findViewById(R.id.rippleView));
        rippleView.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (rippleView != null)
            rippleView.pauseAnimation();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (rippleView != null)
            rippleView.start();
    }
}
