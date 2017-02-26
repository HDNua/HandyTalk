package com.tistory.hdnua.sutda;

import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import static com.tistory.hdnua.sutda.Handy.SERVER_PORT;
import static com.tistory.hdnua.sutda.Handy.USER_INFO_KEY;
import static com.tistory.hdnua.sutda.Handy.USER_INFO_TXT;
import static com.tistory.hdnua.sutda.Handy.getServerIP;

/**
 * 로그인 폼입니다.
 */
public class LoginAndroidForm extends HDAndroidForm {
    //region 위젯을 정의합니다.
    private EditText _EditText_ID;
    private EditText _EditText_Password;
    private Button _Button_Login;
    private Button _Button_Register;

    private EditText _EditText_ServerIP;

    //endregion



    //region 필드를 정의합니다.
    private LoginManager manager;

    //endregion



    //region 생성자를 정의합니다.
    @Override
    protected void initContentView() { setContentView(R.layout.activity_login); }
    @Override
    protected void initWidgets() {
        _EditText_ID = getItem(R.id._EditText_UserID);
        _EditText_Password = getItem(R.id._EditText_Password);

        _Button_Login = getItem(R.id._Button_Login);
        _Button_Register = getItem(R.id._Button_Register);

        _EditText_ServerIP = getItem(R.id._EditText_ServerIP);
    }
    @Override
    protected void initWidgetActions() {
        _Button_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    login();
                    showToast("로그인 성공!", Toast.LENGTH_SHORT);
                }
                catch (FileNotFoundException ex) {
                    showToast("계정을 생성하세요.", Toast.LENGTH_SHORT);
                }
                catch (Exception ex) {
                    showToast(ex.getMessage(), Toast.LENGTH_SHORT);
                }
            }
        });
        _Button_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RegisterAndroidForm.class);
            }
        });
    }

    @Override
    protected void initEndOfCreation(Bundle savedInstanceState) {
        manager = new LoginManager();
    }

    //endregion



    //region 메서드를 정의합니다.
    /**
     * 서버에 로그인합니다.
     */
    private void login() throws Exception {
        // 입력의 타당성을 검증합니다.
        checkInputValidity();

        // 위젯 필드로부터 획득한 정보로 로그인합니다.
        String inputUserName = _EditText_ID.getText().toString();
        String serverIP = _EditText_ServerIP.getText().toString();
        int port = SERVER_PORT;

        Handy.setServerIP(serverIP);
        manager.login(inputUserName, getServerIP(), port);

        // Activity를 시작하고 자신을 닫습니다.
        startActivity(WaitingRoomAndroidForm.class, new Pair(USER_INFO_KEY, manager.getUserInfo()));
        finish();
    }
    /**
     *
     * @throws Exception
     */
    private void checkInputValidity() throws Exception {
        // 위젯 필드로부터 정보를 가져옵니다.
        String inputUserName = _EditText_ID.getText().toString();
        String inputPassword = _EditText_Password.getText().toString();

        // 로그인을 수행합니다.
        FileInputStream fin = openFileInput(USER_INFO_TXT);
        List<String> lines = Handy.getLinesFromFile(fin);
        boolean found = false;
        for (String line : lines) {
            String[] tokens = line.split(":");
            String dbUserName = tokens[0];
            String dbPassword = tokens[1];
            if (inputUserName.equals(dbUserName)) {
                if (inputPassword.equals(dbPassword)) {
                    found = true;
                }
                else {
                    throw new Exception("패스워드가 틀렸습니다.");
                }
                break;
            }
        }
        if (found == false)
            throw new Exception("계정을 찾을 수 없습니다.");
    }


    //endregion
}