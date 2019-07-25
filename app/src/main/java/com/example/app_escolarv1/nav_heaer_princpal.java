package com.example.app_escolarv1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.application.app_EscolarV1.LoginActivity;
import com.application.app_EscolarV1.R;

import java.io.IOException;

import ca.mimic.oauth2library.OAuth2Client;
import ca.mimic.oauth2library.OAuthResponse;

@SuppressLint("Registered")
public class nav_heaer_princpal extends AppCompatActivity {

    TextView txtescu, txtalum;
    ImageView logoescu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_principal);

        txtescu =  findViewById(R.id.txtescu);
        txtalum =  findViewById(R.id.txtalum);
        logoescu =  findViewById(R.id.logoescu);


    }
}



