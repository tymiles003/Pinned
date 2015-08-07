package com.example.sendforhelp.sendforhelp;

import java.util.ArrayList;

public class HistoryEntry
{
    String dateTime;
    String locationY;
    String locationX;
    ArrayList<String> contacts = new ArrayList<String>();
    String type;

    public HistoryEntry(String dateTime, String locationY,String locationX, ArrayList<String> contacts, String type)
    {
        this.dateTime = dateTime;
        this.locationY = locationY;
        this.locationX = locationX;
        this.contacts = contacts;
        this.type = type;
    }
}
