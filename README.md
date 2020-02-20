# Distributed Key Value Store

![licence](<https://img.shields.io/badge/license-mit-brightgreen>) ![GRPC VERSION](<https://img.shields.io/badge/grpc-1.27.0-brightgreen>) ![version](<https://img.shields.io/badge/version-1.0.0-brightgreen>)

This project is a distributed key-value store system base on Raft protocal. In my parallel and distributed system class, I learned Raft and Paxos consensus algorithms. So I would like to write a project to practice what I learn in class. Distributed Key Value Store is very useful. It can be used as distributed database or service registration and discovery center.

I redesigned this project. The original repo is [here](<https://github.com/zhangyu1402/raft>). Currently I found there are some bad design in previous project.

## Improvement

1. The first and most direct improvement is the write and read speed increase by 220% and 150%.
2. Seperate the whole system into four module, log module, node module, rpc module, storage module. So there is less dependency between deferent modules. So the project become maintainable and extensible.(This is the mooooost important. And that is why I want to rewrite the previous project. :( )
3. Get rid of some unnecessary components like `RepeatTimer`,`RaftyMap`,`mapdb`. I use `scheduledExecutorService` to excute a some timed task instead of `RepeatTimer`.(ps: I wrote it myself, bad performance). For the `RaftyMap` and `mapdb`, I got rid of them because this project  focus on data Consensus. The data persistence part people can use whatever they like just by implementing the interface `Storage`.

##Usage

First you have to clone my code. And modify the `config.json` file with you own configuration. Start you cluster with at least three server.  Then if you want to use my Key-value system as a database in your program, you can implement a RPC client or use mine(I already provide a default implementation). BTW, I didn't implement the data persistence module. But I leave a interfaces for users, you can implement it yourself.

##Design 

The whole system is based on Raft algorithm. So I will This system contains four module.**log module**, **node module**, **rpc module**, **storage module**.

###log module

log module has a interface `LogModule` and its default implementation `LogModuleImpl`.  `LogModule` provide  functions to maintain the logs.

```java
/**
 * LogModule interface is to provide function to manager logEntries for a node
 */
public interface LogModule {

    /**
     * add a logEntry to the end of logs.
     * @param logEntry
     */
    void add(LogEntry logEntry);

    /**
     * remove from the startIndex to the end.
     * @param startIndex
     */
    void removeOnStartIndex(Long startIndex);

    /**
     * get the last logEntry
     * @return
     */
    LogEntry getLast();

    /**
     * get the size to logs
     * @return
     */
    int getSize();

    /**
     * get the index of last logEntry
     * @return
     */
    int getLastIndex();

    /**
     * get the LogEntry with index
     * @param index
     * @return
     */
    LogEntry get(int index);
}
```

### node module

node module contains two interfaces `Node` and `consensus`. The `Node` interface provide functions for customers to add, delete, get, update Key-Value pairs. The `consensus` provides function to keep consensus among the nodes.

```java
/**
 * The methods from user to  post, delete, get, update data
 */
public interface Node {
    public String get(String key);

    public boolean add(String key, String value);

    public boolean del(String key);

    public boolean update(String key, String value);

    public void start();

}
```

```java
public interface Consensus {
    /**
     * function to handle vote  request
     * @param term
     * @param candidateId
     * @param lastLogIndex
     * @param lastLogTerm
     * @return true means vote for it.
     */
    public boolean HandleAskForVote(int term, String candidateId, int lastLogIndex, int lastLogTerm);

    /**
     * function to handle append log request
     *
     * @param param
     * @return true if accept this log,
     */
    public boolean HandleAppendLog(AppendLogRequestParam param);

    /**
     * function to handle heart beat request.
     *
     * @param param
     * @return true if accept this heart beat request.
     */
    public boolean HandleSendHeartBeatTask(AppendLogRequestParam param);
}
```

There is a `DefaultNode` class implements those two interface. `DefaultNode` represents a server node in the distributed key-value store system. I had an idea to put `consensus` and it's implementation into another independent module. But I found the functions in consensus depends on some state of node. (I will keep updating this repo and Maybe I can find a good way to do this)

### RPC module

The RPC module contains a `RpcClient` and a `RpcServer`. I encapsulated the communication functions into these two class. Campared to previous design, I move all the code related to business logic out of this module. In this case, I will easy change the communication library in this project,( If i want to use http instead of rpc.)

### Storage module

Storage module provides some function to store the key value pairs. I just defined the interface but didn't pay much attention to implement it. You can implement it yourself in your favorite way. 

```java

/**
 * interface to store the key value pairs. Currently I just use HashMap to implement this
 * interface. if needed, people can use their own implementation to do data persistence
 */
public interface Storage {

    public void add(String key, String value);

    public String get(String key);

    public void remove(String key);

    public boolean containsKey(String key);
}
```

# Contributing

 Feel free to dive in! [Open an issue ](<https://github.com/zhangyu1402/K-V-Store/issues/new>) or submit PRs.



# License

 [MIT](https://opensource.org/licenses/MIT) © Yu Zhang

