package com.tistory.hdnua.sutda;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.naver.speech.clientapi.SpeechConfig;
import com.tistory.hdnua.sutda.utils.AudioWriterPCM;

import java.lang.ref.WeakReference;

import static com.tistory.hdnua.sutda.Handy.REQUEST_DISCONNECTION;
import static com.tistory.hdnua.sutda.Handy.USER_INFO_KEY;

/**
 * 대기실 폼입니다.
 */
public class WaitingRoomAndroidForm extends HDAndroidForm {
    //region 위젯을 정의합니다.
    private Button _Button_Send;
    private EditText _EditText_ChatBoard;
    private EditText _EditText_Send;

    //endregion



    //region 필드를 정의합니다.
    private UserInfo userInfo;
    WaitingRoomManager manager;

    //endregion



    //region 생성자를 정의합니다.
    @Override
    protected void initContentView() { setContentView(R.layout.activity_waiting_room); }
    @Override
    protected void initWidgets() {
        _Button_Send = getItem(R.id._Button_SendMessage);
        // _Button_Play = getItem(R.id._Button_Play);

        _EditText_ChatBoard = getItem(R.id._EditText_ChatBoard);

        Scroller scroller = new Scroller(_EditText_ChatBoard.getContext());
        _EditText_ChatBoard.setScroller(scroller);

        _EditText_Send = getItem(R.id._EditText_SendMessage);
    }
    @Override
    protected void initWidgetActions() {
        View.OnClickListener onSendClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String userText = _EditText_Send.getText().toString();
                    if (userText.equals("")) {
                        blockInputWidgets();

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                            }
                        });
                        thread.start();
                        thread.join();

                        recognize();
                    }
                    else {
                        String message;
                        message = String.format("%s:%s", userInfo.getUserName(), userText);
                        sendMessage(message);
                    }
                }
                catch (Exception ex) {
                    log(">>>>> %s.%s catches Exception: %s", WaitingRoomAndroidForm.this, this, ex);
                    ex.printStackTrace();
                }

                // 입력 위젯을 초기화합니다.
                clearInputWidgets();
            }
        };
        _Button_Send.setOnClickListener(onSendClickListener);
    }

    @Override
    protected void initEndOfCreation(Bundle savedInstanceState) {
        try {
            Intent intent = getIntent();
            userInfo = (UserInfo)intent.getSerializableExtra(USER_INFO_KEY);
            manager = new WaitingRoomManager(userInfo) {
                @Override
                protected void blockInputWidgets() {
                    WaitingRoomAndroidForm.this.blockInputWidgets();
                }
                @Override
                protected void write(String message) {
                    WaitingRoomAndroidForm.this.write(message);
                }
                @Override
                protected void finish() {
                    send(REQUEST_DISCONNECTION);
                    closeSocket();
                    WaitingRoomAndroidForm.this.finish();
                    System.exit(0);
                }
            };
            manager.start();
        }
        catch (Exception ex) {
            showToast(ex.getMessage(), Toast.LENGTH_SHORT);
        }
    }

    //endregion



    //region 이벤트 핸들러를 재정의합니다.
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            manager.finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "다시 한 번 눌러서 프로그램 종료", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }



    //endregion



    //region 메서드를 정의합니다.
    /**
     * 메시지를 전송합니다.
     * @param message 전송할 메시지입니다.
     */
    private void sendMessage(String message) {
        manager.send(message);
    }

    /**
     * 사용자의 입력을 막습니다.
     */
    private void blockInputWidgets() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _Button_Send.setEnabled(false);
                _EditText_Send.setEnabled(false);
            }
        });
    }
    /**
     * 사용자의 입력을 막습니다.
     */
    private void unblockInputWidgets() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _Button_Send.setEnabled(true);
                _EditText_Send.setEnabled(true);
            }
        });
    }
    /**
     * 입력 위젯을 초기화합니다.
     */
    private void clearInputWidgets() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _EditText_Send.setText("");
                _EditText_Send.requestFocus();
            }
        });
    }
    /**
     * 채팅 화면에 메시지를 출력합니다.
     * @param message 출력할 메시지입니다.
     */
    private void write(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                _EditText_ChatBoard.append(message + "\n");
            }
        });
    }


    /**
     *
     */
    void recognize() {
        Intent intent = new Intent(WaitingRoomAndroidForm.this, MainActivity.class);
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            try {
                String userText = MainActivity.getRecognizedMessage();
                if (userText == null || "".equals(userText)) {
                    throw new NullPointerException("userText");
                }
                String message = String.format("%s:%s", userInfo.getUserName(), userText);
                sendMessage(message);
            }
            catch (Exception ex) {
                ex.printStackTrace();
                sendMessage("");
            }
            finally {
                unblockInputWidgets();
            }
        }
    }

    //endregion
}
