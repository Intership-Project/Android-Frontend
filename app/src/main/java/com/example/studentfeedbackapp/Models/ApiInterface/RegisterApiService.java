package com.example.studentfeedbackapp.Models.ApiInterface;

import com.example.studentfeedbackapp.Models.Request.RegisterRequest;
import com.example.studentfeedbackapp.Models.Response.BatchResponse;
import com.example.studentfeedbackapp.Models.Response.CourseResponse;
import com.example.studentfeedbackapp.Models.Response.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RegisterApiService {
    @GET("course")
    Call<CourseResponse> getCourses();


    // ✅ Fix path to match backend
    @GET("batch/course/{course_id}")
    Call<BatchResponse> getBatches(@Path("course_id") int courseId);
    @POST("student/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest request);
}
