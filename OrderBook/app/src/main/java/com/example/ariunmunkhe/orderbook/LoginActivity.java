package com.example.ariunmunkhe.orderbook;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * A login screen that offers login via UserID/password.
 */
public class LoginActivity extends AppCompatActivity {

    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText txtUserID;
    private EditText txtPassword;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        if (Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        txtUserID = (EditText) findViewById(R.id.txtUserID);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        txtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        txtPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


        Button btnResetPass = (Button) findViewById(R.id.btnResetPass);
        btnResetPass.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                txtUserID.setError(null);
                txtPassword.setError(null);

                // Store values at the time of the login attempt.
                String UserID = txtUserID.getText().toString();

                if (TextUtils.isEmpty(UserID)) {
                    txtUserID.setError("Энэ талбарт утга шаардлагатай");
                    return;
                }
                finish();
            }
        });
        Button btnSetting = (Button) findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(LoginActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_change_password) {
            return true;
        } else if (id == R.id.action_login_out) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid UserID, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        txtUserID.setError(null);
        txtPassword.setError(null);

        // Store values at the time of the login attempt.
        String UserID = txtUserID.getText().toString();
        String password = txtPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            txtPassword.setError("Энэ талбарт утга шаардлагатай");
            focusView = txtPassword;
            cancel = true;
        }

        // Check for a valid UserID address.
        if (TextUtils.isEmpty(UserID)) {
            txtUserID.setError("Энэ талбарт утга шаардлагатай");
            focusView = txtUserID;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(UserID, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUserID;
        private final String mPassword;
        private String mErrorMessage;

        UserLoginTask(String UserID, String password) {
            mUserID = UserID;
            mPassword = password;
            mErrorMessage = "OK";
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            final String METHOD_NAME = "getLoginAccess";

            SoapObject request = new SoapObject(FragmentActivity.NAMESPACE, METHOD_NAME);
            request.addProperty("userID", mUserID);
            request.addProperty("password", mPassword);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE transporte = new HttpTransportSE(FragmentActivity.URL);
            try {
                transporte.call(FragmentActivity.NAMESPACE + METHOD_NAME, envelope);
                SoapPrimitive resultado_xml = (SoapPrimitive) envelope.getResponse();
                String res = resultado_xml.toString();

                if (res.equals("OK")) {
                    return true;
                } else {
                    mErrorMessage = res;
                    return false;
                }

            } catch (Exception e) {
                mErrorMessage = e.getMessage();
                return false;
            }

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                Intent intent = new Intent(LoginActivity.this, FragmentActivity.class);
                FragmentActivity._loginUserID = txtUserID.getText().toString();
                FragmentActivity._Password = txtPassword.getText().toString();

//                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//                View header = navigationView.getHeaderView(0);
//                TextView txtLoginUserName = (TextView) header.findViewById(R.id.txtLoginUserName);
//                txtLoginUserName.setText(txtUserID.getText().toString());

                FragmentActivity.db.execSQL("insert into tbluser\n" +
                        "  (USERID, STUDENTID, PASSWORD, ISACTIVE, islogin)\n" +
                        "  select '" + txtUserID.getText().toString() + "',\n" +
                        "         '',\n" +
                        "         '" + txtPassword.getText().toString() + "',\n" +
                        "         'Y',\n" +
                        "         'Y' WHERE NOT EXISTS (SELECT tbl.USERID from tbluser tbl where tbl.USERID = '" + txtUserID.getText().toString() + "');");
                FragmentActivity.db.execSQL("update tbluser set islogin = 'Y' where USERID = '" + txtUserID.getText().toString() + "';");
                startActivity(intent);
                finish();
            } else {
                if (mErrorMessage.equalsIgnoreCase("Идэвхгүй хэрэглэгч байна.") || mErrorMessage.equalsIgnoreCase("Бүртгэлгүй хэрэглэгч байна.")) {
                    txtUserID.setError(mErrorMessage);
                    txtUserID.requestFocus();
                } else if (mErrorMessage.equalsIgnoreCase("Нууц үг буруу байна.")) {
                    txtPassword.setError(mErrorMessage);
                    txtPassword.requestFocus();
                } else {
                    Toast.makeText(getBaseContext(), mErrorMessage, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}

