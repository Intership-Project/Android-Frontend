package com.example.studentfeedbackapp.Presentation.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.studentfeedbackapp.Models.ApiInterface.RegisterApiService;
import com.example.studentfeedbackapp.Models.Request.Batch;
import com.example.studentfeedbackapp.Models.Request.Course;
import com.example.studentfeedbackapp.Models.Request.RegisterRequest;
import com.example.studentfeedbackapp.Models.Response.BatchResponse;
import com.example.studentfeedbackapp.Models.Response.CourseResponse;
import com.example.studentfeedbackapp.Models.Response.RegisterResponse;
import com.example.studentfeedbackapp.Models.RetrofitClient.RetrofitClient;
import com.example.studentfeedbackapp.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword;
    Spinner spinnerCourse, spinnerBatch;
    Button btnSubmit;
    TextView tvLogin;

    RegisterApiService registerApiService;
    List<Course> courseList = new ArrayList<>();
    List<Batch> batchList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        spinnerCourse = findViewById(R.id.spinnerCourse);
        spinnerBatch = findViewById(R.id.spinnerBatch);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvLogin = findViewById(R.id.tvLogin);

        registerApiService = RetrofitClient.getClient().create(RegisterApiService.class);

        // ✅ Initially disable batch spinner
        spinnerBatch.setEnabled(false);

        fetchCourses();

        btnSubmit.setOnClickListener(v -> registerUser());

        tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void fetchCourses() {
        registerApiService.getCourses()
                .enqueue(new Callback<CourseResponse>() {
                    @Override
                    public void onResponse(Call<CourseResponse> call, Response<CourseResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            courseList = response.body().getData();
                            setupCourseSpinner();
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    "Failed to load courses", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CourseResponse> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this,
                                "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void fetchBatches(int courseId) {
        Log.d("REGISTER", "Fetching batches for courseId: " + courseId);

        registerApiService.getBatches(courseId)
                .enqueue(new Callback<BatchResponse>() {
                    @Override
                    public void onResponse(Call<BatchResponse> call, Response<BatchResponse> response) {
                        batchList.clear();

                        if (response.isSuccessful() && response.body() != null) {
                            batchList = response.body().getData();
                            if (batchList == null) batchList = new ArrayList<>();

                            setupBatchSpinner();

                            if (batchList.isEmpty()) {
                                spinnerBatch.setEnabled(false);
                                Toast.makeText(RegisterActivity.this,
                                        "No batches found for this course", Toast.LENGTH_SHORT).show();
                            } else {
                                spinnerBatch.setEnabled(true);
                            }
                        } else {
                            spinnerBatch.setEnabled(false);
                            Toast.makeText(RegisterActivity.this,
                                    "Failed to load batches", Toast.LENGTH_SHORT).show();
                            setupBatchSpinner();
                        }
                    }

                    @Override
                    public void onFailure(Call<BatchResponse> call, Throwable t) {
                        batchList.clear();
                        spinnerBatch.setEnabled(false);
                        setupBatchSpinner();
                        Toast.makeText(RegisterActivity.this,
                                "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setupCourseSpinner() {
        List<String> courseNames = new ArrayList<>();
        courseNames.add("Select Course");
        for (Course c : courseList) courseNames.add(c.getCoursename());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, courseNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCourse.setAdapter(adapter);

        spinnerCourse.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    int selectedCourseId = courseList.get(position - 1).getCourse_id();

                    batchList.clear();
                    setupBatchSpinner();
                    spinnerBatch.setEnabled(false); // disable until data arrives

                    fetchBatches(selectedCourseId);
                } else {
                    batchList.clear();
                    setupBatchSpinner();
                    spinnerBatch.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                spinnerBatch.setEnabled(false);
            }
        });
    }

    private void setupBatchSpinner() {
        List<String> batchNames = new ArrayList<>();
        batchNames.add("Select Batch");
        for (Batch b : batchList) batchNames.add(b.getBatchname());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, batchNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBatch.setAdapter(adapter);
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedCoursePosition = spinnerCourse.getSelectedItemPosition();
        int selectedBatchPosition = spinnerBatch.getSelectedItemPosition();

        if (selectedCoursePosition <= 0) {
            Toast.makeText(this, "Please select a course", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedBatchPosition <= 0) {
            Toast.makeText(this, "Please select a batch", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedCourseId = courseList.get(selectedCoursePosition - 1).getCourse_id();
        int selectedBatchId = batchList.get(selectedBatchPosition - 1).getBatch_id();

        RegisterRequest request = new RegisterRequest(name, email, password, selectedCourseId, selectedBatchId);

        registerApiService.registerUser(request)
                .enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(RegisterActivity.this,
                                    "Registration Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    "Registration failed!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        Toast.makeText(RegisterActivity.this,
                                "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
