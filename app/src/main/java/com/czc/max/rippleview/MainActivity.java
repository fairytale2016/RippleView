package com.czc.max.rippleview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.czc.max.library.RippleView;

public class MainActivity extends AppCompatActivity {

    private RippleView rippleView1;
    private RippleView rippleView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rippleView1 = ((RippleView) findViewById(R.id.rippleView1));
        rippleView1.start();
        rippleView2 = ((RippleView) findViewById(R.id.rippleView2));
        rippleView2.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (rippleView1 != null)
            rippleView1.pause();
        if (rippleView2 != null)
            rippleView2.pause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (rippleView1 != null)
            rippleView1.start();
        if (rippleView2 != null)
            rippleView2.start();
    }
}
