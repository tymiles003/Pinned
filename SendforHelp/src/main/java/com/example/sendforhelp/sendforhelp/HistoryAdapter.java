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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ListItemViewHolder> {
    Activity context;

    ArrayList<HistoryEntry> entries = new ArrayList<>();

    // Provide a suitable constructor (depends on the kind of dataset)
    public HistoryAdapter(Activity context) {
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HistoryAdapter.ListItemViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_card, parent, false);
        // set the view's size, margins, paddings and layout parameters
        v.setPadding(10, 10, 10, 10);
        return new ListItemViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ListItemViewHolder holder, final int position) {


        String dateTime = "Date: ";
        holder.date.setText(dateTime + entries.get(position).dateTime);
        //mapString.setText(historyEntry.location);

        final HistoryEntry tempHist = entries.get(position);

        String mapEndpoint = "http://maps.google.com/maps/api/staticmap?center=";
        mapEndpoint += tempHist.locationX + "," + tempHist.locationY + "&zoom=15&size=800x200&sensor=false&markers=color:green%7C" + tempHist.locationX + "," + tempHist.locationY;

        ImageLoader.getInstance().displayImage(mapEndpoint, holder.map_link);

        if(entries.get(position).contacts != null && entries.get(position).contacts.size() > 0) {
            String contactString = "Sent to: ";
            for (String str : entries.get(position).contacts) {
                contactString = contactString + str + ", ";
            }
            contactString = contactString.substring(0, contactString.length() - 2);
            holder.contacts.setText(contactString);
        }
        else
            holder.contacts.setVisibility(View.GONE);

        holder.navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Do some shit.
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setMessage("Once you've deleted this Pin, we can't get it back!")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                removeItem(holder.getPosition());
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing, just close.
                    }
                }).show();

            }
        });
    }

    public void clearAll()
    {
        //Remove all entries from the adapter.
        entries.clear();
        notifyDataSetChanged();
    }

    public void addItem(HistoryEntry history) {
        entries.add(history);
        //Notify that a new item has been added
        notifyItemInserted(ContactHolder.contacts.size());
    }

    public void removeItem(int position)
    {
        //Remove a single item from the adapter.
        entries.remove(position);
        notifyItemRemoved(position);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(entries != null)
            return entries.size();
        else
            return 0;
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {

        ImageView map_link;
        TextView date, contacts;
        ImageButton delete;
        Button navigate;

        public ListItemViewHolder(View itemView) {
            super(itemView);
            delete = (ImageButton) itemView.findViewById(R.id.delete_image);
            map_link = (ImageView) itemView.findViewById(R.id.map_string);
            date = (TextView) itemView.findViewById(R.id.time_stamp);
            contacts = (TextView) itemView.findViewById(R.id.contacts);
            navigate = (Button) itemView.findViewById(R.id.navigate);

        }

    }

}