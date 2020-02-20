package com.zhangyu.raft.rpc;

import com.google.protobuf.StringValue;
import com.zhangyu.raft.entity.AppendLogRequestParam;
import com.zhangyu.raft.entity.Endpoint;
import com.zhangyu.raft.node.Consensus;
import com.zhangyu.raft.node.Node;
import com.zhangyu.raft.util.ParamToRequest;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;

public class RpcServer extends RaftGrpc.RaftImplBase {

    private Node node;

    private Consensus consensus;
    private io.grpc.Server server;
    private Endpoint endpoint;

    public RpcServer(Node node, Consensus consensus, Endpoint endpoint) {
        this.node = node;
        this.consensus = consensus;
        this.endpoint = endpoint;
    }

    public static void main(String[] args) {
//        RpcServer rpcServer = new RpcServer();
//        try {
//            rpcServer.start();
//            rpcServer.blockUntilShutdown();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void setConsensus(Consensus consensus) {
        this.consensus = consensus;
    }

    public void setEndpoint(Endpoint endpoint) {
        this.endpoint = endpoint;
    }

    public void start() throws IOException {
        server = ServerBuilder.forPort(endpoint.getPort()).addService(this).build().start();
    }

    public void blockUntilShutdown() throws InterruptedException {
        if (server != null)
            server.awaitTermination();
    }

    public void stop() {
        this.server.shutdown();
    }

    @Override
    public void askForVote(RaftOuterClass.VoteRequest request, StreamObserver<RaftOuterClass.VoteResponse> responseObserver) {
        int term = request.getTerm();
        String candidateId = request.getCandidateId();
        int lastLogIndex = request.getLastLogIndex();
        int lastLogTerm = request.getLastLogTerm();

        boolean status = consensus.HandleAskForVote(term, candidateId, lastLogIndex, lastLogTerm);

        responseObserver.onNext(RaftOuterClass.VoteResponse.newBuilder().setStatus(status).build());
        responseObserver.onCompleted();

    }

    @Override
    public void appendLog(RaftOuterClass.AppendLogRequest request, StreamObserver<RaftOuterClass.AppendLogResponse> responseObserver) {
        AppendLogRequestParam param = ParamToRequest.requestToParam(request);
        boolean status;
        if (param.getLogEntry() == null) {
            status = consensus.HandleSendHeartBeatTask(param);
        } else {
            status = consensus.HandleAppendLog(param);
        }
        responseObserver.onNext(RaftOuterClass.AppendLogResponse.newBuilder().setStatus(status).build());
        responseObserver.onCompleted();

    }

    @Override
    public void get(RaftOuterClass.DataEntry request, StreamObserver<RaftOuterClass.CRUDresponse> responseObserver) {
        String value = node.get(request.getKey());
        RaftOuterClass.DataEntry.Builder builder = RaftOuterClass.DataEntry.newBuilder();
        if (value != null) {
            builder.setKey(request.getKey());
            builder.setValue(StringValue.of(value));
            responseObserver.onNext(RaftOuterClass.CRUDresponse.newBuilder().setStatus(true).setDateEntry(builder.build()).build());
            responseObserver.onCompleted();
        } else {
            responseObserver.onNext(RaftOuterClass.CRUDresponse.newBuilder().setStatus(false).setDateEntry(builder.build()).build());
            responseObserver.onCompleted();
        }

    }

    @Override
    public void del(RaftOuterClass.DataEntry request, StreamObserver<RaftOuterClass.CRUDresponse> responseObserver) {
        super.del(request, responseObserver);
    }

    @Override
    public void post(RaftOuterClass.DataEntry request, StreamObserver<RaftOuterClass.CRUDresponse> responseObserver) {
        boolean status = node.add(request.getKey(), request.getValue().getValue());
        responseObserver.onNext(RaftOuterClass.CRUDresponse.newBuilder().setStatus(status).build());
        responseObserver.onCompleted();
    }

    @Override
    public void update(RaftOuterClass.DataEntry request, StreamObserver<RaftOuterClass.CRUDresponse> responseObserver) {
        super.update(request, responseObserver);
    }
}
