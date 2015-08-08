package com.example.sendforhelp.sendforhelp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import javax.xml.transform.Result;


public class Contacts_Fragment extends Fragment
{
    private RecyclerView mRecyclerView;
    private ContactAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final int CONTACT_PICKER_RESULT = 1001;
    int count = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_contacts_screen, container, false);
        ImageButton fab = (ImageButton) rootView.findViewById(R.id.fab);


        //Set up the new RecyclerView
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.my_recycler_view);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new ContactAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.loadContacts();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //Code for Lollipop
            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    createContact();
                }
            });
        } else {
            //Code for non Lollipop
            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    createContact();
                }
            });
        }


        //Check to see if there's saved contacts. If so, load them
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("com.sendforhelp.sendforhelp", Context.MODE_PRIVATE);

        return rootView;
    }

    public void createContact()
    {

        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(contactPickerIntent, CONTACT_PICKER_RESULT);

        /*LayoutInflater inflater = getActivity().getLayoutInflater();
        final View tempView = inflater.inflate(R.layout.new_contact_dialog, null);
        if(count < 100)
        {
            final AlertDialog builder = new AlertDialog.Builder(getActivity()).setTitle("New Contact")
                    .setView(tempView)
                    .setPositiveButton("Create", null)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            //Do nothing, just close.
                        }
                    }).create();

            builder.setOnShowListener(new DialogInterface.OnShowListener()
            {
                @Override
                public void onShow(DialogInterface dialog)
                {
                    Button b = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                    b.setOnClickListener(new View.OnClickListener()
                    {

                        @Override
                        public void onClick(View view)
                        {
                            EditText newName = (EditText) tempView.findViewById(R.id.new_name);
                            EditText newPhone = (EditText) tempView.findViewById(R.id.new_phone);
                            EditText newEmail = (EditText) tempView.findViewById(R.id.new_email);

                            String name = newName.getText().toString();
                            String phone = newPhone.getText().toString();
                            String email = newEmail.getText().toString();

                            if(name.length() == 0 || phone.length() == 0)
                            {
                                new AlertDialog.Builder(getActivity()).setMessage("Name and Phone number are both required.")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {

                                            }
                                        }).show();

                            }
                            else if(!(phone.length() >= 10 && phone.length() <= 14))
                            {
                                new AlertDialog.Builder(getActivity()).setMessage("Phone number needs to be between 10 and 14 characters. Valid formats include (123)456-7890 and 1234567890")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which)
                                            {

                                            }
                                        }).show();
                            }
                            else
                            {

                                mAdapter.addItem(new Contact(name, phone, email));
                                mRecyclerView.scrollToPosition(count - 1);
                                count++;
                                builder.dismiss();
                            }
                        }
                    });
                }
            });
            builder.show();

        }
        else
        {
        }*/

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if(requestCode == CONTACT_PICKER_RESULT) {

                Uri contactData = data.getData();
                Cursor c = getActivity().getContentResolver().query(contactData, null, null, null, null);
                if (c.moveToFirst()) {
                    Strgit ing id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                }
            }

        } else {
            // gracefully handle failure
            Log.e("ERR:", "Warning: activity result not ok");
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mAdapter.saveContacts();
    }

}
