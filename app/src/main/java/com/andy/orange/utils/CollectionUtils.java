package com.andy.orange.utils;

import java.util.Collection;

/**
 * Created by Administrator on 2017/8/17.
 */

public class CollectionUtils {

    public static boolean isNullOrEmpty(Collection c){
        if (null==c||c.isEmpty()){
            return true;
        }
        return false;
    }
}
