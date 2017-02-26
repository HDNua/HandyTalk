package com.tistory.hdnua.sutda;

import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

import static com.tistory.hdnua.sutda.Handy.*;

/**
 * 채팅 클라이언트 폼입니다.
 */
public class WaitingRoomForm extends HDForm {
    //region 위젯을 정의합니다.
    private JTextArea _chatBoard;

    private JTextField _userInput;
    private Runnable _userInputReaderJob;
    private KeyListener _userInputKeyListener;

    private JButton _sendButton;
    private ActionListener _sendButtonActionListener;

    //endregion


    //region 필드를 정의합니다.
    private UserInfo userInfo;
    WaitingRoomManager manager;

    //endregion


    //region 생성자를 정의합니다.
    /**
     * 채팅 클라이언트 폼입니다.
     */
    public WaitingRoomForm(int width, int height, UserInfo userInfo) {
        super(width, height);
        this.userInfo = userInfo;

        // 기본 필드 정보를 초기화합니다.
        frame.setTitle("채팅 프로그램 클라이언트 for user: " + userInfo.getUserName());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        try {
            manager = new WaitingRoomManager(userInfo) {
                @Override
                protected void blockInputWidgets() {
                    _sendButton.setEnabled(false);
                    _userInput.setEnabled(false);
                }

                @Override
                protected void write(String message) {
                    _chatBoard.append(message + '\n');
                }

                @Override
                protected void finish() {
                    WaitingRoomForm.this.finish();
                }
            };
            manager.start();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

        // 사용자 입력으로 포커스를 맞춥니다.
        _userInput.requestFocus();
    }

    @Override
    protected void initActionListeners() {
        // "Send" 버튼을 클릭했습니다.
        _sendButtonActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String userText = _userInput.getText();
                    if (userText.equals(""))
                        return;

                    String message;
                    message = String.format("%s:%s", userInfo.getUserName(), userText);

                    manager.send(message);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                _userInput.setText("");
                _userInput.requestFocus();
            }
        };

        /**
        // 스레드가 메시지를 수신했습니다.
        _userInputReaderJob = new Runnable() {
            @Override
            public void run() {
                try {
                    String message;
                    while ((message = manager.recv()) != null) {
                        write(message);
                    }
                }
                catch (SocketException ex) {
                    write("Server is out of control; program has been terminated.");
                    manager.blockInputWidgets();
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        */

        // '_userInput' TextField에서 키가 눌렸습니다.
        _userInputKeyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    _sendButtonActionListener.actionPerformed(null);
                    ;
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    _sendButtonActionListener.actionPerformed(null);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) { }
        };

        // 프레임이 종료되었습니다.
        frame.addWindowListener(new WindowListener() {
            public void windowClosing(WindowEvent e) {
                try {
                    write(REQUEST_DISCONNECTION);
                    manager.closeSocket();
                    // _socket.close();
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
        //
        _chatBoard = new JTextArea(10, 30);
        _chatBoard.setLineWrap(true);
        _chatBoard.setWrapStyleWord(true);
        _chatBoard.setEditable(false);

        // 세로 방향 스크롤러입니다.
        JScrollPane qScroller = new JScrollPane(_chatBoard);
        qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        centerPanel.add(qScroller);

        // 사용자 입력 텍스트필드입니다.
        _userInput = new JTextField(20);
        _userInput.addKeyListener(_userInputKeyListener);
        centerPanel.add(_userInput);

        // Send 버튼입니다.
        _sendButton = new JButton("Send");
        _sendButton.addActionListener(_sendButtonActionListener);
        centerPanel.add(_sendButton);
    }

    //endregion


    //region 메서드를 정의합니다.
    /**
     * 메시지를 화면에 출력합니다.
     */
    private void write(String message) {
        _chatBoard.append(message + "\n");
    }

    //endregion


    //region 메서드를 오버라이드합니다.
    @Override
    public void show() {
        super.show();

        // 사용자 입력 필드로 포커스를 맞춥니다.
        _userInput.requestFocus();
    }

    //endregion
}
