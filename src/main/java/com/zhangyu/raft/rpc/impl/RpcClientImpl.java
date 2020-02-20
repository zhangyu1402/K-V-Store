package com.zhangyu.raft.rpc.impl;

import com.google.protobuf.StringValue;
import com.zhangyu.raft.entity.AppendLogRequestParam;
import com.zhangyu.raft.node.DefaultNode;
import com.zhangyu.raft.rpc.RaftGrpc;
import com.zhangyu.raft.rpc.RaftOuterClass.*;
import com.zhangyu.raft.rpc.RpcClient;
import com.zhangyu.raft.util.ParamToRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcClientImpl implements RpcClient {

    public static final Logger LOG = LoggerFactory.getLogger(DefaultNode.class);
    private final ManagedChannel channel;
    private final RaftGrpc.RaftBlockingStub blockingStub;

    private String host;
    private int port;

    public RpcClientImpl(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext()
                .build());
        this.host = host;
        this.port = port;

    }

    RpcClientImpl(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = RaftGrpc.newBlockingStub(channel);
    }


    public static void main(String[] args) {
        RpcClientImpl client = new RpcClientImpl("127.0.0.1", 8001);
    }

    public boolean askForVote(int term, String candidateId, int lastLogIndex, int lastLogTerm) {
        VoteRequest voteRequest = VoteRequest.newBuilder()
                .setCandidateId(candidateId)
                .setLastLogIndex(lastLogIndex)
                .setLastLogTerm(lastLogTerm)
                .setTerm(term)
                .build();
        return blockingStub.askForVote(voteRequest).getStatus();
    }

    public void updateLog() {

    }

    public boolean appendLog(AppendLogRequestParam param) {
        AppendLogRequest request = ParamToRequest.paramToRequest(param);
        AppendLogResponse response = AppendLogResponse.newBuilder().setStatus(false).build();
        try {
            response = blockingStub.appendLog(request);
        } catch (Exception e) {
            LOG.error("can not connect to server: {}:{}. {}", this.host, this.port, e.getMessage());
            e.printStackTrace();
        }
        return response.getStatus();
    }

    @Override
    public String get(String key) {
        DataEntry dataEntry = DataEntry.newBuilder().setKey(key).build();
        return blockingStub.get(dataEntry).getDateEntry().getValue().getValue();
    }

    @Override
    public boolean post(String key, String value) {
        DataEntry dataEntry = DataEntry.newBuilder()
                .setKey(key).setValue(StringValue.of(value)).build();
        return blockingStub.post(dataEntry).getStatus();
    }

    @Override
    public boolean update(String key, String value) {
        return false;
    }

    @Override
    public boolean del(String key) {
        return false;
    }


}
