package com.example.studentfeedbackapp.Models.Response;

import com.example.studentfeedbackapp.Models.Request.FeedbackType;

import java.util.List;

public class FeedbackResponse {

    private String status;
    private List<FeedbackType> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<FeedbackType> getData() {
        return data;
    }

    public void setData(List<FeedbackType> data) {
        this.data = data;
    }
}

