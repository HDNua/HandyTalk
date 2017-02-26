package com.tistory.hdnua.sutda;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;

import static com.tistory.hdnua.sutda.Handy.SERVER_PORT;
import static com.tistory.hdnua.sutda.Handy.USER_INFO_TXT;
import static com.tistory.hdnua.sutda.Handy.getServerIP;
import static com.tistory.hdnua.sutda.Handy.setServerIP;

/**
 * 회원가입 폼입니다.
 */
public class RegisterAndroidForm extends HDAndroidForm {
    //region 위젯을 정의합니다.
    private EditText _EditText_UserID;
    private EditText _EditText_Password;
    private EditText _EditText_PassConfirm;
    private Button _Button_Register;
    private Button _Button_ClearAllData;

    private EditText _EditText_ServerIP;

    //endregion



    //region 필드를 정의합니다.
    private RegisterManager manager;

    //endregion



    //region 생성자를 정의합니다.
    @Override
    protected void initContentView() { setContentView(R.layout.activity_register); }
    @Override
    protected void initWidgets() {
        _EditText_UserID = getItem(R.id._EditText_UserID);
        _EditText_Password = getItem(R.id._EditText_Password);
        _EditText_PassConfirm = getItem(R.id._EditText_PassConfirm);

        _Button_Register = getItem(R.id._Button_Register);
        _Button_ClearAllData = getItem(R.id._Button_ClearAllData);

        _EditText_ServerIP = getItem(R.id._EditText_ServerIP);
    }
    @Override
    protected void initWidgetActions() {
        _Button_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    register();
                    showToast("등록 성공!", Toast.LENGTH_SHORT);
                }
                catch (Exception ex) {
                    showToast(ex.getMessage(), Toast.LENGTH_SHORT);
                    ex.printStackTrace();
                }
            }
        });
        _Button_ClearAllData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    deleteFile(USER_INFO_TXT);
                    showToast("계정 초기화 성공!", Toast.LENGTH_SHORT);
                }
                catch (Exception ex) {
                    showToast(ex.getMessage(), Toast.LENGTH_SHORT);
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void initEndOfCreation(Bundle savedInstanceState) {
        manager = new RegisterManager();
    }

    //endregion



    //region 메서드를 정의합니다.
    /**
     * 서버에 사용자를 등록합니다.
     */
    private void register() throws Exception {
        String userID = _EditText_UserID.getText().toString().trim();
        String password = _EditText_Password.getText().toString();
        String passConfirm = _EditText_PassConfirm.getText().toString();
        String serverIP = _EditText_ServerIP.getText().toString();
        int port = SERVER_PORT;

        // 잘못된 입력을 발견하면 예외를 발생합니다.
        if (password.equals(passConfirm) == false) {
            throw new TextFormatException("패스워드 불일치");
        }
        else if (isValidPassword(password) == false) {
            throw new TextFormatException("유효하지 않은 패스워드");
        }
        else if (isValidUserID(userID) == false) {
            throw new TextFormatException("유효하지 않은 사용자 이름");
        }


        // 파일에 정보를 출력합니다.
        FileOutputStream fout = openFileOutput(USER_INFO_TXT, MODE_APPEND);
        setServerIP(serverIP);
        manager.register(userID, password, fout, getServerIP(), port);

        // 폼을 닫습니다.
        finish();
    }

    /**
     * 사용자 이름이 타당한지 확인합니다.
     */
    private boolean isValidUserID(String userID) {
        if (userID.isEmpty())
            return false;
        else if (userID.contains(":"))
            return false;
        return true;
    }
    /**
     * 패스워드가 타당한지 확인합니다.
     */
    private boolean isValidPassword(String password) {
        return password.isEmpty() == false;
    }

    //endregion
}
