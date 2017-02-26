package com.tistory.hdnua.sutda;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Arrays;

/**
 * 로그인 폼입니다.
 */
public class LoginForm extends HDForm {
    //region 위젯을 정의합니다.
    private JTextField _TextField_ServerIP;
    private JTextField _TextField_PortNumber;
    private JTextField _TextField_UserID;
    private JPasswordField _TextField_Password;

    ActionListener _loginActionListener;
    ActionListener _registerActionListener;

    //endregion


    //region 필드를 정의합니다.
    private int _clientX;
    private int _clientY;
    private int _clientWidth;
    private int _clientHeight;

    /**
     * 클라이언트 폼의 위치를 결정합니다.
     */
    public void setClientFormLocation(int x, int y) {
        _clientX = x;
        _clientY = y;
    }
    /**
     * 클라이언트 폼의 크기를 설정합니다.
     */
    public void setClientFormSize(int width, int height) {
        _clientWidth = width;
        _clientHeight = height;
    }

    public int getClientWidth() { return _clientWidth; }
    public int getClientHeight() { return _clientHeight; }

    LoginManager manager;

    //endregion


    //region 생성자를 정의합니다.
    /**
     * 로그인 폼입니다.
     * @param width 로그인 폼 너비입니다.
     * @param height 로그인 폼 높이입니다.
     * @param clientFormWidth 클라이언트 폼 너비입니다.
     * @param clientFormHeight 클라이언트 폼 높이입니다.
     */
    public LoginForm(int width, int height, int clientFormWidth, int clientFormHeight) {
        super(width, height);
        this._clientWidth = clientFormWidth;
        this._clientHeight = clientFormHeight;
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //
        manager = new LoginManager();

        // 초깃값을 설정합니다.
        _TextField_ServerIP.setText(Handy.getServerIP());
        _TextField_PortNumber.setText(Handy.SERVER_PORT + "");
        _TextField_UserID.setText("PCUser");
        _TextField_Password.setText("0");

        // 서버 IP 텍스트 필드로 포커스를 옮깁니다.
        _TextField_ServerIP.requestFocus();
    }

    @Override
    protected void initActionListeners() {
        _loginActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // 텍스트 필드로부터 값을 획득합니다.
                    String serverIp = _TextField_ServerIP.getText();
                    String portNumberText = _TextField_PortNumber.getText();
                    String userName = _TextField_UserID.getText();
                    char[] password = _TextField_Password.getPassword();
                    int portNumber = Integer.parseInt(portNumberText);

                    // 획득한 정보로 로그인 창을 띄웁니다.
                    login(serverIp, portNumber, userName, password);
                }
                catch (NumberFormatException ex) {
                    MessageBox.show(ex.getLocalizedMessage());
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

        _registerActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterForm form = new RegisterForm(300, 300, LoginForm.this);
                form.show();
                frame.dispose();
            }
        };
    }
    @Override
    protected void initWidgets() {
        _TextField_ServerIP = new JTextField(15);
        centerPanel.add(_TextField_ServerIP);

        _TextField_PortNumber = new JTextField(5);
        centerPanel.add(_TextField_PortNumber);

        _TextField_UserID = new JTextField(20);
        centerPanel.add(_TextField_UserID);

        _TextField_Password = new JPasswordField(20);
        centerPanel.add(_TextField_Password);

        JButton buttonLogin = new JButton("로그인");
        buttonLogin.addActionListener(_loginActionListener);
        centerPanel.add(buttonLogin);

        JButton buttonRegister = new JButton("등록");
        buttonRegister.addActionListener(_registerActionListener);
        centerPanel.add(buttonRegister);
    }

    //endregion


    //region 메서드를 정의합니다.
    /**
     * 로그인합니다.
     * @param serverIp 서버의 IP 주소입니다.
     * @param portNumber 포트 번호입니다.
     * @param userName 사용자 이름입니다.
     * @param password 패스워드입니다.
     */
    private void login(String serverIp, int portNumber, String userName, char[] password) {
        try {
            FileInputStream fin = new FileInputStream("users.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fin);
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(":");
                String userNameInFile = tokens[0];
                String passwordInFile = tokens[1];

                if (userName.equals(userNameInFile)) {
                    if (isPasswordCorrect(password, passwordInFile)) {
                        found = true;
                    }
                    else {
                        throw new Exception("패스워드가 일치하지 않습니다.");
                    }
                    break;
                }
            }
            reader.close();
            if (found == false)
                throw new Exception("계정을 찾을 수 없습니다.");

            // 로그인을 시도합니다.
            Handy.setServerIP(serverIp);
            manager.login(userName, serverIp, portNumber);

            // 로그인에 성공하면 사용자 정보를 생성하고 대기실 폼으로 전환합니다.
            UserInfo userInfo = new UserInfo(userName);
            WaitingRoomForm form = new WaitingRoomForm(_clientWidth, _clientHeight, userInfo);
            form.show();

            // 로그인 폼을 닫습니다.
            finish();
        }
        catch (Exception ex) {
            // 상태 표시줄에 에러 메시지를 간략히 표시합니다.
            statusLabel.setText(ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * 사용자 이름 항목의 텍스트를 설정합니다.
     */
    public void setUserNameText(String text) {
        _TextField_UserID.setText(text);
    }
    /**
     * IP 항목의 텍스트를 설정합니다.
     */
    public void setIPAddressText(String IPAddressText) {
        _TextField_ServerIP.setText(IPAddressText);
    }

    /**
     * 패스워드가 일치하는지 확인합니다.
     * @param input 입력 문자열입니다.
     * @param password 비교할 패스워드 문자열입니다.
     * @return 패스워드가 일치하면 참을 반환합니다.
     */
    private static boolean isPasswordCorrect(char[] input, String password) {
        boolean isCorrect = true;
        char[] correctPassword = password.toCharArray();

        if (input.length != correctPassword.length) {
            isCorrect = false;
        }
        else {
            isCorrect = Arrays.equals (input, correctPassword);
        }

        // Zero out the password.
        Arrays.fill(correctPassword,'0');

        // Oracle의 문서 참조.
        return isCorrect;
    }

    //endregion
}
