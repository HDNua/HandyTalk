package com.tistory.hdnua.sutda;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 서버 정보를 보관합니다.
 */

public class Handy {
    //region 상수를 정의합니다.
    /// public static final String SERVER_IP = "172.30.1.34";
    public static final int SERVER_PORT = 5000;

    private static String serverIP = "127.0.0.1";
    public static String getServerIP() {
        return serverIP;
    }
    public static void setServerIP(String serverIP) {
        Handy.serverIP = serverIP;
    }

    //endregion


    //region 서버 명령을 정의합니다.
    public static final String REQUEST_REGISTER = "Register";
    public static final String REQUEST_LOGIN = "Login";
    public static final String REQUEST_WAITING = "Waiting";
    public static final String REQUEST_GAME = "Game";

    //endregion


    //region 데이터 상수를 정의합니다.
    public static final String USER_INFO_KEY = "userInfo";
    public static final String USER_INFO_TXT = "users.txt";


    //endregion


    //region 회원 가입 명령을 정의합니다.
    public static final String RESPONSE_REGISTER_GOOD = "0";
    public static final String RESPONSE_REGISTER_DUPLICATED_USERID = "1";
    public static final String RESPONSE_REGISTER_INVALID_USERID = "2";

    //endregion


    //region 로그인 명령을 정의합니다.
    public static final String RESPONSE_LOGIN_GOOD = "0";
    public static final String RESPONSE_LOGIN_ALREADY_LOGIN_USERID = "1";
    public static final String RESPONSE_LOGIN_UNREGISTERED_USERID = "2";
    public static final String RESPONSE_LOGIN_INVALID_USERID = "3";

    //endregion


    //region 클라이언트 응답을 정의합니다.
    public static final String REQUEST_DISCONNECTION = "goodbye";

    //endregion



    //region 메서드를 정의합니다.
    /**
     * 파일로부터 문자열 줄 리스트를 획득합니다.
     * @param fin 줄 리스트를 획득할 파일 입력 스트림입니다.
     * @return 줄 리스트를 반환합니다.
     * @throws IOException 입출력 예외입니다.
     */
    public static List<String> getLinesFromFile(FileInputStream fin) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(fin);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        ArrayList<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();

        return lines;
    }

    //endregion
}
