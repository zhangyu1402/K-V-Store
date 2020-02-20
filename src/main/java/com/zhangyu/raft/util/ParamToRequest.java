package com.zhangyu.raft.util;

import com.google.protobuf.StringValue;
import com.zhangyu.raft.entity.AppendLogRequestParam;
import com.zhangyu.raft.entity.DataEntry;
import com.zhangyu.raft.entity.LogEntry;
import com.zhangyu.raft.enums.CommandEnum;
import com.zhangyu.raft.rpc.RaftOuterClass;

public class ParamToRequest {
    public static RaftOuterClass.AppendLogRequest paramToRequest(AppendLogRequestParam param) {
        RaftOuterClass.AppendLogRequest.Builder requestBuilder = RaftOuterClass.AppendLogRequest.newBuilder();
        requestBuilder.setPrevLogIndex(param.getPrevLogIndex());
        requestBuilder.setPrevLogTerm(param.getPrevLogTerm());
        requestBuilder.setTerm(param.getTerm());
        requestBuilder.setLeaderId(param.getLeadId());

        if (param.getLogEntry() == null) {
            return requestBuilder.build();
        }

        RaftOuterClass.LogEntry.Builder logEntryBuilder = RaftOuterClass.LogEntry.newBuilder();
        logEntryBuilder.setTerm(param.getLogEntry().getTerm());
        logEntryBuilder.setIndex(param.getLogEntry().getIndex());

        RaftOuterClass.DataEntry.Builder dataBuilder = RaftOuterClass.DataEntry.newBuilder();

        dataBuilder.setKey(param.getLogEntry().getDataEntry().getKey());
        dataBuilder.setValue(StringValue.of(param.getLogEntry().getDataEntry().getValue()));

        logEntryBuilder.setDataEntry(dataBuilder.build());

        if (param.getLogEntry().getCommand() != null) {
            logEntryBuilder.setCommand(StringValue.of(param.getLogEntry().getCommand().getValue()));
        }

        if (param.getLogEntry().getPreVal() != null) {
            logEntryBuilder.setPreVal(StringValue.of(param.getLogEntry().getPreVal()));
        }
        requestBuilder.setLogEntry(logEntryBuilder.build());
        return requestBuilder.build();
    }

    public static AppendLogRequestParam requestToParam(RaftOuterClass.AppendLogRequest request) {
        AppendLogRequestParam param = new AppendLogRequestParam();
        param.setLeadId(request.getLeaderId());
        param.setPrevLogIndex(request.getPrevLogIndex());
        param.setPrevLogTerm(request.getPrevLogTerm());
        param.setTerm(request.getTerm());
        if (request.getLogEntry().getTerm() == 0) {
            return param;
        }
        LogEntry logEntry = new LogEntry();
        DataEntry dataEntry = new DataEntry(request.getLogEntry().getDataEntry().getKey(), request.getLogEntry().getDataEntry().getValue().getValue());
        logEntry.setDataEntry(dataEntry);
        logEntry.setIndex(request.getLogEntry().getIndex());
        logEntry.setTerm(request.getLogEntry().getTerm());

        if (!request.getLogEntry().getCommand().equals("")) {
            logEntry.setCommand(CommandEnum.of(request.getLogEntry().getCommand().getValue()));
        }
        if (!request.getLogEntry().getPreVal().equals("")) {
            logEntry.setPreVal(request.getLogEntry().getPreVal().getValue());
        }
        param.setLogEntry(logEntry);
        return param;

    }
}
