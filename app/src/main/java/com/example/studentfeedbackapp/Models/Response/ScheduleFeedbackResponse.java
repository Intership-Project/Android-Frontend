package com.example.studentfeedbackapp.Models.Response;

import com.google.gson.annotations.SerializedName;

public class ScheduleFeedbackResponse {

    @SerializedName("status")
private String status;

    @SerializedName("faculty_name")
    private String facultyName;

    @SerializedName("subject_name")
    private String subjectName;

    public String getStatus() {
        return status;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public String getSubjectName() {
        return subjectName;
    }
}

