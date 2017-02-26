package com.tistory.hdnua.sutda;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Job입니다.
 */
public abstract class Job implements Runnable {
    //region 필드를 정의합니다.
    protected Server server;

    protected Socket socket;
    protected BufferedReader reader;
    protected PrintWriter writer;

    private ClientInfo clientInfo;

    /**
     * 클라이언트 정보를 획득합니다.
     */
    public ClientInfo getClientInfo() {
        return clientInfo;
    }
    /**
     * 클라이언트 정보를 설정합니다.
     */
    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    //endregion



    //region 생성자를 정의합니다.
    /**
     * Job입니다.
     * @param server 서버 객체입니다.
     * @param socket 서버와 연결된 소켓입니다.
     * @param reader 소켓 입력 스트림입니다.
     * @param writer 소켓 출력 스트림입니다.
     */
    public Job(Server server, Socket socket, BufferedReader reader, PrintWriter writer) {
        this.server = server;

        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
    }

    //endregion



    //region 메서드를 정의합니다.
    /**
     * 소켓 writer로 문자열을 출력합니다.
     * @param message 전송할 메시지입니다.
     */
    protected void send(String message) {
        writer.println(message);
        writer.flush();
    }
    /**
     * 소켓 reader로부터 문자열을 획득합니다.
     * @throws IOException readLine() 메서드가 실패하면 예외를 던집니다.
     */
    protected String recv() throws IOException {
        String message;
        while ((message = reader.readLine()) != null)
            break;
        return message;
    }

    //endregion



    //region 구형 정의를 보관합니다.

    //endregion
}
