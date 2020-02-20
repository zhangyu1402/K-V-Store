package com.zhangyu.raft.log_module;

import com.zhangyu.raft.entity.LogEntry;


/**
 * LogModule interface is to provide function to manager logEntries for a node
 */
public interface LogModule {

    /**
     * add a logEntry to the end of logs.
     *
     * @param logEntry
     */
    void add(LogEntry logEntry);

    /**
     * remove from the startIndex to the end.
     *
     * @param startIndex
     */
    void removeOnStartIndex(Long startIndex);

    /**
     * get the last logEntry
     * @return
     */
    LogEntry getLast();

    /**
     * get the size to logs
     * @return
     */
    int getSize();

    /**
     * get the index of last logEntry
     * @return
     */
    int getLastIndex();

    /**
     * get the LogEntry with index
     * @param index
     * @return
     */
    LogEntry get(int index);

}
