package com.tistory.hdnua.sutda;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.tistory.hdnua.sutda.Handy.REQUEST_LOGIN;
import static com.tistory.hdnua.sutda.Handy.RESPONSE_LOGIN_ALREADY_LOGIN_USERID;
import static com.tistory.hdnua.sutda.Handy.RESPONSE_LOGIN_GOOD;
import static com.tistory.hdnua.sutda.Handy.RESPONSE_LOGIN_UNREGISTERED_USERID;

/**
 * 로그인 관리자입니다.
 */
public class LoginManager {
    //region 필드를 정의합니다.
    private String errorMessage;
    private LoginJob loginJob;

    public String getErrorMessage() { return errorMessage; }
    public UserInfo getUserInfo() { return loginJob.getUserInfo(); }

    //endregion



    //region 생성자를 정의합니다.
    /**
     * 로그인합니다.
     * @param userName      사용자 이름입니다.
     * @throws Exception    일반 예외를 발생합니다.
     */
    public void login(String userName, String ip, int port) throws Exception {
        // 서버 소켓을 획득하고 연결을 초기화합니다.
        errorMessage = null;
        loginJob = new LoginJob(userName, ip, port);
        Thread thread = new Thread(loginJob);
        thread.start();
        thread.join();
    }

    //endregion



    //region Job을 정의합니다.
    /**
     * 로그인 Job입니다.
     */
    class LoginJob extends NetworkJob {
        //region 필드를 정의합니다.
        UserInfo userInfo;
        public UserInfo getUserInfo() { return userInfo; }

        //endregion


        //region 생성자를 정의합니다.
        public LoginJob(String userName, String ip, int port) throws IOException {
            super(ip, port);
            this.userInfo = new UserInfo(userName);
        }
        @Override
        public void tryRoutine() throws IOException, TextFormatException {
            // 서버에 메시지를 보내고 응답을 대기합니다.
            send(REQUEST_LOGIN);
            send(userInfo.userName);

            // 서버의 응답에 따라 분기합니다.
            String line = recv();
            switch (line) {
                case RESPONSE_LOGIN_GOOD: // 사용할 수 있는 사용자 ID입니다.
                    break;

                case RESPONSE_LOGIN_ALREADY_LOGIN_USERID: // 이미 있는 사용자 이름입니다.
                    throw new TextFormatException("이미 접속한 사용자입니다.");

                case RESPONSE_LOGIN_UNREGISTERED_USERID:
                    throw new TextFormatException("등록되지 않은 사용자입니다.");

                default: // 알 수 없는 오류입니다.
                    throw new TextFormatException("알 수 없는 오류");
            }

            preventSocketClose();
        }
        @Override
        protected void catchRoutine(Exception ex) {
            errorMessage = ex.getMessage();
        }

        //endregion
    }

    //endregion
}
