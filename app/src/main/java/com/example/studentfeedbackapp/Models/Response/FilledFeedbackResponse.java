package com.example.studentfeedbackapp.Models.Response;

public class FilledFeedbackResponse {
    private String status;
    private String message;
    private FeedbackData data;

    public String getStatus() {
        return status;
    }
    public String getMessage() { return message; }

    public FeedbackData getData() {
        return data;
    }

    public static class FeedbackData {
        private int filledfeedbacks_id;
        private int student_id;
        private int schedulefeedback_id;
        private String comments;
        private int rating;
        private String review_status;

        public int getFilledfeedbacks_id() {
            return filledfeedbacks_id;
        }

        public int getStudent_id() {
            return student_id;
        }

        public int getSchedulefeedback_id() {
            return schedulefeedback_id;
        }

        public String getComments() {
            return comments;
        }

        public int getRating() {
            return rating;
        }

        public String getReview_status() {
            return review_status;
        }
    }
}
