package com.example.android.tablayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

public class SplashScreen extends Activity {

    ProgressBar progressBar;
    int progressStatus = 0;
    int progressMaxValue = 100;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.splash_screen);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setMax(progressMaxValue);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progressStatus < 100)
                {
                    progressStatus += 1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });
                    try
                    {
                        Thread.sleep(200);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                if(progressStatus ==100){
                    Intent i = new Intent(SplashScreen.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }).run();
    }
}
