package com.example.sendforhelp.sendforhelp;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class Drawer_Fragment extends Fragment
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Inflate the correct layout file and get the alarm field activity_login.
        View view = inflater.inflate(R.layout.fragment_drawer, container, false);

        final Button homeButton = (Button) view.findViewById(R.id.home_button);
        final Button contactButton = (Button) view.findViewById(R.id.contacts_button);
        final Button settingsButton = (Button) view.findViewById(R.id.settings_button);
        final Button historyButton = (Button) view.findViewById(R.id.history_button);
        final TextView title = (TextView) getActivity().findViewById(R.id.title);

        homeButton.setTransformationMethod(null);
        contactButton.setTransformationMethod(null);
        settingsButton.setTransformationMethod(null);
        historyButton.setTransformationMethod(null);

        homeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction trans = getFragmentManager().beginTransaction();

                Main_Fragment frag = new Main_Fragment();
                trans.replace(R.id.container, frag);
                trans.addToBackStack(null);
                trans.commit();
                title.setText("Home");

                DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                mDrawerLayout.closeDrawers();
            }
        });

        contactButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction trans = getFragmentManager().beginTransaction();

                Contacts_Fragment frag = new Contacts_Fragment();
                trans.replace(R.id.container, frag);
                trans.addToBackStack(null);
                trans.commit();
                title.setText("Contacts");

                DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                mDrawerLayout.closeDrawers();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction trans = getFragmentManager().beginTransaction();

                Settings_Fragment frag = new Settings_Fragment();
                trans.replace(R.id.container, frag);
                trans.addToBackStack(null);
                trans.commit();
                title.setText("Settings");

                DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                mDrawerLayout.closeDrawers();
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction trans = getFragmentManager().beginTransaction();

                History_Fragment frag = new History_Fragment();
                trans.replace(R.id.container, frag);
                trans.addToBackStack(null);
                trans.commit();
                title.setText("History");

                DrawerLayout mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                mDrawerLayout.closeDrawers();
            }
        });

        if(MainActivity.settings.disableHistory)
            historyButton.setVisibility(View.GONE);
        else
            historyButton.setVisibility(View.VISIBLE);
        return view;
    }
}
