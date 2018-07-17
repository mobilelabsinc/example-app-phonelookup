package com.mobilelabsinc.PhoneLookup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.os.StrictMode;
import android.util.Log;

public class SplashScreen extends Activity {
    private Thread mSplashThread;  
    public boolean isStop = false;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onPostCreate(savedInstanceState);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDeath()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyLog()
                .penaltyDeath()
                .build());
                 
        StrictMode.allowThreadDiskReads();
        StrictMode.allowThreadDiskWrites();
        
        Log.e("PhoneLookup", "StrictMode enabled.");

        // Splash screen view
        setContentView(R.layout.splash);

        final SplashScreen sPlashScreen = this;   

        // The thread to wait for splash screen events
        mSplashThread =  new Thread()
        {
            @Override
            public void run(){
                try 
                {
                    synchronized(this)
                    {
                        // Wait given period of time or exit on touch
                        isStop = true;
                        wait(1000);
                    }
                }
                catch(InterruptedException ex)
                {                    
                }

                finish();

                // Run next activity
                if(isStop)
                {
                    Intent intent = new Intent();
                    intent.setClass(sPlashScreen, Login.class);
                    startActivity(intent);
                }
            }
        };

        mSplashThread.start();        
    }

    /**
     * Processes splash screen touch events
     */
    @Override
    public boolean onTouchEvent(MotionEvent evt)
    {
        if(evt.getAction() == MotionEvent.ACTION_DOWN)
        {
            synchronized(mSplashThread){
                mSplashThread.notifyAll();
                isStop = true;
            }
        }
        return true;
    }    

    /* @Override
        public boolean onKeyDown(int keyCode, KeyEvent event) {
            // TODO Auto-generated method stub
            if(keyCode==KeyEvent.KEYCODE_BACK){
                synchronized(mSplashThread){
                    isStop = false;
                    mSplashThread=null;


                }
            }
            return super.onKeyDown(keyCode, event);
        }*/




}
