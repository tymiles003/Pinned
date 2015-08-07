package com.example.sendforhelp.sendforhelp;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Main_Fragment extends Fragment
{
    Typeface tf;
    GoogleMap map;
    MapFragment mMapFragment;
    boolean toggleConfirm = false;
    ViewGroup view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //Get the layout ready and get the clickable Image.
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main_screen, container, false);
        view = rootView;
        final Button smsButton = (Button) rootView.findViewById(R.id.sms_button);
        final Button saveButton = (Button) rootView.findViewById(R.id.save_button);
        saveButton.setTransformationMethod(null);
        smsButton.setTransformationMethod(null);

        tf = Typeface.createFromAsset(getActivity().getAssets(), "coolvetica rg.ttf");

        smsButton.setTypeface(tf);
        saveButton.setTypeface(tf);

        //Add the map fragment into the container
        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction =
                getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapview_container, mMapFragment);
        fragmentTransaction.commit();

        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity().getApplicationContext(), "Working...", Toast.LENGTH_SHORT).show();
                saveLocation();
            }
        });

        //When the user clicks the image, the app will begin locating and sending out messages
        smsButton.setOnClickListener(new View.OnClickListener()
        {

            public void onClick(View v)
            {

                //Let the user know that the app is working.
                if(MainActivity.settings.twoButtonSending)
                {
                    if(checkContactList())
                    {
                        if (toggleConfirm == false)
                        {
                            smsButton.setText("Click to confirm");
                            toggleConfirm = true;
                        } else
                        {
                            sendLocation();
                            toggleConfirm = false;
                            Toast.makeText(getActivity().getApplicationContext(), "Working...", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else
                {
                    if(checkContactList())
                    {
                        sendLocation();
                        Toast.makeText(getActivity().getApplicationContext(), "Working...", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

        //Check for whether the intent came from the widget or not.
        if(getActivity().getIntent().hasExtra("widget"))
        {
            //Let the user know that the app is working.
            saveLocation();
            Toast.makeText(getActivity().getApplicationContext(), "Working...", Toast.LENGTH_SHORT).show();
        }

        return rootView;
    }


    public void sendLocation()
    {


        final Button smsButton = (Button) getView().findViewById(R.id.sms_button);
        final Button saveButton = (Button) getView().findViewById(R.id.save_button);

        if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            // THIS PHONE HAS SMS FUNCTIONALITY

            smsButton.setClickable(false);
            smsButton.setText("Sharing...");
            saveButton.setClickable(false);
            saveButton.setBackgroundColor(getResources().getColor(R.color.grey));
            //Uses the result of the ObtainLoc object to send out the message.
            ObtainLoc.LocationResult locResult = new ObtainLoc.LocationResult()
            {
                @Override
                public void gotLocation(Location location)
                {

                    String locMessage;

                    //Get the longitude and latitude from the results.
                    double longit = location.getLongitude();
                    double latit = location.getLatitude();

                    //Construct the location data message.
                    String mapString = "http://maps.google.com/?q=" + latit + "," + longit;
                    locMessage = MainActivity.settings.message + mapString;

                    //Send out the location data message via SMS.
                    Toast.makeText(getActivity().getApplicationContext(), "Sending SMS", Toast.LENGTH_SHORT).show();


                    try
                    {
                        //Make an SMS manager object and get the stored phone numbers from preferences
                        SmsManager sms = SmsManager.getDefault();
                        ArrayList<String> contactNames = new ArrayList<String>();

                        for (Contact contact : ContactHolder.contacts)
                        {
                            sms.sendTextMessage(contact.phone, null, locMessage, null, null);
                            contactNames.add(contact.name);
                        }
                        addHistoryEntry(longit, latit, contactNames, "Share");


                    } catch (Exception ex)
                    {
                        //Gather exception data in case it's needed.
                        Log.e("ERR:", ex.toString());
                    }




                }
            };
            //Create a new location object and get the location.
            ObtainLoc myLocation = new ObtainLoc();
            myLocation.getLocation(getActivity(), locResult);

        } else {
            new AlertDialog.Builder(getActivity())
                    .setMessage("Unable to send SMS on this device. You can still Pin Locations!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                        }
                    }).setCancelable(false).show();
        }

        smsButton.setClickable(true);
        smsButton.setText("Share Location");
        saveButton.setClickable(true);
        saveButton.setBackgroundColor(getResources().getColor(R.color.dark_blue));
    }

    public boolean checkContactList()
    {
        if (ContactHolder.contacts.size() == 0)
        {
            new AlertDialog.Builder(getActivity())
                    .setMessage("You have no saved Contacts. Please visit the Contacts page in the menu and add some.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                        }
                    }).setCancelable(false).show();
            return false;
        }
        else
            return true;
    }

    public void saveLocation()
    {
        final Button smsButton = (Button) view.findViewById(R.id.sms_button);
        final Button saveButton = (Button) view.findViewById(R.id.save_button);

        smsButton.setClickable(false);
        saveButton.setText("Saving...");
        saveButton.setClickable(false);
        smsButton.setBackgroundColor(getResources().getColor(R.color.grey));

        //Uses the result of the ObtainLoc object to send out the message.
        ObtainLoc.LocationResult locResult = new ObtainLoc.LocationResult()
        {
            @Override
            public void gotLocation(Location location)
            {

                String locMessage;
                ArrayList<String> contactNames = new ArrayList<String>();

                //Get the longitude and latitude from the results.
                double longit = location.getLongitude();
                double latit = location.getLatitude();

                //Construct the location data message.
                String mapString = "http://maps.google.com/?q=" + latit + "," + longit;
                locMessage = MainActivity.settings.message + mapString;

                //Send out the location data message via SMS.
                Toast.makeText(getActivity().getApplicationContext(), "Location saved, view it in History.", Toast.LENGTH_SHORT).show();
                for(Contact contact : ContactHolder.contacts)
                {
                    contactNames.add(contact.name);
                }
                addHistoryEntry(longit, latit, contactNames, "Save");

                smsButton.setClickable(true);
                saveButton.setText("Pin Location");
                saveButton.setClickable(true);
                smsButton.setBackgroundColor(getResources().getColor(R.color.dark_blue));
            }
        };
        //Create a new location object and get the location.
        ObtainLoc myLocation = new ObtainLoc();
        myLocation.getLocation(getActivity(), locResult);
    }

    public void addHistoryEntry(double longit, double latit, ArrayList<String> contacts,String type)
    {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        MainActivity.history.add(currentDateTimeString, Double.toString(longit),Double.toString(latit),contacts, type);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        map = mMapFragment.getMap();
        map.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
        map.getUiSettings().setZoomControlsEnabled(true);
        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener()
        {
            @Override
            public void onMyLocationChange(Location lastKnownLocation)
            {
                CameraUpdate myLoc = CameraUpdateFactory.newCameraPosition(
                        new CameraPosition.Builder().target(new LatLng(lastKnownLocation.getLatitude(),
                                lastKnownLocation.getLongitude())).zoom(map.getCameraPosition().zoom).build()
                );
                map.moveCamera(myLoc);
            }
        });

        map.setMyLocationEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(false);
    }
}

