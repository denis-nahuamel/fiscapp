package com.fiscapp.fiscapp;

        import android.content.Context;
        import android.util.AttributeSet;
        import android.view.KeyEvent;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.EditText;
        import android.widget.Toast;

public class CustomEditText extends EditText {

    private KeyImeChange keyImeChangeListener;

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setKeyImeChangeListener(KeyImeChange listener){
        keyImeChangeListener = listener;
    }

    public interface KeyImeChange {
        public void onKeyIme(int keyCode, KeyEvent event);
    }

    @Override
    public boolean onKeyPreIme (int keyCode, KeyEvent event){
        if(keyImeChangeListener != null){
            keyImeChangeListener.onKeyIme(keyCode, event);
        }
        return false;
    }
}