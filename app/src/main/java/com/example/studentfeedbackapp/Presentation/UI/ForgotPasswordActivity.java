package com.example.studentfeedbackapp.Presentation.UI;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentfeedbackapp.Models.ApiInterface.ForgotApiService;
import com.example.studentfeedbackapp.Models.Request.ForgotRequest;
import com.example.studentfeedbackapp.Models.Response.ForgotResponse;
import com.example.studentfeedbackapp.Models.RetrofitClient.RetrofitClient;
import com.example.studentfeedbackapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etEmail, etNewPassword, etConfirmPassword;
    Button btnResetPassword, btnSubmit;
    String resetToken;
    ForgotApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Initialize views
        etEmail = findViewById(R.id.etEmail);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        btnSubmit = findViewById(R.id.btnSubmit);

        // Hide new password fields initially
        etNewPassword.setVisibility(View.GONE);
        etConfirmPassword.setVisibility(View.GONE);
        btnSubmit.setVisibility(View.GONE);

        apiService = RetrofitClient.getClient().create(ForgotApiService.class);

        // Step 1: Request reset token
        btnResetPassword.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show();
                return;
            }

            ForgotRequest forgotRequest = new ForgotRequest(email);
            Log.d(TAG, "Sending forgot password request for email: " + email);

            apiService.forgotPassword(forgotRequest).enqueue(new Callback<ForgotResponse>() {
                @Override
                public void onResponse(Call<ForgotResponse> call, Response<ForgotResponse> response) {
                    if (response.isSuccessful() && response.body() != null &&
                            "success".equalsIgnoreCase(response.body().getStatus())) {

                        resetToken = response.body().getResetToken();
                        Log.d(TAG, "Reset token received: " + resetToken);

                        // Hide reset button, show new password fields + submit
                        btnResetPassword.setVisibility(View.GONE);
                        etNewPassword.setVisibility(View.VISIBLE);
                        etConfirmPassword.setVisibility(View.VISIBLE);
                        btnSubmit.setVisibility(View.VISIBLE);

                        Toast.makeText(ForgotPasswordActivity.this,
                                "Email verified! Enter new password & click Submit.",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this,
                                response.body() != null ? response.body().getMessage() : "Email not found",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ForgotResponse> call, Throwable t) {
                    Toast.makeText(ForgotPasswordActivity.this,
                            "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Step 2: Reset password
        btnSubmit.setOnClickListener(v -> {
            String newPass = etNewPassword.getText().toString().trim();
            String confirmPass = etConfirmPassword.getText().toString().trim();

            if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            Log.d(TAG, "Sending reset request: token=" + resetToken + ", newPassword=" + newPass);
            ForgotRequest resetRequest = new ForgotRequest(resetToken, newPass);

            apiService.resetPassword(resetRequest).enqueue(new Callback<ForgotResponse>() {
                @Override
                public void onResponse(Call<ForgotResponse> call, Response<ForgotResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d(TAG, "Password reset success: " + response.body().getMessage());
                        Toast.makeText(ForgotPasswordActivity.this,
                                response.body().getMessage(),
                                Toast.LENGTH_SHORT).show();
                        finish(); // open login activity
                    } else {
                        Log.d(TAG, "ResetPassword failed - response body: " + response.body() + ", raw: " + response.raw());
                        Toast.makeText(ForgotPasswordActivity.this,
                                "Failed: " + (response.body() != null ? response.body().getMessage() : "Unknown error"),
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ForgotResponse> call, Throwable t) {
                    Toast.makeText(ForgotPasswordActivity.this,
                            "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
}
