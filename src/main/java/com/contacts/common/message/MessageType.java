package com.contacts.common.message;

public enum MessageType {
    ERROR("error"), SUCCESS("success");

    private final String msgType;

    MessageType(String msgType) {
        this.msgType = msgType;
    }

    @Override
    public String toString() {
        return this.msgType;
    }
}
