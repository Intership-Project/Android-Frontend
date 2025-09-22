package com.example.studentfeedbackapp.Presentation.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentfeedbackapp.Models.ApiInterface.FeedbackModultypeApiService;
import com.example.studentfeedbackapp.Models.ApiInterface.ScheduleFeedbackApiService;
import com.example.studentfeedbackapp.Models.ApiInterface.StudentDashboardApiService;
import com.example.studentfeedbackapp.Models.Request.FeedbackModuleType;
import com.example.studentfeedbackapp.Models.Request.FeedbackType;
import com.example.studentfeedbackapp.Models.Response.FeedbackModuleResponse;
import com.example.studentfeedbackapp.Models.Response.FeedbackResponse;
import com.example.studentfeedbackapp.Models.Response.ProfileResponse;
import com.example.studentfeedbackapp.Models.Response.ScheduleFeedbackResponse;
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
    private Button btnViewFeedbackForm, btnLogout;

    private StudentDashboardApiService studentDashboardApiService;
    private FeedbackModultypeApiService feedbackModultypeApiService;

    private List<FeedbackType> feedbackTypeList = new ArrayList<>();
    private List<FeedbackModuleType> moduleTypeList = new ArrayList<>();

    private static final String TAG = "StudentDashboard";

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
        btnLogout = findViewById(R.id.btnLogout);

        studentDashboardApiService = RetrofitClient.getClient().create(StudentDashboardApiService.class);
        feedbackModultypeApiService = RetrofitClient.getClient().create(FeedbackModultypeApiService.class);

        loadStudentProfile();
        loadFeedbackTypes();

        spinnerFeedbackType.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                if (!feedbackTypeList.isEmpty() && position < feedbackTypeList.size()) {
                    FeedbackType selectedFeedback = feedbackTypeList.get(position);
                    loadModuleTypesByFeedbackType(selectedFeedback.getFeedbacktype_id());
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
            }
        });

        btnViewFeedbackForm.setOnClickListener(v -> openFeedbackFormIfSelected());

        btnLogout.setOnClickListener(v -> {
            SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            prefs.edit().clear().apply(); // Clear token + all data

            Toast.makeText(StudentDashboard.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(StudentDashboard.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    // ✅ Updated: check active schedule before opening FeedbackForm
    private void openFeedbackFormIfSelected() {
        int feedbackPos = spinnerFeedbackType.getSelectedItemPosition();
        int modulePos = spinnerModuleType.getSelectedItemPosition();

        if (feedbackPos < 0 || modulePos < 0 || feedbackTypeList.isEmpty() || moduleTypeList.isEmpty()) {
            Toast.makeText(this, "Please select both Feedback Type and Module", Toast.LENGTH_SHORT).show();
            return;
        }

        FeedbackType selectedFeedback = feedbackTypeList.get(feedbackPos);
        FeedbackModuleType selectedModule = moduleTypeList.get(modulePos);

        ScheduleFeedbackApiService scheduleApi = RetrofitClient.getClient()
                .create(ScheduleFeedbackApiService.class);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int studentCourseId = prefs.getInt("course_id", -1);
        int batchId = prefs.getInt("batch_id", -1);

        scheduleApi.getScheduleFeedback().enqueue(new Callback<ScheduleFeedbackResponse>() {
            @Override
            public void onResponse(Call<ScheduleFeedbackResponse> call, Response<ScheduleFeedbackResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ScheduleFeedbackResponse.FeedbackData> scheduleList = response.body().getData();
                    ScheduleFeedbackResponse.FeedbackData activeSchedule = null;

                    for (ScheduleFeedbackResponse.FeedbackData schedule : scheduleList) {
                        if ("active".equalsIgnoreCase(schedule.getStatus())
                                && schedule.getCourse_id() == studentCourseId
                                && schedule.getFeedbacktype_id() == selectedFeedback.getFeedbacktype_id()
                                && (schedule.getFeedbacktype_id() != 2 || schedule.getBatch_id() == batchId)) {
                            activeSchedule = schedule;
                            break;
                        }
                    }

                    if (activeSchedule != null) {
                        Intent intent = new Intent(StudentDashboard.this, FeedbackForm.class);
                        intent.putExtra("feedbackTypeId", selectedFeedback.getFeedbacktype_id());
                        intent.putExtra("moduleTypeId", selectedModule.getFeedbackModuleTypeId());
                        startActivity(intent);
                    } else {
                        Toast.makeText(StudentDashboard.this, "No active feedback available for selected type/module", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(StudentDashboard.this, "Failed to fetch feedback schedules", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ScheduleFeedbackResponse> call, Throwable t) {
                Toast.makeText(StudentDashboard.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Schedule API Failure", t);
            }
        });
    }

    // Load Feedback Types
    private void loadFeedbackTypes() {
        Call<FeedbackResponse> call = studentDashboardApiService.getFeedbackTypes();
        call.enqueue(new Callback<FeedbackResponse>() {
            @Override
            public void onResponse(Call<FeedbackResponse> call, Response<FeedbackResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    feedbackTypeList.clear();
                    feedbackTypeList.addAll(response.body().getData());

                    List<String> feedbackNames = new ArrayList<>();
                    for (FeedbackType f : feedbackTypeList) feedbackNames.add(f.getFbtypename());

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(StudentDashboard.this,
                            android.R.layout.simple_spinner_item, feedbackNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerFeedbackType.setAdapter(adapter);
                } else {
                    Toast.makeText(StudentDashboard.this, "Failed to load Feedback Types", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FeedbackResponse> call, Throwable t) {
                Toast.makeText(StudentDashboard.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "FeedbackTypes API Failure", t);
            }
        });
    }

    private void loadModuleTypesByFeedbackType(int feedbackTypeId) {
        Call<FeedbackModuleResponse> call = feedbackModultypeApiService.getFeedbackModulesByFeedbackType(feedbackTypeId);
        call.enqueue(new Callback<FeedbackModuleResponse>() {
            @Override
            public void onResponse(Call<FeedbackModuleResponse> call, Response<FeedbackModuleResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    moduleTypeList.clear();
                    moduleTypeList.addAll(response.body().getData());

                    if (moduleTypeList.isEmpty()) {
                        spinnerModuleType.setAdapter(null);
                        Toast.makeText(StudentDashboard.this, "No Module Types found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    List<String> moduleNames = new ArrayList<>();
                    for (FeedbackModuleType m : moduleTypeList)
                        moduleNames.add(m.getFbModuleTypeName());

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(StudentDashboard.this,
                            android.R.layout.simple_spinner_item, moduleNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerModuleType.setAdapter(adapter);
                } else {
                    Toast.makeText(StudentDashboard.this, "Failed to load Module Types", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<FeedbackModuleResponse> call, Throwable t) {
                Toast.makeText(StudentDashboard.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "ModuleTypes API Failure", t);
            }
        });
    }

    private void loadStudentProfile() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String token = prefs.getString("token", "");
        if (token.isEmpty()) {
            Toast.makeText(this, "Token missing! Please login again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        Call<ProfileResponse> call = studentDashboardApiService.getStudentProfile("Bearer " + token);
        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvName.setText("Name: " + response.body().getData().getStudentname());
                    tvEmail.setText("Email: " + response.body().getData().getEmail());
                } else {
                    Toast.makeText(StudentDashboard.this, "Failed to load profile", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(StudentDashboard.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Profile API Failure", t);
            }
        });
    }
}
