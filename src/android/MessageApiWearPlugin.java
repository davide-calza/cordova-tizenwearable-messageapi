package it.davidecalza.cordova.messageapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.support.v4.content.LocalBroadcastManager;

import com.samsung.android.sdk.accessory.SAPeerAgent;

import java.io.IOException;

import static it.davidecalza.cordova.messageapi.MessageApiWearService.mMessage;
import static it.davidecalza.cordova.messageapi.MessageApiWearService.agents;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;

public class MessageApiWearPlugin extends CordovaPlugin {

  private String lastID;

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
    cordova.getActivity().startService(new Intent(cordova.getActivity(), MessageApiWearService.class));
  }

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
    Log.i("WearService", "exec");
    if (action.equals("getMessages")) {
      try { getMessages(args, callbackContext); }
      catch (Exception ex) { callbackContext.error("Error on getting messages"); return false; }
    }
    else if (action.equals("sendMessage")) {
      try { sendMessage(args, callbackContext); }
      catch (Exception ex) { callbackContext.error("Error on sending messages"); return false; }
    }
    else return false;
    return true;
  }

  private void getMessages(JSONArray args, final CallbackContext callbackContext) throws Exception {
    IntentFilter filter = new IntentFilter(Intent.ACTION_SEND);
    BroadcastReceiver receiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra("WearMessage");
        Log.i("WearService", "Received: " + msg);
        lastID = msg.split("_")[0];
        String wearMsg = msg.split("_")[1];

        PluginResult resultOK = new PluginResult(PluginResult.Status.OK, wearMsg);
        resultOK.setKeepCallback(true);
        callbackContext.sendPluginResult(resultOK);
      }
    };
    LocalBroadcastManager.getInstance(this.cordova.getActivity().getApplicationContext()).registerReceiver(receiver, filter);
  }

  private void sendMessage(JSONArray args, final CallbackContext callbackContext) throws Exception {
    String msg = args.getString(0);

    if(agents!=null){
      for (SAPeerAgent p : agents) {
        if (p.getPeerId().equals(lastID)) {
          if(sendData(p, "Hello from Phone") != -1){
            Log.i("WearService", "sent");
            PluginResult resultOK = new PluginResult(PluginResult.Status.OK, "Sent");
            resultOK.setKeepCallback(true);
            callbackContext.sendPluginResult(resultOK); 
          }
        }
      }
    }
  }

  public int sendData(SAPeerAgent peerAgent, String message) {
        int tid;
        if (mMessage != null) {
            try {
                tid = mMessage.send(peerAgent, message.getBytes());
                return tid;
            } catch (IOException e) {
                e.printStackTrace();
                return -1;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return -1;
    }
}
