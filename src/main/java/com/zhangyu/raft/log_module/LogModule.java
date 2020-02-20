package com.zhangyu.raft.log_module;

import com.zhangyu.raft.entity.LogEntry;


/**
 * LogModule interface is to provide function to manager logEntries for a node
 */
public interface LogModule {

    void write(LogEntry logEntry);

    LogEntry read(Long index);

    void removeOnStartIndex(Long startIndex);

    LogEntry getLast();

    int getSize();

    int getLastIndex();

    LogEntry get(int index);


}
