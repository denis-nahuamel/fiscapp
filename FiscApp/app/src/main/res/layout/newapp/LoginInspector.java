package com.example.willy.newapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Willy on 10/4/2018.
 */

public class LoginInspector extends Activity implements View.OnClickListener {

    Button btnLogin;
    CircularProgressButton btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        buttonEffect(btnLogin);




        //You can choose the color and the image after the loading is finished
        //btn.doneLoadingAnimation(fillColor, bitmap);
        //btn.revertAnimation();
    }


    public void openSweetAlert(String msj) {
        SweetAlertDialog sweet = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        sweet
                .setTitleText("(GPS) " + msj + " Guardado")
                .setContentText("Gracias por contribuir.")
                .setConfirmText("OK")
                .show();


        sweet.setCanceledOnTouchOutside(true);

        //Button btn = (Button) sweet.findViewById(R.id.confirm_button);
       // btn.setBackgroundColor(ContextCompat.getColor(MapsActivity.this,R.color.colorPrimary));

    }

    @Override
    public void onClick(View view) {
        if(view == btnLogin) {

            //openSweetAlert("Hola Yodeling boy");
            startActivity(new Intent(this, MapActivity.class));
        }
        if(view == btn) {
            btn.startAnimation();
        }
    }

    public static void buttonEffect(View button){
        button.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(0xe0a0aa1d, PorterDuff.Mode.SRC_ATOP);
                        v.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                v.getBackground().clearColorFilter();
                                v.invalidate();
                            }
                        }, 50L);
                        break;
                    }
                }
                return false;
            }
        });
    }
}
