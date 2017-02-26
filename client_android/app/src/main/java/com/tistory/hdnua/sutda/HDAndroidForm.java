package com.tistory.hdnua.sutda;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

/**
 * 안드로이드 폼입니다.
 */
public abstract class HDAndroidForm extends AppCompatActivity {
    //region 생성자를 정의합니다.
    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        initContentView();
        super.onCreate(savedInstanceState);
        initWidgets();
        initWidgetActions();

        initEndOfCreation(savedInstanceState);
    }
    /**
     * ContentView를 초기화합니다.
     */
    protected abstract void initContentView();
    /**
     * 위젯을 초기화합니다.
     */
    protected abstract void initWidgets();
    /**
     * 위젯 액션을 초기화합니다.
     */
    protected abstract void initWidgetActions();
    /**
     * 생성자의 마지막을 정의합니다.
     */
    protected abstract void initEndOfCreation(Bundle savedInstanceState);

    //endregion



    //region 공용 메서드를 정의합니다.
    /**
     * id에 해당하는 위젯을 가져옵니다.
     * @param id 위젯의 ID입니다.
     * @param <T> 위젯의 형식입니다.
     * @return id에 해당하는 위젯을 형식을 맞춰서 가져옵니다.
     */
    protected final <T> T getItem(int id) {
        return (T)findViewById(id);
    }

    /**
     * Activity를 시작합니다.
     * @param formClass Activity의 클래스입니다.
     * @param <T> AppCompatActivity를 상속하는 Activity입니다.
     */
    protected <T extends AppCompatActivity> void startActivity(Class<T> formClass) {
        Intent intent = new Intent(HDAndroidForm.this, formClass);
        startActivity(intent);
    }
    /**
     * Activity를 시작합니다.
     * @param formClass Activity의 클래스입니다.
     * @param pair Extra로 등록할 <Key, Value> 쌍입니다.
     * @param <T> AppCompatActivity를 상속하는 Activity입니다.
     */
    protected <T extends AppCompatActivity> void startActivity(Class<T> formClass, Pair<String, UserInfo> pair) {
        Intent intent = new Intent(HDAndroidForm.this, formClass);
        intent.putExtra(pair.first, pair.second);
        startActivity(intent);
    }

    /**
     * 출력 스트림에 개행 문자를 넣습니다.
     */
    protected void log() {
        Log.d("Handy> ", "\n");
    }
    /**
     * 출력 스트림에 형식화된 문자열을 출력합니다.
     * @param fmt 문자열 형식입니다.
     * @param args 형식 문자열의 인자입니다.
     */
    protected void log(String fmt, Object... args) {
        Log.d("Handy> ", String.format(fmt + "\n", args));
    }
    /**
     * 출력 스트림에 객체를 출력합니다.
     * @param o 출력할 객체입니다. null이라면 "null" 문자열을 출력합니다.
     */
    protected void log(Object o) {
        Log.d("Handy> ", (o != null) ? o.toString() : "null");
    }

    /**
     * 예외 스택을 추적합니다.
     * @param th 예외 객체입니다.
     */
    protected void printStackTrace(Throwable th) {
        log(Log.getStackTraceString(th));
    }

    /**
     * 토스트 메시지를 띄웁니다.
     * @param message 보일 메시지입니다.
     * @param duration 메시지를 띄울 시간입니다.
     */
    protected void showToast(String message, int duration) {
        Toast.makeText(getApplicationContext(), message, duration).show();
    }

    //endregion
}
