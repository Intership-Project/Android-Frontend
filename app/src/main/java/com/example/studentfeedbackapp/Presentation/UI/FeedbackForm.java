package com.example.studentfeedbackapp.Presentation.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentfeedbackapp.Models.ApiInterface.QuestionApiService;
import com.example.studentfeedbackapp.Models.ApiInterface.ScheduleFeedbackApiService;
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

        // Start by loading the schedule
        loadScheduleAndQuestions();
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

                    // Take first schedule
                    ScheduleFeedbackResponse.FeedbackData schedule = scheduleList.get(0);

                    // Set faculty and subject
                    tvFaculty.setText(schedule.getFacultyname());
                    tvSubject.setText(schedule.getSubjectname());

                    // Load questions
                    int feedbackTypeId = schedule.getFeedbacktype_id();
                    loadQuestions(feedbackTypeId);

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

                    // Display questions
                    tvQ1.setText(questions.size() > 0 ? questions.get(0).getQuestionText() : "");
                    tvQ2.setText(questions.size() > 1 ? questions.get(1).getQuestionText() : "");
                    tvQ3.setText(questions.size() > 2 ? questions.get(2).getQuestionText() : "");
                    tvQ4.setText(questions.size() > 3 ? questions.get(3).getQuestionText() : "");
                    tvQ5.setText(questions.size() > 4 ? questions.get(4).getQuestionText() : "");

                    // Show/hide radio groups
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
}
