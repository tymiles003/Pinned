package com.example.sendforhelp.sendforhelp;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class ObtainLoc {
    Timer timer1;
    LocationManager locMan;
    LocationResult locResult;
    boolean gpsEnabled = false;
    boolean wifiEnabled = false;

    public boolean getLocation(Context context, LocationResult result)
    {
        locResult = result;

        //If there's no manager, get a new one.
        if(locMan == null)
            locMan = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        try
        {
            //Check to see if GPS is enabled on the device.
            gpsEnabled = locMan.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch(Exception e){}

        try
        {
            //Check to see if Wifi is enabled on the device.
            wifiEnabled = locMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch(Exception e){}

        //If neither is enabled, we can't get a location.
        if(!wifiEnabled && !gpsEnabled)
            return false;

        //Create a location update request based on which is enabled.
        if(gpsEnabled)
            locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, locationListenerGPS);
        if(wifiEnabled)
            locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0, locationListenerWifi);

        //Start a timer so it can later be decided which location is newest.
        timer1 = new Timer();
        timer1.schedule(new getLastLocation(), 20000);
        return true;
    }

    LocationListener locationListenerGPS = new LocationListener()
    {
        public void onLocationChanged(Location location)
        {
            timer1.cancel();
            locResult.gotLocation(location);
            locMan.removeUpdates(this);
            locMan.removeUpdates(locationListenerWifi);
        }
        public void onProviderEnabled(String provider){}
        public void onProviderDisabled(String provider){}
        public void onStatusChanged(String provider, int status, Bundle extras){}
    };

    LocationListener locationListenerWifi = new LocationListener()
    {
        public void onLocationChanged(Location location)
        {
            timer1.cancel();
            locResult.gotLocation(location);
            locMan.removeUpdates(this);
            locMan.removeUpdates(locationListenerGPS);
        }
        public void onProviderEnabled(String provider){}
        public void onProviderDisabled(String provider){}
        public void onStatusChanged(String provider, int status, Bundle extras){}
    };

    class getLastLocation extends TimerTask
    {
        @Override
        public void run() {
            locMan.removeUpdates(locationListenerGPS);
            locMan.removeUpdates(locationListenerWifi);

            Location wifiLoc = null, gpsLoc = null;
            if (gpsEnabled)
                gpsLoc = locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (wifiEnabled)
                wifiLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (gpsLoc != null && wifiLoc != null) {
                if (gpsLoc.getTime() > wifiLoc.getTime())
                    locResult.gotLocation(gpsLoc);
                else
                    locResult.gotLocation(wifiLoc);
                return;
            }
            if (gpsLoc != null) {
                locResult.gotLocation(gpsLoc);
                return;
            }
            if (wifiLoc != null)
            {
                locResult.gotLocation(wifiLoc);
                return;
            }
            locResult.gotLocation(null);
        }

    }
    public static abstract class LocationResult
    {
        public abstract void gotLocation(Location location);
    }
}


