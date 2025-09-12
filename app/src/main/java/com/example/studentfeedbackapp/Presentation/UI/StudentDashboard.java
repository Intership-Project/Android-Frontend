package com.example.studentfeedbackapp.Presentation.UI;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentfeedbackapp.Models.ApiInterface.FeedbackModultypeApiService;
import com.example.studentfeedbackapp.Models.ApiInterface.StudentDashboardApiService;
import com.example.studentfeedbackapp.Models.Request.FeedbackModuleType;
import com.example.studentfeedbackapp.Models.Request.FeedbackType;
import com.example.studentfeedbackapp.Models.Response.FeedbackModuleResponse;
import com.example.studentfeedbackapp.Models.Response.FeedbackResponse;
import com.example.studentfeedbackapp.Models.Response.ProfileResponse;
import com.example.studentfeedbackapp.Models.RetrofitClient.RetrofitClient;
import com.example.studentfeedbackapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentDashboard extends AppCompatActivity {
    private Spinner spinnerFeedbackType;
    private Spinner spinnerModuleType;
    private TextView tvName, tvEmail;
    private StudentDashboardApiService studentDashboardApiService;
    private FeedbackModultypeApiService feedbackModultypeApiService;
    private List<String> feedbackNames = new ArrayList<>(); // Spinner ke liye sirf names

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_dashboard);

        spinnerFeedbackType = findViewById(R.id.spinnerFeedbackType);
        spinnerModuleType = findViewById(R.id.spinnerModuleType);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);

        studentDashboardApiService = RetrofitClient.getClient().create(StudentDashboardApiService.class);
        feedbackModultypeApiService = RetrofitClient.getClient().create(FeedbackModultypeApiService.class);
                 //
        // 🔑 Profile data load karo
        loadStudentProfile();

        loadFeedbackTypes();
        loadFeedbackModuleTypes();

    }

    private void loadFeedbackTypes() {

        Call<FeedbackResponse> call = studentDashboardApiService.getFeedbackTypes();
        call.enqueue(new Callback<FeedbackResponse>() {
            @Override
            public void onResponse(Call<FeedbackResponse> call, Response<FeedbackResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<FeedbackType> feedbackList = response.body().getData();
                    feedbackNames.clear();
                    for (FeedbackType f : feedbackList) {
                        feedbackNames.add(f.getFbtypename()); // Spinner me show karne ke liye
                    }
                    setSpinner(feedbackNames);
                } else {
                    Toast.makeText(com.example.studentfeedbackapp.Presentation.UI.StudentDashboard.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FeedbackResponse> call, Throwable t) {
                Toast.makeText(com.example.studentfeedbackapp.Presentation.UI.StudentDashboard.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setSpinner(List<String> feedbackNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, feedbackNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFeedbackType.setAdapter(adapter);
    }



    private void loadFeedbackModuleTypes() {
        Call<FeedbackModuleResponse> call = feedbackModultypeApiService.getFeedbackModuleTypes();
        call.enqueue(new Callback<FeedbackModuleResponse>() {
            @Override
            public void onResponse(Call<FeedbackModuleResponse> call, Response<FeedbackModuleResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<FeedbackModuleType> moduleList = response.body().getData();
                    List<String> moduleNames = new ArrayList<>();
                    for (FeedbackModuleType m : moduleList) {
                        moduleNames.add(m.getFbModuleTypeName());
                    }
                    setSpinnerModule(moduleNames);
                }
            }

            @Override
            public void onFailure(Call<FeedbackModuleResponse> call, Throwable t) {
                Toast.makeText(StudentDashboard.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSpinnerModule(List<String> moduleNames) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, moduleNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModuleType.setAdapter(adapter);
    }

    private void loadStudentProfile() {
        // SharedPreferences se token get karo
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", "");

        if (token.isEmpty()) {
            Toast.makeText(this, "Token missing! Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        // API call with Authorization header
        Call<ProfileResponse> call = studentDashboardApiService.getStudentProfile("Bearer " + token);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String name = response.body().getData().getStudentname();
                    String email = response.body().getData().getEmail();

                    // TextViews me set karo
                    tvName.setText("Name: " + name);
                    tvEmail.setText("Email: " + email);
                } else {
                    Log.e("PROFILE_ERROR", "Failed to load profile | Code: "
                            + response.code() + " | Message: " + response.message());

                    Toast.makeText(StudentDashboard.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(StudentDashboard.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
