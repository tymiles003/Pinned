package com.example.sendforhelp.sendforhelp;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;

public class MainActivity extends ActionBarActivity
{
    Typeface tf;
    private DrawerLayout mDrawerLayout;
    View drawerView;
    public static Settings settings = new Settings();
    public static History history = new History();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tf = Typeface.createFromAsset(getAssets(), "coolvetica rg.ttf");

        ContactHolder.loadContacts(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = findViewById(R.id.content_drawer);
        toolbar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mDrawerLayout.isDrawerOpen(drawerView))
                    mDrawerLayout.closeDrawer(drawerView);
                else
                    mDrawerLayout.openDrawer(drawerView);
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);

        FragmentTransaction trans = getFragmentManager().beginTransaction();

        Main_Fragment frag = new Main_Fragment();
        trans.replace(R.id.container, frag);
        trans.addToBackStack(null);
        trans.commit();

        //Load the test ad.
        //AdView adView = (AdView) findViewById(R.id.adView);
        //AdRequest adRequest = new AdRequest.Builder().build();
        //adView.loadAd(adRequest);

    }

    public void updateNavDrawer()
    {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        Drawer_Fragment newFrag = new Drawer_Fragment();
        trans.replace(R.id.content_drawer, newFrag);
        trans.commit();
    }

    @Override
    public void onBackPressed()
    {
        FragmentManager fragMan = getFragmentManager();
        if(fragMan.getBackStackEntryCount() != 1)
            fragMan.popBackStack();
        else
            super.onBackPressed();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        loadSettings();
        updateNavDrawer();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        saveSettings();
    }


    public void saveSettings()
    {
        SharedPreferences prefs = this.getSharedPreferences(this.getLocalClassName(), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String mySettings = gson.toJson(settings);
        prefs.edit().putString("Settings", mySettings).apply();

        String myHistory = gson.toJson(history);
        prefs.edit().putString("History", myHistory).apply();
    }

    public void loadSettings()
    {
        SharedPreferences prefs = this.getSharedPreferences(this.getLocalClassName(), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String settingsString = prefs.getString("Settings", null);
        if(settingsString == null)
        {
            saveSettings();
        }
        else
        {
            this.settings = gson.fromJson(settingsString, Settings.class);
        }
        String myHistory = prefs.getString("History", null);
        if (myHistory != null)
            history = gson.fromJson(myHistory, History.class);
    }


}



