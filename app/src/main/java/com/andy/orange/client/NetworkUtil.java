package com.andy.orange.client;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

/**
 * Created by Administrator on 2017/9/4.
 */

public class NetworkUtil {

    //NetworkAvailable
    public static int NET_CNNT_BAIDU_OK=1;
    //no NetworkAvailable
    public static int NET_CNNT_DAIDU_TIMEOUT=2;
    //Net no ready
    public static int NET_NOT_PREPARE=3;
    //net error
    public static int NET_ERROR=4;
    // TIMEOUT
    private static int TIMEOUT=3000;

    /*
    * 判断网络连接情况
    * */
    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager manager= (ConnectivityManager) context.getApplicationContext().getSystemService(
                Context.CONNECTIVITY_SERVICE
        );

        if (null==manager){
            return false;
        }else {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (null==info||!info.isAvailable()){
                return false;
            }else {
                return true;
            }
        }

    }

    /*
    * 获取本地IP
    * */
    public static String getLocalIpAddr() {

        String addr = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface networkInterface = en.nextElement();
                for (Enumeration<InetAddress> enumeration = networkInterface.getInetAddresses(); enumeration.hasMoreElements();) {
                    InetAddress inetAddress = enumeration.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        addr = inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return addr;
    }

    /*
    * 获取当前的网络状态
    * */
    public static int getNetState(Context context){

        ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null!=manager){
            NetworkInfo info=manager.getActiveNetworkInfo();
            if (null!=info){
                if (info.isAvailable()&&info.isConnected()){
                    if (!connectionNetwork()){
                        return NET_CNNT_DAIDU_TIMEOUT;
                    }else {
                        return NET_CNNT_BAIDU_OK;
                    }
                }else {
                    return NET_NOT_PREPARE;
                }
            }
        }

        return NET_ERROR;
    }

    /*
    *ping百度，判断网络链接状态
    * */
    private static boolean connectionNetwork() {

        boolean ret=false;
        HttpURLConnection conn=null;

        try {
            conn= (HttpURLConnection) new URL("http://www.baidu.com").openConnection();
            conn.setConnectTimeout(TIMEOUT);
            conn.connect();
            ret=true;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(null!=conn){
                conn.disconnect();
            }
            conn=null;
        }
        return ret;
    }

    /*
    * 判断是否为3G网络
    * */
    public static boolean is3G(Context cxt){
        ConnectivityManager manager= (ConnectivityManager) cxt.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=manager.getActiveNetworkInfo();
        if (null!=info&&info.getType()==manager.TYPE_MOBILE){
            return true;
        }
        return false;
    }

    /*
    * 判断是否为2G网络
    * */
    public static boolean is2G(Context cxt){
        ConnectivityManager manager= (ConnectivityManager) cxt.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=manager.getActiveNetworkInfo();
        if (null!=info&&(info.getSubtype()== TelephonyManager.NETWORK_TYPE_EDGE
        ||info.getSubtype()==TelephonyManager.NETWORK_TYPE_GPRS||
        info.getSubtype()==TelephonyManager.NETWORK_TYPE_CDMA)){
            return true;
        }
        return false;
    }

    /*
    * 判断是否为wifi网络
    * */
    public static boolean isWifi(Context cxt){
        ConnectivityManager manager= (ConnectivityManager) cxt.getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info=manager.getActiveNetworkInfo();
        if (null!=info&&info.getType()==manager.TYPE_WIFI){
            return true;
        }else {
            return false;
        }
    }

    /*
    * 判断wifi是否开启
    * */
    public static boolean isWifiEnabled(Context cxt){
        ConnectivityManager manager= (ConnectivityManager) cxt.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager telManager= (TelephonyManager) cxt.getSystemService(Context.TELEPHONY_SERVICE);

        return ((manager.getActiveNetworkInfo()!=null&&manager.getActiveNetworkInfo().getState()==NetworkInfo.State.CONNECTED)
            ||telManager.getNetworkType()==TelephonyManager.NETWORK_TYPE_UMTS);
    }
}
