package com.tistory.hdnua.sutda;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 메시지 박스입니다.
 */
public class MessageBox {

    /**
     * 메시지 박스입니다.
     * @param message 화면에 출력할 메시지입니다.
     */
    public MessageBox(String message) {

        // 프레임을 생성합니다.
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 라벨 위젯을 초기화하고 폼에 추가합니다.
        JLabel label = new JLabel(message);
        frame.getContentPane().add(BorderLayout.CENTER, label);

        // 버튼 위젯을 초기화하고 폼에 추가합니다.
        JButton button = new JButton("확인");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 창을 보이지 않게 만들고
                // dispose()를 호출하여 메모리에서 해제합니다.
                frame.setVisible(false);
                frame.dispose();
            }
        });
        frame.getContentPane().add(BorderLayout.SOUTH, button);

        // 프레임 초기화를 마칩니다.
        frame.setSize(300, 300);
        frame.setVisible(true);
    }

    /**
     * 화면에 메시지를 출력하는 메시지 박스를 띄웁니다.
     * @param message 화면에 출력할 메시지입니다.
     */
    public static void show(String message) {
        new MessageBox(message);
    }
}
