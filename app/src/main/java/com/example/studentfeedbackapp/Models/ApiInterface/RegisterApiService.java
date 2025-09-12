package com.example.studentfeedbackapp.Models.ApiInterface;

import com.example.studentfeedbackapp.Models.Request.RegisterRequest;
import com.example.studentfeedbackapp.Models.Response.BatchResponse;
import com.example.studentfeedbackapp.Models.Response.CourseResponse;
import com.example.studentfeedbackapp.Models.Response.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RegisterApiService {
    @GET("course")
    Call<CourseResponse> getCourses(@Header("Authorization") String authHeader);

    @GET("batch")
    Call<BatchResponse> getBatches(@Query("courseId") String courseId, @Header("Authorization") String authHeader);

    @POST("student/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest request, @Header("Authorization") String authHeader);
}
