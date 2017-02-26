package com.tistory.hdnua.sutda;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import static com.tistory.hdnua.sutda.Handy.*;

/**
 * 회원가입 관리자입니다.
 */
public class RegisterManager {
    //region 필드를 정의합니다.
    private String errorMessage;
    private RegisterJob registerJob;

    public String getErrorMessage() { return errorMessage; }

    //endregion



    //region 메서드를 정의합니다.
    /**
     * 회원을 등록합니다.
     * @param userID      사용자 이름입니다.
     * @param password    사용자 패스워드입니다.
     * @param fout        사용자 정보를 저장할 파일 출력 스트림입니다.
     */
    public void register(String userID, String password, FileOutputStream fout, String ip, int port) throws Exception {
        // 서버 소켓을 획득하고 연결을 초기화합니다.
        errorMessage = null;
        registerJob = new RegisterJob(userID, ip, port);
        try {
            Thread thread = new Thread(registerJob);
            thread.start();
            thread.join();
            if (errorMessage != null)
                throw new Exception(errorMessage);
        }
        finally {
            registerJob.closeSocket();
        }

        // 파일에 정보를 출력합니다.
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(fout));
        String userInfoText = String.format("%s:%s", userID, password);
        writer.println(userInfoText);
        writer.close();

    }

    //endregion



    //region Job을 정의합니다.
    /**
     * 등록 Job입니다.
     */
    class RegisterJob extends NetworkJob {
        String userName;


        public RegisterJob(String userName, String ip, int port) throws IOException {
            super(ip, port);
            this.userName = userName;
        }

        @Override
        public void tryRoutine() throws IOException, TextFormatException {
            // 서버에 메시지를 보내고 응답을 대기합니다.
            send(REQUEST_REGISTER);
            send(userName);

            // 서버의 응답에 따라 분기합니다.
            String line = recv();
            switch (line) {
                case RESPONSE_REGISTER_GOOD: // 사용할 수 있는 사용자 ID입니다.
                    break;

                case RESPONSE_REGISTER_DUPLICATED_USERID: // 이미 있는 사용자 이름입니다.
                    throw new TextFormatException("이미 있는 사용자 이름");

                case RESPONSE_REGISTER_INVALID_USERID: // 잘못된 사용자 이름입니다.
                    throw new TextFormatException("잘못된 사용자 이름");

                default: // 알 수 없는 오류입니다.
                    throw new TextFormatException("알 수 없는 오류");
            }
        }
        @Override
        protected void catchRoutine(Exception ex) {
            errorMessage = ex.getMessage();
        }
    }

    //endregion
}
