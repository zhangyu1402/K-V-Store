package com.zhangyu.raft.entity;

public class AppendLogRequestParam {
    private int term;
    private String leadId;
    private int prevLogIndex;
    private int prevLogTerm;
    private int leaderCommit;
    private LogEntry logEntry;
    private String command;

    public AppendLogRequestParam() {
    }

    private AppendLogRequestParam(Builder builder) {
        term = builder.term;
        leadId = builder.leadId;
        prevLogIndex = builder.prevLogIndex;
        prevLogTerm = builder.prevLogTerm;
        leaderCommit = builder.leaderCommit;
        logEntry = builder.logEntry;
        command = builder.command;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }

    public int getPrevLogIndex() {
        return prevLogIndex;
    }

    public void setPrevLogIndex(int prevLogIndex) {
        this.prevLogIndex = prevLogIndex;
    }

    public int getPrevLogTerm() {
        return prevLogTerm;
    }

    public void setPrevLogTerm(int prevLogTerm) {
        this.prevLogTerm = prevLogTerm;
    }

    public int getLeaderCommit() {
        return leaderCommit;
    }

    public void setLeaderCommit(int leaderCommit) {
        this.leaderCommit = leaderCommit;
    }

    public LogEntry getLogEntry() {
        return logEntry;
    }

    public void setLogEntry(LogEntry logEntry) {
        this.logEntry = logEntry;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public static final class Builder {
        private int term;
        private String leadId;
        private int prevLogIndex;
        private int prevLogTerm;
        private int leaderCommit;
        private LogEntry logEntry;
        private String preVal;
        private String command;

        public Builder() {
        }

        public Builder term(int val) {
            term = val;
            return this;
        }

        public Builder leadId(String val) {
            leadId = val;
            return this;
        }

        public Builder prevLogIndex(int val) {
            prevLogIndex = val;
            return this;
        }

        public Builder prevLogTerm(int val) {
            prevLogTerm = val;
            return this;
        }

        public Builder leaderCommit(int val) {
            leaderCommit = val;
            return this;
        }

        public Builder logEntry(LogEntry val) {
            logEntry = val;
            return this;
        }

        public Builder preVal(String val) {
            preVal = val;
            return this;
        }

        public Builder command(String val) {
            command = val;
            return this;
        }

        public AppendLogRequestParam build() {
            return new AppendLogRequestParam(this);
        }
    }
}
