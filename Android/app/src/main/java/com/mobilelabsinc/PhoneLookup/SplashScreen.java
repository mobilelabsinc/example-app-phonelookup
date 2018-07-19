package com.mobilelabsinc.PhoneLookup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends Activity {
    private boolean isStop = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Splash screen view
        setContentView(R.layout.splash);

        final SplashScreen sPlashScreen = this;   

        // The thread to wait for splash screen events
        Thread mSplashThread = new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        // Wait given period of time or exit on touch
                        isStop = true;
                        wait(1000);
                    }
                } catch (InterruptedException ignored) {
                }

                finish();

                // Run next activity
                if (isStop) {
                    Intent intent = new Intent();
                    intent.setClass(sPlashScreen, Login.class);
                    startActivity(intent);
                }
            }
        };

        mSplashThread.start();        
    }
}
