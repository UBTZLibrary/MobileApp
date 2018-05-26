package com.example.ariunmunkhe.orderbook;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import static com.example.ariunmunkhe.orderbook.FragmentActivity.db;

/**
 * Created by ariunmunkh.e on 2017-01-21.
 */

public class SettingsActivity  extends AppCompatActivity {

    EditText txtNameSpace;
    EditText txtConnUrl;
    Button btnSaveSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        // Set up the login form.
        txtNameSpace = (EditText) findViewById(R.id.txtNameSpase);
        txtConnUrl = (EditText) findViewById(R.id.txtConnUrl);
        btnSaveSetting = (Button) findViewById(R.id.btnSaveSetting);
        getSetting();
        btnSaveSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    boolean cancel = false;
                    View focusView = null;

                    if (TextUtils.isEmpty(txtNameSpace.getText().toString())) {
                        txtNameSpace.setError("Энэ талбарт утга шаардлагатай");
                        focusView = txtNameSpace;
                        cancel = true;
                    }

                    if (TextUtils.isEmpty(txtConnUrl.getText().toString())) {
                        txtConnUrl.setError("Энэ талбарт утга шаардлагатай");
                        focusView = txtConnUrl;
                        cancel = true;
                    }
                    if (cancel) {
                        focusView.requestFocus();
                        return;
                    }
                    setSetting();
                } catch (Throwable throwable) {
                    Toast.makeText(getBaseContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    void getSetting() {
        Cursor c = db.rawQuery("SELECT entryID,namespace,url from tblsetting where entryid = '1';", null);
        if (c.getCount() == 0) {
            return;
        }
        while (c.moveToNext()) {
            txtNameSpace.setText(c.getString(1));
            txtConnUrl.setText(c.getString(2));
        }
    }

    void setSetting() {
        if (getTestConn()) {
            FragmentActivity.NAMESPACE = txtNameSpace.getText().toString();
            FragmentActivity.URL = txtConnUrl.getText().toString();
            db.execSQL("update tblsetting set namespace = '" + txtNameSpace.getText().toString()
                    + "', url = '" + txtConnUrl.getText().toString() + "' where entryid = '1';");
            finish();
            Toast.makeText(getBaseContext(), "Амжилтай хадгаллаа.", Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getBaseContext(), "Тохиргоо буруу байна.", Toast.LENGTH_LONG).show();
        }
    }

    boolean getTestConn() {

        final String METHOD_NAME = "getTestConn";

        SoapObject request = new SoapObject(txtNameSpace.getText().toString(), METHOD_NAME);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transporte = new HttpTransportSE(txtConnUrl.getText().toString());
        try {
            transporte.call(txtNameSpace.getText().toString() + METHOD_NAME, envelope);
            SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();
            String res = resultado_xml.toString();

            if (res.equals("OK")) {
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

}
