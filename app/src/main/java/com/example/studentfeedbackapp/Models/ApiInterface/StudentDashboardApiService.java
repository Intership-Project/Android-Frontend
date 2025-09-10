package com.example.studentfeedbackapp.Models.ApiInterface;

import com.example.studentfeedbackapp.Models.Request.FeedbackResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface StudentDashboardApiService {
    @GET("feedbacktype") // <-- yaha aapka API endpoint daalo
    Call<FeedbackResponse> getFeedbackTypes();
}
