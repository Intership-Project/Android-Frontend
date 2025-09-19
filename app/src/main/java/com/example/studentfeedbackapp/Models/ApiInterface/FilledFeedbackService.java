package com.example.studentfeedbackapp.Models.ApiInterface;

import com.example.studentfeedbackapp.Models.Request.FilledFeedbackRequest;
import com.example.studentfeedbackapp.Models.Response.FilledFeedbackResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FilledFeedbackService {
    @POST("filledfeedback/")
    Call<FilledFeedbackResponse> submitFeedback(@Body FilledFeedbackRequest request);

}
