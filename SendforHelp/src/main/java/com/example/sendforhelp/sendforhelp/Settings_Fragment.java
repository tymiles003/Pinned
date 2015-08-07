package com.example.sendforhelp.sendforhelp;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.gson.Gson;

public class Settings_Fragment extends Fragment
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_settings_screen, container, false);
        final SwitchCompat requireConfirm = (SwitchCompat) rootView.findViewById(R.id.one_press_enable);
        final SwitchCompat disableHistory = (SwitchCompat) rootView.findViewById(R.id.history_disable);
        final SwitchCompat combineSMSEmail = (SwitchCompat) rootView.findViewById(R.id.combine_enable);
        final Button editMessage = (Button) rootView.findViewById(R.id.edit_message);

        requireConfirm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                MainActivity.settings.twoButtonSending = requireConfirm.isChecked();
            }
        });

        disableHistory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                MainActivity.settings.disableHistory = disableHistory.isChecked();
                updateNavDrawer();
            }
        });

        combineSMSEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                MainActivity.settings.mergeEmailSMS = combineSMSEmail.isChecked();
            }
        });

        editMessage.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                LayoutInflater inflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View inflatedLayout= inflater.inflate(R.layout.edit_message_alert, null);
                final EditText message = (EditText) inflatedLayout.findViewById(R.id.edit_message_text);
                message.setText(MainActivity.settings.message);

                new AlertDialog.Builder(getActivity())
                        .setView(inflatedLayout)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //Save the user's edited message.
                                MainActivity.settings.message = message.getText().toString();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //Dont save. Do nothing.
                            }
                        }).show();
            }
        });


        return rootView;
    }

    @Override
    public void onResume()
    {
        final SwitchCompat twoButton = (SwitchCompat) getView().findViewById(R.id.one_press_enable);
        final SwitchCompat disableHistory = (SwitchCompat) getView().findViewById(R.id.history_disable);
        final SwitchCompat combineSMSEmail = (SwitchCompat) getView().findViewById(R.id.combine_enable);

        twoButton.setChecked(MainActivity.settings.twoButtonSending);
        disableHistory.setChecked(MainActivity.settings.disableHistory);
        combineSMSEmail.setChecked(MainActivity.settings.mergeEmailSMS);

        super.onResume();
        loadSettings();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        saveSettings();
    }

    public void saveSettings()
    {
        SharedPreferences prefs = getActivity().getSharedPreferences(getActivity().getLocalClassName(), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String mySettings = gson.toJson(MainActivity.settings);
        prefs.edit().putString("Settings", mySettings).apply();
    }

    public void loadSettings()
    {
        SharedPreferences prefs = getActivity().getSharedPreferences(getActivity().getLocalClassName(), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String settingsString = prefs.getString("Settings", null);
        MainActivity.settings = gson.fromJson(settingsString, Settings.class);

    }

    public void updateNavDrawer()
    {
        FragmentTransaction trans = getFragmentManager().beginTransaction();
        Drawer_Fragment newFrag = new Drawer_Fragment();
        trans.replace(R.id.content_drawer, newFrag);
        trans.commit();
    }

}