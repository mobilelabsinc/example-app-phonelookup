package com.mobilelabsinc.PhoneLookup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends Activity {
    Context mContext;
    EditText Username;
    EditText Password;

    public static final String PREFS_NAME = "com.mobilelabsinc.PhoneLookup_preferences";
    private static SharedPreferences prefs;
    private SharedPreferences.Editor spe;

    private String KEY_Username = "USERNAME";
    private String KEY_Password = "PASSWORD";
    private String KEY_Remember = "REMEMBER";
    public CheckBox rememberMe;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onPostCreate(savedInstanceState);
    }


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        spe = prefs.edit();
        mContext = this;



        setContentView(R.layout.login);
        setTitle("Login");


        SharedPreferences settings = mContext.getSharedPreferences(Settings.PREFS_NAME, 0);


        Username = (EditText)findViewById(R.id.usernameEditText);
        Password = (EditText)findViewById(R.id.passwordEditText);
        rememberMe = (CheckBox)findViewById(R.id.rememberMe);

        if(settings.getString(KEY_Remember, "1").equals("0")){
            rememberMe.setChecked(true);
            Username.setText(settings.getString(KEY_Username,""));
            Password.setText(settings.getString(KEY_Password,""));
        }

        Button login =(Button)findViewById(R.id.loginButton);
        login.setOnClickListener(listener);
    }

    public OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if(Username.getText().toString().toLowerCase().equals(getResources().getString(R.string.Username))&
                    Password.getText().toString().toLowerCase().equals(getResources().getString(R.string.Password)))
            {
                if(rememberMe.isChecked())
                {
                    persistent(KEY_Username, Username.getText().toString().toLowerCase());
                    persistent(KEY_Password, Password.getText().toString().toLowerCase());
                    persistent(KEY_Remember, "0");
                }
                else
                {
                    persistent(KEY_Remember, "1");
                }

                Intent intent = new Intent();
                intent.setClass(mContext, SearchScreen.class);
                startActivity(intent);
                finish();
            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Invalid Login Credentials")
                .setCancelable(false)
                .setNegativeButton("OK", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    };

    /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return true;

    };
     */

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

    public void persistent(String KEY, String users)
    {
        SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
        spe.putString(KEY, users);
        spe.commit();
    }
}
