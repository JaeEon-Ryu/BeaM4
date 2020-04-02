package com.example.beam4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        startActivity(new Intent(this, MainActivity.class));
        // splash 시간 주는 자리
        finish();
    }
}
