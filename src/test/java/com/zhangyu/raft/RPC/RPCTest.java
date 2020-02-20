package com.zhangyu.raft.RPC;

import com.zhangyu.raft.entity.AppendLogRequestParam;
import com.zhangyu.raft.entity.DataEntry;
import com.zhangyu.raft.entity.LogEntry;
import com.zhangyu.raft.rpc.RaftGrpc;
import com.zhangyu.raft.rpc.RaftOuterClass;
import com.zhangyu.raft.rpc.RpcClient;
import com.zhangyu.raft.rpc.impl.RpcClientImpl;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.Test;

import java.io.IOException;

public class RPCTest {

    private AppendLogRequestParam param;

    @Test
    public void rpcServerTest() throws IOException, InterruptedException {
        TestServer server = new TestServer();
        server.start(5000);
        server.blockUntilShutdown();
    }

    @Test
    public void rpcClientTest() {
        param = new AppendLogRequestParam();
        param.setPrevLogIndex(1);
        param.setPrevLogTerm(1);
        param.setLeadId("1");
        param.setTerm(1);
        LogEntry logEntry = new LogEntry();
        logEntry.setTerm(1);
        logEntry.setIndex(1);
        logEntry.setDataEntry(new DataEntry("1", "1"));
        param.setLogEntry(logEntry);
        RpcClient client = new RpcClientImpl("127.0.0.1", 5000);
        client.appendLog(param);

    }

    class TestServer extends RaftGrpc.RaftImplBase {
        private io.grpc.Server server;

        @Override
        public void appendLog(RaftOuterClass.AppendLogRequest request, StreamObserver<RaftOuterClass.AppendLogResponse> responseObserver) {
            System.out.println(request);
        }

        public void start(int port) throws IOException {
            server = ServerBuilder.forPort(port).addService(this).build().start();
        }

        public void blockUntilShutdown() throws InterruptedException {
            if (server != null)
                server.awaitTermination();
        }

    }
}
