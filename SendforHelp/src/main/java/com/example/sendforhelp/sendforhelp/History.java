package com.example.sendforhelp.sendforhelp;

import java.util.ArrayList;


public class History
{
    public ArrayList<HistoryEntry> history = new ArrayList<HistoryEntry>();

    public void add(String dateTime, String locationX,String locationY, ArrayList<String> contacts, String type)
    {
        history.add(new HistoryEntry(dateTime, locationX,locationY, contacts, type));
    }

    public void clear()
    {
        history.clear();
    }

    public boolean isEmpty()
    {
        return history.isEmpty();
    }
}
