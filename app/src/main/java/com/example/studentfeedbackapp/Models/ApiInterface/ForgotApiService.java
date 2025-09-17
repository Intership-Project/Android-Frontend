package com.example.studentfeedbackapp.Models.ApiInterface;

import com.example.studentfeedbackapp.Models.Request.ForgotRequest;
import com.example.studentfeedbackapp.Models.Response.ForgotResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ForgotApiService {

    @POST("student/forgotpassword")
    Call<ForgotResponse> forgotPassword(@Body ForgotRequest request);

    @POST("student/resetpassword")
    Call<ForgotResponse> resetPassword(@Body ForgotRequest request);
}
