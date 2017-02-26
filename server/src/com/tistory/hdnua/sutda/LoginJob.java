package com.tistory.hdnua.sutda;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import static com.tistory.hdnua.sutda.Handy.RESPONSE_LOGIN_ALREADY_LOGON_USERID;
import static com.tistory.hdnua.sutda.Handy.RESPONSE_LOGIN_GOOD;
import static com.tistory.hdnua.sutda.Handy.RESPONSE_LOGIN_UNREGISTERD_USERID;

/**
 * 로그인 Job입니다.
 */
public class LoginJob extends Job {
    //region 필드를 정의합니다.
    private String errorMessage;

    public String getErrorMessage() { return errorMessage; }

    //endregion



    //region 생성자를 정의합니다.
    public LoginJob(Server server, Socket socket, BufferedReader reader, PrintWriter writer) {
        super(server, socket, reader, writer);
    }

    @Override
    public void run() {
        try {
            errorMessage = null;

            // 클라이언트로부터 사용자 ID를 획득합니다.
            String userID = recv();
            if (server.doesClientNameExist(userID) == false) {
                send(RESPONSE_LOGIN_UNREGISTERD_USERID);
                throw new Exception("등록되지 않은 사용자 이름입니다.");
            }
            else if (server.doesClientLogOn(userID)) {
                send(RESPONSE_LOGIN_ALREADY_LOGON_USERID);
                throw new Exception("이미 접속한 사용자입니다.");
            }

            // 타당한 사용자 아이디임을 확인했습니다.
            send(RESPONSE_LOGIN_GOOD);

            // 클라이언트를 데이터베이스에 등록합니다.
            // ClientInfo client = new ClientInfo(userID);
            ClientInfo client = new ClientInfo(userID, socket, reader, writer);
            server.requestLoginClient(client);

            setClientInfo(client);
        }
        catch (Exception ex) {
            errorMessage = ex.getLocalizedMessage();
        }
        finally {
            // 클라이언트와의 연결이 끊어졌음을 기록하고 다른 클라이언트에게 알립니다.
            System.out.println("LoginJob: Client " + this + " closed its connection.");
        }
    }

    //endregion





    //region 메서드를 정의합니다.
    /**
     * 사용자 이름이 타당한지 확인합니다.
     */
    private boolean isUserIdValid(String userID) {
        if (userID.isEmpty())
            return false;
        return true;
    }

    //endregion
}
