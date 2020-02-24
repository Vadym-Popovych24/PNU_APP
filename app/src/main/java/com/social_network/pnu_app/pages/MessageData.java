package com.social_network.pnu_app.pages;

import java.util.Date;

public class MessageData {
    public String userName;
    public String textMessage;
    private long messageTime;

    public MessageData(){}

    public MessageData(String userName, String textMessage){
        this.userName = userName;
        this.textMessage = textMessage;

        this.messageTime = new Date().getTime();
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
