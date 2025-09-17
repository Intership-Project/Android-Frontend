package com.example.studentfeedbackapp.Presentation.UI;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentfeedbackapp.R;

public class FeedbackSuccess extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_success);

        TextView tvMessage = findViewById(R.id.tvSuccess);
        String message = getIntent().getStringExtra("message");
        tvMessage.setText(message);
    }
}
