package com.example.studentfeedbackapp.Presentation.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.studentfeedbackapp.MainActivity;
import com.example.studentfeedbackapp.Models.ApiInterface.LoginApiService;
import com.example.studentfeedbackapp.Models.Request.LoginRequest;
import com.example.studentfeedbackapp.Models.Response.LoginResponse;
import com.example.studentfeedbackapp.Models.RetrofitClient.RetrofitClient;
import com.example.studentfeedbackapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etPassword;
    Button btnLogin;
    TextView tvRegister,tvForgotPassword;


    LoginApiService loginApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        // Set EditText text & hint color to black
        etEmail.setTextColor(getResources().getColor(android.R.color.black));
        etEmail.setHintTextColor(getResources().getColor(android.R.color.black));

        etPassword.setTextColor(getResources().getColor(android.R.color.black));
        etPassword.setHintTextColor(getResources().getColor(android.R.color.black));

        // Retrofit API Service initialize
        loginApiService = RetrofitClient
                .getClient()
                .create(LoginApiService.class);

        // Login Button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Enter email & password", Toast.LENGTH_SHORT).show();
                    return;
                }

                loginUser(email, password);


            }
        });

        // Register Link
        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // ✅ Forgot Password Link
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginUser(String email, String password) {
        LoginRequest request = new LoginRequest(email, password);

        Call<LoginResponse> call = loginApiService.login(request);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();

                    if ("success".equalsIgnoreCase(loginResponse.getStatus())) {
                        String token = loginResponse.getData().getToken();
                        String studentName = loginResponse.getData().getStudentname();

                        // ✅ Token ko SharedPreferences me save karo
                        getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                                .edit()
                                .putString("token", token)
                                .apply();
                        Log.d("TOKEN_SAVE", "Token saved: " + token);


                        Toast.makeText(LoginActivity.this, "Welcome " + studentName, Toast.LENGTH_SHORT).show();

                        // ✅ Go to MainActivity
                        Intent intent = new Intent(LoginActivity.this, StudentDashboard.class);
                        intent.putExtra("token", token);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LOGIN_ERROR", t.getMessage(), t);
                Toast.makeText(LoginActivity.this, "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
