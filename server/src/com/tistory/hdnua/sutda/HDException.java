package com.tistory.hdnua.sutda;

public class HDException extends Exception {
    public final String senderName;

    public HDException(String s, String senderName) {
        super(s);
        this.senderName = senderName;
    }
}
