package com.example.studentfeedbackapp.Presentation.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

    private Spinner spinnerFeedbackType, spinnerModuleType;
    private TextView tvName, tvEmail;
    private Button btnViewFeedbackForm;

    private StudentDashboardApiService studentDashboardApiService;
    private FeedbackModultypeApiService feedbackModultypeApiService;

    private List<FeedbackType> feedbackTypeList = new ArrayList<>();
    private List<FeedbackModuleType> moduleTypeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_dashboard);

        spinnerFeedbackType = findViewById(R.id.spinnerFeedbackType);
        spinnerModuleType = findViewById(R.id.spinnerModuleType);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        btnViewFeedbackForm = findViewById(R.id.btnViewFeedbackForm);

        studentDashboardApiService = RetrofitClient.getClient().create(StudentDashboardApiService.class);
        feedbackModultypeApiService = RetrofitClient.getClient().create(FeedbackModultypeApiService.class);

        loadStudentProfile();
        loadFeedbackTypes();
        loadFeedbackModuleTypes();

        btnViewFeedbackForm.setOnClickListener(v -> openFeedbackFormIfSelected());
    }

    private void openFeedbackFormIfSelected() {
        int feedbackPos = spinnerFeedbackType.getSelectedItemPosition();
        int modulePos = spinnerModuleType.getSelectedItemPosition();

        if (feedbackPos >= 0 && modulePos >= 0 && !feedbackTypeList.isEmpty() && !moduleTypeList.isEmpty()) {
            FeedbackType selectedFeedback = feedbackTypeList.get(feedbackPos);
            FeedbackModuleType selectedModule = moduleTypeList.get(modulePos);

            // Pass extras to FeedbackForm
            Intent intent = new Intent(StudentDashboard.this, FeedbackForm.class);
            intent.putExtra("feedbackTypeId", selectedFeedback.getFeedbacktype_id());
            intent.putExtra("moduleTypeId", selectedModule.getFeedbackModuleTypeId());


            startActivity(intent);
        } else {
            Toast.makeText(this, "Please select both Feedback Type and Module", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFeedbackTypes() {
        Call<FeedbackResponse> call = studentDashboardApiService.getFeedbackTypes();
        call.enqueue(new Callback<FeedbackResponse>() {
            @Override
            public void onResponse(Call<FeedbackResponse> call, Response<FeedbackResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    feedbackTypeList = response.body().getData();
                    List<String> feedbackNames = new ArrayList<>();
                    for (FeedbackType f : feedbackTypeList) feedbackNames.add(f.getFbtypename());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(StudentDashboard.this,
                            android.R.layout.simple_spinner_item, feedbackNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerFeedbackType.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<FeedbackResponse> call, Throwable t) {
                Toast.makeText(StudentDashboard.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFeedbackModuleTypes() {
        Call<FeedbackModuleResponse> call = feedbackModultypeApiService.getFeedbackModuleTypes();
        call.enqueue(new Callback<FeedbackModuleResponse>() {
            @Override
            public void onResponse(Call<FeedbackModuleResponse> call, Response<FeedbackModuleResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    moduleTypeList = response.body().getData();
                    List<String> moduleNames = new ArrayList<>();
                    for (FeedbackModuleType m : moduleTypeList) moduleNames.add(m.getFbModuleTypeName());
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(StudentDashboard.this,
                            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, moduleNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerModuleType.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<FeedbackModuleResponse> call, Throwable t) {
                Toast.makeText(StudentDashboard.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadStudentProfile() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", "");
        if (token.isEmpty()) {
            Toast.makeText(this, "Token missing! Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        Call<ProfileResponse> call = studentDashboardApiService.getStudentProfile("Bearer " + token);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvName.setText("Name: " + response.body().getData().getStudentname());
                    tvEmail.setText("Email: " + response.body().getData().getEmail());
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(StudentDashboard.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
