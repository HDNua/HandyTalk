package com.tistory.hdnua.sutda;

import java.lang.ref.WeakReference;

import com.tistory.hdnua.sutda.R;
import com.tistory.hdnua.sutda.utils.AudioWriterPCM;
import com.naver.speech.clientapi.SpeechConfig;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends HDAndroidForm {

    private static final String CLIENT_ID = "클라이언트 ID를 이 곳에 입력합니다."; // "내 애플리케이션"에서 Client ID를 확인해서 이곳에 적어주세요.
    private static final SpeechConfig SPEECH_CONFIG = SpeechConfig.OPENAPI_KR; // or SpeechConfig.OPENAPI_EN

    private RecognitionHandler handler;
    private NaverRecognizer naverRecognizer;

    private TextView txtResult;
    private Button btnStart;
    private String mResult;

    private AudioWriterPCM writer;

    private boolean isRunning;

    private static String recognizedMessage;
    public static String getRecognizedMessage() {
        return recognizedMessage;
    }

    // Handle speech recognition Messages.
    private void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.clientReady:
                // Now an user can speak.
                txtResult.setText("Connected");
                writer = new AudioWriterPCM(
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/NaverSpeechTest");
                writer.open("Test");
                break;

            case R.id.audioRecording:
                writer.write((short[]) msg.obj);
                break;

            case R.id.partialResult:
                // Extract obj property typed with String.
                mResult = (String) (msg.obj);
                txtResult.setText(mResult);
                break;

            case R.id.finalResult:
                // Extract obj property typed with String array.
                // The first element is recognition result for speech.
                String[] results = (String[]) msg.obj;
                mResult = results[0];
                txtResult.setText(mResult);
                break;

            case R.id.recognitionError:
                if (writer != null) {
                    writer.close();
                }

                mResult = "Error code : " + msg.obj.toString();
                txtResult.setText(mResult);
                btnStart.setText(R.string.str_start);
                btnStart.setEnabled(true);
                isRunning = false;

                recognizedMessage = null;
                setResult(RESULT_CANCELED);
                finish();
                break;

            case R.id.clientInactive:
                if (writer != null) {
                    writer.close();
                }

                btnStart.setText(R.string.str_start);
                btnStart.setEnabled(true);
                isRunning = false;

                recognizedMessage = mResult;
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

    @Override
    protected void initContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initWidgets() {
        txtResult = (TextView) findViewById(R.id.txt_result);
        btnStart = (Button) findViewById(R.id.btn_start);

        handler = new RecognitionHandler(this);
        naverRecognizer = new NaverRecognizer(this, handler, CLIENT_ID, SPEECH_CONFIG);

    }

    @Override
    protected void initWidgetActions() {
        btnStart.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isRunning) {
                    // Start button is pushed when SpeechRecognizer's state is inactive.
                    // Run SpeechRecognizer by calling recognize().
                    mResult = "";
                    txtResult.setText("Connecting...");
                    btnStart.setText(R.string.str_listening);
                    isRunning = true;

                    naverRecognizer.recognize();
                } else {
                    // This flow is occurred by pushing start button again
                    // when SpeechRecognizer is running.
                    // Because it means that a user wants to cancel speech
                    // recognition commonly, so call stop().
                    btnStart.setEnabled(false);

                    naverRecognizer.getSpeechRecognizer().stop();
                }
            }
        });
    }

    @Override
    protected void initEndOfCreation(Bundle savedInstanceState) {
        btnStart.performClick();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // initialize() must be called on resume time.
        naverRecognizer.getSpeechRecognizer().initialize();

        mResult = "";
        txtResult.setText("");
        btnStart.setText(R.string.str_start);
        btnStart.setEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // release() must be called on pause time.
        naverRecognizer.getSpeechRecognizer().stopImmediately();
        naverRecognizer.getSpeechRecognizer().release();
        isRunning = false;
    }

    // Declare handler for handling SpeechRecognizer thread's Messages.
    static class RecognitionHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        RecognitionHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivity.get();
            if (activity != null) {
                activity.handleMessage(msg);
            }
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        showToast("중단할 수 없습니다.", Toast.LENGTH_SHORT);
    }
}