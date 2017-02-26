package com.tistory.hdnua.sutda;

import java.io.IOException;
import java.net.SocketException;

import static com.tistory.hdnua.sutda.Handy.REQUEST_WAITING;
import static com.tistory.hdnua.sutda.Handy.SERVER_PORT;

/**
 * 대기실 관리자입니다.
 */

public abstract class WaitingRoomManager {
    //region 필드를 정의합니다.
    private final UserInfo userInfo;
    private WaitingRoomJob waitingRoomJob;

    public WaitingRoomManager(UserInfo userInfo) {
        this.userInfo = userInfo;
    }


    //endregion



    //region 메서드를 정의합니다.
    /**
     *
     * @throws IOException
     */
    public void start() throws IOException {
        waitingRoomJob = new WaitingRoomJob(userInfo);
        Thread thread = new Thread(waitingRoomJob);
        thread.start();
    }

    /**
     *
     */
    protected abstract void blockInputWidgets();
    /**
     *
     * @param message
     */
    protected abstract void write(String message);
    /**
     *
     */
    protected abstract void finish();

    /**
     *
     * @param message
     */
    public void send(String message) {
        waitingRoomJob.send(message);
    }

    public void closeSocket() throws IOException {
        waitingRoomJob.closeSocket();
    }

    public String recv() throws IOException {
        return waitingRoomJob.recv();
    }

    //endregion





    //region Job을 정의합니다.
    class WaitingRoomJob extends NetworkJob {
        UserInfo userInfo;

        public WaitingRoomJob(UserInfo userInfo) throws IOException {
            super(Handy.getServerIP(), SERVER_PORT);
            this.userInfo = userInfo;
        }

        @Override
        public void tryRoutine() {
            try {
                // 서버에 메시지를 보내고 응답을 대기합니다.
                send(REQUEST_WAITING);

                // 채팅을 시작합니다.
                String message;
                while ((message = recv()) != null) {
                    write(message);
                }
            }
            catch (SocketException ex) {
                write("Server is out of control; program has been terminated.");
                blockInputWidgets();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        @Override
        protected void catchRoutine(Exception ex) {
            super.catchRoutine(ex);
            finish();
        }

        @Override
        protected void finallyRoutine() {
            super.finallyRoutine();
            blockInputWidgets();
        }
    }

    //endregion
}
