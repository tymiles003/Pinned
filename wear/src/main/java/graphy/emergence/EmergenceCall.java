package graphy.emergence;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.wearable.activity.InsetActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

public class EmergenceCall extends InsetActivity
{

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).build();
        mGoogleApiClient.connect();

        // Create an intent for the reply action
        Intent actionIntent = new Intent(this, sendRequest.class);
        PendingIntent actionPendingIntent =
                PendingIntent.getActivity(this, 0, actionIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        // Create the action
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_launcher,
                        getString(R.string.label), actionPendingIntent)
                        .build();

        Notification notification = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentText("EmergencE!")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setOngoing(true)
                .extend(new NotificationCompat.WearableExtender().addAction(action))
                .build();


        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(40000, notification);
    }

    @Override
    public void onReadyForContent() {
        setContentView(R.layout.activity_emergence_call);
        Button sendBtn = (Button) findViewById(R.id.send_help);
        sendBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onButtonClicked(v);
            }
        });
    }

    public void onButtonClicked(View target)
    {
        //If unable to connect, we can't do anything with the button click.
        if(mGoogleApiClient == null)
            return;

        //Get the nodes connected to this wearable.
        final PendingResult<NodeApi.GetConnectedNodesResult> nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient);

        nodes.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>()
        {
            @Override
            public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult)
            {
                //Get the resultant list of nodes
                final List<Node> nodes = getConnectedNodesResult.getNodes();

                if (nodes != null)
                {
                    for(int i = 0; i < nodes.size(); i++)
                    {
                        //Grab each node.
                        final Node node = nodes.get(i);

                        //Send the emergency notice to the node, which will in turn cause the app to handle the message.
                        Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), "/MESSAGE", null);
                    }
                }
            }
        });



    }
}