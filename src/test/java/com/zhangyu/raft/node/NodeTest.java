package com.zhangyu.raft.node;

import com.zhangyu.raft.config.Configuration;
import com.zhangyu.raft.config.ConfigurationHelper;
import com.zhangyu.raft.rpc.RpcClient;
import com.zhangyu.raft.rpc.impl.RpcClientImpl;
import org.junit.Test;

import java.io.IOException;

public class NodeTest {

    @Test
    public void initNode1() throws IOException {
        Configuration configuration = ConfigurationHelper.load("config.json");
        Node node = new DefaultNode(configuration);
        node.start();
    }

    @Test
    public void initNode2() throws IOException {
        Configuration configuration = ConfigurationHelper.load("config1.json");
        Node node = new DefaultNode(configuration);
        node.start();
    }

    @Test
    public void initNode3() throws IOException {
        Configuration configuration = ConfigurationHelper.load("config2.json");
        Node node = new DefaultNode(configuration);
        node.start();
    }

    @Test
    public void postTest() throws IOException {

        RpcClient client = new RpcClientImpl("127.0.0.1", 5001);
        long start = System.currentTimeMillis();
        String[] res = new String[1000];
        System.out.println(start);
        for (int i = 0; i < 1000; i++) {
            String s = String.valueOf(i);
            res[i] = client.get(s);
        }
        System.out.println(System.currentTimeMillis() - start);
        for (int i = 0; i < 1000; i++) {
            System.out.println(i + " : " + res[i]);
        }

    }

}
