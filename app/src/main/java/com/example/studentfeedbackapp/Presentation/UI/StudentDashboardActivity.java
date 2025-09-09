package com.example.studentfeedbackapp.Presentation.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentfeedbackapp.R;


public class StudentDashboardActivity extends AppCompatActivity {

    TextView tvStudentName;
    Button btnRegisterFeedback, btnViewFeedback, btnProfile, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);

        tvStudentName = findViewById(R.id.tvStudentName);
        btnRegisterFeedback = findViewById(R.id.btnRegisterFeedback);
        btnViewFeedback = findViewById(R.id.btnViewFeedback);
        btnProfile = findViewById(R.id.btnProfile);
        btnLogout = findViewById(R.id.btnLogout);

        // Dummy data for student
        tvStudentName.setText("Welcome, Rupali");

        // Open Feedback Form
        btnRegisterFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentDashboardActivity.this, FeedbackFormActivity.class);
                startActivity(intent);
            }
        });

        // For now, View Feedback = DashboardActivity (aap chaho to new bana sakte ho)
        btnViewFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentDashboardActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });

        // Profile -> abhi FeedbackFormActivity open ho raha hai (aap chaho to naya ProfileActivity bana lo)
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentDashboardActivity.this, FeedbackFormActivity.class);
                startActivity(intent);
            }
        });

        // Logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Back to login
            }
        });
    }
}
