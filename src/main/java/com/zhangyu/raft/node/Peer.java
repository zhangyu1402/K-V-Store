package com.zhangyu.raft.node;

import com.zhangyu.raft.entity.AppendLogRequestParam;
import com.zhangyu.raft.entity.Endpoint;
import com.zhangyu.raft.rpc.RpcClient;
import com.zhangyu.raft.rpc.impl.RpcClientImpl;

public class Peer implements Consensus, Node {

    private RpcClient client;

    private Endpoint endpoint;

    private String id;

    public Peer(Endpoint endpoint) {

        this.endpoint = endpoint;
        this.id = endpoint.toString();

        client = new RpcClientImpl(endpoint.getHost(), endpoint.getPort());
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean HandleAskForVote(int term, String candidateId, int lastLogIndex, int lastLogTerm) {
        return client.askForVote(term, candidateId, lastLogIndex, lastLogTerm);
    }

    @Override
    public boolean HandleAppendLog(AppendLogRequestParam param) {
        return client.appendLog(param);
    }

    @Override
    public boolean HandleSendHeartBeatTask(AppendLogRequestParam param) {
        return client.appendLog(param);
    }

    @Override
    public String get(String key) {
        return "";
    }

    @Override
    public boolean add(String key, String value) {
        return false;
    }

    @Override
    public boolean del(String key) {
        return false;
    }

    @Override
    public boolean update(String key, String value) {
        return false;
    }

    @Override
    public void start() {

    }
}
