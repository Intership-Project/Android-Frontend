package com.example.studentfeedbackapp.Models.ApiInterface;

import com.example.studentfeedbackapp.Models.Response.QuestionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface QuestionApiService {
    @GET("feedbackquestion/feedbacktype/{feedbacktype_id}")
    Call<QuestionResponse> getQuestions(@Path("feedbacktype_id") int feedbackTypeId);
}

