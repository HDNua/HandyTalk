package com.tistory.hdnua.sutda;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 클라이언트 정보입니다.
 */
public class ClientInfo {
    //region 필드를 정의합니다.
    private String userName;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public String getUserName() { return userName; }
    public Socket getSocket() { return socket; }
    public BufferedReader getReader() { return reader; }
    public PrintWriter getWriter() { return writer; }

    @Override
    public int hashCode() {
        return userName.hashCode();
    }
    @Override
    public boolean equals(Object obj) {
        return hashCode() == obj.hashCode();
    }
    @Override
    public String toString() {
        return userName;
    }

    //endregion



    //region 생성자를 정의합니다.
    @Deprecated
    public ClientInfo(String userName) {
        this.userName = userName;
    }
    public ClientInfo(String userName,
                      Socket socket,
                      BufferedReader reader,
                      PrintWriter writer) {
        this.userName = userName;
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
    }

    public void setProperties(Socket socket, BufferedReader reader, PrintWriter writer) {
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
    }

    //endregion



    //region 메서드를 정의합니다.


    //endregion








    //region 구형 정의를 보관합니다.


    //endregion
}
