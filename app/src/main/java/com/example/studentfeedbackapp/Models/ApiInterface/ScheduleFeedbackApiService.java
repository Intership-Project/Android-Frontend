package com.example.studentfeedbackapp.Models.ApiInterface;

import com.example.studentfeedbackapp.Models.Response.ScheduleFeedbackResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ScheduleFeedbackApiService {
    @GET("schedulefeedback/:id")

    Call<ScheduleFeedbackResponse> getScheduleFeedback();
}

