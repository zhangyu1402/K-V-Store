package com.zhangyu.raft.node;

/**
 * The methods from user to  post, delete, get, update data
 */
public interface Node {
    public String get(String key);

    public boolean add(String key, String value);

    public boolean del(String key);

    public boolean update(String key, String value);

    public void start();

}
