package com.mobilelabsinc.PhoneLookup;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class TabBaseControls extends TabActivity{

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Resusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, SearchScreen.class);

        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("search").setIndicator("Search",
                res.getDrawable(R.drawable.menu_search))
                .setContent(intent);

        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, Login.class);
        spec = tabHost.newTabSpec("logout").setIndicator("Logout",
                res.getDrawable(R.drawable.menu_logout))
                .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setOnTabChangedListener(new OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {
                // TODO Auto-generated method stub
                Log.d("", tabId);
                if(tabId.equals("search")){
                    setTitle("Search");
                }
                if(tabId.equals("logout")){
                    setTitle("Login");
                    finish();                   
                }
                if(tabId.equals("controls")){
                    setTitle("Controls");
                }
            }
        });
        ViewGroup decorView= (ViewGroup) getWindow().getDecorView();
        LinearLayout root= (LinearLayout) decorView.getChildAt(0);
        FrameLayout titleContainer= (FrameLayout) root.getChildAt(0);
        TextView title= (TextView) titleContainer.getChildAt(0);
        title.setGravity(Gravity.CENTER);

        tabHost.setCurrentTab(0);


    }
}
