package com.example.studentfeedbackapp.Models.ApiInterface;

import com.example.studentfeedbackapp.Models.Request.RegisterRequest;
import com.example.studentfeedbackapp.Models.Response.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterApiService {
    @POST("student/register")
    Call<RegisterResponse> registerUser(@Body RegisterRequest request);
}
