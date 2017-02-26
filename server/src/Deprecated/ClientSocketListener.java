package Deprecated;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;

import com.tistory.hdnua.sutda.HDException;
import com.tistory.hdnua.sutda.MessageInfo;
import com.tistory.hdnua.sutda.Server;


@Deprecated
/**
 * 클라이언트 관리자입니다.
 */
public class ClientSocketListener implements Runnable {
    //region 필드를 정의합니다.
    private Server _server;

    private ClientInfo_dep _clientInfo;
    public ClientInfo_dep getClientInfo() { return _clientInfo; }
    public String getName() { return _clientInfo.getName(); }

    //endregion


    //region 생성자를 정의합니다.
    /**
     * 클라이언트 관리자입니다.
     * @param server 핸디톡 서버입니다.
     * @param clientSocket 클라이언트 소켓입니다.
     */
    public ClientSocketListener(Server server, int clientID, Socket clientSocket)
        throws IOException, HDException {
        _server = server;
        BufferedReader reader = new BufferedReader
                (new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());


        String message = recv(reader);
        switch (message) {
            //
            case "Register":
                send(writer, "OK");
                break;

            //
            case "Login":

                break;

            //
            case "WaitingRoom":

                break;

            //
            case "Game":

                break;

            //
            default:
                throw new HDException("", null);
        }







        String clientName;
        while ((clientName = reader.readLine()) != null) {
            break;
        }

        // 획득한 클라이언트의 이름이 타당한지 검사합니다.
        switch (checkClientName(clientName)) {
            case 0:
                writer.println("0");
                writer.flush();
                break;

            case 1:
                writer.println("1");
                writer.flush();
                throw new HDException("Duplicated client name", clientName);

            case 2:
                writer.println("2");
                writer.flush();
                throw new HDException("Empty client name", clientName);

            default:
                writer.println("-1");
                writer.flush();
                throw new HDException("Unknown exception for client name", clientName);
        }

        // 필드를 업데이트합니다.
        _clientInfo = new ClientInfo_dep(clientID, clientName, clientSocket);
    }


    //endregion


    //region Runnable 인터페이스를 구현합니다.
    /**
     * 스레드 진입점입니다.
     */
    public void run() {
        try {
            String recvMessage;
            BufferedReader reader = _clientInfo.getReader();

            // 메시지 루프입니다.
            while ((recvMessage = reader.readLine()) != null) {
                try {
                    // 클라이언트로부터 메시지를 수신한 경우
                    // 메시지를 터미널에 출력합니다.
                    System.out.println("read " + recvMessage);

                    // Goodbye 메시지를 수신하면 연결을 종료합니다.
                    if (recvMessage.equals("goodbye")) {
                        return;
                    }

                    // 메시지를 분석합니다.
                    MessageInfo info = MessageInfo.interpret(recvMessage);

                    // 메시지를 전송합니다.
                    sendMessage(info);
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
            System.out.println("Client " + _clientInfo + " closed its connection.");
            String message = String.format("[%s] 님이 접속을 종료했습니다.", _clientInfo.getName());
            MessageInfo messageInfo = new MessageInfo("System", null, message);
            sendMessage(messageInfo);
            // _server.requestRemoveClient_dep(_clientInfo.getClientID());
        }
    }

    //endregion



    //region 메서드를 정의합니다.
    /**
     * 클라이언트 이름이 사용 가능한지 확인합니다.
     * @param clientName 검사할 클라이언트의 이름입니다.
     * @return  0: 사용할 수 있음
     *          1: 리스트에 이미 같은 이름의 클라이언트가 존재함
     *          2: 빈 문자열
     *         -1: 일반적으로 불가능한 것으로 간주되는 문자열
     */
    private int checkClientName(String clientName) {
        if (clientName.isEmpty())
            return 2;
        else if (_server.doesClientNameExist(clientName))
            return 1;
        return 0;
    }

    /**
     * 주어진 정보를 바탕으로 메시지를 클라이언트에 전달합니다.
     * @param info 메시지에 대한 정보입니다.
     */
    private void sendMessage(MessageInfo info) {
        // _server.tell(info);
    }

    /**
     *
     * @param writer
     * @param message
     */
    private void send(PrintWriter writer, String message) {
        writer.println(message);
        writer.flush();
    }
    /**
     * 클라이언트로부터 문자열을 획득합니다.
     * @param reader 클라이언트 메시지 수신 스트림입니다.
     * @throws IOException readLine() 메서드가 실패하면 예외를 던집니다.
     */
    private String recv(BufferedReader reader) throws IOException {
        String message;
        while ((message = reader.readLine()) != null)
            break;
        return message;
    }


    //endregion
}
