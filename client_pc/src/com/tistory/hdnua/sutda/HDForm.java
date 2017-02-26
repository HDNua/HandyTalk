package com.tistory.hdnua.sutda;

import javax.swing.*;
import java.awt.*;

/**
 * 한 도영의 GUI 폼입니다.
 */
public abstract class HDForm {
    //region 레이아웃 필드입니다.
    protected JFrame frame;
    protected JPanel northPanel;
    protected JPanel southPanel;
    protected JPanel centerPanel;

    //endregion


    //region 위젯 필드입니다.
    protected JLabel statusLabel;

    //endregion


    //region 생성자를 정의합니다.
    /**
     * 한 도영의 GUI 폼입니다.
     * @param width 폼 너비입니다.
     * @param height 폼 높이입니다.
     */
    public HDForm(int width, int height) {
        initFrame(width, height);
        initNorthPanel();
        initSouthPanel();
        initCenterPanel();

        // 액션 리스너를 초기화합니다.
        initActionListeners();

        // 위젯을 초기화합니다.
        initWidgets();
    }

    /**
     * 액션 리스너를 초기화합니다.
     */
    protected abstract void initActionListeners();
    /**
     * 위젯을 초기화합니다.
     */
    protected abstract void initWidgets();



    /**
     * 프레임을 초기화합니다.
     */
    private void initFrame(int width, int height) {
        // 프레임을 생성하고 기본값을 정의합니다.
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);
    }
    /**
     * 북쪽 패널을 초기화합니다.
     */
    private void initNorthPanel() {
        // 패널을 정의하고 초기화한 후 프레임에 넣습니다.
        northPanel = new JPanel();
        northPanel.setBackground(Color.white);
        frame.getContentPane().add(BorderLayout.NORTH, northPanel);

        // 위젯을 정의하고 초기화한 후 패널에 넣습니다.
        JLabel label = new JLabel("2011722086 한도영");
        northPanel.add(label);
    }
    /**
     * 남쪽 패널을 초기화합니다.
     */
    private void initSouthPanel() {
        // 패널을 정의하고 초기화한 후 프레임에 넣습니다.
        southPanel = new JPanel();
        southPanel.setBackground(Color.white);
        frame.getContentPane().add(BorderLayout.SOUTH, southPanel);

        // 위젯을 정의하고 초기화한 후 패널에 넣습니다.
        statusLabel = new JLabel("Ready");
        southPanel.add(statusLabel);
    }
    /**
     * 중앙 패널을 초기화합니다.
     */
    private void initCenterPanel() {
        // 패널을 정의하고 초기화한 후 프레임에 넣습니다.
        centerPanel = new JPanel();
        centerPanel.setBackground(Color.CYAN);
        frame.getContentPane().add(BorderLayout.CENTER, centerPanel);

        // 패널의 레이아웃을 정의합니다.
        centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
    }

    //endregion



    //region 메서드를 정의합니다.
    /**
     * 프레임을 표시할지 여부를 설정합니다.
     */
    public final void setVisible(boolean value) {
        frame.setVisible(value);
    }
    /**
     * 프레임을 화면에 표시합니다.
     */
    public void show() {
        setVisible(true);
    }

    /**
     * 폼의 위치를 설정합니다.
     */
    public void setLocation(int x, int y) {
        frame.setLocation(x, y);
    }
    /**
     * 폼의 크기를 설정합니다.
     * @param width 폼의 너비입니다.
     * @param height 폼의 높이입니다.
     */
    public void setPreferredSize(int width, int height) {
        frame.setPreferredSize(new Dimension(width, height));
    }

    public int getWidth() { return frame.getWidth(); }
    public int getHeight() { return frame.getHeight(); }

    public int getX() { return frame.getX(); }
    public int getY() { return frame.getY(); }

    /**
     * 자원 해제를 요청합니다.
     */
    public void finish() {
        frame.dispose();
    }

    //endregion
}
