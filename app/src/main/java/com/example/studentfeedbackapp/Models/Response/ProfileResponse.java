package com.example.studentfeedbackapp.Models.Response;

import com.google.gson.annotations.SerializedName;

public class ProfileResponse {
    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private ProfileData data;

    public String getStatus() {
        return status;
    }

    public ProfileData getData() {
        return data;
    }

    public static class ProfileData {
        @SerializedName("student_id")
        private int studentId;

        @SerializedName("studentname")
        private String studentname;

        @SerializedName("email")
        private String email;

        // ✅ String type क्योंकि API से text आता है
        @SerializedName("coursename")
        private String courseName;

        @SerializedName("batchname")
        private String batchName;

        public int getStudentId() { return studentId; }
        public String getStudentname() { return studentname; }
        public String getEmail() { return email; }

        // ✅ अब String return करें
        public String getCourseName() { return courseName; }
        public String getBatchName() { return batchName; }
    }
}
