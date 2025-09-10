package com.example.studentfeedbackapp.Presentation.UI;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.studentfeedbackapp.Models.ApiInterface.StudentDashboardApiService;
import com.example.studentfeedbackapp.Models.Request.FeedbackResponse;
import com.example.studentfeedbackapp.Models.Request.FeedbackType;
import com.example.studentfeedbackapp.Models.RetrofitClient.RetrofitClient;
import com.example.studentfeedbackapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudentDashboard extends AppCompatActivity {
    private Spinner spinnerFeedbackType;
    private StudentDashboardApiService studentDashboardApiService;
    private List<String> feedbackNames = new ArrayList<>(); // Spinner ke liye sirf names

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_dashboard);

        spinnerFeedbackType = findViewById(R.id.spinnerFeedbackType);


        studentDashboardApiService = RetrofitClient.getClient().create(StudentDashboardApiService.class);

        loadFeedbackTypes();

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
    }
