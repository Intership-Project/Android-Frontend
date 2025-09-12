package com.example.studentfeedbackapp.Models.Response;

import com.example.studentfeedbackapp.Models.Request.FeedbackModuleType;

import java.util.List;

public class FeedbackModuleResponse {

    private String status;
    private List<FeedbackModuleType> data;

    public String getStatus() { return status; }
    public List<FeedbackModuleType> getData() { return data; }
}
