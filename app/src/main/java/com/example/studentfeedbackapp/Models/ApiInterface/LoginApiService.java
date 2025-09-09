package com.example.studentfeedbackapp.Models.ApiInterface;

import com.example.studentfeedbackapp.Models.Request.LoginRequest;
import com.example.studentfeedbackapp.Models.Response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginApiService {
    @POST("student/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}
