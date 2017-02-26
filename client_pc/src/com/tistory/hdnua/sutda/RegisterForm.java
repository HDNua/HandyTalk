package com.tistory.hdnua.sutda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import static com.tistory.hdnua.sutda.Handy.*;

/**
 * 회원가입 폼입니다.
 */
public class RegisterForm extends HDForm {
    //region 위젯을 정의합니다.
    private LoginForm parent;
    private JTextField _TextField_UserID;
    private JTextField _TextField_Password;
    private JTextField _TextField_PassConfirm;
    private JButton _Button_Register;
    private ActionListener _registerActionListener;
    private ActionListener _cancelActionListener;

    private JTextField _TextField_ServerIP;

    //endregion



    //region 필드를 정의합니다.
    private RegisterManager manager;

    //endregion



    //region 생성자를 정의합니다.
    /**
     * 한 도영의 GUI 폼입니다.
     * @param width 폼 너비입니다.
     * @param height 폼 높이입니다.
     * @param loginForm 부모 LoginForm 개체입니다.
     */
    public RegisterForm(int width, int height, LoginForm loginForm) {
        super(width, height);
        this.parent = loginForm;

        int margin = 20;
        setLocation(parent.getX() + margin, parent.getY() + margin);

        // 필드를 초기화합니다.
        manager = new RegisterManager();
    }

    @Override
    protected void initActionListeners() {

        _registerActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    String userIDText = _TextField_UserID.getText();
                    String passwordText = _TextField_Password.getText();
                    String passConfirmText = _TextField_PassConfirm.getText();

                    // 잘못된 입력을 발견하면 예외를 발생합니다.
                    if (passwordText.equals(passConfirmText) == false) {
                        throw new TextFormatException("패스워드 불일치");
                    }
                    else if (isValidPassword(passwordText) == false) {
                        throw new TextFormatException("유효하지 않은 패스워드");
                    }
                    else if (isValidUserID(userIDText) == false) {
                        throw new TextFormatException("유효하지 않은 사용자 이름");
                    }

                    // 파일에 정보를 출력합니다.
                    FileOutputStream fout = new FileOutputStream(USER_INFO_TXT);
                    manager.register(userIDText, passwordText, fout, Handy.getServerIP(), SERVER_PORT);

                    // 창을 닫고 로그인 폼을 엽니다.
                    onClose();
                }
                catch (Exception ex) {
                    MessageBox.show(ex.getMessage());
                }
            }
        };
        _cancelActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finish();
            }
        };

        // 프레임이 종료되었습니다.
        frame.addWindowListener(new WindowListener() {
            public void windowClosing(WindowEvent e) {
                try {
                    onClose();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            public void windowClosed(WindowEvent e) { }

            public void windowOpened(WindowEvent e) { }
            public void windowIconified(WindowEvent e) { }
            public void windowDeiconified(WindowEvent e) { }
            public void windowActivated(WindowEvent e) { }
            public void windowDeactivated(WindowEvent e) { }
        });
    }

    @Override
    protected void initWidgets() {
        GridLayout gridLayout = new GridLayout(5, 2);
        centerPanel.setLayout(gridLayout);

        JLabel labelServerIP = new JLabel("서버 IP");
        centerPanel.add(labelServerIP);
        _TextField_ServerIP = new JTextField(15);
        _TextField_ServerIP.setText(getServerIP());
        centerPanel.add(_TextField_ServerIP);

        JLabel labelUserID = new JLabel("아이디");
        centerPanel.add(labelUserID);
        _TextField_UserID = new JTextField(15);
        centerPanel.add(_TextField_UserID);

        JLabel labelPassword = new JLabel("비밀번호");
        centerPanel.add(labelPassword);
        _TextField_Password = new JTextField(15);
        centerPanel.add(_TextField_Password);

        JLabel labelPassConfirm = new JLabel("확인");
        centerPanel.add(labelPassConfirm);
        _TextField_PassConfirm = new JTextField(15);
        centerPanel.add(_TextField_PassConfirm);

        _Button_Register = new JButton("등록");
        _Button_Register.addActionListener(_registerActionListener);
        centerPanel.add(_Button_Register);

        JButton cancelButton = new JButton("취소");
        cancelButton.addActionListener(_cancelActionListener);
        centerPanel.add(cancelButton);
    }

    //endregion


    //region 메서드를 정의합니다.
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

    /**
     * 창을 닫을 때 발생합니다.
     */
    private void onClose() {
        LoginForm loginForm = new LoginForm(
                parent.getWidth(),
                parent.getHeight(),
                parent.getClientWidth(),
                parent.getClientHeight());
        loginForm.setIPAddressText(_TextField_ServerIP.getText());

        String userIDText = _TextField_UserID.getText();
        if (isValidUserID(userIDText)) {
            loginForm.setUserNameText(userIDText);
        }
        loginForm.show();
        finish();
    }

    //endregion
}
