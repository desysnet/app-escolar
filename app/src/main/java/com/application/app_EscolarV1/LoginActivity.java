package com.application.app_EscolarV1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import ca.mimic.oauth2library.OAuth2Client;
import ca.mimic.oauth2library.OAuthError;
import ca.mimic.oauth2library.OAuthResponse;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    //Declaracion
    EditText Lbl_UserId,Lbl_Password;
    Button btn_Login;
    final String url_Login = "https://desysnet.azure-api.net/v1/oauth/token";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        Lbl_UserId = (EditText) findViewById(R.id.Lbl_UserId);
        Lbl_Password = (EditText) findViewById(R.id.Lbl_Password);
        btn_Login = (Button) findViewById(R.id.btn_Login);

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String UserId = Lbl_UserId.getText().toString();
                String password = Lbl_Password.getText().toString();
                new LoginUser().execute(UserId, password);
            }

        });
    }

    public class LoginUser extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings){
            String User = strings[0];
            String Pass = strings[1];
            OAuth2Client client = new OAuth2Client.Builder("","",url_Login)
                    .grantType("password")
                    .username(User)
                    .password(Pass).build();
            OAuthResponse response = null;
            try {
                response = client.requestAccessToken();
                if (response.isSuccessful()) {
                    String accessToken = response.getAccessToken();
                    String refreshToken = response.getRefreshToken();
                    //abre dashboard
                    Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(i);
                    finish();
                }
                else {
                    OAuthError error = response.getOAuthError();
                    String errorMsg = error.getError();
                }
                response.getCode();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
