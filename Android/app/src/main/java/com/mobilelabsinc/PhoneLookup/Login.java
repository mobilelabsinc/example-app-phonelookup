package com.mobilelabsinc.PhoneLookup;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;

import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends AppCompatActivity {
    private Context mContext;
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;

        setContentView(R.layout.login);

        usernameEditText = findViewById(R.id.login_username_edit);
        passwordEditText = findViewById(R.id.login_password_edit);
        Button signInButton = findViewById(R.id.login_sign_in_button);
        signInButton.setOnClickListener(signInButtonOnClickListener);
    }

    private final OnClickListener signInButtonOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            //Compare username and password to one stored in strings.xml
            if (usernameEditText.getText().toString().equals(getString(R.string.valid_username)) &&
                    passwordEditText.getText().toString().equals(getString(R.string.valid_password))) {

                //Launch the Search Screen Activity
                Intent intent = new Intent();
                intent.setClass(mContext, SearchScreen.class);
                startActivity(intent);
                finish();
            }
            else {

                //Alert to invalid login
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("Invalid Login Credentials")
                        .setCancelable(false)
                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }
    };

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        // a long press of the call key.
        // do our work, returning true to consume it.  by
        // returning true, the framework knows an action has
        // been performed on the long press, so will set the
        // canceled flag for the following up event.
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyLongPress(keyCode, event);
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
