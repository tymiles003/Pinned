package com.example.sendforhelp.sendforhelp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ListItemViewHolder> {
    Activity context;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ContactAdapter(Activity context) {
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ContactAdapter.ListItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_contact_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        v.setPadding(10,10,10,10);
        return new ListItemViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ListItemViewHolder holder, final int position) {

        //holder.label.setText(mDataset.get(position));
        holder.edit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                LayoutInflater inflater = context.getLayoutInflater();
                final View tempView = inflater.inflate(R.layout.new_contact_dialog, null);
                final EditText newName = (EditText) tempView.findViewById(R.id.new_name);
                final EditText newPhone = (EditText) tempView.findViewById(R.id.new_phone);
                final EditText newEmail = (EditText) tempView.findViewById(R.id.new_email);
                newName.setText(ContactHolder.contacts.get(position).name);
                newPhone.setText(ContactHolder.contacts.get(position).phone);
                newEmail.setText(ContactHolder.contacts.get(position).email);

                new AlertDialog.Builder(context).setTitle("Edit Contact")
                        .setView(tempView)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                String name = newName.getText().toString();
                                String phone = newPhone.getText().toString();
                                String email = newEmail.getText().toString();

                                ContactHolder.contacts.get(position).name = name;
                                ContactHolder.contacts.get(position).phone = phone;
                                ContactHolder.contacts.get(position).email = email;
                                holder.name.setText(name);
                                holder.phone.setText(phone);
                                holder.email.setText(email);
                                ContactHolder.updateContact(position, name, phone, email);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Do nothing, just close.
                    }
                }).show();
            }
        });

        holder.name.setText(ContactHolder.contacts.get(position).name);
        holder.phone.setText(ContactHolder.contacts.get(position).phone);
        holder.email.setText(ContactHolder.contacts.get(position).email);

        holder.delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(context).setTitle("Delete Contact")
                        .setMessage("This action can't be undone")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                removeItem(holder.getPosition());
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Do nothing, just close.
                    }
                }).show();

            }
        });
    }

    public void addItem(Contact contact) {
        ContactHolder.contacts.add(contact);
        //Notify that a new item has been added
        notifyItemInserted(ContactHolder.contacts.size());
    }

    public void removeItem(int position)
    {
        ContactHolder.contacts.remove(position);
        notifyItemRemoved(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(ContactHolder.contacts != null)
            return ContactHolder.contacts.size();
        else
            return 0;
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {

        ImageView edit;
        TextView name, email, phone;
        ImageButton delete;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            delete = (ImageButton) itemView.findViewById(R.id.delete_image);
            name = (TextView) itemView.findViewById(R.id.contact_name);
            email = (TextView) itemView.findViewById(R.id.email_address);
            phone = (TextView) itemView.findViewById(R.id.phone_number);
            edit = (ImageView) itemView.findViewById(R.id.edit);
        }

    }

    public void loadContacts()
    {
        SharedPreferences prefs = context.getSharedPreferences(this.context.getLocalClassName(), Context.MODE_PRIVATE);
        ContactHolder.contacts.clear();

        Gson gson = new Gson();
        String contacts = prefs.getString("Contacts", null);
        ContactHolder.contacts = gson.fromJson(contacts, new TypeToken<List<Contact>>(){}.getType());

        if(ContactHolder.contacts != null)
        {
            for (Contact contact : ContactHolder.contacts)
            {
                //addItem(contact);
                notifyItemInserted(ContactHolder.contacts.indexOf(contact));
            }
        }
        else
        {
            ContactHolder.contacts = new ArrayList<Contact>();
        }

        /*Set<String> nameSet;
        Set<String> phoneSet;
        Set<String> emailSet;


        try
        {
            nameSet = prefs.getStringSet("names", null);
            phoneSet =prefs.getStringSet("phones", null);
            emailSet =prefs.getStringSet("emails", null);

            ArrayList<String> names = new ArrayList<String>(nameSet);
            ArrayList<String> phones = new ArrayList<String>(phoneSet);
            ArrayList<String> emails = new ArrayList<String>(emailSet);


            if (names != null)
            {
                for (int i = 0; i < names.size(); i++)
                {
                    addItem(names.get(i), phones.get(i), emails.get(i));
                    notifyItemInserted(i);
                }
            }
        }catch(Exception e){}*/

    }

    public void saveContacts()
    {
        SharedPreferences prefs = context.getSharedPreferences(this.context.getLocalClassName(), Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String contacts = gson.toJson(ContactHolder.contacts);
        prefs.edit().putString("Contacts", contacts).commit();

        /*
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> phones = new ArrayList<String>();
        ArrayList<String> emails = new ArrayList<String>();
        for(Contact contact : mDataset)
        {
            names.add(contact.name);
            phones.add(contact.phone);
            emails.add(contact.email);
        }

        //Set the values
        Set<String> nameSet = new HashSet<String>();
        Set<String> phoneSet = new HashSet<String>();
        Set<String> emailSet = new HashSet<String>();
        nameSet.addAll(names);
        phoneSet.addAll(phones);
        emailSet.addAll(emails);
        prefs.edit().putStringSet("names", nameSet).apply();
        prefs.edit().putStringSet("phones", phoneSet).apply();
        prefs.edit().putStringSet("emails", emailSet).apply();
        prefs.edit().commit();*/
    }
}