package com.example.ariunmunkhe.orderbook;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by ariunmunkh.e on 2017-01-14.
 */

public class ChangePasswordActivity extends AppCompatActivity {

    EditText txtPassword;
    EditText txtPasswordNew;
    EditText txtPasswordReNew;
    Button btnChangePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        // Set up the login form.
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        txtPassword = (EditText) findViewById(R.id.txtPasswordCh);
        txtPasswordNew = (EditText) findViewById(R.id.txtPasswordNew);
        txtPasswordReNew = (EditText) findViewById(R.id.txtPasswordReNew);
        btnChangePassword = (Button) findViewById(R.id.btnChangePassword);
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String retMessage;
                    txtPassword.setError(null);
                    txtPasswordNew.setError(null);
                    txtPasswordReNew.setError(null);
                    if (txtPasswordNew.getText().toString().equals(txtPasswordReNew.getText().toString())) {
                        retMessage = setChangePassword(txtPassword.getText().toString(), txtPasswordNew.getText().toString());
                        if (retMessage.equals("OK")) {
                            finish();
                        } else {
                            txtPassword.setError(retMessage);
                            txtPassword.requestFocus();
                        }
                    } else {
                        txtPasswordNew.setError("Ялгаатай байна.");
                        txtPasswordReNew.setError("Ялгаатай байна.");
                        txtPasswordReNew.requestFocus();
                    }

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }

    private String setChangePassword(String password, String newPassword) {

        final String METHOD_NAME = "setChangePassword";
        String mErrorMessage;
        SoapObject request = new SoapObject(FragmentActivity.NAMESPACE, METHOD_NAME);
        request.addProperty("userID", FragmentActivity._loginUserID);
        request.addProperty("password", password);
        request.addProperty("newPassword", newPassword);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        HttpTransportSE transporte = new HttpTransportSE(FragmentActivity.URL);
        try {
            transporte.call(FragmentActivity.NAMESPACE + METHOD_NAME, envelope);
            SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();
            String res = resultado_xml.toString();
            mErrorMessage = res;

        } catch (Exception e) {
            mErrorMessage = e.getMessage();
        }
        return mErrorMessage;
    }
}
