package com.hundredcommits.messengerx.utils;

public class AppUtil {

    private AppUtil() {
    }

    public static boolean isEmpty(String statement){
        return statement == null || statement.trim().equals("");
    }
}
