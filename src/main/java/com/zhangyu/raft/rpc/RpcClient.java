package com.zhangyu.raft.rpc;

import com.zhangyu.raft.entity.AppendLogRequestParam;

/**
 * interface to call RPC function
 */
public interface RpcClient {

    /**
     * ask for vote
     *
     * @param term
     * @param candidateId
     * @param lastLogIndex
     * @param lastLogTerm
     * @return true if vote, false if reject
     */
    public boolean askForVote(int term, String candidateId, int lastLogIndex, int lastLogTerm);

    public void updateLog();

    /**
     * I want to use one RPC function to do two thing. when there is no LogEntry in params, it is to send
     * a heartbeat. otherwise it is to append a LogEntry.
     *
     * @param appendLogRequestParam contains all parameters need to construct a request to append log.
     * @return
     */
    public boolean appendLog(AppendLogRequestParam appendLogRequestParam);

    public String get(String key);

    public boolean post(String key, String value);

    public boolean update(String key, String value);

    public boolean del(String key);
}
