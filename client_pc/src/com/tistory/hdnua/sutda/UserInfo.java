package com.tistory.hdnua.sutda;

import java.io.Serializable;

/**
 * 사용자 정보입니다.
 */
public class UserInfo implements Serializable {
    //region 필드를 정의합니다.
    String userName;

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    //endregion


    //region 생성자를 정의합니다.
    public UserInfo(String userName) {
        this.userName = userName;
    }

    //endregion
}
