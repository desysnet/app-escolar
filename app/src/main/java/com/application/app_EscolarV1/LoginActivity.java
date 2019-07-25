package com.application.app_EscolarV1;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_escolarv1.PrincipalActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ca.mimic.oauth2library.OAuth2Client;

import ca.mimic.oauth2library.OAuthResponse;

public class LoginActivity extends AppCompatActivity {

    //Declaracion
    EditText Lbl_UserId,Lbl_Password;
    Button btn_Login;
    ProgressBar progressBar;
    final String url_Login = "https://desysnet.azure-api.net/v1/oauth/token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        //enlace
        Lbl_UserId =  findViewById(R.id.Lbl_UserId);
        Lbl_Password =  findViewById(R.id.Lbl_Password);
        btn_Login =  findViewById(R.id.btn_Login);
        progressBar = findViewById(R.id.progressBar4);

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                progressBar.setVisibility(View.VISIBLE);
                if (isOnline()) {
                    showLoginError(getString(R.string.error_network));
                    return;
                }
                attemptLogin();
            }

        });

        // Setup
        Lbl_Password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    if (isOnline()) {
                        showLoginError(getString(R.string.error_network));
                        return false;
                    }
                }attemptLogin();
                return true;
            }
        });

    }

    private void showLoginError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }

    @SuppressLint("StaticFieldLeak")
    public class LoginUser extends AsyncTask<String, Void, String>{
        @SuppressLint({"WrongThread", "ShowToast"})
        @Override
        protected String doInBackground(String... strings){
            String User = strings[0];
            String Pass = strings[1];

            Map < String , String > app =  new  HashMap <> ();
            app.put ( "app", "AME" );


            OAuth2Client client = new OAuth2Client.Builder("","",url_Login)
                    .grantType("password")
                    .username(User)
                    .password(Pass)
                    .parameters(app)
                    .build();

            //OAuth2Client client = new OAuth2Client.Builder(""+User+"",""+Pass+"","","",  ""+url_Login+"").build();
            OAuthResponse response = null;

            try {
                response = client.requestAccessToken();
                if (response.isSuccessful()) {

                    String accessToken = response.getAccessToken();

                    String refreshToken = response.getRefreshToken();

                    //abre dashboard
                    Intent i = new Intent(LoginActivity.this, PrincipalActivity.class);
                    startActivity(i);
                    finish();
                }else {

                    showToast("User or Password mismatched!");

                }

            } catch (IOException e) {
                e.printStackTrace();
            }


            /*
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody formBody = new FormBody.Builder()
                    .add("grant_type", "password")
                    .add("Username", User)
                    .add("Password", Pass)
                    .build();


            Request request = new Request.Builder()
                    .url(url_Login)
                    .post(formBody)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .build();

            Response response = null;

            try{
                response = okHttpClient.newCall(request).execute();
                if(response.isSuccessful()){
                    String result = response.body().string();
                    if(result.equalsIgnoreCase("login")){
                        //abre dashboard
                        Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                        startActivity(i);
                        finish();
                    }else{
                        showToast("Email or Password mismatched!");
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }*/
            return null;
        }
    }
    public void showToast(final String Text){
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if ((Lbl_UserId != null) && (Lbl_Password != null)){
                Toast.makeText(LoginActivity.this, Text, Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }


    private void attemptLogin() {


        // Store values at the time of the login attempt.
        String UserId = Lbl_UserId.getText().toString();
        String password = Lbl_Password.getText().toString();

        new LoginUser().execute(UserId, password);

        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(UserId)) {
            Lbl_UserId.setError(getString(R.string.error_field_required));
            focusView = Lbl_UserId;
            cancel = true;
        }


        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            Lbl_Password.setError(getString(R.string.error_field_required));
            focusView = Lbl_Password;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        assert cm != null;
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork == null || !activeNetwork.isConnected();
    }



}
