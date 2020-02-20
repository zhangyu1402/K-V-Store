package com.zhangyu.raft.enums;

public enum CommandEnum {
    POST("POST"),
    GET("GET"),
    DEL("DEL"),
    UPDATE("UPDATE");


    String value;

    CommandEnum(String value) {
        this.value = value;
    }

    public static CommandEnum of(String value) {
        switch (value) {
            case "POST":
                return POST;
            case "GET":
                return GET;
            case "DEL":
                return DEL;
            case "UPDATE":
                return UPDATE;
            default:
                return null;
        }
    }

    public String getValue() {
        return value;
    }
}
