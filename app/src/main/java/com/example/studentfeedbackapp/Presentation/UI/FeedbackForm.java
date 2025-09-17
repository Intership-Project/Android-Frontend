package com.example.studentfeedbackapp.Presentation.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentfeedbackapp.Models.ApiInterface.FilledFeedbackService;
import com.example.studentfeedbackapp.Models.ApiInterface.QuestionApiService;
import com.example.studentfeedbackapp.Models.ApiInterface.ScheduleFeedbackApiService;
import com.example.studentfeedbackapp.Models.Request.FilledFeedbackRequest;
import com.example.studentfeedbackapp.Models.Response.FilledFeedbackResponse;
import com.example.studentfeedbackapp.Models.Response.QuestionResponse;
import com.example.studentfeedbackapp.Models.Response.ScheduleFeedbackResponse;
import com.example.studentfeedbackapp.Models.RetrofitClient.RetrofitClient;
import com.example.studentfeedbackapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackForm extends AppCompatActivity {

    private TextView tvFaculty, tvSubject, tvQ1, tvQ2, tvQ3, tvQ4, tvQ5;
    private RadioGroup rgOptions1, rgOptions2, rgOptions3, rgOptions4, rgOptions5;
    private EditText edtcomment;
    private Button btnSubmit;

    private int scheduleFeedbackId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_form);

        // Initialize views
        tvFaculty = findViewById(R.id.txtfaculty);
        tvSubject = findViewById(R.id.txtsubject);
        tvQ1 = findViewById(R.id.tvQuestion1);
        tvQ2 = findViewById(R.id.tvQuestion2);
        tvQ3 = findViewById(R.id.tvQuestion3);
        tvQ4 = findViewById(R.id.tvQuestion4);
        tvQ5 = findViewById(R.id.tvQuestion5);

        rgOptions1 = findViewById(R.id.rgOptions1);
        rgOptions2 = findViewById(R.id.rgOptions2);
        rgOptions3 = findViewById(R.id.rgOptions3);
        rgOptions4 = findViewById(R.id.rgOptions4);
        rgOptions5 = findViewById(R.id.rgOptions5);

        edtcomment = findViewById(R.id.edtcomment);
        btnSubmit = findViewById(R.id.btnSubmit);

        loadScheduleAndQuestions();

        btnSubmit.setOnClickListener(v -> submitFeedback());
    }

    private void loadScheduleAndQuestions() {
        ScheduleFeedbackApiService scheduleApi = RetrofitClient.getClient()
                .create(ScheduleFeedbackApiService.class);

        scheduleApi.getScheduleFeedback().enqueue(new Callback<ScheduleFeedbackResponse>() {
            @Override
            public void onResponse(Call<ScheduleFeedbackResponse> call, Response<ScheduleFeedbackResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ScheduleFeedbackResponse.FeedbackData> scheduleList = response.body().getData();

                    if (scheduleList == null || scheduleList.isEmpty()) {
                        Toast.makeText(FeedbackForm.this, "No schedule found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ScheduleFeedbackResponse.FeedbackData schedule = scheduleList.get(0);
                    scheduleFeedbackId = schedule.getScheduleFeedbackId();
                    tvFaculty.setText(schedule.getFacultyname());
                    tvSubject.setText(schedule.getSubjectname());
                    loadQuestions(schedule.getFeedbacktype_id());
                } else {
                    Toast.makeText(FeedbackForm.this, "Failed to load schedule", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ScheduleFeedbackResponse> call, Throwable t) {
                Toast.makeText(FeedbackForm.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadQuestions(int feedbackTypeId) {
        QuestionApiService questionApi = RetrofitClient.getClient()
                .create(QuestionApiService.class);

        questionApi.getQuestions(feedbackTypeId).enqueue(new Callback<QuestionResponse>() {
            @Override
            public void onResponse(Call<QuestionResponse> call, Response<QuestionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<QuestionResponse.QuestionData> questions = response.body().getData();

                    if (questions == null || questions.isEmpty()) {
                        Toast.makeText(FeedbackForm.this, "No questions found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    tvQ1.setText(questions.size() > 0 ? questions.get(0).getQuestionText() : "");
                    tvQ2.setText(questions.size() > 1 ? questions.get(1).getQuestionText() : "");
                    tvQ3.setText(questions.size() > 2 ? questions.get(2).getQuestionText() : "");
                    tvQ4.setText(questions.size() > 3 ? questions.get(3).getQuestionText() : "");
                    tvQ5.setText(questions.size() > 4 ? questions.get(4).getQuestionText() : "");

                    rgOptions1.setVisibility(questions.size() > 0 ? View.VISIBLE : View.GONE);
                    rgOptions2.setVisibility(questions.size() > 1 ? View.VISIBLE : View.GONE);
                    rgOptions3.setVisibility(questions.size() > 2 ? View.VISIBLE : View.GONE);
                    rgOptions4.setVisibility(questions.size() > 3 ? View.VISIBLE : View.GONE);
                    rgOptions5.setVisibility(questions.size() > 4 ? View.VISIBLE : View.GONE);
                } else {
                    Toast.makeText(FeedbackForm.this, "Failed to load questions", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QuestionResponse> call, Throwable t) {
                Toast.makeText(FeedbackForm.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getSelectedRating(RadioGroup radioGroup) {
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) return 0;

        // Question 1
        if (radioGroup.getId() == R.id.rgOptions1) {
            if (selectedId == R.id.rbQ1Option1) return 1;
            if (selectedId == R.id.rbQ1Option2) return 2;
            if (selectedId == R.id.rbQ1Option3) return 3;
            if (selectedId == R.id.rbQ1Option4) return 4;
        }
        // Question 2
        else if (radioGroup.getId() == R.id.rgOptions2) {
            if (selectedId == R.id.rbQ2Option1) return 1;
            if (selectedId == R.id.rbQ2Option2) return 2;
            if (selectedId == R.id.rbQ2Option3) return 3;
            if (selectedId == R.id.rbQ2Option4) return 4;
        }
        // Question 3
        else if (radioGroup.getId() == R.id.rgOptions3) {
            if (selectedId == R.id.rbQ3Option1) return 1;
            if (selectedId == R.id.rbQ3Option2) return 2;
            if (selectedId == R.id.rbQ3Option3) return 3;
            if (selectedId == R.id.rbQ3Option4) return 4;
        }
        // Question 4
        else if (radioGroup.getId() == R.id.rgOptions4) {
            if (selectedId == R.id.rbQ4Option1) return 1;
            if (selectedId == R.id.rbQ4Option2) return 2;
            if (selectedId == R.id.rbQ4Option3) return 3;
            if (selectedId == R.id.rbQ4Option4) return 4;
        }
        // Question 5
        else if (radioGroup.getId() == R.id.rgOptions5) {
            if (selectedId == R.id.rbQ5Option1) return 1;
            if (selectedId == R.id.rbQ5Option2) return 2;
            if (selectedId == R.id.rbQ5Option3) return 3;
            if (selectedId == R.id.rbQ5Option4) return 4;
        }

        return 0;

    }

    private void submitFeedback() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        int studentId = prefs.getInt("student_id", -1);
        if (studentId == -1) {
            Toast.makeText(this, "Student ID not found. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        String comment = edtcomment.getText().toString().trim();

        RadioGroup[] groups = {rgOptions1, rgOptions2, rgOptions3, rgOptions4, rgOptions5};
        int total = 0;
        int answeredCount = 0;

        for (RadioGroup rg : groups) {
            int rating = getSelectedRating(rg);
            if (rating > 0) {
                total += rating;
                answeredCount++;
            }
        }

        if (answeredCount == 0) {
            Toast.makeText(this, "Please select at least one answer", Toast.LENGTH_SHORT).show();
            return;
        }

        // Correct average calculation
        int averageRating = Math.round((float) total / answeredCount);
        Log.d("FEEDBACK_FORM", "Total: " + total + ", Count: " + answeredCount + ", AverageRating: " + averageRating);

        FilledFeedbackRequest request = new FilledFeedbackRequest(
                studentId,
                scheduleFeedbackId,
                comment,
                averageRating
        );

        FilledFeedbackService api = RetrofitClient.getClient().create(FilledFeedbackService.class);
        Call<FilledFeedbackResponse> call = api.submitFeedback(request);

        call.enqueue(new Callback<FilledFeedbackResponse>() {
            @Override
            public void onResponse(Call<FilledFeedbackResponse> call, Response<FilledFeedbackResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(FeedbackForm.this, "Feedback Saved: " + response.body().getStatus(), Toast.LENGTH_SHORT).show();
                    // ✅ Redirect to success screen
                    Intent intent = new Intent(FeedbackForm.this,FeedbackSuccess.class);
                    intent.putExtra("message", "Your feedback has been submitted successfully!");
                    startActivity(intent);

                    finish();
                } else {
                    Toast.makeText(FeedbackForm.this, "Failed to save feedback", Toast.LENGTH_SHORT).show();
                    Log.e("FEEDBACK_FORM", "Response null or unsuccessful");
                }
            }

            @Override
            public void onFailure(Call<FilledFeedbackResponse> call, Throwable t) {
                Toast.makeText(FeedbackForm.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("FEEDBACK_FORM", "Submit failed", t);
            }
        });
    }
}
