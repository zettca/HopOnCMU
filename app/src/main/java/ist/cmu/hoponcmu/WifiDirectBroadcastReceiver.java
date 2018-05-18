package ist.cmu.hoponcmu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import pt.inesc.termite.wifidirect.SimWifiP2pBroadcast;
import pt.inesc.termite.wifidirect.SimWifiP2pInfo;

public class WifiDirectBroadcastReceiver extends BroadcastReceiver {

    MainActivity activity;

    public WifiDirectBroadcastReceiver(MainActivity activity){
        this.activity=activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (SimWifiP2pBroadcast.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)){
            int state = intent.getIntExtra(SimWifiP2pBroadcast.EXTRA_WIFI_STATE,1);
            if (state == SimWifiP2pBroadcast.WIFI_P2P_STATE_ENABLED){
                //TODO
            }
            else {
                //TODO
            }
        } else if (SimWifiP2pBroadcast.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            //TODO
        } else if (SimWifiP2pBroadcast.WIFI_P2P_NETWORK_MEMBERSHIP_CHANGED_ACTION.equals(action)) {
            SimWifiP2pInfo ginfo = (SimWifiP2pInfo) intent.getSerializableExtra(SimWifiP2pBroadcast.EXTRA_GROUP_INFO);
            //TODO
        } else if (SimWifiP2pBroadcast.WIFI_P2P_GROUP_OWNERSHIP_CHANGED_ACTION.equals(action)) {
            SimWifiP2pInfo ginfo = (SimWifiP2pInfo) intent.getSerializableExtra(SimWifiP2pBroadcast.EXTRA_GROUP_INFO);
            //TODO
        }

    }
}
