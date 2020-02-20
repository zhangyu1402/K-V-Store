package com.zhangyu.raft.log_module;

import com.zhangyu.raft.entity.LogEntry;

import java.util.ArrayList;
import java.util.List;

public class LogModuleImpl implements LogModule {

    private List<LogEntry> logs;

    public LogModuleImpl() {
        logs = new ArrayList<>();
    }

    public void write(LogEntry logEntry) {
        logs.add(logEntry);
    }

    public LogEntry read(Long index) {
        return null;
    }

    public void removeOnStartIndex(Long startIndex) {

    }

    public LogEntry getLast() {

        return logs.get(logs.size() - 1);
    }

    @Override
    public int getSize() {
        return logs.size();
    }

    public int getLastIndex() {

        return logs.size() - 1;
    }

    @Override
    public LogEntry get(int index) {
        if (index < logs.size()) return logs.get(index);
        return null;
    }
}
