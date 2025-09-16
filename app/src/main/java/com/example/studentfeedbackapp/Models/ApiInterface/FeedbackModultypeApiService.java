package com.example.studentfeedbackapp.Models.ApiInterface;

import com.example.studentfeedbackapp.Models.Response.FeedbackModuleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FeedbackModultypeApiService {


    @GET("feedbackmoduletype/byfeedbacktype/{feedbacktype_id}")
    Call<FeedbackModuleResponse> getFeedbackModulesByFeedbackType(@Path("feedbacktype_id") int feedbackTypeId);
}
