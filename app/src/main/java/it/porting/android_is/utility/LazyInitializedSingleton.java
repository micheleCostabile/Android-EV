package it.porting.android_is.utility;

import java.util.HashMap;
import java.util.Map;

public class LazyInitializedSingleton {

    private static LazyInitializedSingleton instance;
    private static Map<String, Object> user = new HashMap<>();

    private LazyInitializedSingleton(){}

    public static LazyInitializedSingleton getInstance(){
        if(instance == null){
            instance = new LazyInitializedSingleton();
        }
        return instance;
    }

    public void clearInstance(){
        this.user = null;
        instance = null;
    }


    public void setUser(Map<String, Object> user){
        this.user = user;
    }

    public Map<String, Object> getUser(){
        return this.user;
    }
}