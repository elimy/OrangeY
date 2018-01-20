package com.andy.orange.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/9/4.
 */

public class ProgressReceiver extends BroadcastReceiver {

    private Handler mHandler;

    public ProgressReceiver(){}

    public ProgressReceiver(Handler handler){
        mHandler=handler;
    }

    /*
    *接收广播，并将通过handler将消息传递给UI线程
    * */
    @Override
    public void onReceive(Context context, Intent intent) {

        if (mHandler==null){
            return;
        }
        Bundle bundle=intent.getExtras();
        Message message=Message.obtain();
        if (bundle.get("progress")!=null){

            int pos = (int) bundle.get("progress");
            message.arg1= pos;

        }else {
            Log.d("progress","progress is null");
        }

        if (bundle.get("max")!=null){
            int max = (int) bundle.get("max");
            message.arg2= max;
        }

        mHandler.sendMessage(message);
    }
}
