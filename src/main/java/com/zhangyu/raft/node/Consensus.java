package com.zhangyu.raft.node;

import com.zhangyu.raft.entity.AppendLogRequestParam;

/**
 * Consensus interface provide function to keep system Consensus
 */
public interface Consensus {

    /**
     * function to handle vote  request
     *
     * @param term
     * @param candidateId
     * @param lastLogIndex
     * @param lastLogTerm
     * @return true means vote for it.
     */
    public boolean HandleAskForVote(int term, String candidateId, int lastLogIndex, int lastLogTerm);

    /**
     * function to handle append log request
     *
     * @param param
     * @return true if accept this log,
     */
    public boolean HandleAppendLog(AppendLogRequestParam param);

    /**
     * function to handle heart beat request.
     *
     * @param param
     * @return true if accept this heart beat request.
     */
    public boolean HandleSendHeartBeatTask(AppendLogRequestParam param);
}
