package com.zhangyu.raft.storage;


import java.util.HashMap;
import java.util.Map;

public class DefaultStorage implements Storage {

    private Map<String, String> db;

    public DefaultStorage() {
        db = new HashMap<>();
    }

    @Override
    public void add(String key, String value) {
        db.put(key, value);
    }

    @Override
    public String get(String key) {
        return db.get(key);
    }

    @Override
    public void remove(String key) {
        db.remove(key);
    }

    @Override
    public boolean containsKey(String key) {
        return db.containsKey(key);
    }


}
