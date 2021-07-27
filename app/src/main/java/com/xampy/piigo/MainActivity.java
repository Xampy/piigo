package com.xampy.piigo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        getSupportActionBar().hide();







    final long last_time = System.currentTimeMillis();


        new Thread(new Runnable() {
            @Override
            public void run() {

                while (System.currentTimeMillis() - last_time < 2000);

                Intent home_activity = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(home_activity);
                //Finish this fullscreen activity
                finish();
                //[END database checking]
            }
        }).start();
    }
}
