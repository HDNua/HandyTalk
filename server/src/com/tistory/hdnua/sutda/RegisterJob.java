package com.tistory.hdnua.sutda;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import static com.tistory.hdnua.sutda.Handy.RESPONSE_REGISTER_DUPLICATED_USERID;
import static com.tistory.hdnua.sutda.Handy.RESPONSE_REGISTER_INVALID_USERID;
import static com.tistory.hdnua.sutda.Handy.RESPONSE_REGISTER_GOOD;

/**
 * 회원가입 Job입니다.
 */
public class RegisterJob extends Job {
    //region 필드를 정의합니다.
    private String errorMessage;

    public String getErrorMessage() { return errorMessage; }

    //endregion



    //region 생성자를 정의합니다.
    public RegisterJob(Server server, Socket socket, BufferedReader reader, PrintWriter writer) {
        super(server, socket, reader, writer);
    }

    @Override
    public void run() {
        try {
            errorMessage = null;

            // 클라이언트로부터 사용자 ID를 획득합니다.
            String userID = recv();
            if (server.doesClientNameExist(userID)) {
                send(RESPONSE_REGISTER_DUPLICATED_USERID);
                throw new Exception("이미 있는 사용자 이름");
            }
            else if (isUserIdValid(userID) == false) {
                send(RESPONSE_REGISTER_INVALID_USERID);
                throw new Exception("잘못된 사용자 이름");
            }

            // 타당한 사용자 아이디임을 확인했습니다.
            send(RESPONSE_REGISTER_GOOD);

            // 클라이언트를 데이터베이스에 등록합니다.
            ClientInfo client = new ClientInfo(userID, socket, reader, writer);
            server.requestRegisterClient(client);
            setClientInfo(client);
        }
        catch (Exception ex) {
            errorMessage = ex.getLocalizedMessage();
        }
        finally {
            // 클라이언트와의 연결이 끊어졌음을 기록하고 다른 클라이언트에게 알립니다.
            System.out.println("RegisterJob: Client " + this + " closed its connection.");
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
