package com.example.studentfeedbackapp.Models.Response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuestionResponse {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<QuestionData> data;

    public String getStatus() {
        return status;
    }

    public List<QuestionData> getData() {
        return data;
    }

    public class QuestionData {
        @SerializedName("feedbackquestion_id")
        private int feedbackQuestionId;

        @SerializedName("questiontext")
        private String questionText;

        public int getFeedbackQuestionId() {
            return feedbackQuestionId;
        }

        public String getQuestionText() {
            return questionText;
        }

        public int getQuestiontext() {
            return 0;
        }
    }
}


