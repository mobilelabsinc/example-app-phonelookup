package com.mobilelabsinc.PhoneLookup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        // Splash screen view
        setContentView(R.layout.splash);

        Intent intent = new Intent();
        intent.setClass(this, Login.class);
        startActivity(intent);
    }
}
