package com.zhangyu.raft.entity;

import com.zhangyu.raft.enums.CommandEnum;

import java.io.Serializable;

public class LogEntry implements Serializable, Comparable {

    private int index;

    private int term;

    private DataEntry dataEntry;

    private String preVal;

    private CommandEnum command;

    public CommandEnum getCommand() {
        return command;
    }

    public void setCommand(CommandEnum command) {
        this.command = command;
    }

    public int compareTo(Object o) {
        return 0;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public DataEntry getDataEntry() {
        return dataEntry;
    }

    public void setDataEntry(DataEntry dataEntry) {
        this.dataEntry = dataEntry;
    }

    public String getPreVal() {
        return preVal;
    }

    public void setPreVal(String preVal) {
        this.preVal = preVal;
    }
}
