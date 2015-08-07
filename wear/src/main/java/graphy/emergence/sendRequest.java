package graphy.emergence;

import android.os.Bundle;
import android.support.wearable.activity.InsetActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.List;

/**
 * Created by justingagnon on 10/21/14.
 */
public class sendRequest extends InsetActivity
{
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(this).addApi(Wearable.API).build();
        mGoogleApiClient.connect();
    }
    @Override
    public void onReadyForContent() {
        setContentView(R.layout.activity_emergence_call);
        onButtonClicked();

    }

    public void onButtonClicked()
    {

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
