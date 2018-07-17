package com.mobilelabsinc.PhoneLookup;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Settings extends Activity {
    Context mContext;
    public static final String PREFS_NAME = "com.mobilelabsinc.PhoneLookup_preferences";

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onPostCreate(savedInstanceState);
    }

    private static SharedPreferences prefs;
    private SharedPreferences.Editor spe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        spe = prefs.edit();

        mContext= this;

        setTitle("Settings");

        /* killer broadcast*/
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.package.ACTION_LOGOUT");
        registerReceiver(Killer, intentFilter);

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unregisterReceiver(Killer);
    }

    public BroadcastReceiver Killer= new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {                        
            //At this point you should start the login activity and finish this one
            finish();
        }
    };

    public boolean onCreateOptionsMenu(android.view.Menu menu) {        
        MenuInflater inflater = getMenuInflater(); 
        inflater.inflate(R.menu.menu_view, menu);
        return true;       
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        // TODO Auto-generated method stub
        switch(item.getItemId()){
        case R.id.search:
            intent.setClass(mContext, SearchScreen.class);              
            startActivity(intent);              
            return true;
        case R.id.logout:
            intent.setClass(mContext, Login.class);
            startActivity(intent);
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction("com.package.ACTION_LOGOUT");
            this.sendBroadcast(broadcastIntent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // a long press of the call key.
            // do our work, returning true to consume it.  by
            // returning true, the framework knows an action has
            // been performed on the long press, so will set the
            // canceled flag for the following up event.
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
                && !event.isCanceled()) {
            // if the call key is being released, AND we are tracking
            // it from an initial key down, AND it is not canceled,
            // then handle it.
            finish();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }



}
