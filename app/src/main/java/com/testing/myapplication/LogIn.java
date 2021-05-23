package com.testing.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import static Utils.Utils.changeAppLanguage;
import static Utils.Utils.getLanguage;
import static Utils.Utils.hasSet;
import static Utils.Utils.setLanguage;

public class LogIn extends AppCompatActivity {
    private static final String TAG = "LogIn";
    TextInputEditText textInputEditTextUsername, textInputEditTextPassword;
    Button buttonLogin;
    TextView textViewSignUp;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //language
        String sta = getLanguage(this);
        if(!hasSet(this)) {
            setLanguage(this, getResources().getConfiguration().locale.getLanguage());
        }else
            setLanguage(this,getLanguage(this));
        changeAppLanguage(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        textInputEditTextUsername = findViewById(R.id.username);
        textInputEditTextPassword = findViewById(R.id.password);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewSignUp = findViewById(R.id.signUpText);
        progressBar = findViewById(R.id.progress);

        textInputEditTextPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        });
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String username, password;
                username = String.valueOf(textInputEditTextUsername.getText());
                password = String.valueOf(textInputEditTextPassword.getText());

                if(!username.equals("")&&!password.equals(""))
                {
                    //Start ProgressBar first (Set visibility VISIBLE)
                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[2];
                            field[0] = "username";
                            field[1] = "password";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = username;
                            data[1] = password;
                            Log.d(TAG, String.format("Address: http://%s/LoginRegister/login.php",getResources().getString(R.string.IP)));
                            PutData putData = new PutData(String.format("http://%s/LoginRegister/login.php",getResources().getString(R.string.IP)), "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if(result.equals("Login Success"))
                                    {
                                        //跳转到主界面
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();

                                    }
                                    //End ProgressBar (Set visibility to GONE)
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }
            }
        });

    }
}