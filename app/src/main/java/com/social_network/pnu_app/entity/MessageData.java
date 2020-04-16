package com.social_network.pnu_app.entity;

import java.util.Date;
import java.util.Map;

public class MessageData {

    String message;


    boolean seen;
    long time;
    String type;
    String key;

    public MessageData(String message, boolean seen, String type, String key){
        this.message = message;
        this.seen=seen;
        this.time = new Date().getTime();
        this.type = type;
        this.key=key;

    }

   public MessageData(){}


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public boolean isSeen() { return seen; }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getKey() { return key; }

    public void setKey(String key) { this.key = key; }


}
