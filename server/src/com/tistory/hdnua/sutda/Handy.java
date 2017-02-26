package com.tistory.hdnua.sutda;

/**
 * 한 도영의 정의 집합입니다.
 */
public class Handy {
    //region 서버 데이터 상수를 정의합니다.
    public static final String CLIENT_DATABASE_FILE = "users.txt";

    //endregion



    //region 서버 명령 상수를 정의합니다.
    public static final String COMMAND_REGISTER = "Register";
    public static final String COMMAND_LOGIN = "Login";
    public static final String COMMAND_WAITING = "Waiting";
    public static final String COMMAND_GAME = "Game";

    //endregion



    //region 회원 가입 명령 상수를 정의합니다.
    public static final String RESPONSE_REGISTER_GOOD = "0";
    public static final String RESPONSE_REGISTER_DUPLICATED_USERID = "1";
    public static final String RESPONSE_REGISTER_INVALID_USERID = "2";

    //endregion



    //region 로그인 명령 상수를 정의합니다.
    public static final String RESPONSE_LOGIN_GOOD = "0";
    public static final String RESPONSE_LOGIN_ALREADY_LOGON_USERID = "1";
    public static final String RESPONSE_LOGIN_UNREGISTERD_USERID = "2";
    public static final String RESPONSE_LOGIN_INVALID_USERID = "3";

    //endregion
}
