package com.tistory.hdnua.sutda;


import android.os.Bundle;

/**
 * 시작 폼입니다.
 */
public class FirstAndroidForm extends HDAndroidForm {
    //region 생성자를 정의합니다.
    @Override
    protected void initContentView() { setContentView(R.layout.activity_first); }
    @Override
    protected void initWidgets() { }
    @Override
    protected void initWidgetActions() { }

    @Override
    protected void initEndOfCreation(Bundle savedInstanceState) {
        startActivity(LoginAndroidForm.class);
        finish();
    }

    //endregion
}