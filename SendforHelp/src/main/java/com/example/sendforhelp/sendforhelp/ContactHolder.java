package com.example.sendforhelp.sendforhelp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ContactHolder
{
    Activity activity;
    public static ArrayList<Contact> contacts = new ArrayList<Contact>();

    public ContactHolder(Activity activity)
    {
        this.activity = activity;
    }
    public static void updateContact(int position, String name, String phone, String email)
    {
        contacts.get(position).name = name;
        contacts.get(position).phone = phone;
        contacts.get(position).email = email;
    }

    public static void loadContacts(Activity activity)
    {
        SharedPreferences prefs = activity.getSharedPreferences(activity.getLocalClassName(), Context.MODE_PRIVATE);
        ContactHolder.contacts.clear();

        Gson gson = new Gson();
        String contacts = prefs.getString("Contacts", null);
        ContactHolder.contacts = gson.fromJson(contacts, new TypeToken<List<Contact>>(){}.getType());

        if(contacts == null)
            ContactHolder.contacts = new ArrayList<Contact>();
    }


}
