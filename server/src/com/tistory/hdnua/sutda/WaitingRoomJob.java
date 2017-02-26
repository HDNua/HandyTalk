package com.tistory.hdnua.sutda;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

/**
 * 대기실 Job입니다.
 */
public class WaitingRoomJob extends Job {
    //region 상수를 정의합니다.
    public final String EOF_MARKER = "goodbye";

    //endregion



    //region 생성자를 정의합니다.
    public WaitingRoomJob(Server server, Socket socket, BufferedReader reader, PrintWriter writer) {
        super(server, socket, reader, writer);
    }

    @Override
    public void run() {
        try {
            String message;

            //
            server.requestTell(new MessageInfo("System", null, "[" + getClientInfo().getUserName() + "] 님이 접속했습니다."));

            // 메시지 루프입니다.
            while ((message = reader.readLine()) != null) {
                try {
                    // Goodbye 메시지를 수신하면 연결을 종료합니다.
                    if (message.equals(EOF_MARKER)) {
                        return;
                    }

                    // 메시지를 분석합니다.
                    MessageInfo info = MessageInfo.interpret(message);
                    System.out.println("Server received client message: " + message);

                    // 메시지를 전송합니다.
                    if (info != null) {
                        sendMessage(info);
                    }
                }
                catch (HDException ex) {
                    ex.printStackTrace();
                }
            }
        }
        catch (SocketException ex) {
            ex.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            // 클라이언트와의 연결이 끊어졌음을 기록하고 다른 클라이언트에게 알립니다.
            ClientInfo clientInfo = getClientInfo();
            System.out.println("WaitingRoomJob: Client " + clientInfo + " closed its connection.");
            String message = String.format("[%s] 님이 접속을 종료했습니다.", clientInfo.getUserName());
            MessageInfo messageInfo = new MessageInfo("System", null, message);
            sendMessage(messageInfo);
            server.requestLogoutClient(clientInfo);
        }
    }

    //endregion



    //region 메서드를 정의합니다.
    /**
     * 주어진 정보를 바탕으로 메시지를 클라이언트에 전달합니다.
     * @param info 메시지에 대한 정보입니다.
     */
    private void sendMessage(MessageInfo info) {
        server.requestTell(info);
    }

    //endregion
}
