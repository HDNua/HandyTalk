package Deprecated;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Deprecated
/**
 * 클라이언트 정보를 보관합니다.
 */
public class ClientInfo_dep {
    //region 필드를 정의합니다.
    private Socket socket;
    private int clientID;
    private String name;

    private BufferedReader reader;
    private PrintWriter writer;

    public int getClientID() { return clientID; }
    public String getName() { return name; }

    public BufferedReader getReader() { return reader; }

    public String toString() {
        return String.format("%s (%d)", name, clientID);
    }

    //endregion


    //region 생성자를 정의합니다.
    /**
     * 클라이언트 정보입니다.
     * @param clientID 클라이언트의 ID입니다.
     * @param clientName 클라이언트의 이름입니다.
     * @param socket 클라이언트의 소켓입니다.
     */
    public ClientInfo_dep(int clientID, String clientName, Socket socket) throws IOException {
        this.clientID = clientID;
        this.name = clientName;
        this.socket = socket;

        InputStreamReader isReader = new InputStreamReader(socket.getInputStream());
        this.reader = new BufferedReader(isReader);
        this.writer = new PrintWriter(socket.getOutputStream());
    }

    //endregion


    //region 메서드를 정의합니다.
    /**
     * 메시지를 전송합니다.
     */
    public void write(String message) {
        writer.println(message);
        writer.flush();
    }

    //endregion
}
