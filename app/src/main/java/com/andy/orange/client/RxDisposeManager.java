package com.andy.orange.client;


import android.support.v4.util.ArrayMap;

import java.util.Set;

import io.reactivex.disposables.Disposable;

/**
 * Created by Andy Lau on 2017/8/10.
 */

public class RxDisposeManager {

    private static RxDisposeManager instance=null;
    private ArrayMap<Object,Disposable> maps;

    /*
    * 单例模式
    * */
    public static RxDisposeManager getInstance(){
        if (instance==null){
            synchronized (RxDisposeManager.class){
                if (instance==null){
                    instance=new RxDisposeManager();
                }
            }
        }
        return instance;
    }

    /*
    * 默认私有构造
    * */
    private RxDisposeManager(){
        maps=new ArrayMap<>();
    }

    /*
    * 添加disposable
    * */
    public void add(Object tag,Disposable disposable){
        maps.put(tag,disposable);
    }

    /*
    * 移除某个disposable
    * */
    public void remove(Object tag){
        if (!maps.isEmpty()){
            maps.remove(tag);
        }
    }

    /*
    * 移除所有的disposable
    * */
    public void removeAll(){
        if (!maps.isEmpty()){
            maps.clear();
        }
    }

    /*
    * 取消并移除指定的disposable
    * */
    public void cancel(Object tag) {
        if (maps.isEmpty()) {
            return;
        }
        if (maps.get(tag) == null) {
            return;
        }
        if (!maps.get(tag).isDisposed()) {
            maps.get(tag).dispose();
            maps.remove(tag);
        }
    }

    /*
    * 取消map中所有的disposable并移除
    * */
    public void cancelAll() {
        if (maps.isEmpty()) {
            return;
        }
        Set<Object> keys = maps.keySet();
        for (Object apiKey : keys) {
            cancel(apiKey);
        }
    }
}
