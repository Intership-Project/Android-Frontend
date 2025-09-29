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
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackForm extends AppCompatActivity {

    private TextView tvFaculty, tvSubject, tvQ1, tvQ2, tvQ3, tvQ4, tvQ5;
    private RadioGroup rgOptions1, rgOptions2, rgOptions3, rgOptions4, rgOptions5;
    private EditText edtComment;
    private Button btnSubmit;

    private int scheduleFeedbackId = 0;
    private int selectedFeedbackTypeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_form);

        selectedFeedbackTypeId = getIntent().getIntExtra("feedbackTypeId", -1);

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

        edtComment = findViewById(R.id.edtcomment);
        btnSubmit = findViewById(R.id.btnSubmit);

        loadScheduleAndQuestions();

        btnSubmit.setOnClickListener(v -> submitFeedback());
    }

    private void loadScheduleAndQuestions() {
        ScheduleFeedbackApiService scheduleApi = RetrofitClient.getClient()
                .create(ScheduleFeedbackApiService.class);

        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int studentCourseId = prefs.getInt("course_id", -1);
        int batchId = prefs.getInt("batch_id", -1);

        scheduleApi.getScheduleFeedback().enqueue(new Callback<ScheduleFeedbackResponse>() {
            @Override
            public void onResponse(Call<ScheduleFeedbackResponse> call, Response<ScheduleFeedbackResponse> response) {
                Log.d("SCHEDULE_DEBUG_JSON", new Gson().toJson(response.body()));

                if (response.isSuccessful() && response.body() != null) {
                    List<ScheduleFeedbackResponse.FeedbackData> scheduleList = response.body().getData();
                    if (scheduleList == null || scheduleList.isEmpty()) {
                        Toast.makeText(FeedbackForm.this, "No schedule found", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    ScheduleFeedbackResponse.FeedbackData selectedSchedule = null;

                    for (ScheduleFeedbackResponse.FeedbackData schedule : scheduleList) {
                        Log.d("SCHEDULE_DEBUG", "Matched Schedule -> ID: " + schedule.getScheduleFeedbackId()
                                + ", Course: " + schedule.getCourse_id()
                                + ", FeedbackType: " + schedule.getFeedbacktype_id()
                                + ", Status: " + schedule.getStatus()
                                + ", Batch: " + schedule.getBatch_id());

                        // Check active + correct course + feedback type
                        if ((selectedFeedbackTypeId == -1 || schedule.getFeedbacktype_id() == selectedFeedbackTypeId)
                                && schedule.getCourse_id() == studentCourseId
                                && "active".equalsIgnoreCase(schedule.getStatus())) {

                            // Lab feedback: batch must match
                            if (schedule.getFeedbacktype_id() == 2) {
                                if (schedule.getBatch_id() == null || schedule.getBatch_id() != batchId) {
                                    continue; // skip if batch not match
                                }
                            }

                            selectedSchedule = schedule;
                            Log.d("SCHEDULE_DEBUG", "✅ Selected Schedule: " + schedule.getScheduleFeedbackId());
                            break;
                        }
                    }

                    if (selectedSchedule == null) {
                        Toast.makeText(FeedbackForm.this, "No active schedule found for your course/batch", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    scheduleFeedbackId = selectedSchedule.getScheduleFeedbackId();
                    tvFaculty.setText(selectedSchedule.getFacultyname());
                    tvSubject.setText(selectedSchedule.getSubjectname());

                    loadQuestions(selectedSchedule.getFeedbacktype_id());
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

        if (radioGroup == rgOptions1) {
            if (selectedId == R.id.rbQ1Option1) return 1;
            if (selectedId == R.id.rbQ1Option2) return 2;
            if (selectedId == R.id.rbQ1Option3) return 3;
            if (selectedId == R.id.rbQ1Option4) return 4;
        } else if (radioGroup == rgOptions2) {
            if (selectedId == R.id.rbQ2Option1) return 1;
            if (selectedId == R.id.rbQ2Option2) return 2;
            if (selectedId == R.id.rbQ2Option3) return 3;
            if (selectedId == R.id.rbQ2Option4) return 4;
        } else if (radioGroup == rgOptions3) {
            if (selectedId == R.id.rbQ3Option1) return 1;
            if (selectedId == R.id.rbQ3Option2) return 2;
            if (selectedId == R.id.rbQ3Option3) return 3;
            if (selectedId == R.id.rbQ3Option4) return 4;
        } else if (radioGroup == rgOptions4) {
            if (selectedId == R.id.rbQ4Option1) return 1;
            if (selectedId == R.id.rbQ4Option2) return 2;
            if (selectedId == R.id.rbQ4Option3) return 3;
            if (selectedId == R.id.rbQ4Option4) return 4;
        } else if (radioGroup == rgOptions5) {
            if (selectedId == R.id.rbQ5Option1) return 1;
            if (selectedId == R.id.rbQ5Option2) return 2;
            if (selectedId == R.id.rbQ5Option3) return 3;
            if (selectedId == R.id.rbQ5Option4) return 4;
        }

        return 0;
    }

    private void submitFeedback() {
        SharedPreferences prefs = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        int studentId = prefs.getInt("student_id", -1);
        int courseId = prefs.getInt("course_id", -1);
        int batchId = prefs.getInt("batch_id", -1);
        Log.d("DEBUG_COURSE", "Student Course ID: " + courseId);


        if (studentId == -1) {
            Toast.makeText(this, "Student ID not found. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        String comment = edtComment.getText().toString().trim();

        RadioGroup[] groups = {rgOptions1, rgOptions2, rgOptions3, rgOptions4, rgOptions5};
        int total = 0, answeredCount = 0;
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

        int averageRating = Math.round((float) total / answeredCount);

        // ✅ Use correct constructor with 6 parameters
        FilledFeedbackRequest request = new FilledFeedbackRequest(
                studentId,
                courseId,
                batchId,
                scheduleFeedbackId,
                comment,
                averageRating
        );

        FilledFeedbackService api = RetrofitClient.getClient().create(FilledFeedbackService.class);
        api.submitFeedback(request).enqueue(new Callback<FilledFeedbackResponse>() {
            @Override
            public void onResponse(Call<FilledFeedbackResponse> call, Response<FilledFeedbackResponse> response) {
                Log.d("DEBUG_COURSE", "Student Course ID: " + courseId);
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(FeedbackForm.this, "Feedback Saved", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(FeedbackForm.this, FeedbackSuccess.class);
                    intent.putExtra("message", "Your feedback has been submitted successfully!");
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(FeedbackForm.this, "Failed to save feedback", Toast.LENGTH_SHORT).show();
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