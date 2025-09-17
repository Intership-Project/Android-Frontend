package com.example.studentfeedbackapp.Models.Request;

import com.google.gson.annotations.SerializedName;

public class FilledFeedbackRequest {
    @SerializedName("student_id")
    private int studentId;

    @SerializedName("schedulefeedback_id")
    private int scheduleFeedbackId;

    @SerializedName("comments")
    private String comments;

    @SerializedName("rating") // backend expects integer
    private int rating;  // ✅ change from float to int

    public FilledFeedbackRequest(int studentId, int scheduleFeedbackId, String comments, int rating) {
        this.studentId = studentId;
        this.scheduleFeedbackId = scheduleFeedbackId;
        this.comments = comments;
        this.rating = rating;
    }
}
