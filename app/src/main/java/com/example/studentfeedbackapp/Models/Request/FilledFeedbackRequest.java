package com.example.studentfeedbackapp.Models.Request;

import com.google.gson.annotations.SerializedName;

public class FilledFeedbackRequest {

    @SerializedName("student_id")
    private int studentId;

    @SerializedName("course_id")
    private int courseId;

    @SerializedName("batch_id")
    private int batchId;

    @SerializedName("schedulefeedback_id")
    private int scheduleFeedbackId;

    @SerializedName("comment")
    private String comment;

    @SerializedName("average_rating")
    private int averageRating;

    // ✅ Constructor with 6 parameters
    public FilledFeedbackRequest(int studentId, int courseId, int batchId, int scheduleFeedbackId,
                                 String comment, int averageRating) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.batchId = batchId;
        this.scheduleFeedbackId = scheduleFeedbackId;
        this.comment = comment;
        this.averageRating = averageRating;
    }

    // Getters & Setters
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }

    public int getBatchId() { return batchId; }
    public void setBatchId(int batchId) { this.batchId = batchId; }

    public int getScheduleFeedbackId() { return scheduleFeedbackId; }
    public void setScheduleFeedbackId(int scheduleFeedbackId) { this.scheduleFeedbackId = scheduleFeedbackId; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public int getAverageRating() { return averageRating; }
    public void setAverageRating(int averageRating) { this.averageRating = averageRating; }
}
