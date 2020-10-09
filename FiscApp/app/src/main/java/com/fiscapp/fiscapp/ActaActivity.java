package com.fiscapp.fiscapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

import hardware.print.BarcodeUtil;
import hardware.print.printer;

public class ActaActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnGuardarActa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.acta_actvity);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        btnGuardarActa = (Button) findViewById(R.id.btnGuardarActa);
        btnGuardarActa.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btnGuardarActa) {
            //testPrint();
            finish();
        }
    }


    private int mKeyTextSize = 24;
    private int mValueTextSize = 20;
    private int mValue2TextSize = 22;
    private int mLineTextSize = 18;
    private int mTitleTextSize = 40;
    /* BarCode Image size  */
    private int mBarcodeSize = 80;
    private int mOffsetX = 210;
    private int mStepDis = 1;
    private boolean bold = true;
    private float mCardWidth = 0;




    private Switch mToggleButton;
    private Button mStepButton;
    private Button mPrintButton;
    private Spinner mSpinner;
    private TextView mStatusTextView;

    private String mTitleStr = "Printer test ";
    private printer.PrintType mTitleType = printer.PrintType.Centering;
    private CheckBox mBoldCheckbox;
    private Spinner mAlignSpinner;
    private EditText mEditText;

    private boolean titleBold = true;

    printer mPrinter = new printer();

    private void testPrint() {

        mTitleStr =  "77023812";
        String mRazonSocial = "Willy Rosa";
        String mDireccion = "Manahua 2E";
        String mDpto = "Cusco";
        String mProvincia = "Cusco";
        String mDistrito = "Santiago";

        int result = mPrinter.Open();
        if(result == 0) {
            // TODO Auto-generated method stub
            mPrinter.PrintStringEx(mTitleStr, mTitleTextSize, false, titleBold, mTitleType);
            String str = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
            //mPrinter.PrintString(str, mValueTextSize);
            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();
            /*mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineStringByType("Type", mKeyTextSize, printer.PrintType.Left, true);
            mPrinter.PrintLineString("Industrial Android ", mValueTextSize, mOffsetX, false);//160
            mPrinter.PrintLineEnd();
            mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineString("Intelligent Terminal", mValueTextSize, mOffsetX, false);//160
            mPrinter.PrintLineEnd();*/
            mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineStringByType("Razon Social", mKeyTextSize, printer.PrintType.Left, true);
            mPrinter.PrintLineString(mRazonSocial, mValue2TextSize, mOffsetX, false);//160
            mPrinter.PrintLineEnd();
            mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineStringByType("Domicilio", mKeyTextSize, printer.PrintType.Left, true);
            mPrinter.PrintLineString(mDireccion, mValue2TextSize, mOffsetX, false);//160
            mPrinter.PrintLineEnd();
            mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineStringByType("Departamento", mKeyTextSize, printer.PrintType.Left, true);
            mPrinter.PrintLineString(mDpto, mValue2TextSize, mOffsetX, false);//160
            mPrinter.PrintLineEnd();
            mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineStringByType("Provincia", mKeyTextSize, printer.PrintType.Left, true);
            mPrinter.PrintLineString(mProvincia, mValue2TextSize, mOffsetX, false);//160
            mPrinter.PrintLineEnd();
            /*mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineStringByType("Thermal printer", mKeyTextSize, printer.PrintType.Left, true);
            mPrinter.PrintLineString(" 2'' 384dots ", mValue2TextSize, mOffsetX, false);//160
            mPrinter.PrintLineEnd();
            mPrinter.PrintLineInit(mKeyTextSize);
            //mPrinter.PrintLineString("Thermal printer", mKeyTextSize, PrintType.Left, true);
            mPrinter.PrintLineString("thermal printer ", mValue2TextSize, mOffsetX, false);//160
            mPrinter.PrintLineEnd();
            mPrinter.PrintLineInit(mKeyTextSize);*/

            mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineStringByType("Distrito", mKeyTextSize, printer.PrintType.Left, true);
            mPrinter.PrintLineString(mDistrito, mValue2TextSize, mOffsetX, false);//160
            mPrinter.PrintLineEnd();
            mPrinter.PrintLineInit(mLineTextSize);
//        mPrinter.PrintLineString(str, mLineTextSize, PrintType.Centering, true);
//        mPrinter.PrintLineEnd();
            mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineStringByType("Working temperature", mKeyTextSize, printer.PrintType.Left, true);
            mPrinter.PrintLineStringByType("-25~65℃", mKeyTextSize, printer.PrintType.Right, false);//160
            mPrinter.PrintLineEnd();
            mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineStringByType("Storage temperature", mKeyTextSize, printer.PrintType.Left, true);
            mPrinter.PrintLineStringByType("-30~70 ℃", mKeyTextSize, printer.PrintType.Right, false);//160
            mPrinter.PrintLineEnd();
            mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineStringByType("Humidity", mKeyTextSize, printer.PrintType.Left, true);
            mPrinter.PrintLineStringByType("0~95%", mKeyTextSize, printer.PrintType.Right, false);//160
            mPrinter.PrintLineEnd();
            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();
            //Bitmap bm=BitmapFactory.decodeResource(getResources(), R.drawable.logo);
            Bitmap bm = null;
            try {
                bm = BarcodeUtil.encodeAsBitmap("Thanks for using our Android terminal",
                        BarcodeFormat.QR_CODE, 80, 80);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            if (bm != null) {
                mPrinter.PrintBitmap(bm);
            }
            mPrinter.PrintLineInit(40);
            mPrinter.PrintLineStringByType("", mKeyTextSize, printer.PrintType.Right, true);//160
            mPrinter.PrintLineEnd();

            mPrinter.printBlankLine(40);

            mPrinter.Close();
        }


    }

    private void testPrint3() {

    }

    private void testPrint2() {

        mTitleStr =  "77023812";
        String mRazonSocial = "Willy Rosa";
        String mDireccion = "Manahua 2E";
        String mDpto = "Cusco";
        String mProvincia = "Cusco";
        String mDistrito = "Santiago";

        int result = mPrinter.Open();
        if(result == 0) {
            // TODO Auto-generated method stub
            mPrinter.PrintStringEx(mTitleStr, mTitleTextSize, false, titleBold, mTitleType);
            String str = "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~";
            //mPrinter.PrintString(str, mValueTextSize);
            mPrinter.PrintLineInit(mLineTextSize);
            mPrinter.PrintLineStringByType(str, mLineTextSize, printer.PrintType.Centering, false);
            mPrinter.PrintLineEnd();

            mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineStringByType("Razon Social", mKeyTextSize, printer.PrintType.Left, true);
            mPrinter.PrintLineString(mRazonSocial, mValue2TextSize, mOffsetX, false);//160
            mPrinter.PrintLineEnd();

            mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineStringByType("Domicilio", mKeyTextSize, printer.PrintType.Left, true);
            mPrinter.PrintLineString(mDireccion, mValue2TextSize, mOffsetX, false);//160
            mPrinter.PrintLineEnd();

            mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineStringByType("Departamento", mKeyTextSize, printer.PrintType.Left, true);
            mPrinter.PrintLineString(mDpto, mValue2TextSize, mOffsetX, false);//160
            mPrinter.PrintLineEnd();

            mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineStringByType("Provincia", mKeyTextSize, printer.PrintType.Left, true);
            mPrinter.PrintLineString(mProvincia, mValue2TextSize, mOffsetX, false);//160
            mPrinter.PrintLineEnd();

            mPrinter.PrintLineInit(mKeyTextSize);
            mPrinter.PrintLineStringByType("Distrito", mKeyTextSize, printer.PrintType.Left, true);
            mPrinter.PrintLineString(mDistrito, mValue2TextSize, mOffsetX, false);//160
            mPrinter.PrintLineEnd();


            Bitmap bm=BitmapFactory.decodeResource(getResources(), R.drawable.escudo9);

            mPrinter.PrintBitmap(bm);

            /*Bitmap bm = null;
            try {
                bm = BarcodeUtil.encodeAsBitmap("Thanks for using our Android terminal",
                        BarcodeFormat.QR_CODE, 80, 80);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            if (bm != null) {
                mPrinter.PrintBitmap(bm);
            }*/
            mPrinter.PrintLineInit(40);
            mPrinter.PrintLineStringByType("", mKeyTextSize, printer.PrintType.Right, true);//160
            mPrinter.PrintLineEnd();

            mPrinter.printBlankLine(40);

            mPrinter.Close();
        }


    }
}
