package com.example.studentfeedbackapp.Models.ApiInterface;

import com.example.studentfeedbackapp.Models.Response.FeedbackModuleResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface FeedbackModultypeApiService {
    @GET("feedbackmoduletype") // API endpoint for module types
    Call<FeedbackModuleResponse> getFeedbackModuleTypes();
}
