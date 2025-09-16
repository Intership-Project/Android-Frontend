package com.example.studentfeedbackapp.Presentation.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentfeedbackapp.Models.ApiInterface.QuestionApiService;
import com.example.studentfeedbackapp.Models.Response.QuestionResponse;
import com.example.studentfeedbackapp.Models.RetrofitClient.RetrofitClient;
import com.example.studentfeedbackapp.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public class FeedbackForm extends AppCompatActivity {

    private TextView tvFaculty, tvSubject, tvQ1, tvQ2, tvQ3, tvQ4, tvQ5;
    private RadioGroup rgOptions1, rgOptions2, rgOptions3, rgOptions4, rgOptions5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_form);

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

        // Receive feedbackTypeId from previous Activity
        int feedbackTypeId = getIntent().getIntExtra("feedbackTypeId", -1);
        String facultyName = getIntent().getStringExtra("facultyName");
        String subjectName = getIntent().getStringExtra("subjectName");


        if (facultyName != null) tvFaculty.setText(facultyName);
        if (subjectName != null) tvSubject.setText(subjectName);


        if (feedbackTypeId != -1) {
            loadQuestions(feedbackTypeId);
        } else {
            Toast.makeText(this, "Feedback type not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadQuestions(int feedbackTypeId) {
        QuestionApiService apiService = RetrofitClient.getClient().create(QuestionApiService.class);
        apiService.getQuestions(feedbackTypeId).enqueue(new Callback<QuestionResponse>() {
            @Override
            public void onResponse(Call<QuestionResponse> call, Response<QuestionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<QuestionResponse.QuestionData> questions = response.body().getData();


                    if (questions == null || questions.isEmpty()) {
                        Toast.makeText(FeedbackForm.this, "No questions found", Toast.LENGTH_SHORT).show();
                        return;
                    }


// Display all questions available (up to 5)
                    tvQ1.setText(questions.size() > 0 ? questions.get(0).getQuestionText() : "");
                    tvQ2.setText(questions.size() > 1 ? questions.get(1).getQuestionText() : "");
                    tvQ3.setText(questions.size() > 2 ? questions.get(2).getQuestionText() : "");
                    tvQ4.setText(questions.size() > 3 ? questions.get(3).getQuestionText() : "");
                    tvQ5.setText(questions.size() > 4 ? questions.get(4).getQuestionText() : "");


// Hide unused RadioGroups
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

    // QuestionApiService interface
    public interface QuestionApiService {
        @GET("feedbackquestion/feedbacktype/{feedbacktype_id}")
        Call<QuestionResponse> getQuestions(@Path("feedbacktype_id") int feedbackTypeId);
    }
}