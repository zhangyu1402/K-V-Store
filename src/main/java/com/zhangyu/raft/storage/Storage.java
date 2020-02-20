package com.zhangyu.raft.storage;


/**
 * interface to store the key value pairs. Currently I just use HashMap to implement this
 * interface. if needed, people can use their own implementation to do data persistence
 */
public interface Storage {

    public void add(String key, String value);

    public String get(String key);

    public void remove(String key);

    public boolean containsKey(String key);

}
