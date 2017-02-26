package com.tistory.hdnua.sutda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 네트워크 통신 작업을 하는 Job입니다.
 */
public abstract class NetworkJob implements Runnable {
    //region 필드를 정의합니다.
    private String ip;
    private int port;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    //endregion


    //region 생성자를 정의합니다.
    public NetworkJob(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public final void run() {
        try {
            socket = new Socket(ip, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream());
            tryRoutine();
        }
        catch (Exception ex) {
            catchRoutine(ex);
        }
        finally {
            finallyRoutine();
        }
    }

    protected abstract void tryRoutine() throws Exception;
    protected void catchRoutine(Exception ex) { }
    protected void finallyRoutine() {
        try {
            closeSocket();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //endregion



    //region 메서드를 정의합니다.
    /**
     * 서버에 메시지를 전송합니다.
     * @param message 전송할 메시지입니다.
     */
    protected void send(String message) {
        writer.println(message);
        writer.flush();
    }
    /**
     * 서버로부터 메시지를 수신합니다.
     * @return 서버로부터 수신한 메시지를 반환합니다.
     */
    protected String recv() throws IOException {
        String message;
        while ((message = reader.readLine()) != null)
            break;
        return message;
    }

    /**
     * 소켓 연결을 종료합니다.
     */
    public void closeSocket() throws IOException {
        if (socket != null) {
            socket.close();
        }
    }
    /**
     * 소켓 종료를 막습니다.
     */
    public void preventSocketClose() {
        socket = null;
    }

    //endregion
}
