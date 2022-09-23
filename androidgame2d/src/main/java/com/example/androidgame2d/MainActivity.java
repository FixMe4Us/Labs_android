package com.example.androidgame2d;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Устанвка полноэкранного режима
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                W);
    }
}