package com.example.sendforhelp.sendforhelp;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;


public class History_Fragment extends Fragment
{
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private HistoryAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_history, container, false);

        //Set up the new RecyclerView
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.history_container);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new HistoryAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        ImageView clearHistory = (ImageView) rootView.findViewById(R.id.clear_history_button);

        clearHistory.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Clear History")
                        .setMessage("This can't be undone. Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                MainActivity.history.clear();
                                LinearLayout historyHolder = (LinearLayout) getView().findViewById(R.id.history_container);
                                historyHolder.removeAllViews();
                                ImageLoader.getInstance().clearMemoryCache();
                                ImageLoader.getInstance().clearDiskCache();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {

                            }
                        }).show();
            }
        });

        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        TextView noHistory = (TextView) getView().findViewById(R.id.no_history);

        if(MainActivity.history.isEmpty())
            noHistory.setVisibility(View.VISIBLE);
        else
            noHistory.setVisibility(View.GONE);

        loadHistory();
    }

    public void loadHistory()
    {
        mAdapter.clearAll();
        for(HistoryEntry historyEntry : MainActivity.history.history)
        {
            mAdapter.addItem(historyEntry);
        }
    }

}
