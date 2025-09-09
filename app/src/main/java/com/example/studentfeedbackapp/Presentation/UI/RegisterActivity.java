package com.example.studentfeedbackapp.Presentation.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentfeedbackapp.Models.ApiInterface.RegisterApiService;
import com.example.studentfeedbackapp.Models.Entities.Batch;
import com.example.studentfeedbackapp.Models.Entities.Course;
import com.example.studentfeedbackapp.Models.Request.RegisterRequest;
import com.example.studentfeedbackapp.Models.Response.RegisterResponse;
import com.example.studentfeedbackapp.Models.RetrofitClient.RetrofitClient;
import com.example.studentfeedbackapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword, etConfirmPassword;
    Spinner spinnerCourse, spinnerBatch;
    Button btnSubmit;
    TextView tvLogin;

    RegisterApiService registerApiService;
    List<Course> courseList;
    List<Batch> batchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // UI Components
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        spinnerCourse = findViewById(R.id.spinnerCourse);
        spinnerBatch = findViewById(R.id.spinnerBatch);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvLogin = findViewById(R.id.tvLogin);

        // Set all EditText text and hint color to black
        etName.setTextColor(getResources().getColor(android.R.color.black));
        etName.setHintTextColor(getResources().getColor(android.R.color.black));

        etEmail.setTextColor(getResources().getColor(android.R.color.black));
        etEmail.setHintTextColor(getResources().getColor(android.R.color.black));

        etPassword.setTextColor(getResources().getColor(android.R.color.black));
        etPassword.setHintTextColor(getResources().getColor(android.R.color.black));

        etConfirmPassword.setTextColor(getResources().getColor(android.R.color.black));
        etConfirmPassword.setHintTextColor(getResources().getColor(android.R.color.black));

        // Retrofit API
        registerApiService = RetrofitClient.getClient().create(RegisterApiService.class);

        // Dummy courses
        courseList = new ArrayList<>();
        courseList.add(new Course(1, "DMC"));
        courseList.add(new Course(2, "DAC"));
        courseList.add(new Course(3, "DBDA"));
        courseList.add(new Course(4, "DESD"));
        courseList.add(new Course(5, "DITISS"));

        // Dummy batches
        batchList = new ArrayList<>();
        batchList.add(new Batch(1, "W1", 1));
        batchList.add(new Batch(2, "W2", 1));
        batchList.add(new Batch(3, "W3", 1));
        batchList.add(new Batch(14, "D1", 2));
        batchList.add(new Batch(15, "D2", 2));
        batchList.add(new Batch(16, "D3", 2));
        batchList.add(new Batch(17, "E1", 3));
        batchList.add(new Batch(18, "E2", 3));
        batchList.add(new Batch(19, "E3", 3));
        batchList.add(new Batch(20, "B1", 4));
        batchList.add(new Batch(21, "B2", 4));
        batchList.add(new Batch(22, "B3", 4));
        batchList.add(new Batch(8, "T1", 5));
        batchList.add(new Batch(9, "T2", 5));
        batchList.add(new Batch(10, "T3", 5));

        setupCourseSpinner();

        btnSubmit.setOnClickListener(v -> registerUser());

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void setupCourseSpinner() {
        List<String> courseNames = new ArrayList<>();
        courseNames.add("Select Course"); // Default prompt
        for (Course c : courseList) courseNames.add(c.getCoursename());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, courseNames) {
            @Override
            public boolean isEnabled(int position) {
                return true;
            }

            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(android.R.color.darker_gray)); // default prompt grey
                } else {
                    tv.setTextColor(getResources().getColor(android.R.color.black)); // all other black
                }
                return tv;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourse.setAdapter(adapter);

        spinnerCourse.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    setupBatchSpinner(0); // Clear batch spinner
                } else {
                    int selectedCourseId = courseList.get(position - 1).getCourse_id();
                    setupBatchSpinner(selectedCourseId);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });
    }

    private void setupBatchSpinner(int courseId) {
        List<String> batchNames = new ArrayList<>();
        batchNames.add("Select Batch"); // Default prompt
        for (Batch b : batchList) {
            if (b.getCourse_id() == courseId) batchNames.add(b.getBatchname());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, batchNames) {
            @Override
            public boolean isEnabled(int position) {
                return true;
            }

            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    tv.setTextColor(getResources().getColor(android.R.color.darker_gray)); // default prompt grey
                } else {
                    tv.setTextColor(getResources().getColor(android.R.color.black)); // all other black
                }
                return tv;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBatch.setAdapter(adapter);
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (spinnerCourse.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a course", Toast.LENGTH_SHORT).show();
            return;
        }
        if (spinnerBatch.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select a batch", Toast.LENGTH_SHORT).show();
            return;
        }

        int courseId = courseList.get(spinnerCourse.getSelectedItemPosition() - 1).getCourse_id();
        int batchId = -1;
        String batchName = spinnerBatch.getSelectedItem().toString();
        for (Batch b : batchList) {
            if (b.getBatchname().equals(batchName) && b.getCourse_id() == courseId) {
                batchId = b.getBatch_id();
                break;
            }
        }

        RegisterRequest request = new RegisterRequest(name, email, password, courseId, batchId);

        registerApiService.registerUser(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
