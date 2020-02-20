package com.zhangyu.raft.node;

import com.zhangyu.raft.config.Configuration;
import com.zhangyu.raft.config.ConfigurationHelper;
import com.zhangyu.raft.entity.AppendLogRequestParam;
import com.zhangyu.raft.entity.DataEntry;
import com.zhangyu.raft.entity.Endpoint;
import com.zhangyu.raft.entity.LogEntry;
import com.zhangyu.raft.enums.StateEnum;
import com.zhangyu.raft.log_module.LogModule;
import com.zhangyu.raft.log_module.LogModuleImpl;
import com.zhangyu.raft.rpc.RpcServer;
import com.zhangyu.raft.storage.DefaultStorage;
import com.zhangyu.raft.storage.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import static com.zhangyu.raft.enums.CommandEnum.POST;
import static com.zhangyu.raft.enums.StateEnum.*;


/**
 * a node's default implementation. it implements all function in Consensus and Node
 * interface.
 */
public class DefaultNode implements Consensus, Node {

    public static final Logger LOG = LoggerFactory.getLogger(DefaultNode.class);
    public Peer leader;
    private String id;
    private int term;
    private LogModule logModule;
    private Endpoint endpoint;
    private StateEnum curState;
    private Set<Peer> peers;
    private Storage storage;
    private RpcServer rpcServer;
    private boolean suspend;
    private int heartBeatTimeUnit;
    private ScheduledExecutorService scheduledExecutorService;
    private ExecutorService threadPoolExecutor;
    private Future heartBeatTaskFuture;
    private boolean receiveHeartBeat;
    private Random random;

    public DefaultNode(Configuration configuration) {
        endpoint = configuration.origin.endpoint;
        term = 1;
        id = configuration.origin.id;
        peers = new HashSet<>();
        curState = StateEnum.FOLLOWER;
        heartBeatTimeUnit = configuration.origin.heartbeatTimeout;
        threadPoolExecutor = Executors.newCachedThreadPool();
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        random = new Random();
        receiveHeartBeat = false;
        storage = new DefaultStorage();
        leader = null;
        rpcServer = new RpcServer(this, this, endpoint);
        for (Endpoint e : configuration.registry) {
            peers.add(new Peer(e));
        }
    }

    public static void main(String[] args) {
        try {
            Configuration config = ConfigurationHelper.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Endpoint getEndpoint() {
        return endpoint;
    }

    public boolean askForVote() {
        term++;
        AppendLogRequestParam param = new AppendLogRequestParam.Builder()
                .leadId(this.id)
                .term(this.term)

                .build();
        int[] count = new int[]{0};
        LOG.info("asking for vote, term : {}", term);
        LogEntry lastLog = logModule.getLast();
        peers.parallelStream().forEach(peer -> {
            if (peer.HandleAskForVote(term, id, (int) lastLog.getIndex(), (int) lastLog.getTerm()))
                synchronized (count) {
                    count[0]++;
                }
        });
        if (count[0] >= peers.size()) {
            synchronized (this) {
                curState = StateEnum.LEADER;
            }
            return true;
        }
        curState = FOLLOWER;
        return false;
    }

    public boolean appendLog() {
//        peers.parallelStream().forEach(peer -> {
//            LogEntry logEntry = new LogEntry();
//            logEntry.setTerm(term);
//            logEntry.setIndex(logModule.getLastIndex()+1);
//            DataEntry dataEntry = new DataEntry(key,value);
//            logEntry.setDataEntry(dataEntry);
//            logEntry.setCommand(POST);
//            String preVal = storage.containsKey(key)? storage.get(key):null;
//            AppendLogRequestParam param = new AppendLogRequestParam.Builder()
//                    .command("POST")
//                    .prevLogIndex(logModule.getLastIndex())
//                    .term(term)
//                    .prevLogTerm(logModule.getLast().getTerm())
//                    .preVal(preVal)
//                    .leadId(endpoint.toString())
//                    .logEntry(logEntry)
//                    .build();
//        });
        return false;

    }

    public boolean handleHeartBeartTask() {
        receiveHeartBeat = true;
        curState = FOLLOWER;
        return true;
    }

    public boolean sendHeartBeatTask() {
        LOG.info("sending heart beat");
        AppendLogRequestParam param = new AppendLogRequestParam.Builder()
                .leadId(endpoint.toString())
                .term(term)
                .prevLogIndex(logModule.getLastIndex())
                .prevLogTerm(logModule.getLast().getTerm())
                .build();
        peers.parallelStream().forEach(peer -> {
            boolean status = peer.HandleSendHeartBeatTask(param);
            if (!status) {

            }
        });
        return true;
    }

    @Override
    public String get(String key) {
        if (storage.containsKey(key)) return storage.get(key);
        return null;
    }

    @Override
    public boolean add(String key, String value) {

        if (curState == LEADER) {
            LOG.debug("I am the leader, handle add request");
            LogEntry logEntry = new LogEntry();
            logEntry.setTerm(term);
            logEntry.setIndex(logModule.getLastIndex() + 1);
            DataEntry dataEntry = new DataEntry(key, value);
            logEntry.setDataEntry(dataEntry);
            logEntry.setCommand(POST);
            String preVal = storage.containsKey(key) ? storage.get(key) : null;
            AppendLogRequestParam param = new AppendLogRequestParam.Builder()
                    .command("POST")
                    .prevLogIndex(logModule.getLastIndex())
                    .term(term)
                    .prevLogTerm(logModule.getLast().getTerm())
                    .preVal(preVal)
                    .leadId(endpoint.toString())
                    .logEntry(logEntry)
                    .build();
            int[] vote = new int[]{0};
            peers.parallelStream().forEach((e) -> {
                if (e.HandleAppendLog(param)) {
                    vote[0]++;
                }
            });
            System.out.println(vote[0]);
            if (vote[0] > peers.size() / 2) {
                storage.add(key, value);
                logModule.write(logEntry);
                return true;
            }
            return false;

        } else {
            return leader.add(key, value);
        }
//        return false;
    }

    public boolean del(String key) {
        return false;
    }

    @Override
    public boolean update(String key, String value) {
        return false;
    }

    public void init() {
        logModule = new LogModuleImpl();
        LogEntry logEntry = new LogEntry();
        logEntry.setIndex(-1);
        logEntry.setTerm(0);
        logModule.write(logEntry);
    }

    public void start() {
        init();
        System.out.println("Node start");
        LOG.info("Node start");
        threadPoolExecutor.submit(() -> {
            try {
                rpcServer.start();
                rpcServer.blockUntilShutdown();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        try {
            lifeCycleBegin();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void lifeCycleBegin() throws InterruptedException {
        while (!suspend) {
            switch (curState) {
                case FOLLOWER:
                    LOG.info("update state to FOLLOWER");
                    Thread.sleep(heartBeatTimeUnit + random.nextInt(10000));
                    if (!receiveHeartBeat) curState = CANDIDATE;
                    break;
                case CANDIDATE:
                    LOG.info("update state to CANDIDATE");
                    askForVote();
                    break;

                case LEADER:
                    LOG.info("update state to LEADER term:{}", term);
//                    heartBeatTaskFuture = scheduledExecutorService.scheduleAtFixedRate(this::sendHeartBeatTask,10,heartBeatTimeUnit, TimeUnit.MILLISECONDS);
                    sendHeartBeatTask();
                    Thread.sleep(heartBeatTimeUnit);
//                    this.node.sendHeartbeat();
//                    this.node.getHeartbeatTimer().start();
//                    synchronized (this) {
//                        try {
//                            this.wait();
//                        } catch (InterruptedException e) {
//                            LOG.warn(e.getMessage());
//                        }
//                    }
                    break;
            }
        }
    }

    @Override
    public boolean HandleAskForVote(int term, String candidateId, int lastLogIndex, int lastLogTerm) {
        if (this.term >= term) return false;
        LogEntry lastLog = logModule.getLast();
        if (lastLog.getIndex() > lastLogIndex) return false;
        if (lastLog.getTerm() > lastLogTerm) return false;
        this.term = term;
        return true;
    }

    @Override
    public boolean HandleAppendLog(AppendLogRequestParam param) {
        System.out.println(param.getTerm() + " " + param.getPrevLogIndex() + " " + param.getPrevLogTerm() + "|" + term + " " + logModule.getLastIndex());
        if (param.getTerm() < term) return false;
        int preInx = logModule.getLastIndex();
        int preTerm = logModule.getLast().getTerm();
        if (param.getPrevLogIndex() != preInx) return false;
        if (param.getPrevLogTerm() != preTerm) return false;
        LogEntry logEntry = new LogEntry();
        logEntry.setTerm(term);
        logEntry.setIndex(logModule.getLastIndex() + 1);
        DataEntry dataEntry = param.getLogEntry().getDataEntry();
        logEntry.setDataEntry(dataEntry);
        logEntry.setCommand(POST);
        logModule.write(logEntry);
        storage.add(dataEntry.getKey(), dataEntry.getValue());
        return true;
    }

    @Override
    public boolean HandleSendHeartBeatTask(AppendLogRequestParam param) {
        LOG.info("received heartbeat");
        String leaderId = param.getLeadId();
        LOG.debug("leader id is {}", leaderId);
        for (Peer peer : peers) {
            if (peer.getId().equals(leaderId)) {
                leader = peer;
                break;
            }
        }
        receiveHeartBeat = true;
        curState = FOLLOWER;
        return true;
    }
}
