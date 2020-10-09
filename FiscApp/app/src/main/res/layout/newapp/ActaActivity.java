package com.example.willy.newapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class ActaActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnGuardarActa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.pruebas_act);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        btnGuardarActa = (Button) findViewById(R.id.btnGuardarActa);
        btnGuardarActa.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btnGuardarActa) {
            finish();
        }
    }
}
