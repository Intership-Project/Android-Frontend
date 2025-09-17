package com.example.studentfeedbackapp.Models.ApiInterface;

import com.example.studentfeedbackapp.Models.Response.FeedbackResponse;
import com.example.studentfeedbackapp.Models.Response.ProfileResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface StudentDashboardApiService {
    @GET("feedbacktype") // <-- yaha aapka API endpoint daalo
    Call<FeedbackResponse> getFeedbackTypes();

    @GET("student/profile")
    Call<ProfileResponse> getStudentProfile(@Header("Authorization") String token);
}
