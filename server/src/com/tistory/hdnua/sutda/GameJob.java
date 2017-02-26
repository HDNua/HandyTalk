package com.tistory.hdnua.sutda;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 게임 Job입니다.
 */
public class GameJob extends Job {

    public GameJob(Server server, Socket socket, BufferedReader reader, PrintWriter writer) {
        super(server, socket, reader, writer);
    }

    @Override
    public void run() {

    }
}
